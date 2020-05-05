package com.cmft.slas.cmuop.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author liurp001
 * @Since 2020/1/15
 */
@Data
@Accessors(chain = true)
public class EntitySentimentCountDTO {
    Integer sentiment;

    Integer count;
}
