<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/style.css" />" />
	<title><spring:message code="home.title" /></title>
</head>
<body>
	<div class="all">
		<header class="top">
			<h1><spring:message code="home.title" /></h1>
			<jsp:include page="templates/navigation.jsp"></jsp:include>
		</header>
		
		<div class="content">
			<p>
			<sp:loggedIn status="true">
				<p> hello, ${loggedInUser.identifier}</p>
				<p>
					<c:url value="logout" var="logout"></c:url>
					<a href="${logout}">Logout</a>
				</p>
			</sp:loggedIn>
			
			<sp:loggedIn status="false">
				<jsp:include page="templates/login.jsp" />
				<a href="register"><spring:message code="register.new" /></a>
			</sp:loggedIn>
			</p>
		</div>
		
		<jsp:include page="templates/footer.jsp"></jsp:include>
	</div>
</body>
</html>