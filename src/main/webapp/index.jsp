<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="style.css">
   <title>Start Page</title>
</head>

<body style="font-size: large;">

      <div class="content">
         <div class="title">
            <h2>Simplex method</h2>
         </div>
         <div class="form">
            <form method="POST" action="table">
               <div> 
                  <span>Количество переменных</span>
                  <input type="number" name="amountVariables" value="" />
               </div>
               <div>
                  <span>Количество костант</span>
                  <input type="number" name="amountConstraints" value="" />
               </div>
            <div>
               <input type="submit" value="Submit"   style="margin: auto;padding: 8px 8px;" />
            </div>

            </form>
         </div>

      </div>
</body>
