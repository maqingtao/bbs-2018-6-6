package com.qingtao.control;

import com.qingtao.Service.UserService;
import com.qingtao.po.Bbsuser;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by think on 2017/8/21.
 */
@WebServlet(name = "UserControl",urlPatterns = {"/user"})
public class UserControl extends HttpServlet{
    @Autowired
    private UserService service;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
        CommonsMultipartResolver commonsMultipartResolver = new
                CommonsMultipartResolver(req.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(req))//如果返回为真，则返回为流格式multipart/form-data
        {//纯注册
            reg(req,commonsMultipartResolver,resp);
        } else {
            String action=req.getParameter("action");
            switch (action) {
                case "login":
                    login(req, resp);
                    break;
                case "pic":
                    pic(req, resp);
                    break;
                case "logout":
                    logout(req, resp);
                    break;
                case  "ur"://修改用户的每页行数
                    ur(req,resp);
                      break;
            }
        }
    }

    private void ur(HttpServletRequest req, HttpServletResponse resp) {
        String pagenum=req.getParameter("pagenum");
       Bbsuser  user=(Bbsuser)req.getSession().getAttribute("user");
       user.setPagenum(Integer.parseInt(pagenum));
       System.out.println( service.updataPageNumById(user));
        RequestDispatcher dispatcher=null;
        dispatcher=req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reg(HttpServletRequest req, CommonsMultipartResolver commonsMultipartResolver,HttpServletResponse resp) {
            Bbsuser user=service.uploadPic(req,commonsMultipartResolver);
            user=service.save(user);
        RequestDispatcher dispatcher=null;
        dispatcher=req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher dispatcher=null;
        req.getSession().removeAttribute("user");//登出操作去掉session
        dispatcher=req.getRequestDispatcher("index");//转向index
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pic(HttpServletRequest req, HttpServletResponse resp) {
                 String id=req.getParameter("id");
              Bbsuser bbsuser=service.findOne(Integer.parseInt(id));
              byte []buffer=bbsuser.getPic();
        try {
            ServletOutputStream out= resp.getOutputStream();
            out.write(buffer);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        Bbsuser bbsuser=new Bbsuser(username,password);
        String flag=((bbsuser=service.login(bbsuser))==null)?"false":"true";

        if (flag.equals("true"))
        {
            req.getSession().setAttribute("user",bbsuser);//设置session保证任何地方都能使用
            String sun=req.getParameter("sun");
            if (sun!=null)
            {
                Cookie cookie=new Cookie("qingtaou",bbsuser.getUsername());
                cookie.setMaxAge(3600*24*7);
                resp.addCookie(cookie);
                Cookie cookie1=new Cookie("qingtaop",bbsuser.getPassword());
                cookie.setMaxAge(3600*24*7);
                resp.addCookie(cookie1);
            }
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer=null;
        try {
                 writer=resp.getWriter();
                 writer.println(flag);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void init(ServletConfig config) throws ServletException {

    }
}
