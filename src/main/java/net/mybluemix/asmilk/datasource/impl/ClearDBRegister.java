package net.mybluemix.asmilk.datasource.impl;

import org.springframework.orm.jpa.vendor.Database;

import com.jayway.jsonpath.DocumentContext;

import net.mybluemix.asmilk.datasource.DataSourceRegister;

public class ClearDBRegister extends DataSourceRegister {

	private static final String PATH_CREDENTIALS = "$.cleardb[0].credentials";
	private static final String PATH_DATABASE = PATH_CREDENTIALS + ".name";
	private static final String PATH_HOSTNAME = PATH_CREDENTIALS + ".hostname";
	private static final String PATH_PORT = PATH_CREDENTIALS + ".port";
	private static final String PATH_USERNAME = PATH_CREDENTIALS + ".username";
	private static final String PATH_PASSWORD = PATH_CREDENTIALS + ".password";

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String C3P0_MAX_SIZE = "4";

	private static final String FORMAT_URL = "jdbc:mysql://%1$s:%2$s/%3$s?serverTimezone=UTC";

	@Override
	public void register(DocumentContext docCtx) {
		String hostname = docCtx.read(PATH_HOSTNAME);
		String port = docCtx.read(PATH_PORT);
		String database = docCtx.read(PATH_DATABASE);
		String username = docCtx.read(PATH_USERNAME);
		String password = docCtx.read(PATH_PASSWORD);

		String url = String.format(FORMAT_URL, hostname, port, database);

		super.setSystemProperty(Database.MYSQL, DRIVER, url, username, password, C3P0_MAX_SIZE);
	}

}
