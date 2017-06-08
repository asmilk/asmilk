package net.mybluemix.asmilk.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(FormLoginFilter.class);
	private static final String AUTH_CAPTCHA_WRONG = "auth.captcha.wrong";

	@Autowired
	private MessageSource messageSource;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LOG.info("!!!FormLoginFilter.attemptAuthentication()!!!");

		String captcha = request.getParameter("captcha");
		LOG.info("captcha: {}", captcha);

		if (!"abc".equalsIgnoreCase(captcha)) {
			throw new CaptchaException(
					this.messageSource.getMessage(AUTH_CAPTCHA_WRONG, null, LocaleContextHolder.getLocale()));
		}

		return super.attemptAuthentication(request, response);
	}

}
