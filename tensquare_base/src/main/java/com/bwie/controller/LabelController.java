package com.bwie.controller;

import com.bwie.pojo.Label;
import com.bwie.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:44
 */
@RestController
@RequestMapping("/label")
public class LabelController {
            @Autowired
            private LabelService labelService ;

    @GetMapping("/{id}")
    public Label getById(@PathVariable("id") String id){

        return labelService.getById(id);
    }
}
