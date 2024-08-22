package com.jinxun.project.model.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询Order请求
 *
 * @author xiangxiang
 */
@Data
public class OrderTableQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页号
     */
    private Integer current;

    /**
     * 展示条数
     */
    private Integer pageSize;

    /**
     * 工单ID
     */
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
    private Integer handle_dept_id;

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
     * 用户 id
     */
    private Long UserId;
}