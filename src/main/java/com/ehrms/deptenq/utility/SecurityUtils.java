package com.ehrms.deptenq.utility;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/**
 * Utility class for Spring Security.
 */

public final class SecurityUtils {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

	
    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static UserDetails getCurrentUser() {
    	try {
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	UserDetails user = (UserDetails)principal;
	    	return user;
    	} catch(Exception a) {
//    		throw new UsernameNotFoundException("Invalid username or password.");
    		throw new SessionAuthenticationException("Session timeout");
//    		return null;
    	}

    }
    
    public static String getRole() {
    	return getCurrentUser().getAuthorities().stream().findFirst().get().getAuthority();
    }
    
    public static List<? extends GrantedAuthority> getRoles() {
    	
    	return getCurrentUser().getAuthorities().stream().collect(Collectors.toList());
    }

	/*
	 * get Authority methods
	 * 
	 * @return  authority for specific user role.
	 * 
	 * @auther
	 */
    
    public static List<String> getRolesInString() {
    	return getCurrentUser().getAuthorities().stream().map(s->s.getAuthority()).collect(Collectors.toList());
    }
    
    
    public static String getAuthority() {

    	String roleauthority = getCurrentUser().getAuthorities().stream().findFirst().get().getAuthority();
    	String authority = null;
    	
		switch (roleauthority) {
			case "ROLE_DEPARTMENT":
				authority = "DEP";
				break;
			case "ROLE_AA":
				authority = "AA";
				break;
			case "ROLE_16B":
				authority = "16B";
				break;
			case "ROLE_VAC":
				authority = "VAC";
				break;
			case "ROLE_BCCELL":
				authority = "BC";
				break;
			case "ROLE_GAD" :
				authority = "GAD";
				break;
			case "ROLE_L&JD" :
				authority = "L&JD";
				break;
			case "ROLE_MPSC" :
				authority = "MPSC";
				break;
			case "ROLE_COMMISSIONERSPORTS" :
				authority = "COMSPORTS";
				break;
			case "ROLE_SCHOOLEDUCATION" :
				authority = "SCHOOLED";
				break;
			case "ROLE_CSCOMMITEE" :
				authority = "CSCOMMITTE";
				break;
			case "ROLE_CMCOMMITEE" :
				authority = "CMCOMMITTE";
				break;
			case "ROLE_FINANCEDEPARTMENT" :
				authority = "FINANCEDEP";
				break;
			case "ROLE_COMPETENTAUTHORITY" :
				authority = "CPTAUTH";
				break;
			case "ROLE_EMPLOYEE" :
				authority = "EMPLOYEE";
				break;
			case "ROLE_HOD" :
				authority = "HOD";
				break;
			default:
				authority = getCurrentUser().getAuthorities().stream().findFirst().get().getAuthority();
				break;
				
		}
		
		return authority;
    	
    }
    
    public static String getLoginUserIp() {
    	if(SecurityUtils.isAuthenticated()) {
    		return ((WebAuthenticationDetails)SecurityContextHolder
        			.getContext().getAuthentication().getDetails()).getRemoteAddress();
    	} else {
    		return "0.0.0.0";
    	}
    }

    
    /**
     * Return the current user, or throws an exception, if the user is not
     * authenticated yet.
     *
     * @return the current user
     */
    public static UserDetails getCurrentUserModel() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserDetails user = (UserDetails)principal;
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof User) {
//                return (User) authentication.getPrincipal();
//            }
//        }
    	return user;
//        throw new IllegalStateException("User not found!");
    } 

    /**
     * If the current user has a specific authority (security role).
     *
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
            }
        }
        return false;
    }
    
    public static String getCurrentIPAddress() {
        try {
            return ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
					.getRemoteAddress();
        } catch(Exception a) {
        	LOG.error("Exception", a);
        	return null;
        }
    }

}
