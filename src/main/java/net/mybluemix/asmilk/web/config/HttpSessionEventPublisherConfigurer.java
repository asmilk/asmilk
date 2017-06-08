package net.mybluemix.asmilk.web.config;

import javax.servlet.annotation.WebListener;

import org.springframework.security.web.session.HttpSessionEventPublisher;

@WebListener
public class HttpSessionEventPublisherConfigurer extends HttpSessionEventPublisher {

}
