package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping(path = { "/", "/index" }, method = GET)
	public String index() {

		logger.debug("Temperature set to {}. Old temperature was {}.", "AAA", "BBB");
		logger.info("This is info level string");
		logger.trace("TRACE TRACE");
		logger.warn("WARN!!!");

		for (Method method : this.getClass().getMethods()) {
			logger.debug(method.toGenericString());
		}

		try {
			throw new RuntimeException();
		} catch (Exception e) {
			logger.error("this is a test RuntimeException", e);
		}

		return "index";
	}

}
