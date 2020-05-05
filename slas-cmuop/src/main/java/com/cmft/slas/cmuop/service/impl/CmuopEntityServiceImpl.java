package com.cmft.slas.cmuop.service.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.cmft.slas.cmuop.service.EntityTypeService;
import com.cmft.slas.cmuop.vo.EntityTreeVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.constant.RedisConstant;
import com.cmft.slas.cmuop.common.utils.CastListUtil;
import com.cmft.slas.cmuop.dto.CmuopEntityDTO;
import com.cmft.slas.cmuop.dto.EntityRelatedDTO;
import com.cmft.slas.cmuop.dto.RelatedEntityDTO;
import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.EntityRelated;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.EntityRelatedMapper;
import com.cmft.slas.cmuop.service.CmuopEntityService;
import com.cmft.slas.common.pojo.Page;
import com.cmft.slas.common.utils.BeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class CmuopEntityServiceImpl implements CmuopEntityService {

    private static final Logger logger = LoggerFactory.getLogger(CmuopEntityServiceImpl.class);

    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private EntityRelatedMapper entityRelatedMapper;

    @Autowired
    private OaAuthorityServiceImpl oaAuthorityService;

    @Autowired
    private EntityTypeService entityTypeService;

    @Value("${oa.data-path}")
    private String oaDataPath;

    @Value("${nuc.callerModule}")
    private String callerModule;

    @Value("${nuc.app-key}")
    private String appKey;

    @Value("${nuc.secret-key}")
    private String secretKey;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int addCmuopEntity(CmuopEntityDTO cmuopEntityDTO) {
        if (null == cmuopEntityDTO.getIsValid()) {
            cmuopEntityDTO.setIsValid((byte)0);
        }
        cleanEntityData();
        entityTypeService.initEntityTypeByEntityCode(cmuopEntityDTO.getEntityCode(), false);
        return cmuopEntityMapper.insertSelective(BeanMapper.map(cmuopEntityDTO, CmuopEntity.class));
    }

    @Override
    public int updateCmuopEntity(CmuopEntityDTO cmuopEntityDTO) {
        cleanEntityData();
        return cmuopEntityMapper.updateByPrimaryKeySelective(BeanMapper.map(cmuopEntityDTO, CmuopEntity.class));
    }

    @Override
    public int deleteCmuopEntity(Long id) {
        cleanEntityData();
        return cmuopEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CmuopEntityDTO queryById(Long id) {
        return BeanMapper.copy(cmuopEntityMapper.selectByPrimaryKey(id), CmuopEntityDTO.class);
    }

    @Override
    public long countByCondition(CmuopEntityDTO cmuopEntityDTO) {
        return cmuopEntityMapper.countByCondition(cmuopEntityDTO);

    }

    @Override
    public List<CmuopEntityDTO> queryByCondition(CmuopEntityDTO cmuopEntityDTO) {
        return BeanMapper.mapList(cmuopEntityMapper.queryByCondition(cmuopEntityDTO), CmuopEntityDTO.class);
    }

    @Override
    public PageInfo<CmuopEntityDTO> queryByPage(CmuopEntityDTO cmuopEntityDTO, Page page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            page.setOrderBy("entity_id DESC");
        }
        /*
         * pagehelper分页
         */
        PageHelper.startPage(page);

        List<CmuopEntityDTO> cmuopEntityDTOList =
            BeanMapper.mapList(cmuopEntityMapper.queryByCondition(cmuopEntityDTO), CmuopEntityDTO.class);

        return new PageInfo<>(cmuopEntityDTOList);
    }

    @Override
    public List<CmuopEntityDTO> getCmuopEntityRelatedList() {
        List<CmuopEntityDTO> cmuopEntityDTOList =
            BeanMapper.mapList(cmuopEntityMapper.queryCmuopEntityList(new CmuopEntityDTO()), CmuopEntityDTO.class);
        cmuopEntityDTOList.forEach(cmuopEntity -> {
            ArrayList<CmuopEntityDTO> arrayList = new ArrayList<>();
            String entityCode = cmuopEntity.getEntityCode();
            String searchWords = cmuopEntity.getSearchWords();
            if (StringUtils.isNotBlank(searchWords)) {
                cmuopEntity.setSearchWordList(Arrays.asList(searchWords.split(",")));
            }
            EntityRelatedDTO entityRelatedDTO = new EntityRelatedDTO();
            entityRelatedDTO.setEntityCode(entityCode);
            List<EntityRelated> entityRelatedList = entityRelatedMapper.queryByCondition(entityRelatedDTO);
            entityRelatedList.forEach(entityRelated -> {
                String relatedEntityCode = entityRelated.getRelatedEntityCode();
                CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
                cmuopEntityDTO.setEntityCode(relatedEntityCode);
                List<CmuopEntity> cmuopEntities = cmuopEntityMapper.queryByCondition(cmuopEntityDTO);
                if (CollectionUtils.isNotEmpty(cmuopEntities)) {
                    CmuopEntity cEnitty = cmuopEntities.get(0);
                    cmuopEntityDTO.setEntityName(cEnitty.getEntityName());
                    cmuopEntityDTO.setIsValid(cEnitty.getIsValid());
                    String searchWordsIn = cEnitty.getSearchWords();
                    if (StringUtils.isNotBlank(searchWordsIn)) {
                        cmuopEntityDTO.setSearchWordList(Arrays.asList(searchWordsIn.split(",")));
                    }
                }
                arrayList.add(cmuopEntityDTO);
            });
            cmuopEntity.setRelatedEntityList(arrayList);
        });
        return cmuopEntityDTOList;
    }

    @Override
    public List<String> queryEntityCodeByNuc(String entityName) {
        List<String> businessUnitList = new ArrayList<>();
        Connection connection = null;
        try {
            String dirver = "org.sqlite.JDBC";
            Class.forName(dirver);
            String filePath = oaDataPath.concat(File.separator).concat("origin_bak.db");
            File file = new File(filePath);
            if (!file.exists()) {
                oaAuthorityService.getNucDataFile();
            }
            String url = "jdbc:sqlite:".concat(filePath);
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                .executeQuery("select * from paas_uc_business_unit where business_name like '%" + entityName + "%'");
            while (resultSet.next()) {
                String businessUnit = resultSet.getString("business_unit");
                businessUnitList.add(businessUnit);
            }
        } catch (Exception e) {
            logger.error("读取nuc的数据出现错误：" + ExceptionUtils.getStackTrace(e));
        }
        return businessUnitList;
    }

    @Override
    public List<CmuopEntityDTO> getCmuopEntityList() {
        CmuopEntityDTO cmuopEntityDTOIN = new CmuopEntityDTO();
        cmuopEntityDTOIN.setIfCmuop((byte)1);
        List<CmuopEntityDTO> cmuopEntityDTOList =
            BeanMapper.mapList(cmuopEntityMapper.queryCmuopEntityList(cmuopEntityDTOIN), CmuopEntityDTO.class);
        for (CmuopEntityDTO cmuopEntityDTO : cmuopEntityDTOList) {
            String searchWords = cmuopEntityDTO.getSearchWords();
            if (StringUtils.isNotBlank(searchWords)) {
                String[] split = searchWords.split(",");
                cmuopEntityDTO.setSearchWordList(Arrays.asList(split));
            }

        }
        return cmuopEntityDTOList;
    }

    @Override
    public Map<String, String> getCmuopEntityMap() {
        CmuopEntityDTO params = new CmuopEntityDTO();
        params.setIfCmuop((byte)1);
        List<CmuopEntity> list = cmuopEntityMapper.queryByCondition(params);
        Map<String, String> entityMap = new HashMap<>();
        for (CmuopEntity entity : list) {
            entityMap.put(entity.getEntityCode(), entity.getEntityName());
        }
        return entityMap;
    }

    /**
     * 清除实体数据
     */
    public void cleanEntityData() {
        redisTemplate.delete(RedisConstant.ENTITY_LIST_KEY);
        redisTemplate.delete(RedisConstant.ENTITY_RELATED_LIST_KEY);
    }

    /**
     * 拉平为map返回（深度优先，并按按order_um升序）
     */
    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, RelatedEntityDTO> getRelatedEntityMap(boolean ifFresh) {
        LinkedHashMap<String, RelatedEntityDTO> relatedEntityMap = new LinkedHashMap<>();
        if (!ifFresh) {
            relatedEntityMap = (LinkedHashMap<String, RelatedEntityDTO>)redisTemplate.opsForValue()
                .get(RedisConstant.RELATED_ENTITY_MAP);
        }
        if (MapUtils.isEmpty(relatedEntityMap) || ifFresh) {
            relatedEntityMap = new LinkedHashMap<>();

            // 取一级公司（招商局）添加
            CmuopEntityDTO params = new CmuopEntityDTO();
            params.setParentCode("");
            params.setIfCmuop((byte)1);
            List<CmuopEntity> list = cmuopEntityMapper.queryByCondition(params);
            for (CmuopEntity entity : list) {
                RelatedEntityDTO relatedEntity = new RelatedEntityDTO();
                relatedEntity.setParentCode(entity.getParentCode());

                params = new CmuopEntityDTO();
                params.setParentCode(entity.getEntityCode());
                params.setIfCmuop((byte)1);
                List<CmuopEntity> childrenEntityList = cmuopEntityMapper.queryByCondition(params);
                List<String> childrenList = new ArrayList<>();
                childrenEntityList.forEach(children -> {
                    childrenList.add(children.getEntityCode());
                });
                relatedEntity.setChildrenList(childrenList);
                relatedEntityMap.put(entity.getEntityCode(), relatedEntity);

                // 取二级公司并添加
                for (CmuopEntity childEntity : childrenEntityList) {
                    RelatedEntityDTO secondRelatedEntity = new RelatedEntityDTO();
                    secondRelatedEntity.setParentCode(childEntity.getParentCode());

                    params = new CmuopEntityDTO();
                    params.setParentCode(childEntity.getEntityCode());
                    params.setIfCmuop((byte)1);
                    List<CmuopEntity> secondChildrenEntityList = cmuopEntityMapper.queryByCondition(params);
                    List<String> secondChildrenList = new ArrayList<>();
                    secondChildrenEntityList.forEach(secondChildren -> {
                        secondChildrenList.add(secondChildren.getEntityCode());
                    });
                    secondRelatedEntity.setChildrenList(secondChildrenList);
                    relatedEntityMap.put(childEntity.getEntityCode(), secondRelatedEntity);

                    // 取三级公司并添加
                    for (CmuopEntity thirdchildEntity : secondChildrenEntityList) {
                        RelatedEntityDTO thirdRelatedEntity = new RelatedEntityDTO();
                        thirdRelatedEntity.setParentCode(thirdchildEntity.getParentCode());

                        params = new CmuopEntityDTO();
                        params.setParentCode(thirdchildEntity.getEntityCode());
                        params.setIfCmuop((byte)1);
                        List<CmuopEntity> thirdChildrenEntityList = cmuopEntityMapper.queryByCondition(params);
                        List<String> thirdChildrenList = new ArrayList<>();
                        thirdChildrenEntityList.forEach(thridChildren -> {
                            thirdChildrenList.add(thridChildren.getEntityCode());
                        });
                        thirdRelatedEntity.setChildrenList(thirdChildrenList);
                        relatedEntityMap.put(thirdchildEntity.getEntityCode(), thirdRelatedEntity);

                        /*
                         * 暂未支持四级
                         */
                    }
                }
            }
            redisTemplate.opsForValue().set(RedisConstant.RELATED_ENTITY_MAP, relatedEntityMap, 24L, TimeUnit.HOURS);
        }
        return relatedEntityMap;
    }

    @Override
    public List<CmuopEntityDTO> queryCmuopEntityIsValidList() {
        List<CmuopEntityDTO> cmuopEntityDTOList =
            CastListUtil.castList(redisTemplate.opsForValue().get(RedisConstant.ENTITY_LIST_KEY), CmuopEntityDTO.class);
        if (CollectionUtils.isEmpty(cmuopEntityDTOList)) {
            Example example = new Example(CmuopEntity.class);
            example.createCriteria().andEqualTo("isValid", 1).andEqualTo("isDelete", 0);
            cmuopEntityDTOList = BeanMapper.mapList(cmuopEntityMapper.selectByExample(example), CmuopEntityDTO.class);
            for (CmuopEntityDTO cmuopEntityDTO : cmuopEntityDTOList) {
                String searchWords = cmuopEntityDTO.getSearchWords();
                if (StringUtils.isNotBlank(searchWords)) {
                    String[] split = searchWords.split(",");
                    cmuopEntityDTO.setSearchWordList(Arrays.asList(split));
                }
            }
            redisTemplate.opsForValue().set(RedisConstant.ENTITY_LIST_KEY, cmuopEntityDTOList);
        }
        return cmuopEntityDTOList;
    }

    @Override
    public List<CmuopEntityDTO> queryEntityRelatedIsValid() {
        List<CmuopEntityDTO> cmuopEntityDTOList = CastListUtil
            .castList(redisTemplate.opsForValue().get(RedisConstant.ENTITY_RELATED_LIST_KEY), CmuopEntityDTO.class);
        if (CollectionUtils.isEmpty(cmuopEntityDTOList)) {
            Example cmuopEntityExample = new Example(CmuopEntity.class);
            cmuopEntityExample.createCriteria().andEqualTo("isValid", 1).andEqualTo("isDelete", 0);
            cmuopEntityDTOList =
                BeanMapper.mapList(cmuopEntityMapper.selectByExample(cmuopEntityExample), CmuopEntityDTO.class);
            cmuopEntityDTOList.forEach(cmuopEntity -> {
                ArrayList<CmuopEntityDTO> arrayList = new ArrayList<>();
                String entityCode = cmuopEntity.getEntityCode();
                String searchWords = cmuopEntity.getSearchWords();
                if (StringUtils.isNotBlank(searchWords)) {
                    cmuopEntity.setSearchWordList(Arrays.asList(searchWords.split(",")));
                }
                EntityRelatedDTO entityRelatedDTO = new EntityRelatedDTO();
                entityRelatedDTO.setEntityCode(entityCode);
                entityRelatedDTO.setIsValid((byte)1);
                List<EntityRelated> entityRelatedList = entityRelatedMapper.queryByCondition(entityRelatedDTO);
                entityRelatedList.forEach(entityRelated -> {
                    String relatedEntityCode = entityRelated.getRelatedEntityCode();
                    CmuopEntityDTO cmuopEntityDTO = new CmuopEntityDTO();
                    cmuopEntityDTO.setEntityCode(relatedEntityCode);
                    cmuopEntityDTO.setIsValid((byte)1);
                    List<CmuopEntity> cmuopEntities = cmuopEntityMapper.queryByCondition(cmuopEntityDTO);
                    if (CollectionUtils.isNotEmpty(cmuopEntities)) {
                        CmuopEntity cEnitty = cmuopEntities.get(0);
                        cmuopEntityDTO.setEntityName(cEnitty.getEntityName());
                        cmuopEntityDTO.setIsValid(cEnitty.getIsValid());
                        String searchWordsIn = cEnitty.getSearchWords();
                        if (StringUtils.isNotBlank(searchWordsIn)) {
                            cmuopEntityDTO.setSearchWordList(Arrays.asList(searchWordsIn.split(",")));
                        }
                    }
                    arrayList.add(cmuopEntityDTO);
                });
                cmuopEntity.setRelatedEntityList(arrayList);
            });
            redisTemplate.opsForValue().set(RedisConstant.ENTITY_RELATED_LIST_KEY, cmuopEntityDTOList);
        }
        return cmuopEntityDTOList;
    }

    @Override
    public EntityTreeVo queryEntityTree(String entityCode) {
        EntityTreeVo rootEntityVo = new EntityTreeVo();
        rootEntityVo.setEntityCode(entityCode);
        List<CmuopEntity> entityList = getEntityList();
        Map<String, CmuopEntity> entityMap = getEntityMap(entityList);
        Map<String, List<CmuopEntity>> parentEntityMap = getParentEntityMap(entityList);
        rootEntityVo = processEntityVO(rootEntityVo, entityMap, parentEntityMap);
        return rootEntityVo;
    }

    private EntityTreeVo processEntityVO(EntityTreeVo entityVo, Map<String, CmuopEntity> entityMap, Map<String, List<CmuopEntity>> parentEntityMap) {
         CmuopEntity entity = entityMap.get(entityVo.getEntityCode());
        if(entity == null) {
            return entityVo;
        }
        entityVo = BeanMapper.map(entity, EntityTreeVo.class);
        List<EntityTreeVo> childEntityVoList = new ArrayList<>();
        List<CmuopEntity> childEntityList = parentEntityMap.get(entityVo.getEntityCode());
        if(CollectionUtils.isNotEmpty(childEntityList)) {
            for(CmuopEntity childEntity : childEntityList) {
                EntityTreeVo childEntityVo = new EntityTreeVo();
                childEntityVo.setEntityCode(childEntity.getEntityCode());
                childEntityVo = processEntityVO(childEntityVo, entityMap, parentEntityMap);
                childEntityVoList.add(childEntityVo);
            }
        }
        entityVo.setChildEntity(childEntityVoList);
        return entityVo;
    }

    private List<CmuopEntity> getEntityList() {
        CmuopEntityDTO params = new CmuopEntityDTO();
        params.setIfCmuop((byte)1);
        params.setIsDelete((byte)0);
        List<CmuopEntity> entityList = cmuopEntityMapper.queryByCondition(params);
        return entityList;
    }

    private Map<String, CmuopEntity> getEntityMap(List<CmuopEntity> entityList) {
        if(CollectionUtils.isEmpty(entityList)) {
            return new HashMap<>();
        }
        return entityList.stream().collect(Collectors.toMap(CmuopEntity::getEntityCode, cmuopEntity -> cmuopEntity));
    }

    private Map<String, List<CmuopEntity>> getParentEntityMap(List<CmuopEntity> entityList) {
        Map<String, List<CmuopEntity>> entityMap = new HashMap<>();
        for(CmuopEntity entity: entityList) {
            List<CmuopEntity> entities = entityMap.get(entity.getParentCode());
            if(CollectionUtils.isEmpty(entities)){
                entities = new ArrayList<>();
            }
            entities.add(entity);
            entityMap.put(entity.getParentCode(), entities);
        }
        return entityMap;
    }
}
