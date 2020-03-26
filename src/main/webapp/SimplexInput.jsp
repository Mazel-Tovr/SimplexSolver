<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simplex</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
    <div class="content">
        <div class="title">
           <h2>Simplex method</h2>
        </div>
        <div class="form">
           <form method="POST" action="tablebuild">
           
            <div style="overflow: auto;">
                <table class="table" border=1 style="width: 100%; margin-top: 20px;text-align: center;">
                    <thead>
                        <tr>
                            <th></th>
                                <c:forEach var = "i" begin = "1" end ='<%= Integer.parseInt(request.getParameter("amountVariables"))%>'>
                                    <th><c:out value = "X${i}"/></th>
                                </c:forEach>
                            <th></th>
                            <th></th>    
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var = "i" begin = "1" end = '<%= Integer.parseInt(request.getParameter("amountConstraints"))%>'>
                            <tr>
                                <td><c:out value="y${i}"/></td>
                                <c:forEach begin = "1" end = '<%= Integer.parseInt(request.getParameter("amountVariables"))%>'>
                                    <td><input type="number" name = "y${i}"></td>
                                </c:forEach>
                                <td>
                                    <select name = "y${i}">
                                        <option >=</option>
                                        <option ><=</option>
                                        <option >>=</option>
                                    </select>
                                </td>
                                <td><input type="number" name = "y${i}"></td>
                            </tr>
                        </c:forEach>
                            <tr>
                                <td>z</td>
                                <c:forEach begin = "1" end = '<%= Integer.parseInt(request.getParameter("amountVariables"))%>'>
                                    <td> <input type="number" name = "func"></td>
                                </c:forEach>

                                <td>
                                <select name = "func">
                                    <option >min</option>
                                    <option >max</option>
                                </select>
                                </td>
                                <td><input type="number" name = "func"></td>
                            </tr>
                    </tbody>
                 </table>
            </div>
            <div>
                <table class="table" border=1 style="width: 50%; margin-top: 20px;text-align: center;">
                    <thead>
                    </thead>
                    <tbody>
                     <c:forEach var="i" begin = "1" end = '<%= Integer.parseInt(request.getParameter("amountVariables"))%>'>
                        <tr>
                            <td> <c:out value = "X${i}"/></td>
                                <td>
                                    <select name = "x${i}">
                                        <option ><</option>
                                        <option >></option>
                                    </select>
                                </td>
                                <td>0</td>
                        </tr>
                      </c:forEach>
                    </tbody>
                </table>
            <div>
            <div class="button__">
             <input type "text" name = "amountVariables" value='<%=request.getParameter("amountVariables") %>' style="display:none;"/>
             <input type "text" name = "amountConstraints" value='<%=request.getParameter("amountConstraints") %>' style="display:none;"/>
             <input type="submit" value="Submit"   style="padding: 15px 25px;" />
            </div>
           </form>
        </div>
    </div>
</body>
</html>