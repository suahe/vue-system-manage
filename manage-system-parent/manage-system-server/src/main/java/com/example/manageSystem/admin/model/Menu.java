package com.example.manageSystem.admin.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "sys_menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "menu_id")
    private Integer id ;
    @Column(name = "parent_id")
    private Integer parentId ;//父菜单ID，一级菜单为0
    @Column(name = "name")
    private String name ;//菜单名称
    @Column(name = "url")
    private String url;//菜单URL
    @Column(name = "perms")
    private String perms ;//授权(多个用逗号分隔，如：user:list,user:create)
    @Column(name = "type")
    private Integer type ;//类型 0：目录 1：菜单 2：按钮
    @Column(name = "icon")
    private String icon ;//菜单图标
    @Column(name = "order_num")
    private Integer orderNum ;//排序

    private List<Menu> children = new ArrayList<Menu>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
