package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.entity.CmuopEntity;
import com.cmft.slas.cmuop.entity.UserEntity;
import com.cmft.slas.cmuop.mapper.CmuopEntityMapper;
import com.cmft.slas.cmuop.mapper.UserEntityMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author liurp001
 * @Since 2020/1/13
 */
@Component
public class EntityProcessor {
    @Autowired
    private CmuopEntityMapper cmuopEntityMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    public List<CmuopEntity> generateCompletetree(List<CmuopEntity> allEntities, String userId){
        List<String> relatedNodes = userId == null ? new ArrayList<>() : getRelatedEntities(allEntities, userId);
        CmuopEntity root = null;
        for(CmuopEntity entity: allEntities){
            if(StringUtils.isBlank(entity.getParentCode())){
                root = entity;
                break;
            }
        }
        if(root == null)
            return new ArrayList<>();
        allEntities.remove(root);
        List<CmuopEntity> orderedList = new ArrayList<>();
        orderedList.add(root);
//        orderedList.addAll(breathFirstTree(allEntities, root));
        orderedList.addAll(depthFirstTree(allEntities, relatedNodes, root));
        return orderedList;
    }

    private List<String> getRelatedEntities(List<CmuopEntity> allEntities, String uid){
        List<String> userRelatedEntities = getUserEntityList(uid);
        Map<String, CmuopEntity> findParentMap = allEntities.stream().collect(Collectors.toMap(CmuopEntity::getEntityCode, value -> value));
        List<String> resultList = new ArrayList<>(userRelatedEntities);
        userRelatedEntities.forEach(entityCode -> {
            String code = entityCode;
            while (true) {
                CmuopEntity entity = findParentMap.get(code);
                if (entity == null)
                    break;
                String parentCode = entity.getParentCode();
                resultList.add(parentCode);
                code = parentCode;
            }
        });
        return resultList.stream().distinct().collect(Collectors.toList());
    }


    public List<String> getUserEntityList(String uid){
        Example userEg = new Example(UserEntity.class);
        userEg.createCriteria().andEqualTo("uid", uid).andEqualTo("isDelete", (byte) 0);
        return userEntityMapper.selectByExample(userEg).stream().map(UserEntity::getEntityCode).distinct().collect(Collectors.toList());
    }

    public List<CmuopEntity> getAllEntities(){
        Example example = new Example(CmuopEntity.class);
        example.createCriteria().andEqualTo("ifCmuop", true).andEqualTo("isDelete", (byte)0);
        return cmuopEntityMapper.selectByExample(example);
    }

    public List<CmuopEntity> getAllEntitiesByOrderNum() {
        Example example = new Example(CmuopEntity.class);
        example.createCriteria().andEqualTo("ifCmuop", true).andEqualTo("isDelete", (byte)0);
        example.orderBy("orderNum");
        return cmuopEntityMapper.selectByExample(example);
    }

    private List<CmuopEntity> breathFirstTree(List<CmuopEntity> entityList, CmuopEntity root){
        Map<String, List<CmuopEntity>> subTreeMap = entityList.stream().collect(Collectors.groupingBy(CmuopEntity::getParentCode));
        List<CmuopEntity> nextRoots = subTreeMap.get(root.getEntityCode());
        if(CollectionUtils.isEmpty(nextRoots))
            return nextRoots;
        List<CmuopEntity> thisLevelEntities = new ArrayList<>(nextRoots);
        nextRoots.sort(Comparator.comparing(CmuopEntity::getOrderNum));
        entityList.removeAll(nextRoots);
        nextRoots.forEach(node->{
            List<CmuopEntity> childList = breathFirstTree(entityList,node);
            if(CollectionUtils.isNotEmpty(childList)){
                thisLevelEntities.addAll(childList);
            }
        });
        return thisLevelEntities;
    }

    private List<CmuopEntity> depthFirstTree(List<CmuopEntity> entityList, List<String> relatedNodes, CmuopEntity root){
        Map<String, List<CmuopEntity>> subTreeMap = entityList.stream().collect(Collectors.groupingBy(CmuopEntity::getParentCode));
        List<CmuopEntity> nextRoots = subTreeMap.get(root.getEntityCode());
        if(CollectionUtils.isEmpty(nextRoots))
            return nextRoots;
        List<CmuopEntity> thisLevelEntities = new ArrayList<>();
        nextRoots = sortNodes(nextRoots, relatedNodes);
        nextRoots.forEach(node -> {
            thisLevelEntities.add(node);
            List<CmuopEntity> nextList = subTreeMap.get(node.getEntityCode());
            if(CollectionUtils.isNotEmpty(nextList)){
                List<CmuopEntity> childList = depthFirstTree(nextList,relatedNodes, node);
                if(CollectionUtils.isNotEmpty(childList))
                    thisLevelEntities.addAll(childList);
            }
        });
        return thisLevelEntities;

    }

    private List<CmuopEntity> sortNodes(List<CmuopEntity> nextRoots, List<String> relatedNodes){
        List<CmuopEntity> tempList = new ArrayList<>();
        List<CmuopEntity> thisLevelRelatedNodes = new ArrayList<>();
        nextRoots.forEach(node -> {
            if(relatedNodes.contains(node.getEntityCode())){
                thisLevelRelatedNodes.add(node);
            }else{
                tempList.add(node);
            }
        });
        tempList.sort(Comparator.comparing(CmuopEntity::getOrderNum));
        thisLevelRelatedNodes.sort(Comparator.comparing(CmuopEntity::getOrderNum));
        thisLevelRelatedNodes.addAll(tempList);
        return thisLevelRelatedNodes;
    }

    public List<CmuopEntity> generateEntityList(List<CmuopEntity> allEntities, String userId) {
        List<CmuopEntity> relatedList = userId == null ? Lists.newArrayList() : getRelatedOrderedEntities(allEntities, userId);
        List<CmuopEntity> orderedList = new ArrayList<>();
        orderedList.addAll(relatedList);
        for(CmuopEntity entity: allEntities){
            if(!relatedList.contains(entity)) {
                orderedList.add(entity);
            }
        }
        return orderedList;
    }

    private List<CmuopEntity> getRelatedOrderedEntities(List<CmuopEntity> allEntities, String uid) {
        List<String> userRelatedEntities = getUserEntityList(uid);
        Map<String, CmuopEntity> entityMap = allEntities.stream().collect(Collectors.toMap(CmuopEntity::getEntityCode, value -> value));
        List<CmuopEntity> resultList = Lists.newArrayList();
        userRelatedEntities.forEach(entityCode -> {
            String code = entityCode;
            while (true) {
                CmuopEntity entity = entityMap.get(code);
                if (entity == null)
                    break;
                if(!resultList.contains(entity)) {
                    resultList.add(entity);
                }
                code = entity.getParentCode();
            }
        });
        resultList.sort(new Comparator<CmuopEntity>() {
            @Override
            public int compare(CmuopEntity o1, CmuopEntity o2) {
                return o1.getOrderNum() > o2.getOrderNum() ? 1 : -1;
            }
        });
        return resultList;
    }
}
