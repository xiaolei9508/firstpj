
package com.cmft.slas.cmuop.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmft.slas.cmuop.common.config.db.CommonMapper;
import com.cmft.slas.cmuop.dto.ArticleForSortDTO;
import com.cmft.slas.cmuop.dto.CountDTO;
import com.cmft.slas.cmuop.dto.StatDTO;
import com.cmft.slas.cmuop.entity.Article;
import com.cmft.slas.cmuop.vo.AllArticlePreview;
import com.cmft.slas.cmuop.vo.InfoStatVO;
import com.cmft.slas.cmuop.vo.SelectedPreview;

public interface ArticleMapper extends CommonMapper<Article> {

    List<AllArticlePreview> getAllArticleViaJoin(@Param("dateFrom") Date dateFrom,
                                                 @Param("dateTo") Date dateTo,
                                                 @Param("articleId") List<String> articleId,
                                                 @Param("item") StatDTO dto,
                                                 @Param("entityType") String entityType,
                                                 @Param("columnType") String columnType,
                                                 @Param("sort") String sort,
                                                 @Param("ifStick") Boolean ifStick);

    List<SelectedPreview> getSelectedViaJoin(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo,
                                             @Param("articleId") List<String> articleId,
                                             @Param("sort") String sort);


    Article getArticleByArticleId(@Param("articleId") String articleId);

    List<CountDTO> countIfshow(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo,
        @Param("articleId") List<String> articleId,
        @Param("fieldList") List<String> fieldList, @Param("item") StatDTO dto, @Param("entityType") String entityType,
        @Param("columnType") String columnType);

    List<InfoStatVO> countEntityArticle(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo,
        @Param("articleId") List<String> articleId,
        @Param("fieldList") List<String> fieldList, @Param("item") StatDTO dto, @Param("entityType") String entityType,
        @Param("columnType") String columnType);

    List<InfoStatVO> countTypeArticle(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo,
        @Param("articleId") List<String> articleId,
        @Param("fieldList") List<String> fieldList, @Param("item") StatDTO dto, @Param("entityType") String entityType,
        @Param("columnType") String columnType);

    List<CountDTO> countSentimentArticle(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo,
        @Param("articleId") List<String> articleId,
        @Param("fieldList") List<String> fieldList, @Param("item") StatDTO dto, @Param("entityType") String entityType,
        @Param("columnType") String columnType);


    List<Article> selectArticleListForCarousels();

    List<ArticleForSortDTO> selectArticleListForSort(Integer M);

    List<ArticleForSortDTO> selectArticleListByType(@Param("columnType") String columnType,
                                                    @Param("entityType") String entityType, @Param("offset") Long offset, @Param("M") Integer M);

    List<ArticleForSortDTO> selectStickArticleList(@Param("entityType") String entityType);

    List<ArticleForSortDTO> selectStickArticleListForCard(@Param("entityType") String entityType);

    List<ArticleForSortDTO> selectMuteSticks(@Param("entityType") String entityType);

    List<ArticleForSortDTO> selectMuteSticksForCard(@Param("entityType") String entityType, @Param("limit") Integer limit);

    List<ArticleForSortDTO> selectColumnArticle(@Param("columnType") String columnType,
        @Param("entityType") String entityType, @Param("sentimentType") Integer sentimentType,
        @Param("offset") String offset, @Param("M") Integer M, @Param("list") List<String> list);

    List<ArticleForSortDTO> selectOrderArticle(@Param("columnType") String columnType,
        @Param("entityType") String entityType, @Param("sentimentType") Integer sentimentType,
        @Param("lastDate") String lastDate,
        @Param("list") List<String> list);

    List<ArticleForSortDTO> selectColumnStickArticle(@Param("columnType") String columnType,
        @Param("entityType") String entityType, @Param("sentimentType") Integer sentimentType,
        @Param("list") List<String> list);

    List<Date> getDistinctPubTimeInDate(@Param("lastPosition") String lastPosition,
                                        @Param("entityCode") String entityCode,
                                        @Param("limit") Integer limit);

    List<ArticleForSortDTO> selectLeaderArticleWithCursor(@Param("entityCode") String entityCode,
                                                          @Param("pubTime") String pubTime,
                                                          @Param("orderNum") Integer orderNum,
                                                          @Param("sentiment") Integer sentiment,
                                                          @Param("pageSize") Integer pageSize,
                                                          @Param("unlikes") List<String> unlikes);

    Integer getRecommendNum();
}
