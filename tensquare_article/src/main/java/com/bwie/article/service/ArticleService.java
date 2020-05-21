package com.bwie.article.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import com.bwie.article.dao.ArticleDao;
import com.bwie.article.pojo.Article;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	RedisTemplate redisTemplate ;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Article> findAll() {
		List<Article> articleList = null;
		//获取缓存中的数据，
		 articleList = (List<Article>) redisTemplate.opsForValue().get("findSearchByArticle");
		if(null==articleList){
			List<Article> articleDaoAll = articleDao.findAll();
		}


		return articleList;
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Article> findSearch(Map whereMap, int page, int size) {
		Specification<Article> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return articleDao.findAll(specification, pageRequest);
	}



	/**
	 * 条件查询 (真正的应该场景中，是不会对条件查询的方法做redis的缓存的，一般都是缓存的是单一的对象的数据)
	 * @param whereMap
	 * @return
	 */
	public List<Article> findSearch(Map whereMap) {
		List<Article> articleList = null;
		//添加查询，如果我们通过缓冲的话，是没法过滤数据  containsKey判断key 是否存在
		if(!whereMap.containsKey("id")
				||!whereMap.containsKey("columnid")||
				!whereMap.containsKey("userid")||
				!whereMap.containsKey("title")||
				!whereMap.containsKey("content")||
				!whereMap.containsKey("image")){
			//如果这些参数都不存在，都没有值，则直接通过缓冲去获取数据
			articleList = (List<Article>) redisTemplate.opsForValue().get("findSearchByArticle");
			//articleList 取的数据为空值，则还是通过jpa去获取数据
			if (null==articleList){
				articleList = articleDao.findAll() ;
			}
		}else{
			Specification<Article> specification = createSpecification(whereMap);
			articleList = articleDao.findAll(specification) ;
		}



		return articleList;
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Article findById(String id) {
		Article article = null;
		//通过缓存key 查询当前的数据的缓存
		System.out.println("======================我先获取缓存的数据==========================");
		article = (Article) redisTemplate.opsForValue().get(id+"_Article");
		//判断如果从缓存中的数据为空，则从数据库中获取，并且存入到redis中
		if(null==article){
			System.out.println("======================缓存数据是空的，我从数据库中获取了==========================");
			article = articleDao.findById(id).get();
			//存入到redis中
			System.out.println("======================把从数据库中获取的数据，存到redis中==========================");
//			redisTemplate.opsForValue().set(id+"_Article",article,1, TimeUnit.DAYS);
			redisTemplate.opsForValue().set(id+"_Article",article,20, TimeUnit.SECONDS);
		}

		return article;
	}

	/**
	 * 增加
	 * @param article
	 */
	public void add(Article article) {
		article.setId( idWorker.nextId()+"" );
		articleDao.save(article);
	}

	/**
	 * 修改
	 * @param article
	 */
	public void update(Article article) {
		//直接删除缓存中的数据， 让查询的时候，重新获取即可
		redisTemplate.delete(article.getId()+"__Article");
		articleDao.save(article);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		//直接删除缓存中的数据， 让查询的时候，重新获取即可
		redisTemplate.delete(id+"__Article");
		articleDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Article> createSpecification(Map searchMap) {

		return new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏ID
                if (searchMap.get("columnid")!=null && !"".equals(searchMap.get("columnid"))) {
                	predicateList.add(cb.like(root.get("columnid").as(String.class), "%"+(String)searchMap.get("columnid")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 文章封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                	predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                	predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 所属频道
                if (searchMap.get("channelid")!=null && !"".equals(searchMap.get("channelid"))) {
                	predicateList.add(cb.like(root.get("channelid").as(String.class), "%"+(String)searchMap.get("channelid")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                	predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 审核
	 * 在做增删改的时候，一定要事务的注解
	 * @param articleId
	 */
	@Transactional
	public void examine(String articleId) {
		articleDao.examine(articleId);
	}

	/**
	 * 点赞
	 * 在做增删改的时候，一定要事务的注解
	 * @param articleId
	 */
	@Transactional
	public void updateThumbup(String articleId) {
		articleDao.updateThumbup(articleId);
	}
}
