package com.example.manageSystem.admin.model;

import org.springframework.data.annotation.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="sys_config")
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String paramkey;
    private String paramValue;
    private Short status; //0：隐藏 1：显示
    private String remark; //备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParamkey() {
        return paramkey;
    }

    public void setParamkey(String paramkey) {
        this.paramkey = paramkey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
