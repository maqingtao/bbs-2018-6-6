package com.qingtao.util;

import freemarker.template.Configuration;
import freemarker.template.Template;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by think on 2017/8/21.
 */
public class FreemarkerUtil {
    private static Configuration configuration;

    private static Configuration bulitConfiguration() {

        if (null == configuration) {
            configuration = new Configuration(Configuration.VERSION_2_3_26);


            String path = FreemarkerUtil.class.getResource("/").getPath();
            //path = path.substring(1, path.indexOf("classes"));
            File ftlPathDir = new File(path + File.separator + "templates");//  \  /

            try {
                configuration.setDefaultEncoding("utf-8");
                configuration.setDirectoryForTemplateLoading(ftlPathDir);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return configuration;
        }
        return configuration;
    }

    public static Template getTemplate(String ftlName) {
        Template template = null;
        try {
            template = bulitConfiguration().getTemplate(ftlName);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public static void forward(String ftlName, Map map, HttpServletResponse resp) {//freemarker 转向
        Template temp = getTemplate(ftlName);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            temp.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.flush();
    }
}
