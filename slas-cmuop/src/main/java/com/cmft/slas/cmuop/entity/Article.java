package com.cmft.slas.cmuop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmft.slas.cmuop.common.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Table(name = "t_article")
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
public class Article extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
        
    /**
    * 主键
    */
    @Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tArticleId;
    
    /**
    * 文章id
    */
    private String articleId;
    
    /**
    * 媒体类型
    */
    private Integer mediaType;
    
    /**
    * 内容分类
    */
    private Integer pLabelId;
	
	/**
    * 发布日期
    */     
	private Date pubDate;
	
	/**
    * 发布时间
    */     
	private Date pubTime;
    
    /**
    * 作者
    */
    private String author;
    
    /**
    * 发布来源
    */
    private String source;
    
    /**
    * 来源评级
    */
    private Integer grade;
    
    /**
    * 情感分类
    */
    private Integer sentiment;
    
    /**
    * 推荐指数
    */
    private Integer recommendIndex;
    
    /**
    * 标题
    */
    private String title;
    
    /**
    * 网址
    */
    private String url;
    
    /**
    * 摘要
    */
    private String summary;
    
    /**
    * 情感句
    */
    private String sentiSentences;
    
    /**
    * 展示情感句
    */
    private String sentiSentencesShow;

	/**
    * 是否属于招闻天下
    */
    private Boolean ifZhaowen;
    
	/**
    * 是否来源于oa系统
    */
    private Boolean ifFromOa;
    
    /**
     * oa所属分类
     */
    private String authorizedArea;

    /**
     * 实体等级
     */
    private String entityLevel;
    
	/**
    * 是否股评
    */
    private Boolean ifReport;
    
    /**
    * 观点归属
    */
    private String pointOfView;
    
    /**
    * 行业板块
    */
    private String field;
    
    /**
    * 领导专栏
    */
    private String entrepreneur;
    
    /**
    * 信任等级
    */
    private Integer trustLevel;

    /**
    * 文章类别
    */
    private String typeOfContent;
    
	/**
    * 是否显示
    */
    private Boolean ifShow;
    
	/**
    * 是否分类置顶
    */
    private Boolean ifPartStick;
    
	/**
    * 是否全部置顶
    */
    private Boolean ifAllStick;
    
	/**
    * 是否置顶到轮播
    */
    private Boolean ifStick;
    
    /**
     * 置顶到轮播时间
     */
    private Date stickUpdateTime;

    /**
    * 缩略图地址
    */
    private String imgUrl;

    /**
     * 轮播图地址
     */
    private String topicImgUrl;

    /**
    * 顺序
    */
    private Integer orderNum;

    /**
     * oa重要性
     */
    private Integer oaImportance;

    /**
     * 文章质量
     */
    private Float contentQuality;

    /**
     * 是否推荐
     */
    private Boolean ifRecommend;
}
