package com.deadinside.controll;

import com.deadinside.simplex.DateParser;
import com.deadinside.simplex.SimplexProblem;
import com.deadinside.simplex.SimplexSolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/tablebuild")
public class BuildTable extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SimplexProblem p = DateParser.parse(inputBuilder(req));
        SimplexSolver solver = new SimplexSolver();
        solver.solve(p);

        req.setAttribute("resultList",solver.getSolve());
        req.getRequestDispatcher("/SimplexResult.jsp").forward(req, resp);
//        PrintWriter printWriter = resp.getWriter();
//
//        for (String s : solver.getSolve()) {
//            printWriter.println(s);
//        }

        //printWriter.close();
    }


    private String inputBuilder(HttpServletRequest req)
    {
        StringBuilder input = new StringBuilder();
        int k = Integer.parseInt(req.getParameter("amountConstraints"));
        int z = Integer.parseInt(req.getParameter("amountVariables"));
        input.append(z).append(";").append(k).append(";");

        input.append(req.getParameter("func sign")).append(";");

        for (String s : req.getParameterValues("func")) {
            input.append(s).append(";");
        }

        for (int i = 1; i < z + 1; i++) {
            if(req.getParameter("x"+i+" sign").equals(">"))
            {
                input.append("true;");
            }
            else
            {
                input.append("false;");
            }
        }
        for (int i = 1; i < k+1; i++)
        {
            input.append(req.getParameter("y"+(i)+" sign")).append(";");
            for (String s : req.getParameterValues("y"+(i))) {
                input.append(s).append(";");
            }

        }
        return input.toString();
    }

}
