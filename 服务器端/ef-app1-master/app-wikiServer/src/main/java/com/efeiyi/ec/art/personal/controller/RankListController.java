package com.efeiyi.ec.art.personal.controller;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.model.ArtistBiddingTopListVO;
import com.efeiyi.ec.art.model.ArtistTopListVO;
import com.efeiyi.ec.art.model.InvestorTopListVO;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.base.dao.hibernate.XdoDaoSupport;
import com.ming800.core.base.service.BaseManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Administrator on 2016/4/6.
 */
@Controller
public class RankListController extends BaseController {
    private static Logger logger = Logger.getLogger(RankListController.class);

    @Autowired
    BaseManager baseManager;
    @Autowired
    ResultMapHandler resultMapHandler;
    @Autowired
    private XdoDaoSupport xdoDao;
    @RequestMapping(value = "/app/getInvestorTopList.do", method = RequestMethod.POST)//获取投资者排行榜
    @ResponseBody
    public Map getInvestorTopList(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        logBean.setApiName("getInvestorTopList");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("pageNum")) ||
                    "".equals(jsonObj.getString("timestamp")) || "".equals(jsonObj.getString("pageSize")) ) {
                return  resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("pageSize",jsonObj.getString("pageSize"));
            treeMap.put("pageNum",jsonObj.getString("pageNum"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                return  resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }
            Session session = xdoDao.getSession();
            int beginNum = (jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize"));
            String sqlString = AppConfig.GET_INVESTOR_TOP_LIST+beginNum+","+jsonObj.getInteger("pageSize");
            List<InvestorTopListVO> list = session.createSQLQuery(sqlString).setResultTransformer(Transformers.aliasToBean(com.efeiyi.ec.art.model.InvestorTopListVO.class)).list();
            resultMap = resultMapHandler.handlerResult("0","成功!",logBean);
            resultMap.put("InvestorTopList",list);
        } catch(Exception e){
            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }
        return  resultMap;
    }



    @RequestMapping(value = "/app/getArtistTopList.do", method = RequestMethod.POST)//获取艺术家排行榜
    @ResponseBody
    public Map getArtistTopList(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        logBean.setApiName("getArtistTopList");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("pageNum")) ||
                    "".equals(jsonObj.getString("timestamp")) || "".equals(jsonObj.getString("pageSize")) ) {
                return  resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("pageSize",jsonObj.getString("pageSize"));
            treeMap.put("pageNum",jsonObj.getString("pageNum"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                return  resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }
            Session session = xdoDao.getSession();
            int beginNum = (jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize"));
            String sqlString = AppConfig.GET_ARTIST_TOP_LIST+beginNum+","+jsonObj.getInteger("pageSize");
            List<ArtistTopListVO> list = session.createSQLQuery(sqlString).setResultTransformer(Transformers.aliasToBean(com.efeiyi.ec.art.model.ArtistTopListVO.class)).list();
            resultMap = resultMapHandler.handlerResult("0","成功!",logBean);
            resultMap.put("ArtistTopList",list);
        } catch(Exception e){
            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }
        return  resultMap;
    }




    @RequestMapping(value = "/app/2/getArtistTopList.do", method = RequestMethod.POST)//获取艺术家排行榜
    @ResponseBody
    public Map getArtistTopList2(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        logBean.setApiName("getArtistTopList2");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("pageNum")) ||
                    "".equals(jsonObj.getString("timestamp")) || "".equals(jsonObj.getString("pageSize")) ) {
                return  resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("pageSize",jsonObj.getString("pageSize"));
            treeMap.put("pageNum",jsonObj.getString("pageNum"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                return  resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }
            Session session = xdoDao.getSession();
            int beginNum = (jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize"));
            String sqlString = AppConfig.GET_ARTIST_TOP_LIST2+beginNum+","+jsonObj.getInteger("pageSize");
            List<ArtistBiddingTopListVO> list = session.createSQLQuery(sqlString).setResultTransformer(Transformers.aliasToBean(com.efeiyi.ec.art.model.ArtistBiddingTopListVO.class)).list();
            resultMap = resultMapHandler.handlerResult("0","成功!",logBean);
            resultMap.put("ArtistTopList",list);
        } catch(Exception e){
            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }
        return  resultMap;
    }








    public  static  void  main(String [] arg) throws Exception {


        String appKey = "BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
        long timestamp = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("pageSize","5");
        map.put("pageNum","1");
        map.put("timestamp", timestamp);

        String signmsg = DigitalSignatureUtil.encrypt(map);


        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.69:8001/app/2/getArtistTopList.do";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");

        String json = "{\"pageSize\":\"5\",\"pageNum\":\"1\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";

        JSONObject jsonObj = (JSONObject)JSONObject.parse(json);
        String jsonString = jsonObj.toJSONString();


        StringEntity stringEntity = new StringEntity(jsonString,"utf-8");
        stringEntity.setContentType("text/json");
        stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        System.out.println("url:  " + url);
        try {
            byte[] b = new byte[(int) stringEntity.getContentLength()];
            System.out.println(stringEntity);
            stringEntity.getContent().read(b);
            System.out.println("报文:" + new String(b, "utf-8"));
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent(), "UTF-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
         System.out.println(stringBuilder.toString());
        }catch (Exception e){

        }
    }
}
