package com.bwie.service;

import com.bwie.dao.LabelDao;
import com.bwie.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:44
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao ;

    public Label getById(String id) {

        return labelDao.getById(id);
    }
}
