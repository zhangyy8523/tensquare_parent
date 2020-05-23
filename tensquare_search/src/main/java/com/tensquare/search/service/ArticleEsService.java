package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.ArticleES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/23 8:40
 */
@Service
public class ArticleEsService {
    @Autowired
    private ArticleSearchDao articleSearchDao ;

    @Transactional
    public void save(ArticleES articleES) {
        articleSearchDao.save(articleES);
    }

    /**
     * 检索
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<ArticleES> findByTitleLike(String keywords, Integer page, Integer size) {
       //分页
        PageRequest of = PageRequest.of(page - 1, size);
        //查询数据
        Page<ArticleES> contentLike = articleSearchDao.findByTitleOrContentLike(keywords, keywords, of);
        return contentLike;
    }
}
