package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.RuleDTO;
import com.cmft.slas.cmuop.entity.Rule;

public interface RuleMapper extends CommonMapper<Rule>{
    
    long countByCondition(RuleDTO ruleDTO);

    List<Rule> queryByCondition(RuleDTO ruleDTO);

	List<Rule> queryRuleList();

    List<Rule> queryRootRule();
}
