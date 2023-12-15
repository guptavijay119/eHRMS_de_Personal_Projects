package com.ehrms.deptenq.configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.ehrms.deptenq.filter.CustomBeforeAuthenticationFilter;
import com.ehrms.deptenq.service.UserService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().csrfTokenRepository(csrfTokenRepository()).and().headers().frameOptions().disable().and().headers()
				.xssProtection().xssProtectionEnabled(true).and().contentTypeOptions().and().cacheControl().and()
				.httpStrictTransportSecurity().and()
				.contentSecurityPolicy("script-src 'self' http://localhost:8080/ 'unsafe-inline' 'unsafe-eval'")
				.reportOnly().and().and().authorizeRequests().antMatchers("/Admin/**").hasAnyRole("ADMIN")
				.antMatchers("/departmentalEnquiry/addcasedetails/**", "/departmentalEnquiry/addemployeedetails/**",
						"/departmentalEnquiry/addchargeSheetdetails/**", "/departmentalEnquiry/addsuspensionDetails/**",
						"/departmentalEnquiry/addreinstatedDetails/**",
						"/departmentalEnquiry/addinquiryOfficerDetails/**",
						"/departmentalEnquiry/addPresentingOfficerDetails/**",
						"/departmentalEnquiry/addProsecutionProposalDetails/**",
						"/departmentalEnquiry/addCourtCaseDetails/**",
						"/departmentalEnquiry/addDetailsKeptAbeyanceCases/**",
						"/departmentalEnquiry/deleteEmployeeDetailsRecords/**",
						"/departmentalEnquiry/deleteChargesheetDetailsRecords/**",
						"/departmentalEnquiry/deleteSuspensionDetailsRecords/**",
						"/departmentalEnquiry/deleteReinstatedDetailsRecords/**",
						"/departmentalEnquiry/deletePresentingOfficerDetailsRecords/**",
						"/departmentalEnquiry/deleteProsecutionProposalDetailsRecords/**",
						"/departmentalEnquiry/deleteCourtCaseDetailsRecords/**",
						"/departmentalEnquiry/deleteDetailsKeptAbeyanceDetailsRecords/**",
						"/departmentalEnquiry/deletefinalOutComeDetailsRecords/**",
						"/departmentalEnquiry/deleteDECasesBacklogRecords/**",
						"/departmentalEnquiry/deleteInquiryOfficerDetailsRecords/**",
						"/departmentalEnquiry/addFinalOutcomeDetails/**")
				.hasAnyRole("DEPARTMENT", "VERIFICATION").antMatchers("/favicon.ico").permitAll().and()
				.addFilterBefore(getBeforeAuthenticationFilter(), CustomBeforeAuthenticationFilter.class).formLogin()
				.loginPage("/login").permitAll().and().logout().deleteCookies("JSESSIONID","XSRF-TOKEN","language","X-TenantID").invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll();
		http.sessionManagement().maximumSessions(1);
		http.sessionManagement().sessionFixation().migrateSession();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//    public void configure(WebSecurity web) throws Exception {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowedHttpMethods(Arrays.asList("GET","POST"));
//        firewall.setAllowBackSlash(true);
//        firewall.setAllowNull(true);
//        firewall.setAllowSemicolon(true);
//        firewall.setAllowUrlEncodedSlash(true);
//        firewall.setAllowUrlEncodedCarriageReturn(true);
//        firewall.setAllowUrlEncodedDoubleSlash(true);
//        firewall.setAllowUrlEncodedLineFeed(true);
//        firewall.setAllowUrlEncodedLineSeparator(true);
//        firewall.setAllowUrlEncodedParagraphSeparator(true);
//        firewall.setAllowUrlEncodedPercent(true);
//        firewall.setAllowUrlEncodedPeriod(true);
//        firewall.setAllowedHostnames(hostName -> hostName.equals("localhost") || hostName.equals("127.0.0.1") || hostName.equals("10.186.48.32") ||
//        		 hostName.equals("10.48.102.14") ||  hostName.equals("10.186.48.79") ||
//        		 hostName.equals("10.48.102.3") ||
//        		 hostName.equals("115.124.119.239") ||
//        		 hostName.equals("10.186.48.171"));
//
//        web.httpFirewall(firewall);
//    }


	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserService userService) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder.setConnectTimeout(Duration.ofSeconds(60))
//	    		.setConnectTimeout(30000).setReadTimeout(30000)
				.setReadTimeout(Duration.ofSeconds(60)).build();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider(userService));
	}

	public UsernamePasswordAuthenticationFilter getBeforeAuthenticationFilter() throws Exception {
		CustomBeforeAuthenticationFilter filter = new CustomBeforeAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				super.setDefaultFailureUrl("/login?error");
				super.onAuthenticationFailure(request, response, exception);
			}

		});

		return filter;
	}

	private CookieCsrfTokenRepository csrfTokenRepository() {
		final CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
		repository.setSecure(true);
		repository.setCookieHttpOnly(true);
		return repository;
	}

}
