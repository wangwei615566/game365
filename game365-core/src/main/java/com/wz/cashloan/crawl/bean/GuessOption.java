package com.wz.cashloan.crawl.bean;

public class GuessOption {

    private String name;
    private Double odds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"").append(name).append('\"');
        sb.append(",\"odds\":").append(odds);
        sb.append('}');
        return sb.toString();
    }
}
