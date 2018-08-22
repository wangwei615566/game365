package com.wz.cashloan.crawl.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Guess {

    /**
     * 竞猜结束时间
     */
    private Date guessOverTime;
    /**
     * 竞猜名称
     */
    private String name;
    /**
     * 竞猜选项集合
     */
    private List<GuessOption> options = new LinkedList<GuessOption>();

    public Date getGuessOverTime() {
        return guessOverTime;
    }

    public void setGuessOverTime(Date guessOverTime) {
        this.guessOverTime = guessOverTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GuessOption> getOptions() {
        return options;
    }

    public void setOptions(List<GuessOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"guessOverTime\":\"").append(sdf.format(guessOverTime)).append("\"");
        sb.append(",\"name\":\"").append(name).append('\"');
        sb.append(",\"options\":").append(options);
        sb.append('}');
        return sb.toString();
    }
}
