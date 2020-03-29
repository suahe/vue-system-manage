package com.example.manageSystem.admin.module.dict.dao;

import com.example.manageSystem.admin.model.Dict;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

public interface DictDao extends Mapper<Dict> {

    List<Dict> getDictsByParentCode(String parentCode);

    List<Dict> getDictsByParentId(@Param("parentId") Integer parentId);

    Dict findDictByParentAndCode( @Param("parentId")Integer parentId, @Param("code")String code);

    Dict findDictByParentIdAndCode(@Param("id") Integer id , @Param("parentId")Integer parentId, @Param("code")String code);

    List<Dict> findPage( @Param("parentId")Integer parentId,  @Param("name")String name,  @Param("code")String code);
}
