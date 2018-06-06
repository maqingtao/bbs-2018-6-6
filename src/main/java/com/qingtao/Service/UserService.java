package com.qingtao.Service;

import com.qingtao.dao.userDaoimpl;
import com.qingtao.po.Bbsuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think on 2017/8/21.
 */
@Service
@Transactional
public class UserService {
    Map<String, String> types = new HashMap<>();

    public UserService() {
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    @Autowired
    private userDaoimpl userDaoimpl;//自动装配实现接口

    public Bbsuser login(Bbsuser bbsuser) {
        return userDaoimpl.login(bbsuser.getUsername(), bbsuser.getPassword());
    }
    public  Bbsuser findOne(int id)
    {
        return  userDaoimpl.findOne(id);
    }
    public Bbsuser uploadPic(HttpServletRequest req, CommonsMultipartResolver commonsMultipartResolver)//上传头像
    {   //设置上传参数
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setResolveLazily(true);//设置懒加载，当全部加载之后在解析
        commonsMultipartResolver.setMaxInMemorySize(4096 * 1024);//设置最大缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024);//设置每个文件的大小
        commonsMultipartResolver.setMaxUploadSize(2 * 1024 * 1024);//总上传文件大小
        Bbsuser bbsuser = null;
        if (commonsMultipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest request = commonsMultipartResolver.resolveMultipart(req);
            MultipartFile multipartFile = request.getFile("file0");
            String type =  multipartFile.getContentType();//即将上传的文件格式
            if (types.containsKey(type)) {//生成全局唯一码，作为文件名字，防止冲突
                File imagefile = new File("upload" + File.separator + req.getSession().getId() + types.get(type));//req.getSession().getId()作为文件名防止冲突
                bbsuser = new Bbsuser();
                String reusername = request.getParameter("reusername");
                String repassword = request.getParameter("repassword");
                bbsuser.setUsername(reusername);
                bbsuser.setPassword(repassword);
                bbsuser.setPicPath(imagefile.getPath());
                bbsuser.setPagenum(5);

                try  {
                    //拷贝文件
                    multipartFile.transferTo(imagefile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //把文件数据放到po里
                try (FileInputStream fis = new FileInputStream(imagefile);) {

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    bbsuser.setPic(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return bbsuser;
    }
    public Bbsuser save(Bbsuser user)
    {
        return userDaoimpl.save(user);
    }
    public int updataPageNumById(Bbsuser user)
    {
        return userDaoimpl.updataPageNumById(user.getUserid(),user.getPagenum());
    }
}
