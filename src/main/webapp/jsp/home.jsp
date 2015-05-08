<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>

<c:if test="${!empty username}">
    <div id="signed-in-username">${username}</div>
</c:if>

</body>
</html>