package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping(path = { "/", "/index" }, method = GET)
	public String index() {
		LOG.info("!!!IndexController.index()!!!");
		return "index";
	}
	
	@RequestMapping(path = { "/test" }, method = GET)
	public String test(HttpServletRequest request) {
		LOG.info("!!!IndexController.test()!!!");
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()) {
			String name = headers.nextElement();
			String value = request.getHeader(name);
			LOG.info("{}: {}", name, value);
		}
		return "index";
	}

}
