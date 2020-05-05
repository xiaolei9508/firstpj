package com.cmft.slas.cmuop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cmft.slas.cmuop.dto.SortDTO;
import com.cmft.slas.common.pojo.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.cmft.slas.cmuop.common.utils.BeanMapper;
import com.cmft.slas.cmuop.dto.StockstatusDTO;
import com.cmft.slas.cmuop.entity.Stockstatus;
import com.cmft.slas.cmuop.mapper.StockstatusMapper;
import com.cmft.slas.cmuop.service.StockstatusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("stockstatusService")
public class StockstatusServiceImpl implements StockstatusService {
	
	@Autowired
	private StockstatusMapper stockstatusMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private BeanMapper beanMapper;

    public int addStockstatus(StockstatusDTO stockstatusDTO) {
        return stockstatusMapper.insertSelective(beanMapper.map(stockstatusDTO, Stockstatus.class));
    }

    @Override
    public int updateStockstatus(StockstatusDTO stockstatusDTO) {
        return stockstatusMapper
            .updateByPrimaryKeySelective(beanMapper.map(stockstatusDTO, Stockstatus.class));
    }

    @Override
    public int deleteStockstatus(Long id) {

        return stockstatusMapper.deleteByPrimaryKey(id);

    }

    @Override
    public StockstatusDTO queryById(Long id) {
        return beanMapper.copy(stockstatusMapper.selectByPrimaryKey(id), StockstatusDTO.class);
    }
    
    @Override
    public long countByCondition(StockstatusDTO stockstatusDTO) {
        return stockstatusMapper.countByCondition(stockstatusDTO);

    }

    @Override
    public List<StockstatusDTO> queryByCondition(StockstatusDTO stockstatusDTO) {
        List<StockstatusDTO> stockstatusDTOList = stockstatusMapper.queryByCondition(stockstatusDTO);
        return stockstatusDTOList;
    }

    @Override
    public PageInfo<StockstatusDTO> queryByPage(StockstatusDTO stockstatusDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("t_stock_status_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<StockstatusDTO> stockstatusDTOList = stockstatusMapper.queryByCondition(stockstatusDTO);
        PageInfo<StockstatusDTO> pageInfo = new PageInfo<>(stockstatusDTOList);

        return pageInfo;
    }
    
    @Override
	public List<StockstatusDTO> getStockstatusList(SortDTO sortDTO){
        //1.访问aisp获取数据结果
        //2.更新到Redis
        //3.更新到Mysql
        List<StockstatusDTO> stockStatusListASC = new ArrayList<>();
        Collections.sort(stockStatusListASC, new Comparator<StockstatusDTO>() {
            @Override
            public int compare(StockstatusDTO o1, StockstatusDTO o2) {
                return (int) Math.round(o1.getGrowth()-o2.getGrowth());
            }
        });
        List<StockstatusDTO> stockstatusListDESC = new ArrayList<>();
        stockstatusListDESC.addAll(stockStatusListASC);
        Collections.reverse(stockstatusListDESC);
        redisTemplate.opsForList().rightPushAll("stockStatusListASC",stockStatusListASC);
        redisTemplate.opsForList().rightPushAll("stockStatusListDESC",stockstatusListDESC);
		List<StockstatusDTO> stockstatusDTOList = stockstatusMapper.queryStockstatusList();
        return  stockstatusDTOList;
	}
}
