package com.bwie.spit.controller;

import com.bwie.spit.pojo.Spit;
import com.bwie.spit.service.SpitDbService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/21 9:33
 */
@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitDbService spitDbService;


    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitDbService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",spitDbService.findById(id));
    }





    /**
     * 增加
     * @param Spit
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Spit Spit  ){
        spitDbService.add(Spit);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param Spit
     */
    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody Spit Spit, @PathVariable String id ){
        Spit.set_id(id);
        spitDbService.update(Spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        spitDbService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级id查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/comment/{parentid}/{page}/{size}")
    public Result spitInfoByParentId(@PathVariable String parentid,@PathVariable Integer page,@PathVariable Integer size ){
        Page<Spit> pageList =   spitDbService.spitInfoByParentId(parentid,page,size);


        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageList.getTotalElements(),pageList.getContent()));
    }

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 点赞
     */
    @PutMapping(value = "/thumbup/{id}")
    public Result updateThumbup(@PathVariable String id){
        //判断用户是否点赞
        String userId = "1712a" ;//后面我们会改成动态的获取用户的信息 通过token
        if(redisTemplate.opsForValue().get("updateThumbup_"+userId+"_"+id)!=null){
            return new Result(true,StatusCode.OK,"您已经点过赞了");
        }

        //如果没有点过赞，则点赞功能完成后，插入一条信息到redis中
        spitDbService.updateThumbup(id);
        redisTemplate.opsForValue().set("updateThumbup_"+userId+"_"+id,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}
