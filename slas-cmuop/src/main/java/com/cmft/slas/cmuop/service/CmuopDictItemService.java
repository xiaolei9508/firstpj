/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.CmuopDictItemDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface CmuopDictItemService {

    /**
     * 新增记录
     * 
     * @param cmuopDictItemDTO
     * @return
     */
    int addCmuopDictItem(CmuopDictItemDTO cmuopDictItemDTO);

    /**
     * 修改记录
     * 
     * @param cmuopDictItemDTO
     * @return
     */
    int updateCmuopDictItem(CmuopDictItemDTO cmuopDictItemDTO);

    /**
     * 根据主键id删除
     * 
     * @param id
     * @return
     */
    int deleteCmuopDictItem(Long id);

    /**
     * 根据主键id查询
     * 
     * @param id
     * @return
     */
    CmuopDictItemDTO queryById(Long id);

    /**
     * 按条件查询总数
     * 
     * @param cmuopDictItemDTO
     * @return
     */
    long countByCondition(CmuopDictItemDTO cmuopDictItemDTO);

    /**
     * 按条件查询
     * 
     * @param cmuopDictItemDTO
     * @return
     */
    List<CmuopDictItemDTO> queryByCondition(CmuopDictItemDTO cmuopDictItemDTO);

    List<CmuopDictItemDTO> queryByConditionByDictCode(CmuopDictItemDTO cmuopDictItemDTO);

    /**
     * 分页查询数据
     * 
     * @param cmuopDictItemDTO
     * @param page
     * @return
     */
    PageInfo<CmuopDictItemDTO> queryByPage(CmuopDictItemDTO cmuopDictItemDTO, Page page);

    Boolean cleanDictItemByDictCode(String dictCode);

    String cleanRedisDataByRedisKey(String redisKey);
}
