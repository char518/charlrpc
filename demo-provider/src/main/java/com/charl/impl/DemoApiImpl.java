package com.charl.impl;

import com.charl.DemoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import piggy.call.server.Charl;

@Charl(value = DemoApi.class)
public class DemoApiImpl implements DemoApi {
    private final static Logger LOG = LoggerFactory.getLogger(DemoApiImpl.class);

    public void hello() {
        System.out.println("Hello from Server!!!");
        LOG.info("Hello info from server!!!");
    }

    public String bye(String str) {
        System.out.println("Bye from Server!!!");
        LOG.info("Bye info from server!!!");
        return "Bye from server";
    }
}
