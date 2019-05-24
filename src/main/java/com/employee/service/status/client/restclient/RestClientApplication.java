package com.employee.service.status.client.restclient;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class RestClientApplication {

	//@Value("${baeldung.api.path}")
	//
	private static final String CAMEL_URL_MAPPING = "/camel/*";
	private static final String CAMEL_SERVLET_NAME = "CamelServlet";

	public static void main(String[] args) {
		SpringApplication.run(RestClientApplication.class, args);
	}

	@Bean
	ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean registration =
				new ServletRegistrationBean(new CamelHttpTransportServlet(), CAMEL_URL_MAPPING);
		registration.setName(CAMEL_SERVLET_NAME);
		return registration;
	}
}
