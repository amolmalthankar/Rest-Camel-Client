package com.employee.service.status.client.restclient.route;

import com.employee.service.status.client.restclient.processor.MyProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleRouteBuilder extends RouteBuilder {

    @Value("${applicationurls.gettoken.path}")
    private String getTokenUrlPath;

    @Value("${applicationurls.getemployee.path}")
    private String getEmployeeUrlPath;

    @Value("${applicationurls.addemployee.path}")
    private String addEmployeeUrlPath;

    @Autowired
    CamelContext context;

    private static final String tokenBody = "{\n" +
            "\t\"username\": \"amol\",\n" +
            "\t\"id\": 100,\n" +
            "\t\"role\": \"admin\"\n" +
            "}";

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest().get("/employeeApplication")
                .to("direct:getToken");

        from("direct:getToken")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .process(exchange -> exchange.getIn().setBody(tokenBody))
                .removeHeaders("CamelHttp*")
                .to(getTokenUrlPath + "?bridgeEndpoint=true")
                .to("direct:getEmployeeData")
                .transform().simple("${body}");

        from("direct:getEmployeeData")
                .process(new MyProcessor())
                .setHeader("Token", exchangeProperty("token"))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to(getEmployeeUrlPath + "?bridgeEndpoint=true")
                .choice().when().simple("${body} != 'Already Exists'").to("direct:createEmployeeData")
                .otherwise().transform().simple("Already Exists");

        from("direct:createEmployeeData")
                .setHeader("Token", exchangeProperty("token"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(addEmployeeUrlPath + "?bridgeEndpoint=true")
                .transform().simple("${body}");

        from("direct:employeeExists")
                .transform().simple("${body}");
    }
}
