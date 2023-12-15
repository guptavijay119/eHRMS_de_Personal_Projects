package com.ehrms.deptenq;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.EncodedResourceResolver;

@ComponentScan(basePackages = "com.ehrms.deptenq.*")
@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaAuditing
@EnableScheduling
public class EhrmsApplication extends SpringBootServletInitializer implements WebMvcConfigurer {


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EhrmsApplication.class);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		System.setProperty("spring.config.name", "de-1");
		 System.setProperty("server.servlet.context-path", "/de");
		
		SpringApplication.run(EhrmsApplication.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setSessionTrackingModes(Collections.singleton(javax.servlet.SessionTrackingMode.COOKIE));
	    SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
	    sessionCookieConfig.setHttpOnly(true);
	    sessionCookieConfig.setSecure(true);
		super.onStartup(servletContext);

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry
	      .addResourceHandler("/othercontent/**")
	      .addResourceLocations("classpath:/static/js/",
	    		  "classpath:/static/css/",
	    		  "classpath:/static/js/common/",
	    		  "classpath:/static/common/",
	    		  "classpath:/static/image/",
	    		  "classpath:/",
	    		  "classpath:/static/ehrms_assets/vendors/mdi/css/",
	    		  "classpath:/static/ehrms_assets/vendors/flag-icon-css/css/",
	    		  "classpath:/static/ehrms_assets/vendors/css/",
	    		  "classpath:/static/ehrms_assets/vendors/jquery-bar-rating/",
	    		  "classpath:/static/ehrms_assets/css/demo_1/",
	    		  "classpath:/static/ehrms_assets/vendors/js/",
	    		  "classpath:/static/ehrms_assets/vendors/chart.js/",
	    		  "classpath:/static/ehrms_assets/vendors/flot/",
	    		  "classpath:/static/ehrms_assets/js/",
	    		  "classpath:/static/ehrms_assets/vendors/font-awesome/css/",
	    		  "classpath:/static/ehrms_assets/images/faces-clipart/",
	    		  "classpath:/static/ehrms_assets/fonts/Opensans/",
	    		  "classpath:/static/fonts/",
	    		  "classpath:/static/ehrms_assets/vendors/mdi/fonts/")
	      .resourceChain(true)
	      .addResolver(new EncodedResourceResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	@Override
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

	@Bean
	public PhysicalNamingStrategy physical() {
		return new PhysicalNamingStrategyStandardImpl();
	}

	@Bean
	public ImplicitNamingStrategy implicit() {
		return new ImplicitNamingStrategyLegacyJpaImpl();
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cc = new CookieLocaleResolver();
		cc.setDefaultLocale(new Locale("en"));
		cc.setCookieName("language");
		cc.setCookieSecure(true);
		cc.setCookieHttpOnly(true);
		return cc;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
