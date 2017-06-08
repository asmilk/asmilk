package net.mybluemix.asmilk.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;
	
	@GetMapping("/index")
	public String index() {
		LOG.info("!!!AccountController.index()!!!");
		return "account/index";
	}

	@GetMapping("/form")
	public String form(@ModelAttribute("account") Account account) {
		LOG.info("!!!AccountController.form()!!!");
		LOG.info("account: {}", account);
		return "account/form";
	}

	@PostMapping("/save")
	public String save(Model model, @Valid @ModelAttribute("account") Account account, BindingResult result) {
		LOG.info("!!!AccountController.save()!!!");
		LOG.info("account: {}", account);
		List<ObjectError> errors = result.getAllErrors();
		for (ObjectError error : errors) {
			LOG.info("error: {}", error);
		}
		if (!result.hasErrors()) {
			account = this.accountService.save(account);
			LOG.info("account: {}", account);
			model.addAttribute(account);
		}
		return "account/form";
	}

	@GetMapping("/{id}")
	public String find(Model model, @ModelAttribute("account") Account account, @PathVariable("id") Long id) {
		LOG.info("!!!AccountController.find()!!!");
		LOG.info("account: {}", account);
		LOG.info("id: {}", id);
		account = this.accountService.findOne(id);
		LOG.info("account: {}", account);
		model.addAttribute(account);
		return "account/form";
	}

	@GetMapping("/max")
	public String max(Model model, @ModelAttribute("account") Account account) {
		LOG.info("!!!AccountController.max()!!!");
		LOG.info("account: {}", account);
		account = this.accountService.findMax();
		LOG.info("account: {}", account);
		model.addAttribute(account);
		return "account/form";
	}

	@GetMapping("/all/{index}/{size}")
	public String all(Model model, @ModelAttribute("account") Account account, BindingResult result,
			@PathVariable("index") int index, @PathVariable("size") int size) {
		LOG.info("!!!AccountController.all()!!!");
		LOG.info("account: {}", account);
		LOG.info("index: {}", index);
		LOG.info("size: {}", size);
		Page<Account> page = this.accountService.findAll(new PageRequest(index, size));
		List<Account> list = page.getContent();
		for (Account item : list) {
			LOG.info("item: {}", item);
		}
		model.addAttribute(page);
		return "account/all";
	}

	@GetMapping("/sysgc")
	public String sysGC(@ModelAttribute("account") Account account) {
		LOG.info("!!!AccountController.sysGC()!!!");
		LOG.info("account: {}", account);
		this.accountService.sysGC();
		return "account/form";
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public String handleException(Model model, ConstraintViolationException exception) {
		LOG.info("!!!AccountController.handleException()!!!");
		List<String> messages = new ArrayList<String>();
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			String message = violation.getMessage();
			LOG.info("message: {}", message);
			messages.add(message);
		}
		model.addAttribute("errors", messages);
		model.addAttribute(new Account());
		return "account/form";
	}

}
