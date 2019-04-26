/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.zhusmansan.sxp;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Configuration
@EnableAutoConfiguration
public class StackexchangeProxyApp  {


    @Value("${sxp.siteUrl}")
    private String siteUrl;


    public static void main(String[] args) {
        SpringApplication.run(StackexchangeProxyApp.class, args);
    }

    @GetMapping("/search")
    public ResponseEntity<?> proxySearchToSiteUrl(ProxyExchange<Object> proxy, HttpServletRequest request) throws Exception {
        //FIXME potential security risk. Sanitize user input.
        String query = "&"+request.getQueryString();
        return proxy.uri(siteUrl+query).get();
    }


    //This stuff is needed to handle gzip stackechange replies
    @Component
    public class NettyWebServerFactoryPortCustomizer
            implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

        @Override
        public void customize(NettyReactiveWebServerFactory serverFactory) {
            Compression c = new Compression();
            c.setEnabled(true);
            serverFactory.setCompression(c);
        }
    }
}
