/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.FieldDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface FieldService {

    /**
     * 新增记录
     * 
     * @param fieldDTO
     * @return
     */
    int addField(FieldDTO fieldDTO);

    /**
     * 修改记录
     * 
     * @param fieldDTO
     * @return
     */
    int updateField(FieldDTO fieldDTO);

    /**
     * 根据主键id删除
     * 
     * @param id
     * @return
     */
    int deleteField(Long id);

    /**
     * 根据主键id查询
     * 
     * @param id
     * @return
     */
    FieldDTO queryById(Long id);

    /**
     * 按条件查询总数
     * 
     * @param fieldDTO
     * @return
     */
    long countByCondition(FieldDTO fieldDTO);

    /**
     * 按条件查询
     * 
     * @param fieldDTO
     * @return
     */
    List<FieldDTO> queryByCondition(FieldDTO fieldDTO);

    /**
     * 分页查询数据
     * 
     * @param fieldDTO
     * @param page
     * @return
     */
    PageInfo<FieldDTO> queryByPage(FieldDTO fieldDTO, Page page);

    List<FieldDTO> getFieldList();

}
