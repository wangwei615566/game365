package com.pos.api.controller;

import com.github.pagehelper.Page;
import com.pos.api.service.ApiGameService;
import com.pos.api.service.ApiUserService;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import com.wz.cashloan.core.model.GameModel;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hbx on 2018/8/24.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class GameController extends BaseController {
    private static final Logger logger = Logger.getLogger(GameController.class);
    @Resource
    private ApiGameService apiGameService;

    /**
     * 赛事列表
     *
     * @param searchParams
     * @param current
     * @param pageSize
     */
    @RequestMapping("/game/list.htm")
    public void list(@RequestParam(value = "searchParams", required = false) String searchParams,
                     @RequestParam(value = "current") int current,
                     @RequestParam(value = "pageSize") int pageSize)

    {
        logger.info("请求参数 searchParams：" + searchParams);
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<GameModel> page = apiGameService.listGame(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 赛事竞猜项
     *
     * @param gameId
     * @param userId
     */
    @RequestMapping("/game/listBet.htm")
    public void listBet(@RequestParam(value = "gameId") Long gameId,
                        @RequestParam(value = "userId") Long userId)
//                        @RequestParam(value = "current") int current,
//                        @RequestParam(value = "pageSize") int pageSize)

    {
//        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Map data = apiGameService.listBet(gameId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, data);
//        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 赛事类别
     */
    @RequestMapping("/game/gameClassify.htm")
    public void gameClassify()
//                        @RequestParam(value = "current") int current,
//                        @RequestParam(value = "pageSize") int pageSize)

    {
//        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Map data = apiGameService.gameClassifies();
        ServletUtils.writeToResponse(response, data);
    }


}
