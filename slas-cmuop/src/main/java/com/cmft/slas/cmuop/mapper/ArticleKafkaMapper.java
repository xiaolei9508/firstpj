package com.cmft.slas.cmuop.mapper;

import java.util.List;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.CountDTO;
import com.cmft.slas.cmuop.entity.ArticleKafka;

public interface ArticleKafkaMapper extends CommonMapper<ArticleKafka> {
    List<CountDTO> countArticleKafka(String from, String to);
}
