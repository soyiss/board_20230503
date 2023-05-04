<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-05-03
  Time: 오전 10:20
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
<%--  enctype="multipart/form-data" 이속성이 반드시 있어야만 파일이 전달될 수 있다   --%>
    <form action="/board/save" method="post" enctype="multipart/form-data">

        <input type="text" name="boardTitle" placeholder="글 제목을 입력하세요"><br>
        <input type="text" name="boardWriter" placeholder="작성자"><br>
        <input type="text" name="boardPass" placeholder="글 비밀번호"><br>
        <textarea type="text" name="boardContents" cols="30" rows="10" placeholder="글 내용을 입력하세요"></textarea><br>
<%--   파일 첨부를 위한 인풋( 파일 자체가 네임에 저장되서 전달됨)    --%>
        <input type="file" name="boardFile"><br>
        <input type="submit" value="작성">
    </form>
</div>



<%@include file="../component/footer.jsp"%>
</body>
</html>
