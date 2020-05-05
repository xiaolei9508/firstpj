package com.cmft.slas.cmuop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.dto.RuleNode;
import com.cmft.slas.cmuop.entity.Rule;
import com.cmft.slas.cmuop.mapper.RuleMapper;
import com.cmft.slas.cmuop.service.RuleService;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public Map<String, RuleNode> getRuleTree() {
        List<Rule> list = ruleMapper.queryRootRule();
        Map<String, RuleNode> map = new HashMap<>();
        for (Rule rule : list) {
            RuleNode root = new RuleNode();
            root.setValue(rule.getRuleCondition());
            root.setValueType(6);
            setRuleNode(root, rule);
            map.put(rule.getPartId(), root);
        }
        return map;
    }

    public void setRuleNode(RuleNode ruleNode, Rule rule) {
        if (rule.getLeftParamType() == 6) {
            Rule leftRule = ruleMapper.selectByPrimaryKey(Long.valueOf(rule.getLeftParam()));
            RuleNode leftRuleNode = new RuleNode();
            leftRuleNode.setValue(leftRule.getRuleCondition());
            leftRuleNode.setValueType(6);
            ruleNode.setLeftNode(leftRuleNode);
            setRuleNode(leftRuleNode, leftRule);
        } else {
            RuleNode leftRuleNode = new RuleNode();
            leftRuleNode.setValue(rule.getLeftParam());
            leftRuleNode.setValueType(rule.getLeftParamType());
            ruleNode.setLeftNode(leftRuleNode);
        }
        if (rule.getRightParamType() == 6) {
            Rule rightRule = ruleMapper.selectByPrimaryKey(Long.valueOf(rule.getRightParam()));
            RuleNode rightRuleNode = new RuleNode();
            rightRuleNode.setValue(rightRule.getRuleCondition());
            rightRuleNode.setValueType(6);
            ruleNode.setRightNode(rightRuleNode);
            setRuleNode(rightRuleNode, rightRule);
        } else {
            RuleNode rightRuleNode = new RuleNode();
            rightRuleNode.setValue(rule.getRightParam());
            rightRuleNode.setValueType(rule.getRightParamType());
            ruleNode.setRightNode(rightRuleNode);
        }

    }

}
