package com.jinxun.project.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新Department请求
 *
 * @author xiangxiang
 */
@Data
public class DepartmentUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
    * 序列化 id
    *
    */
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Integer dept_id;

    /**
     * 部门名称
     */
    private String dept_name;
}