package net.mybluemix.asmilk.test;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tester2 {
	
	private static final Logger LOG = LoggerFactory.getLogger(Tester2.class);

	public static void main(String[] args) {
		new Tester2().test();
	}

	void test() {
		String content = "\"1100\"";
		String regex = "\"(?<retcode>\\d+?)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		LOG.info(content);
		
		Properties properties = new Properties();
		if (matcher.find()) {
			LOG.info("find");
			String retcode = matcher.group("retcode");
			//String selector = matcher.group("selector");
			properties.put("retcode", retcode);
			//properties.put("selector", selector);
			LOG.info("retcode: {}", retcode);
			//LOG.info("selector: {}", selector);
		} else {
			LOG.info("else");
		}
	}
}
