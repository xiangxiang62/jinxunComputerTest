package com.jinxun.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Department
 */
@Data
@Accessors(chain = true)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 部门ID
     */

    private Long dept_id;

    /**
     * 部门名称
     */
    private String dept_name;

    /**
     * 用户 id
     */
    private Long creat_user_id;

}
