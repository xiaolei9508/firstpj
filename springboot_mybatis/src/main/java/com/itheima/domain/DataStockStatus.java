package com.itheima.domain;

public class DataStockStatus {
    private Double RT_PCT_CJH;
    private Double RT_LATEST;

    @Override
    public String toString() {
        return "DataStockStatus{" + "RT_PCT_CJH=" + RT_PCT_CJH + ", RT_LATEST=" + RT_LATEST + '}';
    }

    public Double getRT_PCT_CJH() {
        return RT_PCT_CJH;
    }

    public void setRT_PCT_CJH(Double RT_PCT_CJH) {
        this.RT_PCT_CJH = RT_PCT_CJH;
    }

    public Double getRT_LATEST() {
        return RT_LATEST;
    }

    public void setRT_LATEST(Double RT_LATEST) {
        this.RT_LATEST = RT_LATEST;
    }
}
