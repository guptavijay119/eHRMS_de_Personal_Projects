package com.ehrms.deptenq.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class CustomBeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
//	@Autowired
//	private DepartmentRepository departRepo;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String dbValue = request.getParameter("organization");
        final String tenantId = request.getParameter("tenantid");
//        Department depart = departRepo.findById(Long.valueOf(dbValue)).orElse(new Department());
        request.getSession().setAttribute("organization", dbValue);
        request.getSession().setAttribute("tenantid", tenantId);
        return super.attemptAuthentication(request, response);
    }
    
}
