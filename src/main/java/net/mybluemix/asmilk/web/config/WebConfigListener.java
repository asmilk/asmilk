package net.mybluemix.asmilk.web.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import net.mybluemix.asmilk.datasource.DataSourceRegister;
import net.mybluemix.asmilk.datasource.impl.ClearDBRegister;
import net.mybluemix.asmilk.datasource.impl.ElephantSQLRegister;

@WebListener
public class WebConfigListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(WebConfigListener.class);

	private static final String NAME_SERVICES_ENV = "VCAP_SERVICES";

	private DataSourceRegister dataSourceRegister;

	public DataSourceRegister getDataSourceRegister() {
		return dataSourceRegister;
	}

	public void setDataSourceRegister(DataSourceRegister dataSourceRegister) {
		this.dataSourceRegister = dataSourceRegister;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.installRootLoggerHandler();
		this.registerDataSource();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	private void installRootLoggerHandler() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		LOG.info("Installed root logger handler.");
	}

	private void registerDataSource() {
		String services = System.getenv(NAME_SERVICES_ENV);
		if (null != services && !services.equals("")) {
			DocumentContext docCtx = JsonPath.parse(services);
			String label = String.valueOf(docCtx.read("$..label", JSONArray.class).get(0));
			LOG.info("Data Source Label: {}", label);
			switch (label) {
			case "elephantsql":
				this.setDataSourceRegister(new ElephantSQLRegister());
				break;
			case "cleardb":
			default:
				this.setDataSourceRegister(new ClearDBRegister());
			}
			this.getDataSourceRegister().register(docCtx);
		} else {
			LOG.info("No found data source services.");
		}
	}

}
