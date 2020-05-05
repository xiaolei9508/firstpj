package com.cmft.slas.cmuop.processor;

import com.cmft.slas.cmuop.vo.ArticleDetail;
import com.cmft.slas.cmuop.vo.ArticlePreview;

import java.util.List;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
public interface ArticlePreviewProcessor {
    void processPreview(List<? extends ArticlePreview> articlePreview);

    void processDetail(ArticleDetail articleDetail);
}
