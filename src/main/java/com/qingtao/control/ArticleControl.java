package com.qingtao.control;

import com.qingtao.Service.ArticleService;
import com.qingtao.po.Article;
import com.qingtao.po.Bbsuser;
import com.qingtao.po.Pagebean;
import com.qingtao.util.FreemarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@WebServlet(name = "ArticleControl",urlPatterns = "/article",
        initParams = {@WebInitParam(name = "success",value ="/show.ftl" )})
public class ArticleControl extends HttpServlet{
    Map<String,String> map= new HashMap();
    @Autowired
    ArticleService service;
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
           String action=req.getParameter("action");
           Map map1=new HashMap();
           switch (action)
           {
               case "queryall":
                   Pagebean pb=queryall(req,resp);
                   map1.put("pb",pb);
                   break;
               case "delz":
                   delz(req,resp);
                   break;

           }

        map1.put("user",req.getSession().getAttribute("user"));
        FreemarkerUtil.forward(map.get("success").toString(),map1,resp);
    }
    private void delz(HttpServletRequest req, HttpServletResponse resp) {
        String id=req.getParameter("id");
        service.deletZT(Integer.parseInt(id));
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
    private Pagebean queryall(HttpServletRequest req,HttpServletResponse resp) {
        String page=req.getParameter("page");//当前页
        //取出每页的数据个数
        Bbsuser bbsuser=(Bbsuser) req.getSession().getAttribute("user");
        int pagenum=5;
        if(bbsuser!=null)
        {
             pagenum=bbsuser.getPagenum();
        }
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=new PageRequest(Integer.parseInt(page)-1,pagenum,sort);
        Page<Article> pa= service.findAll(pageable,0);
        Pagebean pb=new Pagebean();
        pb.setRowsPerPage(pagenum);
        pb.setCurPage(Integer.parseInt(page));
        pb.setMaxRowCount(pa.getTotalElements());
        pb.setData(pa.getContent());
        pb.setMaxPage(pa.getTotalPages());
        return pb;
    }
}
