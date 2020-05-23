package com.tensquare.search.dao;

import com.tensquare.search.pojo.ArticleES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/23 8:39
 */
public interface  ArticleSearchDao extends ElasticsearchRepository<ArticleES,String> {

        public Page<ArticleES> findByTitleOrContentLike(String title, String content, Pageable pageable);

}
