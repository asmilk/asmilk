package net.mybluemix.asmilk.auditing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	private static final Logger LOG = LoggerFactory.getLogger(SpringSecurityAuditorAware.class);

	@Override
	public String getCurrentAuditor() {
		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof User) {
				username = ((User) principal).getUsername();
				LOG.info("username: {}", username);
			}
		}
		return username;
	}

}
