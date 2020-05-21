package com.bwie.spit.service;

import com.bwie.spit.dao.SpitMongoDbDao;
import com.bwie.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/21 9:30
 */
@Service
public class SpitDbService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private SpitMongoDbDao spitMongoDbDao ;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     * @return
     */
    public List<Spit> findAll() {
        return spitMongoDbDao.findAll();
    }


    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Spit findById(String id) {
        return spitMongoDbDao.findById(id).get();
    }

    /**
     * 增加
     * @param Spit
     */
    public void add(Spit spit) {
        spit.set_id( idWorker.nextId()+"" );
        spit.setPublishtime(new Date()); //发布日期
        spit.setVisits(0); //浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);
        spit.setComment(0);//回复数
        spit.setState("1");//状态


        //判断如果parentId存在，上级的吐槽信息有，则对comment的数据＋1
        if(!StringUtils.isEmpty(spit.getParentid())){
            //更新父节点中的评论数 ＋1
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }


        spitMongoDbDao.save(spit);
    }

    /**
     * 修改
     * @param Spit
     */
    public void update(Spit Spit) {
        spitMongoDbDao.save(Spit);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        spitMongoDbDao.deleteById(id);
    }

    /**
     * 根据上级id
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> spitInfoByParentId(String parentid, Integer page, Integer size) {
        //设置分页参数
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Spit> mongoDbDaoByParentid = spitMongoDbDao.findByParentid(parentid, pageRequest);

        return mongoDbDaoByParentid;
    }


    /**
     * 点赞
     * @param id
     */
    @Transactional
    public void updateThumbup(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
