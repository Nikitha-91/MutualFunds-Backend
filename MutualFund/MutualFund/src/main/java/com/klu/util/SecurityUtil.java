package com.klu.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility to access the currently authenticated user
 * from anywhere in the service or controller layer
 * without injecting HttpServletRequest.
 */
public final class SecurityUtil {

    private SecurityUtil() { /* utility class */ }

    /**
     * Returns the email (principal name) of the currently authenticated user.
     *
     * @return email string, or {@code null} if anonymous
     */
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

    /**
     * Returns the role of the currently authenticated principal.
     * Role authority is prefixed with {@code ROLE_}.
     *
     * @return role string e.g. {@code "ROLE_INVESTOR"}, or {@code null}
     */
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

    /**
     * Checks whether the current user has ADMIN role.
     */
    public static boolean isAdmin() {
        String role = getCurrentRole();
        return "ROLE_ADMIN".equals(role);
    }
}
