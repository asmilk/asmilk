<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<form:form commandName="account" action="/account/save">
		<div>
			<%-- <form:label path="id">Id:</form:label> --%>
			<form:input class="easyui-textbox" path="id" style="width:100%" data-options="label:'Id:',readonly:true"/>
		</div>
		<div>
			<%-- <form:label path="name">Name:</form:label> --%>
			<form:input class="easyui-textbox" path="name" style="width:100%" data-options="label:'Name:',required:true,validType:'length[5,10]'" />
		</div>
		<div>
			<form:hidden path="version" />
		</div>
	</form:form>
</body>
</html>