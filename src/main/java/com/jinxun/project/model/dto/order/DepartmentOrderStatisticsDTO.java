package com.jinxun.project.model.dto.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 每个部门的工单总量和超期率 DTO
 *
 * @Author 香香
 * @Date 2024-08-23 02:13
 **/
@Data
public class DepartmentOrderStatisticsDTO {

    /**
     * 部门 id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 总数
     */
    private int totalOrders;

    /**
     * 超期率
     */
    private BigDecimal overdueRate;


    public DepartmentOrderStatisticsDTO(Long deptId, String deptName, int totalOrders, BigDecimal overdueRate) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.totalOrders = totalOrders;
        this.overdueRate = overdueRate;
    }
}
