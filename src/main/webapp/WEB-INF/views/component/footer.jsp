<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-04-28
  Time: 오전 9:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="footer">
<%--    <p>ICIA Academy Copyright&copy;2023</p>--%>
</div>
<script>
    const date = new Date();

    // 자바스크립스에서도 현재시간을 알 수있다
    console.log(date);

    //현재 년도값만 나타냄
    console.log(date.getFullYear());
    const footer = document.getElementById("footer");
    footer.innerHTML = "<p>&copy;"+ date.getFullYear() + "HRDKOREA All rights reserved</p>"
</script>