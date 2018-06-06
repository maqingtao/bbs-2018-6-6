package com.qingtao.dao;

import com.qingtao.po.Bbsuser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by think on 2017/8/21.
 */
public interface userDaoimpl extends CrudRepository<Bbsuser, Integer> {
    @Query("select c from Bbsuser c where username=:u and password=:p")
    Bbsuser login(@Param("u") String username, @Param("p") String password);

    @Override
    Bbsuser save(Bbsuser user1);

    @Override
    Bbsuser findOne(Integer id);

    @Modifying
    @Query("update Bbsuser set pagenum=:p where userid=:u")
    int updataPageNumById(@Param("u") Integer userid, @Param("p") Integer pagenum);
}
