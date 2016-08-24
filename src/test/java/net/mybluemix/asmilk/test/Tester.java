package net.mybluemix.asmilk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class Tester {

	private static final Logger LOG = LoggerFactory.getLogger(Tester.class);

	public static void main(String[] args) {
		new Tester().test();

	}

	public void test() {
		DocumentContext docCtx = JsonPath.parse(this.getClass().getResourceAsStream("/asmilk_VCAP_Services.json"));
		JSONArray array = docCtx.read("$..label");
		LOG.info(array.get(0).toString());
		
		array = docCtx.read("$..label[0]");
		
		LOG.info("" + array.size());
		
	}

}
