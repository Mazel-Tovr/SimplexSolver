<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="style.css">
   <title>Result</title>
</head>

<body style="font-size: large;">

      <div class="content">
         <div class="title">
            <h2>Simplex result</h2>
         </div>
        <div >
         <div style="width: 100%;">
            <c:forEach var="result" items="${resultList}">
              <br> ${result}
            </c:forEach>
          </div>
        </div>
        </div>
      </div>
</body>
