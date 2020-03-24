package com.example.manageSystem.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name="sys_config")
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String param_key;
    private String param_value;
    private Short status; //0：隐藏 1：显示
    private String remark; //备注

}
