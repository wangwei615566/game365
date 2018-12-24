package com.wz.cashloan.crawl;

import com.wz.cashloan.crawl.bean.Guess;
import com.wz.cashloan.crawl.bean.GuessOption;
import com.wz.cashloan.crawl.bean.Match;
import com.wz.cashloan.crawl.bean.MatchType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取
 *
 */
public class Crawling {
    private static String baseUrl = "https://www.xxdianjing.com/";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 爬取所有
     *
     * @return
     */
    public static List<MatchType> crawlAll() {
        Document doc = null;
        try {
            doc = Jsoup.connect(baseUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Elements screemWrap = doc.getElementsByClass("screenWrap");
        if (screemWrap != null && screemWrap.size() > 0) {
            Elements game_types = screemWrap.get(0).getElementsByTag("li");
            List<MatchType> list = new ArrayList<MatchType>();
            for (Element game_type : game_types) {
                String url = game_type.child(0).attr("href");
                String type_name = game_type.text();
                MatchType matchType = new MatchType(type_name, baseUrl + url);
                list.add(matchType);
                Document match_type_doc = null;
                try {
                    match_type_doc = Jsoup.connect(baseUrl + url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                for (Element game_ope : match_type_doc.getElementsByClass("game-ope")) {
                    Elements lis = game_ope.getElementsByTag("li");
                    if (lis != null && lis.size() > 1) {
                        Element li = lis.get(1);
                        Element a = li.child(0);
                        if (a != null) {
                            String match_url = a.attr("href");
                            if (match_url != null && !match_url.isEmpty()) {
                                String matchCode = match_url.replace("match-", "").replace(".html", "").replace("/", "");
                                Match match = crawlMatchByCode(matchCode);
                                matchType.getMatchList().add(match);
                            }
                        }
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static List<Match> crawlAllMatchGuessStatus() {
        Document doc = null;
        List<Match> list = new LinkedList<Match>();
        try {
            doc = Jsoup.connect(baseUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }
        for (Element game_ope : doc.getElementsByClass("game-ope")) {
            Elements lis = game_ope.getElementsByTag("li");
            if (lis != null && lis.size() > 1) {
                Element li = lis.get(1);
                Element a = li.child(0);
                if (a != null) {
                    String match_url = a.attr("href");
                    if (match_url != null && !match_url.isEmpty()) {
                        String matchCode = match_url.replace("match-", "").replace(".html", "").replace("/", "");
                        Match match = new Match();
                        match.setMatchCode(matchCode);
                        match.setGuessStatus(a.className());
                        list.add(match);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据比赛编号爬取指定比赛信息
     *
     * @param matchCode 比赛编号
     * @return
     */
    public static Match crawlMatchByCode(String matchCode) {
        Match match = new Match();
        match.setMatchCode(matchCode);
        String match_url = "/match-" + matchCode + ".html";
        match.setUrl(baseUrl + match_url);
        Document match_doc = null;
        try {
            match_doc = Jsoup.connect(baseUrl + match_url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //获取比赛双方队伍基本信息
        Elements survey_left = match_doc.getElementsByClass("survey left");
        if (survey_left != null && survey_left.size() > 0) {//获取左队信息
            String survey_left_team_logo = survey_left.get(0).child(0).child(0).attr("src");
            String survey_left_country_logo = survey_left.get(0).child(1).child(0).child(0).attr("src");
            String survey_left_team_name = survey_left.get(0).child(1).child(1).text();
            match.setSurveyLeftTeamLogo(baseUrl + survey_left_team_logo);
            match.setSurveyLeftCountryLogo(baseUrl + survey_left_country_logo);
            match.setSurveyLeftTeamName(survey_left_team_name.substring(1));
        }
        Elements survey_right = match_doc.getElementsByClass("survey right");
        if (survey_right != null && survey_right.size() > 0) {//获取右队信息
            String survey_right_team_logo = survey_right.get(0).child(0).child(0).attr("src");
            String survey_right_country_logo = survey_right.get(0).child(1).child(1).child(0).attr("src");
            String survey_right_team_name = survey_right.get(0).child(1).child(0).text();
            match.setSurveyRightTeamLogo(baseUrl + survey_right_team_logo);
            match.setSurveyRightCountryLogo(baseUrl + survey_right_country_logo);
            match.setSurveyRightTeamName(survey_right_team_name.substring(0, survey_right_team_name.length() - 1));
        }
        //获取比赛基础信息
        Elements gameScore = match_doc.getElementsByClass("gameScore");
        if (gameScore != null && gameScore.size() > 0) {
            String str = gameScore.get(0).child(0).text();
            if (str != null && !str.isEmpty() && str.length() > 18) {//获取比赛名称
                String time = str.substring(0, 15);
                try {
                    match.setMatchTime(sdf.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String matchName = str.substring(17);
                match.setMatchName(matchName);
            }
            str = gameScore.get(0).child(1).text();
            if (str != null && !str.isEmpty() && str.trim().length() > 2 && str.indexOf('-') > 0) {//获取比赛当前胜负场
                String[] scores = str.replaceAll(" ", "").split("-");
                match.setSurveyLeftScore(Integer.parseInt(scores[0]));
                match.setSurveyRightScore(Integer.parseInt(scores[1]));
            }
            Elements gameState = gameScore.get(0).getElementsByClass("gameState");
            if (gameState != null && gameState.size() > 0) {//获取比赛状态
                for (String className : gameState.get(0).classNames()) {
                    if (!className.equals("gameState")) {
                        match.setStatus(className);
                    }
                }
            }
            Elements tbody = gameScore.get(0).getElementsByTag("tbody");
            if (tbody != null && tbody.size() > 0) {//获取比赛各场得分
                Elements trs = tbody.get(0).children();
                for (Element tr : trs) {
                    Elements tds = tr.children();
                    for (int i = 1; i < tds.size(); i++) {
                        if (tds.get(0).text().equals(match.getSurveyLeftTeamName()) && tds.get(i).text().trim().length() > 0) {
                            if (!tds.get(i).text().equals("-")) {
                                match.getSurveyLeftScoreList().add(Integer.parseInt(tds.get(i).text()));
                            }
                        } else if (tds.get(0).text().equals(match.getSurveyRightTeamName()) && tds.get(i).text().trim().length() > 0) {
                            if (!tds.get(i).text().equals("-")) {
                                match.getSurveyRightScoreList().add(Integer.parseInt(tds.get(i).text()));
                            }
                        }
                    }
                }
            }
        }
        //获取比赛竞猜状态
        String html = match_doc.html();
        Document guesslist = null;
        String game_id = "";
        Pattern pattern = Pattern.compile("var game_id = \"(.*?)\"");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            game_id = matcher.group(1);
        }
        try {
            guesslist = Jsoup.connect(baseUrl + "match/getguesslist?id="+game_id).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Elements matchRight = guesslist.getElementsByClass("system-message");
        if (matchRight != null && matchRight.size() > 0) {
            Element div = matchRight.get(0).child(2);
            if (div.className().equals("field-guess")) {
                match.setGuessStatus("field-guess");
            }
            if (div.className().equals("over-guess")) {
                match.setGuessStatus("over-guess");
            }
            if (div.className().equals("open-guess")) {
                match.setGuessStatus("open-guess");
            }
        }
        //获取比赛竞猜信息
        if (match.getGuessStatus()!=null&&match.getGuessStatus().equals("open-guess")) ;
        Elements open_guess = guesslist.getElementsByClass("open-guess");
        if (open_guess != null && open_guess.size() > 0) {
            Element p = open_guess.get(0).child(0);
            if (p != null && p.children().size() > 1 && p.child(1) != null && !p.child(1).text().isEmpty()) {//获取比赛竞猜结束时间
                String str = p.child(1).text();
                str = str.replaceAll("（", "").replaceAll(" 截止）", "");
                Date guessOverTime = null;
                try {
                    guessOverTime = sdf.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                match.setGuessOverTime(guessOverTime);
            }
            if (open_guess.get(0).children().size() > 1) {//获取比赛竞猜玩法
                for (int i = 1; i < open_guess.get(0).children().size(); i++) {
                    Element dl = open_guess.get(0).child(i);
                    Elements dt = dl.getElementsByTag("dt");
                    if (dt != null && dt.size() > 0) {
                        Guess guess = new Guess();
                        match.getGuessList().add(guess);
                        String guessName = dt.get(0).text();
                        guess.setGuessOverTime(match.getGuessOverTime());
                        guess.setName(guessName);
                        Elements dd = dl.getElementsByTag("dd");
                        if (dd != null && dd.size() > 0 && dd.get(0).children().size() > 0) {
                            for (Element child : dd.get(0).children()) {
                                GuessOption guessOption = new GuessOption();
                                guess.getOptions().add(guessOption);
                                String optionName = child.attr("data-showname");
                                if (optionName != null && !optionName.isEmpty()) {
                                    guessOption.setName(optionName);
                                }
                                String odds = child.attr("data-odds");
                                if (odds != null && !odds.isEmpty()) {
                                    try {
                                        guessOption.setOdds(Double.valueOf(odds));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return match;
    }

    public static void main(String[] args) throws IOException {
        Date start = new Date();
        List<MatchType> list1 = crawlAll();
        System.out.println(list1);
//        List<Match> list = crawlAllMatchGuessStatus();
//        System.out.println(list);
        System.out.println(new Date().getTime() - start.getTime());
    }
}
