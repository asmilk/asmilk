<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="wechat.title" /></title>
<link type="text/css" rel="stylesheet" href="/style/jquery-ui.css"></link>
<link type="text/css" rel="stylesheet" href="/style/style.css"></link>
<script type="text/javascript" src="/script/jquery-3.1.0.js"></script>
</head>
<body>
	<img class="qrcode" alt="qrcode" src="https://login.weixin.qq.com/qrcode/${uuid}"></img>	
</body>
</html>