package com.wz.cashloan.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class Goods {
    private Long id;

    private String name;

    private BigDecimal price;

    private String spec;

    private String picture;

    private Byte state;

    private String desc;

    private Date updateTime;

    private Date createTime;

    public Goods() {
    }

    public Goods(String name, BigDecimal price, String spec, String picture, Byte state, String desc, Date updateTime, Date createTime) {
        this.name = name;
        this.price = price;
        this.spec = spec;
        this.picture = picture;
        this.state = state;
        this.desc = desc;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}