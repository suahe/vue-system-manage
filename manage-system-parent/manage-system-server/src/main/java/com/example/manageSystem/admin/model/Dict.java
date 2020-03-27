package com.example.manageSystem.admin.model;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="sys_dict")
public class Dict  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;//
    private Integer parentId;//上级ID
    private String name;//字典名称
    private String code;//字典码
    private String value;//字典值
    private Integer order_num;//排序
    private String remark;//备注
    private Short del_flag;//删除标记 -1：已删除 0：正常

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder_num() {
        return order_num;
    }

    public void setOrder_num(Integer order_num) {
        this.order_num = order_num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(Short del_flag) {
        this.del_flag = del_flag;
    }
}
