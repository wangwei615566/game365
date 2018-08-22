package com.wz.cashloan.crawl.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Match {

    /**
     * 竞猜集合
     */
    private List<Guess> guessList = new LinkedList<Guess>();
    /**
     * 竞猜结束时间
     */
    private Date guessOverTime;
    /**
     * 比赛竞猜状态
     */
    private String guessStatus;
    /**
     * 比赛编号
     */
    private String matchCode;
    /**
     * 比赛名称
     */
    private String matchName;
    /**
     * 比赛时间
     */
    private Date matchTime;
    /**
     * 比赛状态
     */
    private String status;
    /**
     * 左队国家logo
     */
    private String surveyLeftCountryLogo;
    /**
     * 左队胜场
     */
    private int surveyLeftScore = 0;
    /**
     * 左队各场得分
     */
    private List<Integer> surveyLeftScoreList = new LinkedList<Integer>();
    /**
     * 左队logo
     */
    private String surveyLeftTeamLogo;
    /**
     * 左队名称
     */
    private String surveyLeftTeamName;
    /**
     * 右队国家logo
     */
    private String surveyRightCountryLogo;
    /**
     * 右队胜场
     */
    private int surveyRightScore = 0;
    /**
     * +
     * 右队各场得分
     */
    private List<Integer> surveyRightScoreList = new LinkedList<Integer>();
    /**
     * 右队logo
     */
    private String surveyRightTeamLogo;
    /**
     * 右队名称
     */
    private String surveyRightTeamName;
    /**
     * 比赛信息爬取地址
     */
    private String url;

    public List<Guess> getGuessList() {
        return guessList;
    }

    public void setGuessList(List<Guess> guessList) {
        this.guessList = guessList;
    }

    public Date getGuessOverTime() {
        return guessOverTime;
    }

    public void setGuessOverTime(Date guessOverTime) {
        this.guessOverTime = guessOverTime;
    }

    public String getGuessStatus() {
        return guessStatus;
    }

    public void setGuessStatus(String guessStatus) {
        this.guessStatus = guessStatus;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurveyLeftCountryLogo() {
        return surveyLeftCountryLogo;
    }

    public void setSurveyLeftCountryLogo(String surveyLeftCountryLogo) {
        this.surveyLeftCountryLogo = surveyLeftCountryLogo;
    }

    public int getSurveyLeftScore() {
        return surveyLeftScore;
    }

    public void setSurveyLeftScore(int surveyLeftScore) {
        this.surveyLeftScore = surveyLeftScore;
    }

    public List<Integer> getSurveyLeftScoreList() {
        return surveyLeftScoreList;
    }

    public void setSurveyLeftScoreList(List<Integer> surveyLeftScoreList) {
        this.surveyLeftScoreList = surveyLeftScoreList;
    }

    public String getSurveyLeftTeamLogo() {
        return surveyLeftTeamLogo;
    }

    public void setSurveyLeftTeamLogo(String surveyLeftTeamLogo) {
        this.surveyLeftTeamLogo = surveyLeftTeamLogo;
    }

    public String getSurveyLeftTeamName() {
        return surveyLeftTeamName;
    }

    public void setSurveyLeftTeamName(String surveyLeftTeamName) {
        this.surveyLeftTeamName = surveyLeftTeamName;
    }

    public String getSurveyRightCountryLogo() {
        return surveyRightCountryLogo;
    }

    public void setSurveyRightCountryLogo(String surveyRightCountryLogo) {
        this.surveyRightCountryLogo = surveyRightCountryLogo;
    }

    public int getSurveyRightScore() {
        return surveyRightScore;
    }

    public void setSurveyRightScore(int surveyRightScore) {
        this.surveyRightScore = surveyRightScore;
    }

    public List<Integer> getSurveyRightScoreList() {
        return surveyRightScoreList;
    }

    public void setSurveyRightScoreList(List<Integer> surveyRightScoreList) {
        this.surveyRightScoreList = surveyRightScoreList;
    }

    public String getSurveyRightTeamLogo() {
        return surveyRightTeamLogo;
    }

    public void setSurveyRightTeamLogo(String surveyRightTeamLogo) {
        this.surveyRightTeamLogo = surveyRightTeamLogo;
    }

    public String getSurveyRightTeamName() {
        return surveyRightTeamName;
    }

    public void setSurveyRightTeamName(String surveyRightTeamName) {
        this.surveyRightTeamName = surveyRightTeamName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"matchCode\":\"").append(matchCode).append('\"');
        sb.append(",\"matchName\":\"").append(matchName).append('\"');
        if(matchTime!=null){
            sb.append(",\"matchTime\":\"").append(sdf.format(matchTime)).append("\"");
        }
        sb.append(",\"status\":\"").append(status).append("\"");
        sb.append(",\"surveyLeftCountryLogo\":\"").append(surveyLeftCountryLogo).append('\"');
        sb.append(",\"surveyLeftScore\":").append(surveyLeftScore);
        sb.append(",\"surveyLeftScoreList\":").append(surveyLeftScoreList);
        sb.append(",\"surveyLeftTeamLogo\":\"").append(surveyLeftTeamLogo).append('\"');
        sb.append(",\"surveyLeftTeamName\":\"").append(surveyLeftTeamName).append('\"');
        sb.append(",\"surveyRightCountryLogo\":\"").append(surveyRightCountryLogo).append('\"');
        sb.append(",\"surveyRightScore\":").append(surveyRightScore);
        sb.append(",\"surveyRightScoreList\":").append(surveyRightScoreList);
        sb.append(",\"surveyRightTeamLogo\":\"").append(surveyRightTeamLogo).append('\"');
        sb.append(",\"surveyRightTeamName\":\"").append(surveyRightTeamName).append('\"');
        if(guessOverTime!=null){
            sb.append(",\"guessOverTime\":\"").append(sdf.format(guessOverTime)).append('\"');
        }
        sb.append(",\"guessList\":").append(guessList);
        sb.append(",\"guessStatus\":\"").append(guessStatus).append('\"');
        sb.append(",\"url\":\"").append(url).append('\"');
        sb.append('}');
        return sb.toString();
    }

}
