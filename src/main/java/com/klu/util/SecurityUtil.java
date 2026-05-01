package com.klu.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtil {

    private SecurityUtil() {  }

public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        return principal.toString();
    }

public static String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return null;
        }
        return auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse(null);
    }

public static boolean isAdmin() {
        String role = getCurrentRole();
        return "ROLE_ADMIN".equals(role);
    }
}
