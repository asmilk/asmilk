package net.mybluemix.asmilk.datasource.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.orm.jpa.vendor.Database;

import com.jayway.jsonpath.DocumentContext;

import net.mybluemix.asmilk.datasource.DataSourceRegister;

public class ElephantSQLRegister extends DataSourceRegister {

	private static final String PATH_CREDENTIALS = "$.elephantsql[0].credentials";
	private static final String PATH_URI = PATH_CREDENTIALS + ".uri";

	private static final String DRIVER = "org.postgresql.Driver";
	private static final String C3P0_MAX_SIZE = "5";

	private static final String REGEX_URI = "postgres://(?<username>\\w+):(?<password>[\\w-]+)@(?<hostname>[\\w\\.]+):(?<port>[\\d]+)/(?<database>\\w+)";
	private static final String FORMAT_URL = "jdbc:postgresql://%1$s:%2$s/%3$s";

	@Override
	public void register(DocumentContext docCtx) {
		String uri = docCtx.read(PATH_URI);
		Matcher matcher = Pattern.compile(REGEX_URI).matcher(uri);
		if (matcher.find()) {
			String hostname = matcher.group("hostname");
			String port = matcher.group("port");
			String database = matcher.group("database");
			String username = matcher.group("username");
			String password = matcher.group("password");

			String url = String.format(FORMAT_URL, hostname, port, database);

			super.setSystemProperty(Database.POSTGRESQL, DRIVER, url, username, password, C3P0_MAX_SIZE);
		}
	}

}
