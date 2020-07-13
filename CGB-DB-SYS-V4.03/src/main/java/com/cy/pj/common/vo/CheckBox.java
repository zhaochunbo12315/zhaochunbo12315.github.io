package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 借助此对象封装checkbox相关数据
 *
 * @author qilei
 */
@Setter
@Getter
@ToString
public class CheckBox implements Serializable {
    private static final long serialVersionUID = 5138674088528922875L;
    private Integer id;
    private String name;
}
