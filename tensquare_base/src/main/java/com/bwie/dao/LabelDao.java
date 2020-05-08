package com.bwie.dao;


import com.bwie.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:42
 */
public interface LabelDao  extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {
        Label getById(String id);
}
