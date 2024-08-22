package com.jinxun.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Department视图
 *
 * @author xiangxiang
 */
@Data
public class DepartmentVO implements Serializable {


    /**
     * 部门ID
     */
    private Integer dept_id;

    /**
     * 部门名称
     */
    private String dept_name;

    /**
     * 用户 id
     */
    private Long userId;


    /**
    * 序列化 id
    *
    */
    private static final long serialVersionUID = 1L;
}