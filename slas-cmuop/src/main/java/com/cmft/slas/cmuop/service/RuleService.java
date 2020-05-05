package com.cmft.slas.cmuop.service;

import java.util.Map;

import com.cmft.slas.cmuop.dto.RuleNode;

public interface RuleService {

    Map<String, RuleNode> getRuleTree();
}
