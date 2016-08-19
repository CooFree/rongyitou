package com.efeiyi.ec.art.message.dao.hibernate;


import com.efeiyi.ec.art.message.dao.MessageDao;
import com.efeiyi.ec.art.organization.dao.UserDao;
import com.efeiyi.ec.art.organization.model.MyUser;
import com.ming800.core.base.dao.hibernate.BaseDaoSupport;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageDaoHibernate implements MessageDao{

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public Session getSession() {
//        sessionFactory
        return sessionFactory.getCurrentSession();
    }


    @Override
    public List getPageList(String hql, Integer index, Integer size) {
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setMaxResults(size);
        query.setFirstResult(index);
        return query.list();
    }
}
