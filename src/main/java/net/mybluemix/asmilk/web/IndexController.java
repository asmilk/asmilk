package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.service.AccountService;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	AccountService accountService;

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@RequestMapping(path = { "/", "/index" }, method = GET)
	public String index() {
		LOG.info("!!!IndexController.index()!!!");
		Account account = this.getAccountService().findOne(2L);
		LOG.info(account.toString());
		return "index";
	}

	@RequestMapping(path = { "/save" }, method = GET)
	public String save() {
		LOG.info("!!!IndexController.save()!!!");
		Account account = new Account();
		account.setName(String.valueOf(System.currentTimeMillis()));
		account = this.getAccountService().save(account);
		LOG.info(account.toString());
		return "index";
	}

}
