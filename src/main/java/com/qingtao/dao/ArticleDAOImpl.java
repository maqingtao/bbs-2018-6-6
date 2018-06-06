package com.qingtao.dao;

import com.qingtao.po.Article;
import com.qingtao.po.Bbsuser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ArticleDAOImpl {
    @PersistenceContext
    private EntityManager entityManager;
    //通过主贴查询从贴
    public Map<String,Object> queryById(int id) {
        Map<String,Object> map=new HashMap<>();
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("p_10");
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
        storedProcedureQuery.setParameter(1, id);
        storedProcedureQuery.execute();
        List<Object []> items = storedProcedureQuery.getResultList();
        List<Article> list=new ArrayList<>();
         for (Object []item:items)
         {
             Article s=new Article();
             s.setId(Integer.parseInt(item[0].toString()));
             s.setRootid(Integer.parseInt(item[1].toString()));
             s.setTitle(item[2].toString());
             s.setContent(item[3].toString());
             s.setBbsuer(new Bbsuser(Integer.parseInt(item[4].toString())));
             try {
                 Date d=new SimpleDateFormat("yyyy-mm-dd").parse(item[5].toString());
                 s.setDatetime(new java.sql.Date(d.getTime()));
             } catch (ParseException e) {
                 e.printStackTrace();
             }
            list.add(s);
         }
         map.put("title",storedProcedureQuery.getOutputParameterValue(2));
         map.put("list",list);
         return map;
    }
}
