package com.weweibuy.framework.samples.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.core.Ordered;

/**
 * @author durenhao
 * @date 2020/9/26 19:08
 **/
public class HttpServletWebServerFactoryCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>, Ordered {
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) factory;
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(createConnection());
    }


    private Connector createConnection() {
        String protocol = "org.apache.coyote.http11.Http11NioProtocol";
        Connector connector = new Connector(protocol);
        connector.setScheme("http");
        connector.setPort(8081);
//        connector.setRedirectPort(8080);
        return connector;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
