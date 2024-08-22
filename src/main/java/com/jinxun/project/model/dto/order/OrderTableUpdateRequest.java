package com.jinxun.project.model.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新Order请求
 *
 * @author xiangxiang
 */
@Data
public class OrderTableUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
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
     * 是否超期 (0: 否, 1: 是)
     */
    private Integer is_overdue;


}