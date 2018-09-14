package com.wz.manage.controller;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.context.Global;
import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.model.GameBet;
import com.wz.cashloan.core.model.Goods;
import com.wz.cashloan.core.service.GameService;
import com.wz.cashloan.core.service.GoodService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@Scope("prototype")
public class ManageGameController extends ManageBaseController {

    @Resource
    private GameService gameService;

    /**
     * 赛事列表
     * @param searchParams 查询参数 json字符串
     * @param current 当前页
     * @param pageSize 每页页数
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/game/search/list.htm")
    public void searchList(@RequestParam(value="searchParams",required=false) String searchParams,
                           @RequestParam(value = "current") int current,
                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<Map<String,Object>> page = gameService.pageList(params, current, pageSize);
        Map<String,Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 编辑赛事
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/game/search/update.htm")
    public void searchList(@RequestParam(value="id",required = true) Long id,
                           @RequestParam(value="odds") BigDecimal odds) throws Exception {
        Map<String,Object> result = new HashMap<>();
        GameBet gameBet = new GameBet();
        gameBet.setId(id);
        gameBet.setOdds(odds);
        int i = gameService.updateByPrimaryKeySelective(gameBet);
        if(i == 1){
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "成功");
        } else {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "失败");
        }
        ServletUtils.writeToResponse(response, result);
    }


}
