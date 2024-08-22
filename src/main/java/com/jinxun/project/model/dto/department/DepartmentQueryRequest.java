package com.jinxun.project.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询Department请求
 *
 * @author xiangxiang
 */
@Data
public class DepartmentQueryRequest implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 当前页号
     */
    private Integer current;

    /**
     * 展示条数
     */
    private Integer pageSize;

    /**
     * 部门ID
     */
    private Integer dept_id;

    /**
     * 部门名称
     */
    private String dept_name;

    /**
    * 序列化 id
    */
    private static final long serialVersionUID = 1L;
}