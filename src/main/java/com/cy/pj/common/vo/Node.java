package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Node implements Serializable {
    private static final long serialVersionUID = -5167356663824650231L;
    private Integer id;
    private String name;
    private Integer parentId;

}
