package net.mybluemix.asmilk.web.config;

import javax.servlet.annotation.WebFilter;

import org.springframework.web.filter.DelegatingFilterProxy;

@WebFilter(filterName = "springSecurityFilterChain", urlPatterns = "/*")
public class SpringSecurityFilterConfigurer extends DelegatingFilterProxy {

}
