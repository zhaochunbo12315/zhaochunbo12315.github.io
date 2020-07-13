package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基于此对象实现日志数据的封装,数据可能来自于
 * 1)数据库(一行记录映射为一个这样的对象)
 * 2)用户行为(通过此对象封装记录用户行为)
 * 在当前应用中我们可以将此对象理解为一个pojo对象(简单的JAVA对象)，pojo在实际项目中又有很多分类
 * 1)PO 持久化对象(特点：与表中字段有一一映射关系)
 * 2)VO 普通值对象(用户进行值的封装和传递，可以不与表中字段有一一映射关系)
 * 3).........
 * <p>
 * 说明：在java中所有用于存储数据的对象建议实现Serializable接口,并手动添加一个序列化id。
 */
@Setter
@Getter
@ToString
public class SysLog implements Serializable {
    private static final long serialVersionUID = -8427441809753041210L;
    private Integer id;
    //用户名
    private String username;
    //用户操作
    private String operation;
    //请求方法
    private String method;
    //请求参数
    private String params;
    //执行时长(毫秒)
    private Long time;
    //IP地址
    private String ip;
    //创建时间
    private Date createdTime;

}
