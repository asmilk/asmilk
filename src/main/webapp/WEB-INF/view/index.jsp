<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<sec:csrfMetaTags />
<title><fmt:message key="index.title" /></title>
<link type="image/x-icon" rel="shortcut icon" href="/favicon.ico" />
<link type="text/css" rel="stylesheet" href="/style/style.css" />
<script type="text/javascript" src="/script/account.js"></script>
</head>
<body>
	<span>INDEX PAGE</span>
	<br />
	<span>${sessionScope['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE']}</span>
	<br />
	<span>Locale:<%=request.getLocale()%></span>
	<br />
	<fmt:message key="AbstractUserDetailsAuthenticationProvider.badCredentials" />
	<br />
	<img alt="newapp-icon" src="/image/newapp-icon.png" />
	<br />
	<sec:authorize access="isAnonymous()">
		<span>[isAnonymous]</span>
		<form:form commandName="user" action="/login">
			<div id="message">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</div>
			<div>
				<form:label path="username">username:</form:label>
				<form:input path="username" />
			</div>
			<div>
				<form:label path="password">password:</form:label>
				<form:password path="password" />
			</div>
			<div>
				<input type="text" name="captcha" value="" />
			</div>
			<div>
				<input type="checkbox" name="remember-me" value="true" />
				<span>Remember me</span>
			</div>
			<div>
				<input type="submit" value="login" />
			</div>
		</form:form>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<span>[isAuthenticated]</span>
		<div>
			<span>username:</span>
			<sec:authentication property="principal.username" />
		</div>
		<a href="/account/index">account</a>
		<form:form commandName="user" action="/logout">
			<input type="submit" value="logout" />
		</form:form>
	</sec:authorize>
	<sec:authorize access="hasRole('USER')">
		<span>[ROLE_USER]</span>
	</sec:authorize>
	<sec:authorize access="hasRole('ADMIN')">
		<span>[ROLE_ADMIN]</span>
	</sec:authorize>
</body>
</html>