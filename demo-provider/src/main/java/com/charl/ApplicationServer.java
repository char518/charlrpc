package com.charl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServer.class);

    public static void main(String[] args) {
        LOGGER.info("start application!!!");
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
    }

}
