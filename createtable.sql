-- 创建库
CREATE DATABASE IF NOT EXISTS jinxun;

-- 切换库
USE jinxun;

-- 部门表
create table department
(
    id            int auto_increment comment '主键ID'
        primary key,
    dept_id       int          not null comment '部门ID',
    dept_name     varchar(100) not null comment '部门名称',
    creat_user_id bigint       not null comment '创建用户 id',
    constraint dept_id
        unique (dept_id)
)
    comment '部门表' charset = utf8mb4;


-- 工单表
create table order_table
(
    id               int auto_increment comment '工单ID'
        primary key,
    order_no         varchar(50)                        not null comment '工单编号',
    order_type       tinyint                            not null comment '工单类型 (0: 交办, 1: 直接答复, 3: 无效工单)',
    title            varchar(255)                       not null comment '标题',
    content          text                               not null comment '内容',
    handle_dept_id   int                                null comment '处理部门ID',
    create_time      datetime default CURRENT_TIMESTAMP null,
    fenpai_time      datetime                           null comment '分派时间',
    is_overdue       tinyint  default 0                 not null comment '是否超期 (0: 否, 1: 是)',
    handle_dept_name varchar(255)                       null comment '分派部门名',
    create_user_id   bigint                             not null comment '分派部门 id',
    constraint order_no
        unique (order_no),
    constraint order_table_ibfk_1
        foreign key (handle_dept_id) references department (dept_id)
)
    comment '工单表' charset = utf8mb4;

create index handle_dept_id
    on order_table (handle_dept_id);


-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `userAccount`  VARCHAR(256)                           NOT NULL COMMENT '账号',
    `userPassword` VARCHAR(512)                           NOT NULL COMMENT '密码',
    `unionId`      VARCHAR(256)                           NULL COMMENT '微信开放平台id',
    `mpOpenId`     VARCHAR(256)                           NULL COMMENT '公众号openId',
    `userName`     VARCHAR(256)                           NULL COMMENT '用户昵称',
    `userAvatar`   VARCHAR(1024)                          NULL COMMENT '用户头像',
    `userProfile`  VARCHAR(512)                           NULL COMMENT '用户简介',
    `userRole`     VARCHAR(256) DEFAULT 'user'            NOT NULL COMMENT '用户角色：user/admin/ban',
    `createTime`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updateTime`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     TINYINT      DEFAULT 0                 NOT NULL COMMENT '是否删除',
    INDEX idx_unionId (`unionId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- 修改工单表的创建时间默认值
ALTER TABLE `order_table`
    MODIFY `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP;

-- 插入部门数据
INSERT INTO `department` (`dept_id`, `dept_name`, `creat_user_id`) VALUES
(1, '技术部', 101),
(2, '人事部', 102),
(3, '财务部', 103),
(4, '市场部', 104),
(5, '客服部', 105);


-- 插入工单数据
INSERT INTO `order_table` (`order_no`, `order_type`, `title`, `content`, `handle_dept_id`, `create_time`, `fenpai_time`, `is_overdue`, `handle_dept_name`, `create_user_id`) VALUES
('ORD0001', 0, '技术支持请求', '用户报告了一个系统错误，需要技术部处理。', 1, '2024-08-01 10:30:00', NULL, 0, '技术部', 101),
('ORD0002', 1, '人事面试安排', '安排面试时间和地点给候选人。', 2, '2024-08-01 11:00:00', '2024-08-01 11:15:00', 0, '人事部', 102),
('ORD0003', 0, '财务报销申请', '员工提交了报销申请，需要财务部处理。', 3, '2024-08-02 09:00:00', NULL, 1, '财务部', 103),
('ORD0004', 3, '市场活动策划', '制定新的市场活动方案。', 4, '2024-08-03 14:00:00', NULL, 0, '市场部', 104),
('ORD0005', 1, '客服问题反馈', '处理用户对产品的反馈和投诉。', 5, '2024-08-04 16:00:00', '2024-08-05 09:00:00', 1, '客服部', 105);
