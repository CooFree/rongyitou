package com.efeiyi.ec.art.artwork.service;


/**
 * Created by Administrator on 2015/8/3.
 */
public interface PaymentManager {


    void payCallback(String purchaseOrderPaymentId, String transactionNumber);

    boolean verifySign(String sign, String timestamp);

}
