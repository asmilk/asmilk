package net.mybluemix.asmilk.service;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public interface WechatService {

	String jsLogin() throws IOException;
	
	void start() throws IOException, XPathExpressionException, SAXException;

	void login() throws IOException, SAXException, XPathExpressionException;

	void redirect() throws IOException, SAXException, XPathExpressionException;

	void init() throws IOException;

	void statusNotify() throws IOException;

	void syncCheck() throws IOException;

	void sync() throws IOException;
	
	void sendMsg() throws IOException;
	
	void dict() throws IOException;
	
	void forex() throws IOException;

}
