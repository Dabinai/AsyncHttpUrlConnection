package com.dabin.www.twoweek.tools;

import com.dabin.www.twoweek.bean.NewBase;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Dabin on 2017/11/11.
 */

public interface ApiServer {
    @GET("wap/data/news/txs/page_1.json")
    Observable<List<NewBase>> getBase();
}
