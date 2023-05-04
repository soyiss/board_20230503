<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-04-28
  Time: 오후 2:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="/resources/css/main.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
<%@include file="../component/header.jsp" %>
<%@include file="../component/nav.jsp" %>

<div id="section">

  <form action="/board/" method="post" name="deleteForm">
    <input type="text" name="boardPass" id="board-pass" placeholder="비밀번호를 입력하세요"> <br>
    <input type="button" value="수정" onclick="delete_req()">
  </form>

</div>
<%@include file="../component/footer.jsp" %>
</body>
<script>
  const delete_req = () => {
    const passInput = document.getElementById("board-pass").value;
    const passDB = '${board.boardPass}';
    if (passInput == passDB) {
      document.deleteForm.submit();
    } else {
      alert("비밀번호가 일치하지 않습니다!!");
    }
  }
</script>
</html>