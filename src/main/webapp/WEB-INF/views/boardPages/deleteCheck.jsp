<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-04-28
  Time: 오후 2:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/resources/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
<%@include file="../component/header.jsp"%>
<%@include file="../component/nav.jsp"%>
<div id="section">
    <input type="text" id="board-pass" placeholder="비밀번호"><br>
    <button onclick="pass_check()">확인</button>

</div>

<%@include file="../component/footer.jsp"%>
</body>
<script>
    const pass_check = () => {
        const passInput = document.getElementById("board-pass").value;
        const passDB = '${board.boardPass}';
        const id = '${board.id}';
        if (passInput == passDB) {
            location.href = "/board/delete?id=" + id;
        } else {
            alert("비밀번호가 일치하지 않습니다!!");
        }
    }
</script>
</html>