package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.utils.ConstantPayUtil;
import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map createNative(String orderId) {
        try {
            // 根据订单id获取订单信息
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("order_no", orderId);
            Order order = orderService.getOne(orderQueryWrapper);

            HashMap<String, String> map = new HashMap<>();
            //1、设置支付参数
            map.put("appid", ConstantPayUtil.APP_ID);
            map.put("mch_id", ConstantPayUtil.PARTNER);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());
            map.put("out_trade_no", orderId);
            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            map.put("spbill_create_ip", ConstantPayUtil.SPBILL_CREATE_IP);
            map.put("notify_url", ConstantPayUtil.NOTIFY_URL);
            map.put("trade_type", "NATIVE");

            // httpclient根据url访问第三方接口，并返回数据
            HttpClient httpClient = new HttpClient(ConstantPayUtil.SEND_ADDRESS);

            // client设置参数
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, ConstantPayUtil.PARTNER_KEY));
            httpClient.setHttps(true);
            httpClient.post();

            // 返回第三方数据
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            HashMap m = new HashMap<>();
            m.put("out_trade_no", orderId);
            m.put("course_id", order.getCourseId());
            m.put("total_fee", order.getTotalFee());
            m.put("result_code", resultMap.get("result_code"));
            m.put("code_url", resultMap.get("code_url"));

            // 返回结果
            return m;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new HashMap();
        }
    }

    @Override
    public Map queryPayStatus(String orderId) {

        try {
            //1、封装参数
            Map map = new HashMap<>();
            map.put("appid", ConstantPayUtil.APP_ID);
            map.put("mch_id", ConstantPayUtil.PARTNER);
            map.put("out_trade_no", orderId);
            map.put("nonce_str", WXPayUtil.generateNonceStr());

            // 发送请求
            HttpClient httpClient = new HttpClient(ConstantPayUtil.SEND_ADDRESS_PAY);
            // 设置参数
            // 根据商户key生成xml密钥
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, ConstantPayUtil.PARTNER_KEY));
            httpClient.setHttps(true);
            httpClient.post();

            // 返回第三方数据
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new HashMap();
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        String orderId = map.get("out_trade_no");
        if (StringUtils.isEmpty(orderId)) {
            throw new GuliException(20001, "订单不存在!");
        }
        Order order = orderService.getOrderByOrderNo(orderId);

        // 判断订单的状态
        if (order.getStatus().intValue() != 1) {
            order.setStatus(1);
            orderService.updateById(order);
        }

        // 保存支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1); //支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));// 其他信息，使用json字符串的形式存储在数据库
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
