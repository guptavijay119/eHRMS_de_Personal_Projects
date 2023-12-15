package com.ehrms.deptenq.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ehrms.deptenq.models.User;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);
        
    UserDetails getCurrentUser();
    
    User findByRole(String role);
    
  /*  OfficeListMaster getCurrentUserOffice();*/
    
    String getCurrentDepartmentLogin();
    
   /* Department getCurrentDepartment();*/
    
    User getCurrentUserPojo();

}
