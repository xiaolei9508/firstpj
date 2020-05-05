/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.CmuopLabelDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface CmuopLabelService {

    /**
     * 新增记录
     * 
     * @param cmuopLabelDTO
     * @return
     */
    int addCmuopLabel(CmuopLabelDTO cmuopLabelDTO);

    /**
     * 修改记录
     * 
     * @param cmuopLabelDTO
     * @return
     */
    int updateCmuopLabel(CmuopLabelDTO cmuopLabelDTO);

    /**
     * 根据主键id删除
     * 
     * @param id
     * @return
     */
    int deleteCmuopLabel(Long id);

    /**
     * 根据主键id查询
     * 
     * @param id
     * @return
     */
    CmuopLabelDTO queryById(Long id);

    /**
     * 按条件查询总数
     * 
     * @param cmuopLabelDTO
     * @return
     */
    long countByCondition(CmuopLabelDTO cmuopLabelDTO);

    /**
     * 按条件查询
     * 
     * @param cmuopLabelDTO
     * @return
     */
    List<CmuopLabelDTO> queryByCondition(CmuopLabelDTO cmuopLabelDTO);

    /**
     * 分页查询数据
     * 
     * @param cmuopLabelDTO
     * @param page
     * @return
     */
    PageInfo<CmuopLabelDTO> queryByPage(CmuopLabelDTO cmuopLabelDTO, Page page);

    List<CmuopLabelDTO> queryLabelListByCondition(CmuopLabelDTO cmuopLabelDTO);

    CmuopLabelDTO queryCmuopLabelById(Long id);

    CmuopLabelDTO queryByLabelId(Long labelId);
}
