package com.cmft.slas.cmuop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.service.SearchFromESService;
import com.cmft.slas.es.domain.ArticleContent;
import com.cmft.slas.es.repository.ArticleESRepository;

import lombok.val;

/**
 * @Author liurp001
 * @Since 2019/12/31
 */
@Service
public class SearchFromESServiceImpl implements SearchFromESService {

    @Autowired
    private ArticleESRepository articleESRepository;

    @Autowired
    private TransportClient transportClient;

    @Value("${cmuop.es_index}")
    private String esIndex;

    @Override
    public List<String> searchByTitle(String title) {
        QueryBuilder query = QueryBuilders.wildcardQuery("title.keyword", "*" + title + "*");
        List<String> list = new ArrayList<>();
        val res = articleESRepository.search(query);
        res.forEach(articleContent -> {
            list.add(articleContent.getArticleId());
        });
        return list;
    }

    @Override
    public ArticleContent getArticle(String articleId) {
        QueryBuilder query = QueryBuilders.termQuery("articleId", articleId);

        List<ArticleContent> articleContextList = new ArrayList<>();
        val res = articleESRepository.search(query);
        res.forEach(articleContextList::add);
        return CollectionUtils.isEmpty(articleContextList) ? null : articleContextList.get(0);
    }

    @Override
    public boolean updateArticle(ArticleContent articleContent) {
        if(StringUtils.isBlank(articleContent.getContentHtml()))
            return true;
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(transportClient);
        Map<String, Object> params = new HashedMap<>();
        params.put("contentHtml", articleContent.getContentHtml());
        params.put("title", articleContent.getTitle());
        ScriptType type = ScriptType.INLINE;
        String lang = "painless";
        StringBuffer sb = new StringBuffer();
        sb.append("ctx._source.context_html = params.contentHtml;");
        sb.append("ctx._source.title = params.title;");
        val qb = QueryBuilders.boolQuery();
        qb.filter(matchQuery("articleId", articleContent.getArticleId()));
        Script script = new Script(type, lang, sb.toString(), params);
        BulkByScrollResponse response =
            updateByQuery.source(esIndex).script(script).filter(qb).abortOnVersionConflict(false).get();
        return response.getUpdated() > 0;
    }
}
