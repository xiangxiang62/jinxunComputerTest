package com.jinxun.project.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建Department请求
 *
 * @author xiangxiang
 */
@Data
public class DepartmentAddRequest implements Serializable {

    /**
    * 序列化 id
    *
    */
    private static final long serialVersionUID = 1L;


    /**
     * 部门名称
     */
    private String dept_name;


    /**
     * 部门 id
     */
    private Long dept_id;
}