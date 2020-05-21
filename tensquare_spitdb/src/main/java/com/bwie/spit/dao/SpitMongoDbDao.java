package com.bwie.spit.dao;

import com.bwie.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * @描述：吐槽数据访问层
 * @作者：zhangyuyang
 * @日期：2020/5/21 9:28
 */
public interface SpitMongoDbDao  extends MongoRepository<Spit,String> {

    public Page<Spit> findByParentid(String parentid, Pageable pageable);

}
