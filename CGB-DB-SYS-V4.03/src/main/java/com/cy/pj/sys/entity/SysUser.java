package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysUser implements Serializable {
    private static final long serialVersionUID = 5053909074079096841L;
    private Integer id;
    private String username;
    private String password;
    private String salt;//盐值
    private String email;
    private String mobile;
    private Integer valid = 1;
    private Integer deptId;
    private Date createdTime;
    private Date modifiedTime;
    private String createdUser;
    private String modifiedUser;

}
