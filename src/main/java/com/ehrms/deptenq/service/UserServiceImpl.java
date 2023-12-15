package com.ehrms.deptenq.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ehrms.deptenq.configuration.LoginAttemptService;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.LoginAudit;
//import hrms.recruitment.models.OfficeListMaster;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.LoginAuditRepository;
import com.ehrms.deptenq.repository.UserRepository;
//import com.ehrms.deptenq.security.SecurityUtils;
import com.ehrms.deptenq.utility.SecurityUtils;


@Service
@Configuration
public class UserServiceImpl implements UserService {

//	@Lazy
    @Autowired
    private UserRepository userRepository;
    
//	@Lazy
    @Autowired
    private HttpServletRequest request;
    
//    @PersistenceContext
//    private EntityManager em;
       
    
    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @Autowired
    private IGlobalOrgRepository globalRepo;
    
    @Autowired
    private LoginAuditRepository logAudiRepo;
    
	/*
	 * @Autowired private DepartmentMasterRepository departRepo;
	 */
//    @Lazy
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    
//    private AutowireCapableBeanFactory beanFactory;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    

//    public User save(UserRegistrationDto registration){
//        User user = new User();
//        user.setFirstName(registration.getFirstName());
//        user.setLastName(registration.getLastName());
//        user.setEmail(registration.getEmail());
//        user.setPassword(passwordEncoder.encode(registration.getPassword()));
//        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
//        return userRepository.save(user);
//    }

//    @Transactional(transactionManager = "generalTransactionManager")
    @Override
    public UserDetails loadUserByUsername(String email) {
//		System.out.println("hello:"+passwordEncoder.encode("ehrms"));
    	String ip = getClientIP();
//    	SecurityContextHolder.getContext().setAuthentication(null);
//        if (loginAttemptService.isBlocked(ip)) {
//            throw new RuntimeException("blocked");
//        }
    	
    	String dbValue = (String) request.getSession().getAttribute("organization");
    	
		 GlobalOrg org = globalRepo.findByGlobalOrgName(dbValue);
		 request.getSession().setAttribute("departobb", org);
		 request.getSession().setAttribute("tenantid", org.getTenantId());
//		 beanFactory.autowireBean(userRepository);
		/* request.getSession().setAttribute("departobb", depart); */
//    	ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
//	    HttpServletRequest req = sra.getRequest();  
//	    String hiddenCaptcha = request.getParameter("hiddenCaptcha");	
//	    String captcha =	request.getParameter("captcha");
		User user = null;
//		RepositoryFactorySupport factory = new JpaRepositoryFactory(em);
//		userRepository = factory.getRepository(UserRepository.class);

		
//		if(captcha.equals(hiddenCaptcha)) {
			user = userRepository.findByEmailAndPimsEmployeeGlobalOrgId(email,org);
//		}
			LoginAudit audi = new LoginAudit();
			 ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
			    HttpServletRequest req = sra.getRequest();
//    			User user = userRepository.findByEmail(email);
    	        if (user == null){
    	        	
    	        	audi.setEmail(email);
    	        	audi.setSuccess(false);
    	        	audi.setDatetime(LocalDateTime.now());
    	        	audi.setIpaddress(req.getRemoteAddr());
    	        	 logAudiRepo.save(audi);
    	            throw new UsernameNotFoundException("Invalid username or password.");
    	        }
    	        if(user != null && user.getSubDepartment() != null) {
    	        	request.getSession().setAttribute("subdepartment", user.getSubDepartment());
    	        	
    	        	audi.setEmail(email);
    	        	audi.setSuccess(true);
    	        	audi.setDatetime(LocalDateTime.now());
    	        	audi.setIpaddress(req.getRemoteAddr());
    	        }
    	        if(user != null) {
    	        	audi.setEmail(email);
    	        	audi.setSuccess(true);
    	        	audi.setDatetime(LocalDateTime.now());
    	        	audi.setIpaddress(req.getRemoteAddr());
    	        }
    	        logAudiRepo.save(audi);

    	        return new org.springframework.security.core.userdetails.User(user.getEmail(),
    	                user.getPassword(),
    	                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
        
    @Override
    public UserDetails getCurrentUser() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return (UserDetails)principal;
    }

	@Override
	public User findByRole(String role) {
		return null;
	}
	/*
	 * @Override public OfficeListMaster getCurrentUserOffice() { User user =
	 * userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
	 * OfficeListMaster office = null; if(user != null && user.getEmployee() != null
	 * && user.getEmployee().getOfficeCode() != null) { office =
	 * user.getEmployee().getOfficeCode(); } return office; }
	 */

	@Override
	public String getCurrentDepartmentLogin() {
    	String dbValue = (String) request.getSession().getAttribute("department");
    	
//    	switch(dbValue.trim()) {
//    		case DepartmentConstants.PWD:
//    			User user = userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
//    			if(user != null && user.getEmployee() != null && user.getEmployee().getOfficeCode() != null) {
//    				return user.getEmployee().getOfficeCode().getDepartmentCode().getDepartmentName();
//    			}
//    			break;
//    		case DepartmentConstants.GAD:
//    			User user = userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
//    			if(user != null && user.getEmployee() != null && user.getEmployee().getOfficeCode() != null) {
//    				return user.getEmployee().getOfficeCode().getDepartmentCode().getDepartmentName();
//    			}
//    			break;
//    		default:
//    	}
		return dbValue;
	}
	
	private String getClientIP() {
	    String xfHeader = request.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return request.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}


	/*
	 * @Override public Department getCurrentDepartment() { User user =
	 * userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
	 * OfficeListMaster office = null; if(user != null && user.getEmployee() != null
	 * && user.getEmployee().getOfficeCode() != null) { office =
	 * user.getEmployee().getOfficeCode(); } return office.getDepartmentCode(); }
	 * 
	 */
	@Override
	public User getCurrentUserPojo() {
		return userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
	}
}
