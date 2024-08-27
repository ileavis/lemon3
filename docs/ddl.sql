CREATE
    DATABASE /*!32312 IF NOT EXISTS */ `testdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE
    `testdb`;

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user`
(
    `id`          bigint(20)  NOT NULL COMMENT 'id',
    `name`        varchar(30) NOT NULL COMMENT '名字',
    `age`         INT         NOT NULL COMMENT '性别',
    `email`       varchar(50) NOT NULL COMMENT '电子邮件',
    `is_delete`   tinyint(1)           DEFAULT NULL COMMENT '是否已删除 0否 1是',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';


INSERT INTO `t_user` (id, name, age, email) VALUES
                                              (1, 'Jone', 18, 'test1@baomidou.com'),
                                              (2, 'Jack', 20, 'test2@baomidou.com'),
                                              (3, 'Tom', 28, 'test3@baomidou.com'),
                                              (4, 'Sandy', 21, 'test4@baomidou.com'),
                                              (5, 'Billie', 24, 'test5@baomidou.com');