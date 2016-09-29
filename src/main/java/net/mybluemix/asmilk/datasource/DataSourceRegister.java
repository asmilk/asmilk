package net.mybluemix.asmilk.datasource;

import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.vendor.Database;

import com.jayway.jsonpath.DocumentContext;

public abstract class DataSourceRegister {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceRegister.class);

	private static final String PROPERTY_JPA_VENDOR_ADAPTER_DATABASE = "jpa_vendor_adapter.database";
	private static final String PROPERTY_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_JDBC_DRIVER = "jdbc.driver";
	private static final String PROPERTY_JDBC_URL = "jdbc.url";
	private static final String PROPERTY_JDBC_USERNAME = "jdbc.username";
	private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";
	private static final String PROPERTY_C3P0_MAX_SIZE = "c3p0.max_size";

	public abstract void register(DocumentContext docCtx);

	public void setSystemProperty(Database database, Class<? extends Dialect> dialect, String driver,
			String url, String username, String password, String c3p0MaxSize) {
		LOG.info("{}: {}", PROPERTY_JPA_VENDOR_ADAPTER_DATABASE, database.name());
		LOG.info("{}: {}", PROPERTY_HIBERNATE_DIALECT, dialect.getName());
		LOG.info("{}: {}", PROPERTY_JDBC_DRIVER, driver);
		LOG.info("{}: {}", PROPERTY_JDBC_URL, url);
		LOG.info("{}: {}", PROPERTY_JDBC_USERNAME, username);
		LOG.info("{}: {}", PROPERTY_JDBC_PASSWORD, password);
		LOG.info("{}: {}", PROPERTY_C3P0_MAX_SIZE, c3p0MaxSize);

		System.setProperty(PROPERTY_JPA_VENDOR_ADAPTER_DATABASE, database.name());
		System.setProperty(PROPERTY_HIBERNATE_DIALECT, dialect.getName());
		System.setProperty(PROPERTY_JDBC_DRIVER, driver);
		System.setProperty(PROPERTY_JDBC_URL, url);
		System.setProperty(PROPERTY_JDBC_USERNAME, username);
		System.setProperty(PROPERTY_JDBC_PASSWORD, password);
		System.setProperty(PROPERTY_C3P0_MAX_SIZE, c3p0MaxSize);
	}

}
