package com.xiao.xms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@Table(name = "t_permission")
public class Permission {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String permissionname;

    @Column(name = "role_id")
    private Integer roleId;

}