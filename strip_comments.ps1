# strip_comments.ps1
# Removes all Java comments (//  /* */  /** */) from every .java file
# Does NOT touch strings, annotations, code, or formatting.

$root = ".\src\main\java\com\klu"
$files = Get-ChildItem -Path $root -Recurse -Filter "*.java"

function Remove-JavaComments([string]$src) {
    $result   = [System.Text.StringBuilder]::new()
    $i        = 0
    $len      = $src.Length
    $inString = $false   # inside "..."
    $inChar   = $false   # inside '.'

    while ($i -lt $len) {
        $c = $src[$i]

        # ── Toggle string literal ────────────────────────────────────────
        if ($inString) {
            if ($c -eq '\' -and ($i+1) -lt $len) {
                # escape sequence inside string — keep both chars
                [void]$result.Append($c)
                $i++
                [void]$result.Append($src[$i])
                $i++
                continue
            }
            if ($c -eq '"') { $inString = $false }
            [void]$result.Append($c)
            $i++
            continue
        }

        if ($inChar) {
            if ($c -eq '\' -and ($i+1) -lt $len) {
                [void]$result.Append($c)
                $i++
                [void]$result.Append($src[$i])
                $i++
                continue
            }
            if ($c -eq "'") { $inChar = $false }
            [void]$result.Append($c)
            $i++
            continue
        }

        if ($c -eq '"') { $inString = $true; [void]$result.Append($c); $i++; continue }
        if ($c -eq "'") { $inChar   = $true; [void]$result.Append($c); $i++; continue }

        # ── Single-line comment  // ──────────────────────────────────────
        if ($c -eq '/' -and ($i+1) -lt $len -and $src[$i+1] -eq '/') {
            # skip until end of line (keep the newline itself)
            while ($i -lt $len -and $src[$i] -ne "`n") { $i++ }
            continue
        }

        # ── Multi-line / JavaDoc comment  /* ... */ ──────────────────────
        if ($c -eq '/' -and ($i+1) -lt $len -and $src[$i+1] -eq '*') {
            $i += 2   # skip /*
            while ($i -lt $len) {
                if ($src[$i] -eq '*' -and ($i+1) -lt $len -and $src[$i+1] -eq '/') {
                    $i += 2   # skip */
                    break
                }
                # preserve newlines so line numbers stay roughly intact
                if ($src[$i] -eq "`n") { [void]$result.Append("`n") }
                $i++
            }
            continue
        }

        # ── Normal character ─────────────────────────────────────────────
        [void]$result.Append($c)
        $i++
    }

    return $result.ToString()
}

$count = 0
foreach ($file in $files) {
    $original = [System.IO.File]::ReadAllText($file.FullName)
    $cleaned  = Remove-JavaComments $original

    # Collapse 3+ consecutive blank lines into 2 (keeps file readable)
    $cleaned = [System.Text.RegularExpressions.Regex]::Replace($cleaned, "(\r?\n){3,}", "`r`n`r`n")

    # Only write if something actually changed
    if ($cleaned -ne $original) {
        [System.IO.File]::WriteAllText($file.FullName, $cleaned, [System.Text.Encoding]::UTF8)
        Write-Host "  Cleaned: $($file.Name)"
        $count++
    } else {
        Write-Host "  Skipped (no comments): $($file.Name)"
    }
}

Write-Host "`nDone. $count file(s) modified."
