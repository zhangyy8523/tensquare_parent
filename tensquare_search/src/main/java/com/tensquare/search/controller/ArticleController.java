package com.tensquare.search.controller;

import com.tensquare.search.pojo.ArticleES;
import com.tensquare.search.service.ArticleEsService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import utils.Base64Utils;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/23 8:40
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleEsService articleEsService ;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody ArticleES articleES){
        articleEsService.save(articleES);
        return new Result(true, StatusCode.OK,"操作成功");
    }

    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findByTitleLike(@PathVariable String keywords,@PathVariable Integer page,@PathVariable Integer size){


         Page<ArticleES> articleES =  articleEsService.findByTitleLike(Base64Utils.decode(keywords),page,size);
         return new Result(true,StatusCode.OK,"查询成功",new PageResult<ArticleES>(articleES.getTotalElements(),articleES.getContent()));
    }

}
