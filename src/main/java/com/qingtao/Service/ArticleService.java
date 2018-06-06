package com.qingtao.Service;

import com.alibaba.fastjson.JSONObject;
import com.qingtao.dao.IArticleDAO;
import com.qingtao.po.Article;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by think on 2017/8/23.
 */
@Service
@Transactional
public class ArticleService {
    Map<String, String> types = new HashMap<>();

    public ArticleService() {
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    @Autowired
    public IArticleDAO dao;

    public Page<Article> findAll(Pageable pb, Integer rid) {
        return dao.findAll(pb, rid);
    }

    public int deletZT(Integer id) {
        return dao.deletZT(id);
    }

    public String uploadPic(HttpServletRequest req) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        commonsMultipartResolver.setDefaultEncoding("utf-8");//文件可以有中文名
        commonsMultipartResolver.setResolveLazily(true);//设置懒加载
        commonsMultipartResolver.setMaxInMemorySize(4096 * 1024);//设置最大交换空间缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024);
        commonsMultipartResolver.setMaxUploadSize(2 * 1024 * 1024);//设置上传总文件大小
        if (commonsMultipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest request = commonsMultipartResolver.resolveMultipart(req);
            MultipartFile multipartFile = request.getFile("imgFile");
            String type = multipartFile.getContentType();//得到文本类型
            if (types.containsKey(type)) {//生成全局唯一码，作为文件名字，防止冲突
                String s3 = this.getClass().getClassLoader().getResource("").toString();
                String dir = request.getParameter("dir");
                String id = UUID.randomUUID().toString();
                String newFileName = s3 + "static/editor/upload/" + dir + "/" + id + types.get(type);
                newFileName = newFileName.substring(6);//从file://后开始，否则上不去
                File imageFile = new File(newFileName);
                try {
                    multipartFile.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("OK");
         String tpath=req.getRequestURL().toString();
         tpath=tpath.substring(0,tpath.lastIndexOf("/"));
         String path=tpath+"/editor/upload/"+dir+"/";
                JSONObject obj=new JSONObject();
                obj.put("error",0);
                obj.put("url",path+id+types.get(type));
                return obj.toJSONString();
            }
        }

        return "";
    }
}
