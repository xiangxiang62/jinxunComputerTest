package com.jinxun.project.model.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建Order请求
 *
 * @author xiangxiang
 */
@Data
public class OrderTableFenPaiRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单编号
     */
    private Long id;

    /**
     * 处理部门id
     */
    private Long handle_dept_id;

    /**
     * 处理部门名称
     */
    private String handle_dept_name;

}