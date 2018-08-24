package com.wz.cashloan.core.model;

/**
 * Created by Hbx on 2018/8/24.
 */
public class GoodsOrderModel {
    //待付款
    public static byte STATE_FOR_PAY = 1;
    //待发货
    public static byte STATE_FOR_SEND = 2;
    //待收货
    public static byte STATE_FOR_RECEIVE = 3;
    //已完成
    public static byte STATE_FINISH = 4;
    //支付失败
    public static byte STATE_PAY_FAIL = 0;
}
