/**
 */
package com.cmft.slas.cmuop.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.dto.RelatedEntityDTO;
import com.cmft.slas.cmuop.vo.EntityTreeVo;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface CmuopEntityService {

    /**
     * 新增记录
     *
     * @param cmuopEntityDTO
     * @return
     */
    int addCmuopEntity(CmuopEntityDTO cmuopEntityDTO);

    /**
     * 修改记录
     *
     * @param cmuopEntityDTO
     * @return
     */
    int updateCmuopEntity(CmuopEntityDTO cmuopEntityDTO);

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteCmuopEntity(Long id);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    CmuopEntityDTO queryById(Long id);

    /**
     * 按条件查询总数
     *
     * @param cmuopEntityDTO
     * @return
     */
    long countByCondition(CmuopEntityDTO cmuopEntityDTO);

    /**
     * 按条件查询
     *
     * @param cmuopEntityDTO
     * @return
     */
    List<CmuopEntityDTO> queryByCondition(CmuopEntityDTO cmuopEntityDTO);

    /**
     * 分页查询数据
     *
     * @param cmuopEntityDTO
     * @param page
     * @return
     */
    PageInfo<CmuopEntityDTO> queryByPage(CmuopEntityDTO cmuopEntityDTO, Page page);

    List<CmuopEntityDTO> getCmuopEntityList();

    List<CmuopEntityDTO> getCmuopEntityRelatedList();

    List<String> queryEntityCodeByNuc(String entityName);

    Map<String, String> getCmuopEntityMap();

    List<CmuopEntityDTO> queryCmuopEntityIsValidList();

    List<CmuopEntityDTO> queryEntityRelatedIsValid();

    LinkedHashMap<String, RelatedEntityDTO> getRelatedEntityMap(boolean ifFresh);

    EntityTreeVo queryEntityTree(String entityCode);
}
