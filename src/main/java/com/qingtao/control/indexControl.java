package com.qingtao.control;

import com.qingtao.po.Bbsuser;
import com.qingtao.util.FreemarkerUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think on 2017/8/21.
 */
@WebServlet(name = "indexControl",urlPatterns = "/index",
initParams = {@WebInitParam(name = "success",value ="article?action=queryall&page=1" )})
public class indexControl extends HttpServlet{
    Map<String,String> map= new HashMap();

    @Override
    public void init(ServletConfig config) throws ServletException {
       map.put("success",config.getInitParameter("success"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher=null;
               dispatcher=req.getRequestDispatcher(map.get("success"));
               dispatcher.forward(req,resp);

    }
}
