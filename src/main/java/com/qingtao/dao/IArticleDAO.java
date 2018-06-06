package com.qingtao.dao;

import com.qingtao.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


/**
 * Created by think on 2017/8/23.
 */
public interface IArticleDAO extends CrudRepository<Article, Integer> {
    @Query("select a from Article a where a.rootid=:rid")
    Page<Article> findAll(Pageable pb, @Param("rid") Integer rid);

    @Modifying
    @Query("delete from Article where id=:id or rootid=:id")
    int deletZT(@Param("id") Integer id);
}
