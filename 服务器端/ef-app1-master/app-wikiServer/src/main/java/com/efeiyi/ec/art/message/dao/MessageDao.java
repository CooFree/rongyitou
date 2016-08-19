package com.efeiyi.ec.art.message.dao;


import com.efeiyi.ec.art.organization.model.MyUser;
import com.ming800.core.base.dao.BaseDao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ming
 * Date: 12-10-16
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
public interface MessageDao {


         List getPageList(String hql,Integer index,Integer size);
}
