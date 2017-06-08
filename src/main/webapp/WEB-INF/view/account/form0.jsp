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
<title><fmt:message key="account.title" /></title>
<link type="image/x-icon" rel="shortcut icon" href="/favicon.ico" />
<link type="text/css" rel="stylesheet" href="/style/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="/style/style.css" />
<script type="text/javascript" src="/script/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="/script/account.js"></script>
<script type="text/javascript">
	$(function() {
		new Account().init();
	});
</script>
</head>
<body>
	<div class="menu">
		<a href="/index">index</a>
		<a href="/account/all/0/5">all</a>
		<form:form commandName="account" action="/logout">
			<input type="submit" value="logout" />
		</form:form>
	</div>
	<div class="body">
		<form:form commandName="account" action="/account/save">
			<div id="message">
				<form:errors path="*" cssClass="error" />
				<c:forEach var="error" items="${requestScope.errors}">
					<span class="error">${error}</span>
				</c:forEach>
			</div>
			<div>
				<form:label path="id">id:</form:label>
				<form:input path="id" />
			</div>
			<div>
				<form:label path="name">name:</form:label>
				<form:input path="name" />
			</div>
			<div>
				<form:hidden path="version" />
				<input id="button_clear" type="button" value="clear" /> <input
					id="button_find" type="button" value="find" /> <input
					id="button_put" type="button" value="put" /> <input
					id="button_max" type="button" value="max" /> <input
					id="button_sysgc" type="button" value="sysgc" /> <input
					id="button_save" type="submit" value="save" />
			</div>
		</form:form>
	</div>
</body>
</html>