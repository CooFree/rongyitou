package com.efeiyi.ec.art.organization.service.imp;

import com.efeiyi.ec.art.organization.model.SendCode;
import com.efeiyi.ec.art.organization.model.SmsProvider;
import com.efeiyi.ec.art.organization.model.YunPianSmsProvider;
import com.efeiyi.ec.art.organization.service.SmsCheckManager;

public class SmsCheckManagerImpl implements SmsCheckManager {



    @Override
    public String createCheckCode() {

        return String.valueOf(Math.random()).substring(2, 8);
    }



    @Override
    public String send(String phone, String content, String tpl_id, Integer company) {
        SmsProvider smsProvider = null;

            smsProvider = new YunPianSmsProvider();


        if(tpl_id.equals("3")){
            SendCode sendCode = smsProvider.post(phone, content, tpl_id);

            return sendCode.getMsg();
        }else{
                SendCode sendCode = smsProvider.post(phone, content, tpl_id);


                System.out.println(sendCode.getMsg());
                return sendCode.getMsg();
            }

    }

    @Override
    public Boolean checkPhoneRegistered(String phone) {
        /*String hql = "from BigUser b where b.username=:phone";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("phone", phone);
        List<BigUser> bigUserList = basicDao.getObjectList(hql, queryParamMap);
        if (bigUserList.size() < 1) {
            return true;
        } else {
            return false;
        }*/
    	return true;
    }
}