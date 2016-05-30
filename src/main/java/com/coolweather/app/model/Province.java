package com.coolweather.app.model;

/**
 * Created by christopher on 2016/5/30.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId() {
        return id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;

    }
}
