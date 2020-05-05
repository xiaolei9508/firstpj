package com.cmft.slas.cmuop.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/4/26
 */
@Data
@Accessors(chain = true)
public class ReadStatDTO {
    private Integer readCount;
    private Integer unreadCount;
}
