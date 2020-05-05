 package com.cmft.slas.cmuop.common.dto;

import java.util.Collections;
import java.util.List;

import com.cmft.slas.common.pojo.PageInfo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CmuopPageInfo<T> extends PageInfo<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String lastPosition;

    public CmuopPageInfo() {}

    public CmuopPageInfo(int pageNum, int pageSize, long total, List<T> list) {
        super(pageNum, pageSize, total, list);
    }

    public CmuopPageInfo(List<T> list) {
        this(0, list.size(), list.size(), list);
    }

    public static <T> CmuopPageInfo<T> emptyPage() {
        return new CmuopPageInfo<>(0, 0, 0, Collections.emptyList());
    }
}
