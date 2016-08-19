package com.efeiyi.ec.art.base.service;

import com.efeiyi.ec.art.model.Artwork;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2015/12/10.
 *
 */
public interface ThreadManager {
    void startWork(List<Artwork> artworks) throws Exception;
}
