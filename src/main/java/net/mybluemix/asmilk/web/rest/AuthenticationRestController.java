package net.mybluemix.asmilk.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class AuthenticationRestController {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationRestController.class);

	@RequestMapping(path = { "/error" })
	public String error(HttpServletRequest request) {
		LOG.info("!!!AuthenticationRestController.error()!!!");
		String message = "error";
		Object object = request.getAttribute(WebAttributes.ACCESS_DENIED_403);
		if (null != object && object instanceof AccessDeniedException) {
			AccessDeniedException exception = (AccessDeniedException) object;
			message = exception.getMessage();
			LOG.info("message: {}", message);
		}
		return message;
	}

}
