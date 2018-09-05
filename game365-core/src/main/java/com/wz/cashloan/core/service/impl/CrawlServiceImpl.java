package com.wz.cashloan.core.service.impl;

import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.mapper.CrawlLogMapper;
import com.wz.cashloan.core.mapper.GameClassifyMapper;
import com.wz.cashloan.core.mapper.GameProcessMapper;
import com.wz.cashloan.core.model.CrawlLog;
import com.wz.cashloan.core.model.Game;
import com.wz.cashloan.core.model.GameBet;
import com.wz.cashloan.core.model.GameClassify;
import com.wz.cashloan.core.model.GameProcess;
import com.wz.cashloan.core.service.CrawlService;
import com.wz.cashloan.core.mapper.GameBetMapper;
import com.wz.cashloan.core.mapper.GameMapper;
import com.wz.cashloan.crawl.Crawling;
import com.wz.cashloan.crawl.bean.Guess;
import com.wz.cashloan.crawl.bean.GuessOption;
import com.wz.cashloan.crawl.bean.Match;
import com.wz.cashloan.crawl.bean.MatchType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/22 22:01
 */
@Service(value = "crawlService")
public class CrawlServiceImpl implements CrawlService {
    @Resource
    private GameMapper gameMapper;
    @Resource
    private GameBetMapper gameBetMapper;
    @Resource
    private GameClassifyMapper gameClassifyMapper;
    @Resource
    private CrawlLogMapper crawlLogMapper;
    @Resource
    private GameProcessMapper gameProcessMapper;

    @Override
    public void saveAndUpdateMatch() {
        List<MatchType> matchTypes = Crawling.crawlAll();
        String exception_msg = "";
        try {
            Map<String, Object> queryMap = new HashMap<>();
            if (matchTypes != null && matchTypes.size() > 0) {

                for (int i = 0; i < matchTypes.size(); i++) {
                    MatchType matchType = matchTypes.get(i);
                    String name = matchType.getName();
                    GameClassify gameClassify = gameClassifyMapper.selectByName(name);

                    if (gameClassify == null) {
                        gameClassify = new GameClassify();
                        gameClassify.setName(name);

                        gameClassifyMapper.insert(gameClassify);

                    }

                    Long id = gameClassify.getId();

                    List<Match> matchList = matchType.getMatchList();
                    for (int j = 0; j < matchList.size(); j++) {
                        Match match = matchList.get(j);
                        Game game = gameMapper.selectByGameCode(match.getMatchCode());
                        byte state = 0;
                        if ("coming".equals(match.getStatus())) {
                            state = 0;
                        } else if ("carriedOut".equals(match.getStatus())) {
                            state = 1;
                        } else {
                            state = 2;
                        }
                        if (game == null) {
                            game = new Game();
                            game.setExternalGameCode(match.getMatchCode());
                            game.setName(match.getMatchName());
                            game.setGameClassifyId(id);
                            game.setLeftTeam(match.getSurveyLeftTeamName());
                            game.setLeftTeamImg(match.getSurveyLeftTeamLogo());
                            game.setRightTeam(match.getSurveyRightTeamName());
                            game.setRightTeamImg(match.getSurveyRightTeamLogo());
                            //0未开始，1进行中，2结束

                            game.setState(state);
                            game.setContestDate(match.getMatchTime());
                            game.setContestTime(match.getMatchTime());
                            gameMapper.insert(game);
                        } else {
                            if ("待定".equals(game.getRightTeam())) {
                                game.setRightTeam(match.getSurveyRightTeamName());
                                game.setRightTeamImg(match.getSurveyRightTeamLogo());
                            }
                            if ("待定".equals(game.getLeftTeam())) {
                                game.setLeftTeam(match.getSurveyLeftTeamName());
                                game.setLeftTeamImg(match.getSurveyLeftTeamLogo());
                            }
                            if (state != 0) {
                                game.setLeftScore(match.getSurveyLeftScore());
                                game.setRightScore(match.getSurveyRightScore());
                            }
                            game.setState(state);

                            gameMapper.updateByPrimaryKeySelective(game);
                        }

                        Long gameId = game.getId();

                        //保存赛事进度
                        List<Integer> lefts = match.getSurveyLeftScoreList();
                        List<Integer> rights = match.getSurveyRightScoreList();
                        if (state != 0) {
                            for (int k = 0; k < lefts.size(); k++) {
                                String round = String.valueOf(k + 1);
                                queryMap.put("gameId", gameId);
                                queryMap.put("round", round);
                                GameProcess gameProcess = gameProcessMapper.findByMap(queryMap);
                                if (gameProcess == null) {
                                    gameProcess = new GameProcess();
                                    gameProcess.setGameId(gameId);
                                    gameProcess.setLeftScore(lefts.get(k));
                                    gameProcess.setRightScore(rights.get(k));
                                    gameProcess.setRound(round);

                                    gameProcessMapper.insert(gameProcess);
                                }

                            }
                        }


                        List<Guess> guessList = match.getGuessList();
                        for (int k = 0; k < guessList.size(); k++) {
                            Guess guess = guessList.get(k);
                            String guessName = guess.getName();


                            GameBet gameBet = new GameBet();
                            gameBet.setGameId(gameId);
                            gameBet.setGuessOverTime(guess.getGuessOverTime());
                            Date guessOverTime = guess.getGuessOverTime();
                            List<GuessOption> guessOptionList = guess.getOptions();
                            for (int l = 0; l < guessOptionList.size(); l++) {

                                GuessOption guessOption = guessOptionList.get(l);
                                queryMap.put("gameId", gameId);
                                queryMap.put("name", guessName);
                                queryMap.put("team", guessOption.getName());

                                List<GameBet> gameBetList = gameBetMapper.findSelective(queryMap);
                                if (gameBetList.size() < 1) {
                                    gameBet.setName(guessName);
                                    gameBet.setOdds(BigDecimal.valueOf(guessOption.getOdds()));
                                    gameBet.setTeam(guessOption.getName());
                                    gameBet.setGuessOverTime(guessOverTime);
                                    gameBet.setGameId(gameId);

                                    gameBetMapper.insert(gameBet);
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exception_msg = e.getMessage();
        } finally {

            CrawlLog crawlLog = new CrawlLog();
            crawlLog.setCreateTime(new Date());
            crawlLog.setContext(JsonUtil.toString(matchTypes));
            crawlLog.setExceptionMsg(exception_msg);
            crawlLogMapper.insert(crawlLog);
        }

    }
}
