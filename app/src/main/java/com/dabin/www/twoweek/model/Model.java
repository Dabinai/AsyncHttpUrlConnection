package com.dabin.www.twoweek.model;

import com.dabin.www.twoweek.bean.NewBase;
import com.dabin.www.twoweek.tools.ApiServer;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dabin on 2017/11/11.
 */

public class Model implements IModel{

    List<NewBase.DataBean> data;
    private OnFinish onFinish;

    public interface OnFinish{
        void finish(List<NewBase.DataBean> data);
    }

    public void setOnFinish(OnFinish onFinish){
        this.onFinish = onFinish;
    }




    @Override
    public void getUrl(String url) {
        Retrofit build = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiServer apiServer = build.create(ApiServer.class);
        apiServer.getBase().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NewBase>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<NewBase> newBases) {
                        List<NewBase.DataBean> data = newBases.get(0).getData();
                        onFinish.finish(data);
                    }


                });
    }
}
