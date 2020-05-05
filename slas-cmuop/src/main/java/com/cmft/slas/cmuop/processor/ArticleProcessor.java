package com.cmft.slas.cmuop.processor;

import com.cmft.slas.cmuop.vo.ArticleVO;

/**
 * @Author liurp001
 * @Since 2020/1/2
 */
public interface ArticleProcessor {

    void processVO(ArticleVO article, String userId);
}
