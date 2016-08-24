package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@RequestMapping(path = { "/save" }, method = GET)
	public String save() {
		LOG.info("!!!AccountController.save()!!!");
		Account account = new Account();
		account.setName(String.valueOf(System.currentTimeMillis()));
		account = this.getAccountService().save(account);
		LOG.info(account.toString());
		return "index";
	}

	@RequestMapping(path = { "/{id}" }, method = GET)
	public String find(@PathVariable("id") Long id) {
		LOG.info("!!!AccountController.find()!!!");
		Account account = this.getAccountService().findOne(id);
		LOG.info(account.toString());
		return "index";
	}
	
	@RequestMapping(path = { "/max" }, method = GET)
	public String max() {
		LOG.info("!!!AccountController.all()!!!");
		Account account = new Account();
		account = this.getAccountService().findMax();
		LOG.info(account.toString());
		return "index";
	}

}
