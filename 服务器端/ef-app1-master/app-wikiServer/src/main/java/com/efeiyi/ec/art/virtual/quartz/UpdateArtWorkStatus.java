package com.efeiyi.ec.art.virtual.quartz;

import com.efeiyi.ec.art.base.service.ThreadManager;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.organization.OrganizationConst;
import com.ming800.core.base.dao.hibernate.XdoDaoSupport;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/8.
 *
 */
@Component("updateArtWorkStatus")
@Transactional("transactionManager")
public class UpdateArtWorkStatus {
    @Autowired
    private XdoDaoSupport xdoDao;
    @Autowired
    ThreadManager threadManager;

    @Autowired
    ResultMapHandler resultMapHandler;
     @Autowired
     private  HttpServletRequest request;
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
/*    public UpdateArtWorkStatus() {
        super();
    }
    public UpdateArtWorkStatus(ExecutorService pool,HttpServletRequest request) {
        this.pool = pool;
        this.request = request;
    }*/



    public void execute() {//每5分钟执行一次
        try {
            System.out.println("定时任务启动了...");
            //查找状态为31 即拍卖中的项目
            //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //XQuery query = new XQuery("listArtwork2_default",request);
           /* <condition name="type" defaultValue="3" operation="eq"/>
            <condition name="step" defaultValue="31" operation="eq"/>
            <condition name="status" defaultValue="0" operation="ne"/>*/
          /*  LinkedHashMap<String,Object> map = new  LinkedHashMap<String,Object>();
            map.put("type","3");
            map.put("step","31");*/
            String hql = "from Artwork where type='3' and step='31' and status <>'0' ";
            List<Artwork> artworks = sessionFactory.getCurrentSession().createQuery(hql).list();

            if (artworks != null && !artworks.isEmpty()) {
                threadManager.startWork(artworks);
            }
            System.out.println("定时任务结束了...");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            /*if (sessionFactory.openSession()!=null){
                sessionFactory.openSession().close();
            }*/
        }
    }
}
