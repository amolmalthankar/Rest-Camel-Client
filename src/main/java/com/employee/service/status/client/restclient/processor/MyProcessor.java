package com.employee.service.status.client.restclient.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyProcessor  implements Processor {

    public void process(Exchange exchange) throws Exception {
        //System.out.println(exchange.getIn().getBody(String.class));

        //System.out.println("Exchange is -> "+exchange.getIn().getBody(String.class));
        exchange.setProperty("token", exchange.getIn().getBody(String.class));
        System.out.println("Token -> "+ exchange.getProperty("token"));
    }
}

