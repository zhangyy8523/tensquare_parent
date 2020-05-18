package com.bwie.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bwie.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 根据标签id查询标签下的最新问答列表
     * select p.* from tb_problem p where id in(select problemid from tb_pl where labelid=?1 ) order by p.replytime desc
     * @param labelId
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where id in (select problemid from Pl where labelid=?1) order by replytime desc")
    public Page<Problem> findNewListByLabelId(String labelId, Pageable pageable);

    /**
     * 热门问答列表
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "select p.* from tb_problem p where id in(select problemid from tb_pl where labelid='1' ) order by p.reply desc",nativeQuery = true)
    public  Page<Problem> findHotListByLabelId(String labelId, Pageable pageable);

    /**
     * 等待问答列表
     * @param labelId
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where id in (select problemid from Pl where labelid=?1) and reply=0  order by createtime desc")
    public Page<Problem> findWaitListByLabelId(String labelId, Pageable pageable);
}
