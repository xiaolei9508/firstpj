package com.cmft.slas.cmuop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.DictConst;
import com.cmft.slas.cmuop.dto.UserNotLikeDTO;
import com.cmft.slas.cmuop.entity.CmuopDictItem;
import com.cmft.slas.cmuop.entity.UserNotLike;
import com.cmft.slas.cmuop.mapper.CmuopDictItemMapper;
import com.cmft.slas.cmuop.mapper.UserNotLikeMapper;
import com.cmft.slas.cmuop.service.ArticleBlockingService;
import com.cmft.slas.cmuop.vo.BlockingReasonsVO;

import tk.mybatis.mapper.entity.Example;

/**
 * @Author liurp001
 * @Since 2020/1/9
 */
@Service
public class ArticleBlockingServiceImpl implements ArticleBlockingService {
    @Autowired
    private CmuopDictItemMapper cmuopDictItemMapper;

    @Autowired
    private UserNotLikeMapper userNotLikeMapper;

    @Override
    public List<BlockingReasonsVO> getBlockingReasons() {
        Example example = new Example(CmuopDictItem.class);
        example.createCriteria().andEqualTo("dictCode", DictConst.NOT_LIKE_REASON.getCode()).andEqualTo("isDelete", (byte)0);
        List<CmuopDictItem> dictList = cmuopDictItemMapper.selectByExample(example);
        List<BlockingReasonsVO> reasons = new ArrayList<>();
        dictList.forEach(item -> reasons.add(new BlockingReasonsVO().setCode(item.getValue()).setName(item.getText())));
        return reasons;
    }

    @Override
    public String BlockArticle(UserNotLikeDTO userNotLikeDTO) {
        UserNotLike notLike = new UserNotLike();
        BeanUtils.copyProperties(userNotLikeDTO, notLike);
        userNotLikeMapper.insertSelective(notLike);
        return "操作成功";
    }
}
