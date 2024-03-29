package com.heraclitus.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.TargetUrlResolverImpl;
import org.springframework.security.ui.savedrequest.SavedRequest;

import com.heraclitus.domain.Role;

/**
 * I am responsible for determining what url should be the targetUrl for the
 * user based on their {@link GrantedAuthority}.
 * 
 * If I cant match any of the {@link GrantedAuthority}'s with our application
 * {@link Role}'s I default back to the {@link TargetUrlResolverImpl}
 * implementation provided by spring security.
 */
public class RoleBasedTargetUrlResolverImpl implements TargetUrlResolver {
    
    private static final String ADMIN_ROLE_TARGET_URL = "/admin.htm";
    
    private final TargetUrlResolver defaultSpringSecurityUrlResolver;
    
    public RoleBasedTargetUrlResolverImpl(
            final TargetUrlResolver fallbackTargetUrlResolver) {
        defaultSpringSecurityUrlResolver = fallbackTargetUrlResolver;
    }
    
    /**
     * I determine the target url based on the {@link Authentication} and
     * whether their {@link GrantedAuthority} match any of our applications
     * {@link Role} 's.
     */
    public String determineTargetUrl(final SavedRequest savedRequest,
            final HttpServletRequest currentRequest, final Authentication auth) {
        
        if (containsAdminAuthority(auth)) {
            return ADMIN_ROLE_TARGET_URL;
        }
        
        return defaultSpringSecurityUrlResolver.determineTargetUrl(
                savedRequest, currentRequest, auth);
    }
    
    private boolean containsAdminAuthority(final Authentication auth) {
        for (final GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(
                    Role.ADMIN_ROLE.roleName())) {
                
                return true;
            }
        }
        return false;
    }
}
