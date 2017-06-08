package net.mybluemix.asmilk.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import net.mybluemix.asmilk.service.WechatService;

@Controller
@RequestMapping("/wechat")
public class WechatController {

	private static final Logger LOG = LoggerFactory.getLogger(WechatController.class);

	@Autowired
	private WechatService wechatService;

	// @RequestMapping(path = "/login", method = GET, headers =
	// "Accept=application/json")
	// public Account login() {
	// Account account = new Account();
	// account.setId(123456L);
	// account.setName("cnjcndjs");
	// return account;
	// }
	//
	// @RequestMapping(path = "/save", method = PUT, consumes =
	// "application/json")
	// public void save(@RequestBody Account account) {
	// LOG.info("account: {}", account);
	// }

	@RequestMapping(path = "/", method = GET)
	public String jsLogin(Model model) throws IOException, SAXException, XPathExpressionException {
		String uuid = this.wechatService.jsLogin();
		LOG.info("uuid: {}", uuid);
		model.addAttribute("uuid", uuid);
		return "wechat";
	}

}
