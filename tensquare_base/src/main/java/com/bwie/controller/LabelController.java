package com.bwie.controller;

import com.bwie.pojo.Label;
import com.bwie.service.LabelService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:44
 */
@RestController
@RequestMapping("/label")
@CrossOrigin //解决跨域问题
public class LabelController {
            @Autowired
            private LabelService labelService ;

    /**
     * 根据id查询详情
      * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String id){
        Label labelServiceById = labelService.getById(id);
        return new Result(true, StatusCode.OK,"查询成功！",labelServiceById);
    }
    /**
     * 根据id修改
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@RequestBody Label label,@PathVariable("id") String id){
        label.setId(id);
        labelService.updateById(label);
        return new Result(true, StatusCode.OK,"修改成功！");
    }


    /**
     * 查询全部标签
     *
     * @return
     */
    @GetMapping
    public Result queryAllLabelList(){
        List<Label> labels = labelService.queryAllLabelList();

        return new Result(true,StatusCode.OK,labels);
    }

    /**
     * 增加标签
     */
    @PostMapping
    public Result addLabel(@RequestBody Label label){
        label = null;
        label.getLabelname();
            labelService.addLabel(label);
            return new Result(true, StatusCode.OK,"添加成功！");

    }


    /**
     * 删除（通过id删除数据）
     */
    @DeleteMapping("/{id}")
    public  Result deleteById(@PathVariable("id") String id){
        labelService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功！");
    }

    /***
     * /label/search
     * 根据条件查询标签列表
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label){
       List<Label> labels =  labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",labels);
    }
    /***
     * /label/search
     * 根据条件查询标签列表 分页
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearchPage(@RequestBody Label label,@PathVariable Integer page,@PathVariable  Integer size){

        return new Result(true,StatusCode.OK,"查询成功",   labelService.findSearch(label,page,size));
    }

}
