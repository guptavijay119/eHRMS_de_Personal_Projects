package com.ehrms.deptenq.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
public class MultitenantConfiguration {

    @Value("${defaultTenant}")
    private String defaultTenant;
    
    @Autowired
    private ReadProperties properties;

    @Bean
//    @ConfigurationProperties(prefix = "tanant")
    public DataSource dataSource() throws IOException {
    	
//    	ClassPathResource classPathResource = new ClassPathResource("allTenants");
//    	File folder = classPathResource.getFile();
//    	File[] files = folder.listFiles();    	
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        if(properties != null && properties.getTanentid() != null && !properties.getTanentid().isEmpty()) {
	        for (int i=0;i<properties.getTanentid().size();i++) {
//	            Properties tenantProperties = new Properties();
	            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
	
//	            try(FileInputStream fi = new FileInputStream(propertyFile)) {
//	                tenantProperties.load(fi);
	                String tenantId = properties.getTanentid().get(i);
	
	                dataSourceBuilder.driverClassName(properties.getDriverclassname().get(i));
	                dataSourceBuilder.username(properties.getUsername().get(i));
	                dataSourceBuilder.password(properties.getPassword().get(i));
	                dataSourceBuilder.url(properties.getUrl().get(i));
	                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
//	            } catch (IOException exp) {
//	                throw new RuntimeException("Problem in tenant datasource:" + exp);
//	            }
	        }
        }

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get("tenant_1"));
        dataSource.setTargetDataSources(resolvedDataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }

}
