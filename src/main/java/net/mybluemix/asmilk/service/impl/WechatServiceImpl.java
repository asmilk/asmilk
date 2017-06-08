package net.mybluemix.asmilk.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.mybluemix.asmilk.data.BaseRequest;
import net.mybluemix.asmilk.data.Dict;
import net.mybluemix.asmilk.data.Message;
import net.mybluemix.asmilk.data.RequestData;
import net.mybluemix.asmilk.data.WechatStatus;
import net.mybluemix.asmilk.service.WechatService;

@Service
public class WechatServiceImpl implements WechatService {

	private static final Logger LOG = LoggerFactory.getLogger(WechatServiceImpl.class);
	
//	private static final String PROXY_HOST = "192.168.10.1";
//	private static final int PROXY_PORT = 8080;
//	private static final String PROXY_USERNAME = "zengyd";
//	private static final String PROXY_PASSWORD = "zengyd123";

	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	/*private static final HttpRequestFactory CLIENT = new ApacheHttpTransport.Builder()
			.setProxy(new HttpHost(PROXY_HOST, PROXY_PORT))
			.setSocketFactory(WechatServiceImpl.newDefaultSSLSocketFactory()).build()
			.createRequestFactory(initializer -> initializer.setConnectTimeout(0).setReadTimeout(0)
					.setLoggingEnabled(true).setCurlLoggingEnabled(true).setFollowRedirects(true)
					.setSuppressUserAgentSuffix(true).setParser(JSON_FACTORY.createJsonObjectParser())
					.setInterceptor(request -> WechatServiceImpl.intercept(request))
					.setResponseInterceptor(response -> WechatServiceImpl.interceptResponse(response)));*/
	private static HttpRequestFactory CLIENT;
	private static final XPath XPATH = XPathFactory.newInstance().newXPath();

	private static final int RET_OK = 0;
	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final String URL_LOGIN = "http://login.wx.qq.com";
	private static final String PATH_JSLOGIN = "/jslogin";
	private static final String PATH_LOGIN = "/cgi-bin/mmwebwx-bin/login";
	private static final String PATH_INIT = "/webwxinit";
	private static final String PATH_STATUS_NOTIFY = "/webwxstatusnotify";
	private static final String PATH_SYNC = "/webwxsync";
	private static final String PATH_SEND_MSG = "/webwxsendmsg";

	private static String uuid;
	private static int tip = 1;
	private static String redirectUri;
	private static String baseUri;
	private static String passTicket;
	private static RequestData requestData;
	private static WechatStatus status = WechatStatus.NEW;
	private static String userName;
	private static String toUserName;
	private static String text;

	private static DocumentBuilder documentBuilder;

	static {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage(), e.getCause());
		}
		
		ApacheHttpTransport transport = new ApacheHttpTransport.Builder()//.setProxy(new HttpHost(PROXY_HOST, PROXY_PORT))
				.setSocketFactory(WechatServiceImpl.newDefaultSSLSocketFactory()).build();
		DefaultHttpClient client = (DefaultHttpClient) transport.getHttpClient();
