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

	});
</script>
</head>
<body>
	<div class="menu">
		<a href="/index">index</a> <a href="/account/form">form</a>
		<form:form commandName="account" action="/logout">
			<input type="submit" value="logout" />
			<div id="message">
				<form:errors path="*" cssClass="error" />
			</div>
		</form:form>
	</div>
	<span>${requestScope.pageImpl.numberOfElements}</span>
	<div class="body">
		<c:if test="${requestScope.pageImpl.numberOfElements > 0}">
			<div class="paging">
				<span>page:${requestScope.pageImpl.number + 1}/${requestScope.pageImpl.totalPages}</span> <span>count:${requestScope.pageImpl.numberOfElements}</span>
				<span>total:${requestScope.pageImpl.totalElements}</span> <br />
				<c:if test="${requestScope.pageImpl.totalPages > 0}">
					<a href="/account/all/0/${requestScope.pageImpl.size}">|<</a>
				</c:if>
				<c:if test="${requestScope.pageImpl.number > 0 }">
					<a href="/account/all/${requestScope.pageImpl.number - 1}/${requestScope.pageImpl.size}"><</a>
				</c:if>
				<c:if test="${requestScope.pageImpl.number + 1 < requestScope.pageImpl.totalPages}">
					<a href="/account/all/${requestScope.pageImpl.number + 1}/${requestScope.pageImpl.size}">></a>
				</c:if>
				<c:if test="${requestScope.pageImpl.totalPages > 0}">
					<a href="/account/all/${requestScope.pageImpl.totalPages - 1}/${requestScope.pageImpl.size}">>|</a>
				</c:if>
			</div>
			<div class="content">

				<table>
					<tr>
						<td>id</td>
						<td>name</td>
					</tr>
					<c:forEach var="account" items="${requestScope.pageImpl.content}">
						<tr>
							<td><a href="/account/${account.id}">${account.id}</a></td>
							<td>${account.name}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
	</div>
</body>
</html>