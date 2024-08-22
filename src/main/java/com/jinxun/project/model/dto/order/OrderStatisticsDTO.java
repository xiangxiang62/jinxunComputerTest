package com.jinxun.project.model.dto.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询指定月份每天的工单总量和超期率 DTO
 *
 * @Author 香香
 * @Date 2024-08-23 01:58
 **/
@Data
public class OrderStatisticsDTO {


    /**
     * 天数
     */
    private Integer day;

    /**
     * 工单总量
     */
    private Integer totalOrders;

    /**
     * 超期率
     */
    private BigDecimal overdueRate;

    // 构造函数
    public OrderStatisticsDTO(Integer day, Integer totalOrders, BigDecimal overdueRate) {
        this.day = day;
        this.totalOrders = totalOrders;
        this.overdueRate = overdueRate;
    }

}
