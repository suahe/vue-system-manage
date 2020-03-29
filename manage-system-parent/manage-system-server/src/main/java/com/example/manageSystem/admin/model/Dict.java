package com.example.manageSystem.admin.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="sys_dict")
public class Dict  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;//
    private Integer parentId;//上级ID
    private String name;//字典名称
    private String code;//字典码
    private String value;//字典值
    private Integer orderNum;//排序
    private String remark;//备注
    private Short delFlag;//删除标记 -1：已删除 0：正常

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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }

}
