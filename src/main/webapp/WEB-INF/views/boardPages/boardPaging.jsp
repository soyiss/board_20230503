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
  <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
</head>
<body>
<%@include file="../component/header.jsp" %>
<%@include file="../component/nav.jsp" %>

<div id="section">
  <div class="container" id="list">
    <table class="table table-striped table-hover text-center">
      <tr>
        <th>id</th>
        <th>title</th>
        <th>writer</th>
        <th>date</th>
        <th>hits</th>
      </tr>
      <c:forEach items="${boardList}" var="board">
        <tr>
          <td>${board.id}</td>
          <td>
            <a href="/board?id=${board.id}&page=${paging.page}">${board.boardTitle}</a>
          </td>
          <td>${board.boardWriter}</td>
          <td>
            <fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
          </td>
          <td>${board.boardHits}</td>
        </tr>
      </c:forEach>
    </table>
  </div><br>
  <div class="container">
    <ul class="pagination justify-content-center">


      <%--  [이전] 처리 부분    --%>
      <c:choose>
        <%-- 현재 페이지가 1페이지면 이전 글자만 보여줌(클릭이 안됨) --%>
        <c:when test="${paging.page<=1}">
        <%-- class는 부트스트랩 클래스이다 --%>
          <li class="page-item disabled ">
            <a class="page-link" >[이전]</a>
          </li>
        </c:when>
        <%-- 1페이지가 아닌 경우에는 [이전]을 클릭하면 현재 페이지보다 1 작은 페이지 요청 --%>
        <c:otherwise>
          <li class="page-item">
            <a class="page-link" style="color: black" href="/board/paging?page=${paging.page-1}">[이전]</a>
          </li>
        </c:otherwise>
      </c:choose>


      <%--  for(int i=startPage; i<=endPage; i++)      --%>
      <%--  반복변수 i는 var/ 시작값은 begin / 끝값은 end /step는 증감식      --%>
      <%--  반복문에 의해서 123페이지가 보김      --%>
      <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i" step="1">
        <c:choose>
          <%-- 요청한 페이지에 있는 경우 현재 페이지 번호는 텍스트만 보이게 --%>
          <%-- 현재 사용자가 머물고있는 페이지는 클릭이 안되고 active로 호버효과만 나오게 --%>
          <%-- eq는 == 라는 뜻  --%>
          <c:when test="${i eq paging.page}">
            <li class="page-item active">
              <a class="page-link" style="background-color: #808080;border: none;color: #111111">${i}</a>
            </li>
          </c:when>

          <c:otherwise>
            <li class="page-item">
          <%--  현재 사용자가 머문 페이지가 아니면 그걸 누르면 그 페이지로 이동하게끔 한것 --%>
          <%-- 예를 들어 [1] [2] [3] 페이지 중에 머물고있는 페이지가 2페이지이면
                1,3 번호를 클릭하면 그 1번 페이지.3번페이지로 이동할수 있게 함              --%>

              <a class="page-link" style="color: #020202" href="/board/paging?page=${i}"> ${i} </a>
            </li>
          </c:otherwise>
        </c:choose>
      </c:forEach>


        <%--  [다음] 처리 부분    --%>

      <c:choose>
        <c:when test="${paging.page>=paging.maxPage}">
          <li class="page-item disabled">
            <a class="page-link">[다음]</a>
          </li>
        </c:when>
        <c:otherwise>
          <li class="page-item">
            <a class="page-link" style="color: black" href="/board/paging?page=${paging.page+1}">[다음]</a>
          </li>
        </c:otherwise>
      </c:choose>
    </ul>
  </div>
</div>
<%@include file="../component/footer.jsp" %>
</body>
</html>
