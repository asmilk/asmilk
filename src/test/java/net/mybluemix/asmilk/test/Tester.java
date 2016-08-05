package net.mybluemix.asmilk.test;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.vendor.Database;

public class Tester {

	private static final Logger LOG = LoggerFactory.getLogger(Tester.class);

	public static void main(String[] args) {
		new Tester().test();

	}

	public void test() {
		// DocumentContext docCtx =
		// JsonPath.parse(this.getClass().getResourceAsStream("/asmilk_VCAP_Services.json"));
		// JSONArray array = docCtx.read("$..label");
		// System.out.println(array.get(0));

		Properties credentials = new Properties();
		credentials.put("aaa", 111);
		credentials.put("bbb", 222);
		credentials.put("ccc", 333);
		credentials.put("ddd", 444);

		System.out.println(credentials);

		System.out.println(System.getProperties());

		System.setProperty("eee", "555");

		System.out.println(System.getProperties());

		LOG.info("{}: {}", "aaa", "111");
		
		System.out.println(Database.MYSQL.name());
	}

}
