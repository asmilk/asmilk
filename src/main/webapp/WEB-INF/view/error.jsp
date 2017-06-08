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
<title><fmt:message key="error.title" /></title>
<link type="image/x-icon" rel="shortcut icon" href="/favicon.ico" />
<link type="text/css" rel="stylesheet" href="/style/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="/style/style.css" />
<script type="text/javascript" src="/script/jquery-3.1.1.min.js"></script>
</head>
<body>
	<span>ERROR PAGE</span>
	<div id="message">
		<span class="error">${requestScope.SPRING_SECURITY_403_EXCEPTION.message}</span>
		<span class="error">${requestScope.exception.message}</span>
	</div>
</body>
</html>