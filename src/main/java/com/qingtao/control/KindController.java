package com.qingtao.control;
import com.qingtao.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "/file", urlPatterns = {"/kindupload"})
public class KindController extends HttpServlet {
    @Autowired
    ArticleService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String s=service.uploadPic(req);
        PrintWriter printWriter=resp.getWriter();
        printWriter.print(s);
        printWriter.flush();
        printWriter.close();
    }
}
