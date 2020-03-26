package com.deadinside.controll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/table")
public class MainPage extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Integer amountVariables =   Integer.parseInt(req.getParameter("amountVariables"));
//        req.setAttribute("amountVariables",amountVariables);
//        Integer amountConstraints =   Integer.parseInt(req.getParameter("amountConstraints"));
//        req.setAttribute("amountConstraints",amountConstraints);
        req.getRequestDispatcher("/SimplexInput.jsp").forward(req, resp);
    }
}
