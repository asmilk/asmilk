package net.mybluemix.asmilk.web;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticationController {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

	/*
	 * @RequestMapping(path = { "/logout" }, method = GET) public String
	 * logout(HttpServletRequest request) throws ServletException {
	 * LOG.info("!!!AuthenticationController.logout()!!!"); request.logout(); //
	 * SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext
	 * ()); return "redirect:/index"; }
	 */

	@RequestMapping(path = { "/error" })
	public String error(HttpServletRequest request) throws ServletException {
		LOG.info("!!!AuthenticationController.error()!!!");
		LOG.info("Request Headers");
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String name = headers.nextElement();
			String value = request.getHeader(name);
			LOG.info("{}: {}", name, value);
		}

		LOG.info("Request Attributes");
		Enumeration<String> AttributeNames = request.getAttributeNames();
		while (AttributeNames.hasMoreElements()) {
			String name = AttributeNames.nextElement();
			Object value = request.getAttribute(name);
			LOG.info("{}: {}", name, value);
		}

		LOG.info("Session Attributes");
		HttpSession session = request.getSession();
		Enumeration<String> attrs = session.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = attrs.nextElement();
			Object value = session.getAttribute(name);
			LOG.info("{}: {}", name, value);
		}

		Object object = request.getAttribute(WebAttributes.ACCESS_DENIED_403);
		if (null != object && object instanceof AccessDeniedException) {
			AccessDeniedException exception = (AccessDeniedException) object;
			LOG.info("exception: {}", exception);
		}

		return "error";
	}

}
