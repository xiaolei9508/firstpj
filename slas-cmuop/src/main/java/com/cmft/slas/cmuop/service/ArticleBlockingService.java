package com.cmft.slas.cmuop.service;

import com.cmft.slas.cmuop.dto.UserNotLikeDTO;
import com.cmft.slas.cmuop.vo.BlockingReasonsVO;

import java.util.List;

/**
 * @Author liurp001
 * @Since 2020/1/9
 */
public interface ArticleBlockingService {
    List<BlockingReasonsVO> getBlockingReasons();
    String BlockArticle(UserNotLikeDTO userNotLikeDTO);
}
