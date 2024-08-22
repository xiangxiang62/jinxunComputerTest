package com.jinxun.project.model.vo;

import com.jinxun.project.model.entity.OrderTable;
import lombok.Data;

import java.io.Serializable;

/**
 * Order视图
 *
 * @author xiangxiang
 */
@Data
public class OrderVO implements Serializable {


    private static final long serialVersionUID = 1L;

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
    private Long handle_dept_id;


    /**
     * 是否超期 (0: 否, 1: 是)
     */
    private Integer is_overdue;

    /**
     * 用户 id
     */
    private Long UserId;

    public static OrderVO objToVo(OrderTable order){
        OrderVO orderVO = new OrderVO();
        orderVO.id = order.getId();
        orderVO.order_no = order.getOrder_no();
        orderVO.order_type = order.getOrder_type();
        orderVO.title = order.getTitle();
        orderVO.content = order.getContent();
        orderVO.handle_dept_id = order.getHandle_dept_id();
        orderVO.is_overdue = order.getIs_overdue();
        orderVO.UserId = order.getCreate_user_id();
        return orderVO;
    }

}