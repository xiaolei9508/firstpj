package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.common.constant.LabelType;
import com.cmft.slas.cmuop.entity.ArticleLabel;
import com.cmft.slas.cmuop.entity.CmuopLabel;
import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.mapper.ArticleLabelMapper;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.CmuopLabelMapper;
import com.cmft.slas.cmuop.mapper.UserMapper;
import com.cmft.slas.cmuop.vo.ArticleVO;
import com.cmft.slas.cmuop.vo.LabelVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/1/15
 */
@Component
public class LabelProcessor {

    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    @Autowired
    private CmuopLabelMapper labelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CmuopEntityMapper entityMapper;

    public void processAppDetailLabels(ArticleVO articleVO){
        Example example = new Example(ArticleLabel.class);
        example.createCriteria().andEqualTo("articleId", articleVO.getArticleId());
        List<ArticleLabel> articleLabels = articleLabelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(articleLabels))
            return;
        Map<String, List<ArticleLabel>> articleLabelMap = articleLabels.stream().collect(Collectors.groupingBy(ArticleLabel::getLabelType));
        List<LabelVO> allLabels = new ArrayList<>();
        articleLabelMap.forEach((key, list) -> {
            if(CollectionUtils.isNotEmpty(list)) {
                switch (LabelType.getByCode(key)) {
                    case ENTITY:
                        allLabels.addAll(processEntity(list));
                        break;
                    case USER:
                        allLabels.addAll(processUser(list));
                        break;
                    case LABEL:
                        allLabels.addAll(processLabel(list));
                        break;
                    case OTHER:
                        allLabels.addAll(processOthers(list));
                        break;
                }
            }
        });
        articleVO.setLabels(allLabels);
    }

    private List<LabelVO> processLabel(List<ArticleLabel> articleLabels){
        List<String> labelIds = articleLabels.stream().map(ArticleLabel::getLabelId).collect(Collectors.toList());
        Example example = new Example(CmuopLabel.class);
        example.createCriteria().andIn("labelCode", labelIds);
        List<CmuopLabel> labels = labelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(labels))
            return new ArrayList<>();
        List<LabelVO> labelVOS = new ArrayList<>();
        labels.forEach(label ->{
            LabelVO vo = new LabelVO();
            vo.setName(label.getLabelName());
            vo.setCode(label.getLabelCode());
            vo.setType(LabelType.LABEL.code);
            labelVOS.add(vo);
        });
        return labelVOS;
    }

    private List<LabelVO> processUser(List<ArticleLabel> articleLabels){
        List<String> userIds = articleLabels.stream().map(ArticleLabel::getLabelId).collect(Collectors.toList());
        Example example = new Example(User.class);
        example.createCriteria().andIn("uid", userIds);
        List<User> users = userMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(users))
            return new ArrayList<>();
        List<LabelVO> labelVOS = new ArrayList<>();
        users.forEach(user -> {
            LabelVO vo = new LabelVO();
            vo.setCode(user.getUid());
            vo.setName(user.getName());
            vo.setType(LabelType.USER.code);
            labelVOS.add(vo);
        });
        return labelVOS;
    }

    private List<LabelVO> processEntity(List<ArticleLabel> articleLabels){
        List<String> labelCodes = articleLabels.stream().map(ArticleLabel::getLabelId).collect(Collectors.toList());
        Example example = new Example(CmuopLabel.class);
        example.createCriteria().andIn("labelCode", labelCodes);
        List<CmuopLabel> labels = labelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(labels))
            return new ArrayList<>();
        List<LabelVO> labelVOS = new ArrayList<>();
        labels.forEach(label -> {
            LabelVO vo = new LabelVO();
            vo.setCode(label.getLabelCode());
            vo.setName(label.getLabelName());
            vo.setType(LabelType.ENTITY.code);
            labelVOS.add(vo);
        });
        return labelVOS;
    }

    private List<LabelVO> processOthers(List<ArticleLabel> articleLabels){
        List<LabelVO> labels = new ArrayList<>();
        articleLabels.forEach(articleLabel -> {
            LabelVO label = new LabelVO();
            label.setCode("");
            label.setName(articleLabel.getLabelId());
            label.setType(LabelType.OTHER.code);
            labels.add(label);
        });
        return labels;
    }
}
