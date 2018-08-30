package com.pos.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.common.util.OrderNoUtil;
import com.wz.cashloan.core.mapper.GoodsMapper;
import com.wz.cashloan.core.mapper.GoodsOrderMapper;
import com.wz.cashloan.core.model.Goods;
import com.wz.cashloan.core.model.GoodsOrder;
import com.wz.cashloan.core.model.GoodsOrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Hbx on 2018/8/24.
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;

    /**
     * 按条件分頁查询
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    public Page<Goods> listGoods(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        List<Goods> goods = goodsMapper.listSelective(params);
        return (Page<Goods>) goods;
    }

    /**
     * 商品下单
     *
     * @param userId
     * @param goodsId
     * @param num
     * @param amount
     * @return
     */
    public int saveGoodsOrder(Long userId, Long goodsId, int num, double amount) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            return -1;
        }
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setAmount(BigDecimal.valueOf(amount));
        goodsOrder.setNum(num);
        goodsOrder.setOrderNo(OrderNoUtil.getSerialNumber());
        goodsOrder.setUserId(userId);
        goodsOrder.setState(GoodsOrderModel.STATE_FOR_PAY);
        goodsOrder.setGoodsId(goodsId);

        return goodsOrderMapper.insert(goodsOrder);
    }
}
