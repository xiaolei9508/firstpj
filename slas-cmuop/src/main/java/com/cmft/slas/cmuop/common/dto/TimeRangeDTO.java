package com.cmft.slas.cmuop.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TimeRangeDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TimeRange timeRange;
}
