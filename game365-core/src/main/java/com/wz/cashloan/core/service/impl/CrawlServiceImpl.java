package com.wz.cashloan.core.service.impl;

import com.wz.cashloan.core.mapper.GameClassifyMapper;
import com.wz.cashloan.core.model.Game;
import com.wz.cashloan.core.model.GameClassify;
import com.wz.cashloan.core.service.CrawlService;
import com.wz.cashloan.core.mapper.GameBetMapper;
import com.wz.cashloan.core.mapper.GameMapper;
import com.wz.cashloan.crawl.Crawling;
import com.wz.cashloan.crawl.bean.Match;
import com.wz.cashloan.crawl.bean.MatchType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/22 22:01
 */
@Service
public class CrawlServiceImpl implements CrawlService {
    @Resource
    private GameMapper gameMapper;
    @Resource
    private GameBetMapper gameBetMapper;
    @Resource
    private GameClassifyMapper gameClassifyMapper;

    @Override
    public void saveAndUpdateMatch() {
        List<MatchType> matchTypes = Crawling.crawlAll();

        if (matchTypes != null && matchTypes.size() > 0) {

            for (int i = 0; i < matchTypes.size(); i++) {
                MatchType matchType = matchTypes.get(i);
                String name = matchType.getName();
                GameClassify gameClassify = gameClassifyMapper.selectByName(name);

                if (gameClassify == null) {
                    gameClassify.setName(name);

                    gameClassifyMapper.insert(gameClassify);

                }

                Long id = gameClassify.getId();

                List<Match> matchList = matchType.getMatchList();
                for (int j = 0; j < matchList.size(); j++) {
                    Match match = matchList.get(j);
                    Game game = gameMapper.selectByGameCode(match.getMatchCode());
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
                        byte state = 0;
                        if ("coming".equals(match.getStatus())) {

                        }
                        game.setState(state);
//                    game.setContestDate();
                        gameMapper.insert(game);
                    }
                }

            }
        }

    }
}
