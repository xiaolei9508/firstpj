package com.cmft.slas.cmuop.processor.processorImpl;

import com.cmft.slas.cmuop.entity.UserView;
import com.cmft.slas.cmuop.mapper.UserViewMapper;
import com.cmft.slas.cmuop.vo.AppArticlePreview;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IfReadProcessor {
    @Autowired
    private UserViewMapper userViewMapper;

    public void processIfRead(List<AppArticlePreview> appArticleList, String uid){
        if(CollectionUtils.isEmpty(appArticleList) || StringUtils.isBlank(uid))
            return ;
        appArticleList.forEach(appArticlePreview -> appArticlePreview.setIfRead(false));
        Example example = new Example(UserView.class);
        example.createCriteria()
                .andIn("articleId", appArticleList.stream()
                                                            .map(AppArticlePreview::getArticleId)
                                                            .collect(Collectors.toList()))
                .andEqualTo("uid", uid)
                .andEqualTo("isDelete", (byte)0);
        List<UserView> viewRecords = userViewMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(viewRecords))
            return;
        List<String> viewedIds = viewRecords.stream().map(UserView::getArticleId).collect(Collectors.toList());
        appArticleList.forEach(article -> {
            if(viewedIds.contains(article.getArticleId()))
                article.setIfRead(true);
        });
    }
}
