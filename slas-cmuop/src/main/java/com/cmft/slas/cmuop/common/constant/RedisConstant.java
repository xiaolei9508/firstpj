package com.cmft.slas.cmuop.common.constant;

/**
 * redis key
 */
public interface RedisConstant {

    /**
     * 数据字典列表值
     */
    public static String DICTITEM_LIST_KEY = "dictItem_list";

    /**
     * 数据字典根据dictCode分类
     */
    public static String DICTITEM_DATA_DICTCODE_KEY = "dictItem_%s";

    /**
     * 实体列表
     */
    public static String ENTITY_LIST_KEY = "entity_list";

    /**
     * 实体关系列表
     */
    public static String ENTITY_RELATED_LIST_KEY = "entity_related_list";

    /**
     * 行业列表
     */
    public static String FIELD_LIST_KEY = "field_list";

    /**
     * 标签根据labelType分类
     */
    public static String LABEL_DATA_LABELTYPE_KEY = "label_%s";

    /**
     * 实体上下级映射表
     */
    public static String RELATED_ENTITY_MAP = "related_entity_map";
}
