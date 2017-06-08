package net.mybluemix.asmilk.web.config;

import javax.servlet.annotation.WebFilter;

import org.springframework.web.filter.RequestContextFilter;

@WebFilter(filterName = "localizationFilter", urlPatterns = "/*")
public class RequestContextFilterConfigurer extends RequestContextFilter {

}