//		client.getCredentialsProvider().setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
//				new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD));
		WechatServiceImpl.CLIENT = transport.createRequestFactory(initializer -> initializer.setConnectTimeout(0)
				.setReadTimeout(0).setLoggingEnabled(true).setCurlLoggingEnabled(true).setFollowRedirects(true)
				.setSuppressUserAgentSuffix(true).setParser(JSON_FACTORY.createJsonObjectParser()).setNumberOfRetries(0)
				.setIOExceptionHandler((request, supportsRetry) -> {
					LOG.info("!!!!!!######@@@@@@@@IOExceptionHandler@@@@@@@#####!!!!!!");
					return true;
				}).setUnsuccessfulResponseHandler((request, response, supportsRetry) -> {
					LOG.info("!!!!!!######@@@@@@@@UnsuccessfulResponseHandler@@@@@@@#####!!!!!!");
					return true;
				}).setThrowExceptionOnExecuteError(true).setInterceptor(request -> WechatServiceImpl.intercept(request))
				.setResponseInterceptor(response -> WechatServiceImpl.interceptResponse(response)));
		
	}

	@Autowired
	private MessageSource messageSource;

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

	private Properties getContent(InputStream input) throws IOException {
		String content = IOUtils.toString(input, DEFAULT_CHARSET);
		LOG.info("content: {}", content);

		String regex = "\\s*(?<key>\\S+?)\\s*=\\s*['\"]*(?<value>\\S+?)['\"]*\\s*;";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		Properties properties = new Properties();
		while (matcher.find()) {
			String key = matcher.group("key");
			String value = matcher.group("value");
			properties.put(key, value);
			LOG.info("{}: {}", key, value);
		}
		return properties;
	}

	private String encodeSyncKey(JSONObject syncKey) {
		Number count = syncKey.getAsNumber("Count");
		int size = count.intValue();
		LOG.info("size: {}", size);

		JSONArray list = (JSONArray) syncKey.get("List");
		JSONObject[] objects = new JSONObject[size];
		objects = list.toArray(objects);
		String result = "";
		for (JSONObject item : objects) {
			if (0 < result.length()) {
				result += "|";
			}
			String key = item.getAsString("Key");
			String val = item.getAsString("Val");
			result += key + "_" + val;
		}
		LOG.info("result: {}", result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void readMsg(DocumentContext jpath) {
		LOG.info("!!!readMsg:begin!!!");
		status = WechatStatus.SYNCHRONIZED;
		JSONArray addMsgList = jpath.read("$.AddMsgList", JSONArray.class);
		LOG.info("addMsgList: {}", addMsgList.toJSONString());

		if (null != addMsgList) {
			int size = addMsgList.size();
			LinkedHashMap<String, Object>[] map = new LinkedHashMap[size];
			map = addMsgList.toArray(map);
			for (LinkedHashMap<String, Object> item : map) {
				Integer msgType = (Integer) item.get("MsgType");
				LOG.info("msgType: {}", msgType);
				String fromUserName = (String) item.get("FromUserName");
				LOG.info("fromUserName: {}", fromUserName);
				String content = (String) item.get("Content");
				LOG.info("content: {}", content);
				switch (msgType) {
				case 1:
					if (null != content) {
						String regex =  "(?<to>\\S*?)(:<br/>)?@asrobot (?<cmd>\\w+):?(?<args>\\S*)";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(content);

						if (matcher.find()) {
							String to = matcher.group("to");
							String cmd = matcher.group("cmd");
							String args = matcher.group("args");
							LOG.info("to: {}", to);
							LOG.info("cmd: {}", cmd);
							LOG.info("args: {}", args);

							toUserName = fromUserName;
							status = WechatStatus.READED;

							switch (cmd) {
							case "intro":
								text = this.messageSource.getMessage("wechat.content.intro", null, Locale.getDefault());
								break;
							case "now":
								text = "Now: " + System.currentTimeMillis();
								break;
							case "git":
								text = "Git: https://github.com/asmilk/asmilk";
								break;
							case "dict":
								if (null != args && !"".equals(args)) {
									text = args;
									status = WechatStatus.DO_DICT;
								} else {
									text = this.messageSource.getMessage("wechat.error.bad.args",
											new Object[] { "<NULL>" }, Locale.getDefault());
								}
								break;
							case "forex":
								if (null != args && !"".equals(args)) {
									text = args;
									status = WechatStatus.DO_FOREX;
								} else {
									text = this.messageSource.getMessage("wechat.error.bad.args",
											new Object[] { "<NULL>" }, Locale.getDefault());
								}
								break;
							default:
								text = this.messageSource.getMessage("wechat.error.no.cmd", new Object[] { cmd },
										Locale.getDefault());
								break;
							}
							return;
						}
					}
					break;
				}
			}
		}
		LOG.info("!!!readMsg:end!!!");
	}

	private Properties getSyncCheck(InputStream is) throws IOException {
		String content = IOUtils.toString(is, DEFAULT_CHARSET);
		LOG.info("content: {}", content);

		String regex = "window.synccheck=\\{retcode:\"(?<retcode>\\d+?)\",selector:\"(?<selector>\\d+?)\"\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		Properties properties = new Properties();
		if (matcher.find()) {
			String retcode = matcher.group("retcode");
			String selector = matcher.group("selector");
			properties.put("retcode", retcode);
			properties.put("selector", selector);
			LOG.info("retcode: {}", retcode);
			LOG.info("selector: {}", selector);
		}
		return properties;
	}

	@Override
	public String jsLogin() throws IOException {
		LOG.info("!!!jsLogin:begin!!!");
		GenericUrl url = new GenericUrl(URL_LOGIN + PATH_JSLOGIN);
		url.set("appid", "wx782c26e4c19acffb");
		url.set("fun", "new");
		url.set("lang", "zh_CN");
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			Properties properties = this.getContent(response.getContent());
			int code = Integer.parseInt(properties.getProperty("window.QRLogin.code"));
			switch (code) {
			case HttpStatus.SC_OK:
				uuid = properties.getProperty("window.QRLogin.uuid");
				LOG.info("uuid: {}", uuid);
				status = WechatStatus.JSLOGINED;
				break;
			default:
				throw new HttpResponseException(code, "wechat.error.jslogin");
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!jsLogin:end!!!");
		}
		return uuid;
	}

	@Async
	@Override
	public void login() throws IOException, SAXException, XPathExpressionException {
		LOG.info("!!!login:begin!!!");
		LOG.info("tip: {}", tip);
		GenericUrl url = new GenericUrl(URL_LOGIN + PATH_LOGIN);
		url.set("uuid", uuid);
		url.set("tip", tip);
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			Properties properties = this.getContent(response.getContent());
			int code = Integer.parseInt(properties.getProperty("window.code"));
			switch (code) {
			case HttpStatus.SC_OK:
				redirectUri = properties.getProperty("window.redirect_uri");
				LOG.info("redirectUri: {}", redirectUri);
				baseUri = redirectUri.substring(0, redirectUri.lastIndexOf("/"));
				LOG.info("baseUri: {}", baseUri);
				status = WechatStatus.LOGINED;
				break;
			case HttpStatus.SC_CREATED:
			case HttpStatus.SC_REQUEST_TIMEOUT:
				tip = 0;
				break;
			default:
				throw new HttpResponseException(code, "wechat.error.login");
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!login:end!!!");
		}
	}

	@Async
	@Override
	public void redirect() throws IOException, SAXException, XPathExpressionException {
		LOG.info("!!!redirect:begin!!!");
		GenericUrl url = new GenericUrl(redirectUri);
		url.set("uuid", uuid);
		url.set("fun", "new");
		url.set("version", "v2");
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			Document document = documentBuilder.parse(response.getContent());
			int ret = ((Double) XPATH.evaluate("/error/ret", document, XPathConstants.NUMBER)).intValue();
			LOG.info("ret: {}", ret);
			String message = (String) XPATH.evaluate("/error/message", document, XPathConstants.STRING);
			LOG.info("message: {}", message);
			switch (ret) {
			case RET_OK:
				BaseRequest baseRequest = new BaseRequest();

				String skey = (String) XPATH.evaluate("/error/skey", document, XPathConstants.STRING);
				LOG.info("skey: {}", skey);
				baseRequest.setSkey(skey);

				String wxsid = (String) XPATH.evaluate("/error/wxsid", document, XPathConstants.STRING);
				LOG.info("wxsid: {}", wxsid);
				baseRequest.setSid(wxsid);

				Double wxuin = (Double) XPATH.evaluate("/error/wxuin", document, XPathConstants.NUMBER);
				LOG.info("wxuin: {}", wxuin);
				baseRequest.setUin(wxuin.longValue());
				requestData = new RequestData();
				requestData.setBaseRequest(baseRequest);

				passTicket = (String) XPATH.evaluate("/error/pass_ticket", document, XPathConstants.STRING);
				LOG.info("pass_ticket: {}", passTicket);
				status = WechatStatus.REDIRECTED;
				break;
			default:
				throw new HttpResponseException(ret, message);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!redirect:end!!!");
		}
	}

	@Async
	@Override
	public void init() throws IOException {
		LOG.info("!!!init:begin!!!");
		GenericUrl url = new GenericUrl(baseUri + PATH_INIT);
		url.set("pass_ticket", passTicket);
		LOG.info("url: {}", url.build());
		JsonHttpContent requestContent = new JsonHttpContent(JSON_FACTORY, requestData);
		requestContent.writeTo(System.out);
		System.out.println();
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildPostRequest(url, requestContent);
			response = request.execute();
			DocumentContext jpath = JsonPath.parse(response.getContent());
			int ret = jpath.read("$.BaseResponse.Ret", Integer.class);
			LOG.info("ret: {}", ret);
			String errMsg = jpath.read("$.BaseResponse.ErrMsg");
			LOG.info("errMsg: {}", errMsg);
			switch (ret) {
			case RET_OK:
				userName = jpath.read("$.User.UserName");
				LOG.info("userName: {}", userName);
				JSONObject syncKey = jpath.read("$.SyncKey", JSONObject.class);
				LOG.info("syncKey: {}", syncKey.toJSONString());
				requestData.setSyncKey(syncKey);
				status = WechatStatus.INITIALIZED;
				break;
			default:
				throw new HttpResponseException(ret, errMsg);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!init:end!!!");
		}
	}

	@Async
	@Override
	public void statusNotify() throws IOException {
		LOG.info("!!!statusNotify:begin!!!");
		GenericUrl url = new GenericUrl(baseUri + PATH_STATUS_NOTIFY);
		url.set("pass_ticket", passTicket);
		LOG.info("url: {}", url.build());
		requestData.setCode(3);
		requestData.setFromUserName(userName);
		requestData.setToUserName(userName);
		requestData.setClientMsgId(System.currentTimeMillis());
		JSONObject syncKey = requestData.getSyncKey();
		requestData.setSyncKey(null);
		JsonHttpContent requestContent = new JsonHttpContent(JSON_FACTORY, requestData);
		requestContent.writeTo(System.out);
		System.out.println();
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildPostRequest(url, requestContent);
			response = request.execute();
			DocumentContext jpath = JsonPath.parse(response.getContent());
			int ret = jpath.read("$.BaseResponse.Ret", Integer.class);
			LOG.info("ret: {}", ret);
			String errMsg = jpath.read("$.BaseResponse.ErrMsg");
			LOG.info("errMsg: {}", errMsg);
			switch (ret) {
			case RET_OK:
				String msgID = jpath.read("$.MsgID");
				LOG.info("msgID: {}", msgID);
				requestData.setCode(null);
				requestData.setFromUserName(null);
				requestData.setToUserName(null);
				requestData.setClientMsgId(null);
				requestData.setSyncKey(syncKey);
				status = WechatStatus.NOTIFIED;
				break;
			default:
				throw new HttpResponseException(ret, errMsg);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!statusNotify:end!!!");
		}
	}

	@Async
	@Override
	public void syncCheck() throws IOException {
		LOG.info("!!!syncCheck:begin!!!");
		GenericUrl url = new GenericUrl("https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck");
		url.set("skey", requestData.getBaseRequest().getSkey());
		url.set("sid", requestData.getBaseRequest().getSid());
		url.set("uin", requestData.getBaseRequest().getUin());
		url.set("synckey", this.encodeSyncKey(requestData.getSyncKey()));
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			Properties properties = this.getSyncCheck(response.getContent());
			int retcode = Integer.parseInt(properties.getProperty("retcode"));
			LOG.info("retcode: {}", retcode);
			switch (retcode) {
			case RET_OK:
				int selector = Integer.parseInt(properties.getProperty("selector"));
				LOG.info("selector: {}", selector);
				if (selector > 0) {
					status = WechatStatus.CHECKED;
				} else {
					status = WechatStatus.SYNCHRONIZED;
				}
				break;
			default:
				throw new HttpResponseException(retcode, "wechat.error.synccheck");
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!syncCheck:end!!!");
		}
	}

	@Async
	@Override
	public void sync() throws IOException {
		LOG.info("!!!sync:begin!!!");
		GenericUrl url = new GenericUrl(baseUri + PATH_SYNC);
		url.set("sid", requestData.getBaseRequest().getSid());
		url.set("skey", requestData.getBaseRequest().getSkey());
		url.set("pass_ticket", passTicket);
		LOG.info("url: {}", url.build());
		JsonHttpContent requestContent = new JsonHttpContent(JSON_FACTORY, requestData);
		requestContent.writeTo(System.out);
		System.out.println();
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildPostRequest(url, requestContent);
			response = request.execute();
			DocumentContext jpath = JsonPath.parse(response.getContent());
			int ret = jpath.read("$.BaseResponse.Ret", Integer.class);
			LOG.info("ret: {}", ret);
			String errMsg = jpath.read("$.BaseResponse.ErrMsg");
			LOG.info("errMsg: {}", errMsg);
			switch (ret) {
			case RET_OK:
				JSONObject syncKey = jpath.read("$.SyncKey", JSONObject.class);
				LOG.info("syncKey: {}", syncKey.toJSONString());
				requestData.setSyncKey(syncKey);
				this.readMsg(jpath);
				break;
			default:
				throw new HttpResponseException(ret, errMsg);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!sync:end!!!");
		}
	}

	@Async
	@Override
	public void sendMsg() throws IOException {
		LOG.info("!!!sendMsg:begin!!!");
		GenericUrl url = new GenericUrl(baseUri + PATH_SEND_MSG);
		url.set("pass_ticket", passTicket);
		LOG.info("url: {}", url.build());
		long now = System.currentTimeMillis();
		Message message = new Message();
		message.setType(1);
		message.setContent(text);
		message.setFromUserName(userName);
		message.setToUserName(toUserName);
		message.setLocalID(now);
		message.setClientMsgId(now);
		requestData.setMessage(message);
		requestData.setScene(0);
		JSONObject syncKey = requestData.getSyncKey();
		requestData.setSyncKey(null);
		JsonHttpContent requestContent = new JsonHttpContent(JSON_FACTORY, requestData);
		requestContent.writeTo(System.out);
		System.out.println();
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildPostRequest(url, requestContent);
			response = request.execute();
			DocumentContext jpath = JsonPath.parse(response.getContent());
			int ret = jpath.read("$.BaseResponse.Ret", Integer.class);
			LOG.info("ret: {}", ret);
			String errMsg = jpath.read("$.BaseResponse.ErrMsg");
			LOG.info("errMsg: {}", errMsg);
			switch (ret) {
			case RET_OK:
				String msgID = jpath.read("$.MsgID");
				LOG.info("msgID: {}", msgID);
				String localID = jpath.read("$.LocalID");
				LOG.info("localID: {}", localID);
				requestData.setMessage(null);
				requestData.setScene(null);
				requestData.setSyncKey(syncKey);
				status = WechatStatus.SYNCHRONIZED;
				break;
			default:
				throw new HttpResponseException(ret, errMsg);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!sendMsg:end!!!");
		}
	}

	@Async
	@Override
	public void dict() throws IOException {
		LOG.info("!!!dict:begin!!!");
		String[] args = text.split(",", 2);
		LOG.info("args: {}", Arrays.toString(args));
		String type = "jc";
		String word = "";
		if (args.length == 1 && !"".equals(args[0])) {
			word = args[0];
		} else if (args.length == 2 && ("jc".equalsIgnoreCase(args[0]) || "cj".equalsIgnoreCase(args[0]))
				&& !"".equals(args[1])) {
			type = args[0];
			word = args[1];
		} else {
			text = this.messageSource.getMessage("wechat.error.bad.args", new Object[] { text }, Locale.getDefault());
			status = WechatStatus.READED;
			return;
		}
		String lang = this.messageSource.getMessage("wechat.dict.lang." + type, null, Locale.getDefault());

		GenericUrl url = new GenericUrl("http://m.hujiang.com/d/dict_jp_api.ashx?w=" + word + "&type=" + type);
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			int statusCode = response.getStatusCode();
			LOG.info("statusCode: {}", statusCode);
			String statusMessage = response.getStatusMessage();
			LOG.info("statusMessage: {}", statusMessage);
			switch (statusCode) {
			case HttpStatus.SC_OK:
				text = "<" + lang + ">" + word;
				try {
					Dict[] dicts = new Dict[] {};
					dicts = response.parseAs(dicts.getClass());
					for (Dict dict : dicts) {
						LOG.info("dict: {}", dict);
						text += System.lineSeparator() + dict.getPinYin() + dict.getPronounceJp() + dict.getPronounce()
								+ dict.getTone() + System.lineSeparator()
								+ dict.getComment().replaceAll("<br/>", System.lineSeparator()).replaceAll("<.+?>", "");
					}

				} catch (JsonParseException | IllegalArgumentException e) {
					text = this.messageSource.getMessage("wechat.error.no.word", new Object[] { text },
							Locale.getDefault());
					LOG.error(e.getMessage(), e.getCause());
				}
				status = WechatStatus.READED;
				break;
			default:
				throw new HttpResponseException(statusCode, statusMessage);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!dict:end!!!");
		}
	}

	@Async
	@Override
	public void forex() throws IOException {
		LOG.info("!!!forex:begin!!!");
		GenericUrl url = new GenericUrl("http://www.boc.cn/sourcedb/whpj/");
		LOG.info("url: {}", url.build());
		HttpResponse response = null;
		try {
			HttpRequest request = CLIENT.buildGetRequest(url);
			response = request.execute();
			int statusCode = response.getStatusCode();
			LOG.info("statusCode: {}", statusCode);
			String statusMessage = response.getStatusMessage();
			LOG.info("statusMessage: {}", statusMessage);
			switch (statusCode) {
			case HttpStatus.SC_OK:
				String content = IOUtils.toString(response.getContent(), DEFAULT_CHARSET);
				String currency = "";
				switch (text.trim().toUpperCase()) {
				case "AED":
					currency = "阿联酋迪拉姆";
					break;
				case "AUD":
				case "澳元":
				case "澳币":
					currency = "澳大利亚元";
					break;
				case "BRL":
					currency = "巴西里亚尔";
					break;
				case "CAD":
				case "加元":
				case "加币":
					currency = "加拿大元";
					break;
				case "CHF":
					currency = "瑞士法郎";
					break;
				case "DKK":
					currency = "丹麦克朗";
					break;
				case "EUR":
					currency = "欧元";
					break;
				case "GBP":
					currency = "英镑";
					break;
				case "HKD":
				case "港元":
					currency = "港币";
					break;
				case "IDR":
					currency = "印尼卢比";
					break;
				case "INR":
					currency = "印度卢比";
					break;
				case "JPY":
				case "日币":
					currency = "日元";
					break;
				case "KRW":
				case "韩币":
				case "韩元":
					currency = "韩国元";
					break;
				case "MOP":
					currency = "澳门元";
					break;
				case "MYR":
					currency = "林吉特";
					break;
				case "NOK":
					currency = "挪威克朗";
					break;
				case "NZD":
					currency = "新西兰元";
					break;
				case "PHP":
					currency = "菲律宾比索";
					break;
				case "RUB":
					currency = "卢布";
					break;
				case "SAR":
					currency = "沙特里亚尔";
					break;
				case "SEK":
					currency = "瑞典克朗";
					break;
				case "SGD":
					currency = "新加坡元";
					break;
				case "THB":
				case "泰铢":
					currency = "泰国铢";
					break;
				case "TWD":
				case "台币":
					currency = "新台币";
					break;
				case "USD":
					currency = "美元";
					break;
				case "ZAR":
					currency = "南非兰特";
					break;
				default:
					currency = text;
				}
				String regex = "<td>(?<currency>" + currency
						+ ")</td>\\s*<td>(?<rate1>\\S*?)</td>\\s*<td>(?<rate2>\\S*?)</td>\\s*<td>(?<rate3>\\S*?)</td>\\s*<td>(?<rate4>\\S*?)</td>\\s*<td>(?<rate5>\\S*?)</td>\\s*<td>(?<date>\\S+?)</td>\\s*<td>(?<time>\\S+?)</td>";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(content);
				while (matcher.find()) {
					currency = matcher.group("currency");
					String rate1 = matcher.group("rate1");
					String rate2 = matcher.group("rate2");
					String rate3 = matcher.group("rate3");
					String rate4 = matcher.group("rate4");
					String rate5 = matcher.group("rate5");
					String date = matcher.group("date");
					String time = matcher.group("time");

					LOG.info("currency: {}", currency);
					LOG.info("rate1: {}", rate1);
					LOG.info("rate2: {}", rate2);
					LOG.info("rate3: {}", rate3);
					LOG.info("rate4: {}", rate4);
					LOG.info("rate5: {}", rate5);
					LOG.info("dateTime: {} {}", date, time);

					String name1 = this.messageSource.getMessage("wechat.forex.rate.buy.exchange", null,
							Locale.getDefault());
					String name2 = this.messageSource.getMessage("wechat.forex.rate.buy.cash", null,
							Locale.getDefault());
					String name3 = this.messageSource.getMessage("wechat.forex.rate.sell.exchange", null,
							Locale.getDefault());
					String name4 = this.messageSource.getMessage("wechat.forex.rate.sell.cash", null,
							Locale.getDefault());
					String name5 = this.messageSource.getMessage("wechat.forex.rate.boc", null, Locale.getDefault());
					String name6 = this.messageSource.getMessage("wechat.forex.rate.datetime", null,
							Locale.getDefault());

					text = currency + System.lineSeparator() + name1 + ":" + rate1 + System.lineSeparator() + name2
							+ ":" + rate2 + System.lineSeparator() + name3 + ":" + rate3 + System.lineSeparator()
							+ name4 + ":" + rate4 + System.lineSeparator() + name5 + ":" + rate5
							+ System.lineSeparator() + name6 + ":" + date + " " + time;
					status = WechatStatus.READED;
					return;
				}
				status = WechatStatus.SYNCHRONIZED;
				break;
			default:
				throw new HttpResponseException(statusCode, statusMessage);
			}
		} finally {
			if (null != response) {
				response.disconnect();
			}
			LOG.info("!!!forex:end!!!");
		}
	}

	@Override
//	@Scheduled(fixedDelay = 1000)
	public void start() throws IOException, XPathExpressionException, SAXException {
		LOG.info("!!!start:begin!!!");
		try {
			switch (status) {
			case NEW:
				break;
			case JSLOGINED:
				this.login();
				break;
			case LOGINED:
				this.redirect();
			case REDIRECTED:
				this.init();
			case INITIALIZED:
				this.statusNotify();
			case NOTIFIED:
			case SYNCHRONIZED:
				this.syncCheck();
				break;
			case CHECKED:
				this.sync();
				break;
			case READED:
				this.sendMsg();
				break;
			case DO_DICT:
				this.dict();
				break;
			case DO_FOREX:
				this.forex();
				break;
			case FAILED:
				break;
			default:
				break;
			}
		} catch (HttpResponseException e) {
			status = WechatStatus.FAILED;
			e.printStackTrace();
		} finally {
			LOG.info("!!!start:end!!!");
		}
	}

}
