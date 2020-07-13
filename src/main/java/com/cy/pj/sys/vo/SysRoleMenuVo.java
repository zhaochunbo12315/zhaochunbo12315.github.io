package com.cy.pj.sys.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysRoleMenuVo implements Serializable {
    private static final long serialVersionUID = -2502237059956002188L;
    private Integer id;
    private String name;
    private String note;
    /**
     * 角色对应的菜单id
     */
    private List<Integer> menuIds;
}
