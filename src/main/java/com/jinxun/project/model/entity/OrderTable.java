package com.jinxun.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Order
 */
@TableName(value = "order_table")
@Data
@Accessors(chain = true)
public class OrderTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    private Long order_no;

    /**
     * 工单类型 (0: 交办, 1: 直接答复, 3: 无效工单)
     */
    private Integer order_type;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 处理部门ID
     */
    private Long handle_dept_id;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

    /**
     * 分派时间
     */
    private LocalDateTime fenpai_time;

    /**
     * 是否超期 (0: 否, 1: 是)
     */
    private Integer is_overdue;

    /**
     * 创建用户 id
     */
    private Long create_user_id;


    /**
     * 处理部门名称
     */
    private String handle_dept_name;

}
