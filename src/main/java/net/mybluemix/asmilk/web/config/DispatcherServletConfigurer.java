package net.mybluemix.asmilk.web.config;

import javax.servlet.annotation.WebServlet;

import org.springframework.web.servlet.DispatcherServlet;

@WebServlet(name = "dispatcher", urlPatterns = { "/" }, loadOnStartup = 1)
public class DispatcherServletConfigurer extends DispatcherServlet {

	private static final long serialVersionUID = 5555920659585156768L;

}
