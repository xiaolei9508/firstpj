package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.StockstatusDTO;
import com.cmft.slas.cmuop.entity.Stockstatus;

public interface StockstatusMapper extends CommonMapper<Stockstatus>{
    
    long countByCondition(StockstatusDTO stockstatusDTO);

    List<StockstatusDTO> queryByCondition(StockstatusDTO stockstatusDTO);

	List<StockstatusDTO> queryStockstatusList();
}
