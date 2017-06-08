package net.mybluemix.asmilk.test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Tester {

	private static final Logger LOG = LoggerFactory.getLogger(Tester.class);

	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

//	private static final String PROXY_HOST = "192.168.10.1";
//	private static final int PROXY_PORT = 8080;
//	private static final String PROXY_USERNAME = "zengyd";
//	private static final String PROXY_PASSWORD = "zengyd123";

	private static HttpRequestFactory FACTORY;

	static {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		LOG.info("Installed root logger handler.");

		ApacheHttpTransport transport = new ApacheHttpTransport.Builder()//.setProxy(new HttpHost(PROXY_HOST, PROXY_PORT))
				.setSocketFactory(Tester.newDefaultSSLSocketFactory()).build();
		DefaultHttpClient client = (DefaultHttpClient) transport.getHttpClient();
		//client.getCredentialsProvider().setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
				//new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD));
		Tester.FACTORY = transport.createRequestFactory(initializer -> initializer.setConnectTimeout(0)
				.setReadTimeout(0).setLoggingEnabled(true).setCurlLoggingEnabled(true).setFollowRedirects(true)
				.setSuppressUserAgentSuffix(true).setParser(JSON_FACTORY.createJsonObjectParser()).setNumberOfRetries(0)
				.setIOExceptionHandler((request, supportsRetry) -> {
					LOG.info("!!!!!!######@@@@@@@@IOExceptionHandler@@@@@@@#####!!!!!!");
					return true;
				}).setUnsuccessfulResponseHandler((request, response, supportsRetry) -> {
					LOG.info("!!!!!!######@@@@@@@@UnsuccessfulResponseHandler@@@@@@@#####!!!!!!");
					return true;
				}).setThrowExceptionOnExecuteError(true).setInterceptor(request -> Tester.intercept(request))
				.setResponseInterceptor(response -> Tester.interceptResponse(response)));
	}

	private static SSLSocketFactory newDefaultSSLSocketFactory() {
		SSLSocketFactory sslSocketFactory = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, null);
			sslSocketFactory = new SSLSocketFactory(sslContext);
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			LOG.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		return sslSocketFactory;
	}

	private static void intercept(HttpRequest request) throws IOException {

		// request.getHeaders().put("X-Requested-With", "XMLHttpRequest");
		// request.getHeaders().put("Referer",
		// "http://osp.voicecloud.cn/index.php/default/qa/index/general/57f7265f?pagefrom=chat");

		request.getHeaders().setContentType("application/json;charset=UTF-8");
		HttpHeaders httpHeaders = request.getHeaders();
		Set<Entry<String, Object>> set = httpHeaders.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			Object value = entry.getValue();
			LOG.info("{}: ({}){}", key, value.getClass(), value);
		}
	}

	private static void interceptResponse(HttpResponse response) throws IOException {
		int statusCode = response.getStatusCode();
		String message = response.getStatusMessage();
		LOG.info("statusCode: {}", statusCode);
		LOG.info("message: {}", message);
		if (response.isSuccessStatusCode()) {
			HttpHeaders httpHeaders = response.getHeaders();
			Set<Entry<String, Object>> set = httpHeaders.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				Object value = entry.getValue();
				LOG.info("{}: ({}){}", key, value.getClass(), value);
			}
		} else {
			throw new HttpResponseException(statusCode, message);
		}
	}

	public void test() {
		LOG.info("!!!Tester.test() - BEGIN!!!");
		GenericUrl url = new GenericUrl("https://login.wx.qq.com/jslogin");
		url.set("appid", "wx782c26e4c19acffb");
		url.set("fun", "new");
		url.set("lang", "zh_CN");
		LOG.info("url: {}", url.build());
		try {
			// Account account = new Account();
			// account.setId(123);
			// account.setName(String.valueOf(System.currentTimeMillis()));
			// JsonHttpContent content = new JsonHttpContent(JSON_FACTORY,
			// account);
			HttpRequest request = Tester.FACTORY.buildGetRequest(url);
			// HttpRequest request = Tester.FACTORY.buildGetRequest(url);
			HttpResponse response = request.execute();
			int code = response.getStatusCode();
			String message = response.getStatusMessage();
			LOG.info("{}: {}", code, message);
			System.out.println(IOUtils.toString(response.getContent(), "UTF-8"));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		} finally {
			LOG.info("!!!Tester.test() - END!!!");
		}
	}

	public static void main(String[] args) {
		new Tester().test();
		// int math = Math.max(1, 2);
		// System.out.println(math);

		// System.gc();
		// Map map = new HashMap();
		// Object obj = map.get("aaa");
		// System.out.println("obj=" + obj);
		// Long a = (Long) obj;
		// System.out.println("a=" + a);

	}

}
