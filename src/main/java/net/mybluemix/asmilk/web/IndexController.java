package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import net.mybluemix.asmilk.domain.security.User;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping(path = { "/", "/index" }, method = GET)
	public String index(@ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) {
		LOG.info("!!!IndexController.index()!!!");
		LOG.info("Request Headers");
		
		Locale locale = request.getLocale();
		LOG.info("locale: {}", locale);
		
		locale = LocaleContextHolder.getLocale();
		LOG.info("locale: {}", locale);
		
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String name = headers.nextElement();
			String value = request.getHeader(name);
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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				LOG.info("principal: {}", username);
			} else {
				String username = principal.toString();
				LOG.info("principal: {}", username);
			}
		}

		String user1 = request.getRemoteUser();
		LOG.info("request.remoteUser: {}", user1);

		Principal principal = request.getUserPrincipal();
		LOG.info("request.principal: {}", principal);

		if (null != principal) {
			String name = principal.getName();
			LOG.info("request.principal.name: {}", name);
		}

		boolean isAdmin = request.isUserInRole("ADMIN");
		LOG.info("isAdmin: {}", isAdmin);

		boolean isUser = request.isUserInRole("USER");
		LOG.info("isUser: {}", isUser);

		if (null != result) {
			LOG.info("result: {}", result);
		}

		Object object = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (null != object && object instanceof AuthenticationException) {
			AuthenticationException exception = (AuthenticationException) object;
			LOG.info("exception: {}", exception);
		}

		if (null != user) {
			LOG.info("user: {}", user);
		}
		
		

		return "index";
	}

}
