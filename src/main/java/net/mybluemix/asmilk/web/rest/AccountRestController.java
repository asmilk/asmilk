package net.mybluemix.asmilk.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.service.AccountService;

@RestController
@RequestMapping(path = "/account", produces = APPLICATION_JSON_UTF8_VALUE)
public class AccountRestController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountRestController.class);

	@Autowired
	private AccountService accountService;

	// @RequestMapping(path = { "/{id}" }, method = GET)
	@GetMapping("/{id}")
	public Object find(@ModelAttribute("account") Account account, BindingResult result, HttpServletResponse response,
			@PathVariable("id") Long id) {
		LOG.info("!!!AccountRestController.find()!!!");
		LOG.info("account: {}", account);
		LOG.info("id: {}", id);
		Object object = null;
		if (null == id || id <= 0) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			result.rejectValue("id", "account.id.illegal");
			object = result.getFieldErrors();
		} else {
			object = this.accountService.findOne(id);
			if (null == object) {
				result.rejectValue("id", "account.find.null", new Object[] { id }, "!!!No Found Such Account!!!");
				object = result.getFieldErrors();
			}
		}
		LOG.info("object: {}", object);
		return object;
	}

	@RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8_VALUE)
	public Object put(@Valid @RequestBody Account account, BindingResult result, HttpServletResponse response) {
		LOG.info("!!!AccountRestController.put()!!!");
		LOG.info("account: {}", account);
		Object object = null;
		if (result.hasErrors()) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				LOG.info("error: {}", error);
				String field = error.getField();
				String defaultMessage = error.getDefaultMessage();
				LOG.info("field: {}", field);
				LOG.info("defaultMessage: {}", defaultMessage);
			}
			object = errors;
		} else {
			object = this.accountService.save(account);
		}
		LOG.info("object: {}", object);
		return object;
	}

	// @RequestMapping(path = { "/max" }, method = GET)
	@GetMapping("/max")
	public ResponseEntity<Account> max() {
		LOG.info("!!!AccountRestController.max()!!!");
		Account account = this.accountService.findMax();
		LOG.info("account: {}", account);
		if (null == account) {
			return ResponseEntity.badRequest().header("errors", "account.any.null").build();
		}
		return ResponseEntity.ok(account);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<Account>> all(@ModelAttribute("account") Account account, BindingResult result,
			@RequestParam("page") int index, @RequestParam("rows") int size, @RequestParam("sort") String sort,
			@RequestParam("order") String order, @RequestParam("name") String name) {
		LOG.info("!!!AccountController.all()!!!");
		LOG.info("account: {}", account);
		LOG.info("index: {}", index);
		LOG.info("size: {}", size);
		LOG.info("sort: {}", sort);
		LOG.info("order: {}", order);
		LOG.info("name: {}", name);

		PageRequest pageRequest = new PageRequest(index - 1, size, JpaSort.unsafe(Direction.fromString(order), sort));
		Page<Account> page = null;
		if (null != name && !name.equals("")) {
			Account probe = new Account();
			probe.setName(name);
			ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.contains());
			Example<Account> example = Example.of(probe, matcher);
			page = this.accountService.findAll(example, pageRequest);
		} else {
			page = this.accountService.findAll(pageRequest);
		}
		List<Account> list = page.getContent();
		for (Account item : list) {
			LOG.info("item: {}", item);
		}
		return ResponseEntity.ok(page);
	}

	@RequestMapping(path = { "/sysgc" }, method = GET)
	public void sysGC() {
		LOG.info("!!!AccountRestController.sysGC()!!!");
		this.accountService.sysGC();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Long> delete1(@PathVariable("id") Long id) {
		LOG.info("!!!AccountRestController.delete()!!!");
		LOG.info("id: {}", id);
		this.accountService.delete(id);
		return ResponseEntity.ok(id);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody Account[] accounts) {
		LOG.info("!!!AccountRestController.delete()!!!");
		for (Account account : accounts) {
			LOG.info("accounts: {}", account);
			this.accountService.delete(account.getId());
		}
		return ResponseEntity.ok("");
	}

}
