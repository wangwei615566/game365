package com.wz.cashloan.crawl.bean;

import java.util.LinkedList;
import java.util.List;

public class MatchType {
    /**
     * 比赛游戏种类名称
     */
    private String name;
    /**
     * 比赛种类信息爬取地址
     */
    private String url;
    /**
     * 所有比赛信息集合
     */
    private List<Match> matchList = new LinkedList<Match>();

    public MatchType(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"").append(name).append('\"');
        sb.append(",\"url\":\"").append(url).append('\"');
        sb.append(",\"matchList\":").append(matchList);
        sb.append('}');
        return sb.toString();
    }
}
