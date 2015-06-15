<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>

<h1>Home Page</h1>

<c:if test="${!empty username}">
    <div id="signed-in-username">${username}</div>
</c:if>

<form action="signOut" method="POST">
    <input type="submit" value="Sign Out"/>
</form>

</body>
</html>