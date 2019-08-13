package com.gsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gsg.mongo.repository.CartRepository;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.mongo.repository.OfficeRepository;
import com.gsg.mongo.repository.OrderDetailRepository;
import com.gsg.mongo.repository.OrderRepository;
import com.gsg.mongo.repository.SchemeDataRepository;
import com.gsg.mongo.repository.VehicleMasterRepository;
import com.gsg.services.AppUserService;
import com.gsg.services.MasterDataService;
import com.gsg.utilities.CORSFilter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@Configuration
//@EnableSwagger2
@ComponentScan(basePackages = { "com.gsg" })
// @EnableMongoRepositories(basePackages = { "com.gsg.mongo.repository" })
public class ApplicationRunner implements CommandLineRunner {

//	@Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//        		.select()                                  
//                .apis(RequestHandlerSelectors.any())              
//                .paths(PathSelectors.ant("/api/*"))                          
//                .build();   
//    }
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	

	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}

	@Bean
	public FilterRegistrationBean corsFilterRegistration() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
		registrationBean.setName("CORS Filter");
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Autowired
	AppUserService appUserService;

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	OrderDetailRepository orderDtlRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	SchemeDataRepository schemeRepository;

	@Autowired
	VehicleMasterRepository vehicleMasterRepository;

	@Autowired
	CountersRepository counterRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	OfficeRepository ofcRepository;

	@Override
	public void run(String... args) throws Exception {

	

	}
}
