package net.mybluemix.asmilk.web.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.springframework.web.filter.CharacterEncodingFilter;

@WebFilter(filterName = "characterEncoding", urlPatterns = "/*", initParams = {
		@WebInitParam(name = "encoding", value = "UTF-8"), @WebInitParam(name = "forceEncoding", value = "true") })
public class CharacterEncodingFilterConfigurer extends CharacterEncodingFilter {

}
