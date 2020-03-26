package com.deadinside.controll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tablebuild")
public class BuildTable extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String[]> list = new ArrayList<>();

        int k = Integer.parseInt(req.getParameter("amountConstraints"));
        int z = Integer.parseInt(req.getParameter("amountVariables"));

        for (int i = 0; i < k+1; i++)
        {
          list.add( req.getParameterValues("y"+(i+1)));
        }
        list.add(req.getParameterValues("func"));
        req.getParameter("a");
    }
}
