package com.atguigu.orderservice.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPayUtil implements InitializingBean {

    @Value("${wx.pay.appId}")
    private String appId;

    @Value("${wx.pay.notifyurl}")
    private String notifyUrl;

    @Value("${wx.pay.partnerkey}")
    private String partnerKey;  // 商户key

    @Value("${wx.pay.partner}")
    private String partner;     // 商户号

    @Value("${wx.pay.spbillCreateIp}")
    private String spbill_create_ip;

    @Value("${wx.pay.sendAddress}")
    private String sendAddress;

    @Value("${wx.pay.sendAddressPay}")
    private String sendAddressPay;

    public static String APP_ID;
    public static String NOTIFY_URL;
    public static String PARTNER_KEY;
    public static String PARTNER;
    public static String SPBILL_CREATE_IP;
    public static String SEND_ADDRESS;
    public static String SEND_ADDRESS_PAY;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.APP_ID = appId;
        this.NOTIFY_URL = notifyUrl;
        this.PARTNER_KEY = partnerKey;
        this.PARTNER = partner;
        this.SPBILL_CREATE_IP = spbill_create_ip;
        this.SEND_ADDRESS = sendAddress;
        this.SEND_ADDRESS_PAY = sendAddressPay;
    }
}
