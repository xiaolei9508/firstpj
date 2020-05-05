/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;


import com.cmft.slas.cmuop.dto.SortDTO;
import com.cmft.slas.cmuop.dto.StockstatusDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;



public interface StockstatusService{

	/**
     * 新增记录
     * 
     * @param stockstatusDTO
     * @return
     */
    int addStockstatus(StockstatusDTO stockstatusDTO);

    /**
     * 修改记录
     * 
     * @param stockstatusDTO
     * @return
     */
    int updateStockstatus(StockstatusDTO stockstatusDTO);

    /**
     * 根据主键id删除
     * 
     * @param id
     * @return
     */
    int deleteStockstatus(Long id);

    /**
     * 根据主键id查询
     * 
     * @param id
     * @return
     */
    StockstatusDTO queryById(Long id);

    /**
     * 按条件查询总数
     * 
     * @param stockstatusDTO
     * @return
     */
    long countByCondition(StockstatusDTO stockstatusDTO);

    /**
     * 按条件查询
     * 
     * @param stockstatusDTO
     * @return
     */
    List<StockstatusDTO> queryByCondition(StockstatusDTO stockstatusDTO);

    /**
     * 分页查询数据
     * 
     * @param stockstatusDTO
     * @param page
     * @return
     */
    PageInfo<StockstatusDTO> queryByPage(StockstatusDTO stockstatusDTO, Page page);

	List<StockstatusDTO> getStockstatusList(SortDTO sortDTO);
}
