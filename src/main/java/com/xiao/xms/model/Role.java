package com.xiao.xms.model;

import javax.persistence.*;

@Table(name = "t_role")
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String rolename;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return rolename
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * @param rolename
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}