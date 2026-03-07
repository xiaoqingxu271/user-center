-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account`        VARCHAR(64)  NOT NULL COMMENT '账号（唯一）',
    `password`       VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    `username`       VARCHAR(50)  DEFAULT NULL COMMENT '用户名/昵称',
    `gender`         TINYINT      DEFAULT 0 COMMENT '性别（0-未知 1-男 2-女）',
    `avatar`         VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status`         TINYINT      DEFAULT 0 COMMENT '账号状态（0-启用 1-禁用）',
    `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user_id` BIGINT       DEFAULT NULL COMMENT '创建人ID',
    `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user_id` BIGINT       DEFAULT NULL COMMENT '更新人ID',
    `is_deleted`     TINYINT      DEFAULT 0 COMMENT '是否删除（0-未删除 1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`) COMMENT '账号唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

