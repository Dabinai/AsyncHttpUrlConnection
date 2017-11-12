package com.dabin.www.twoweek.presenter;

import com.dabin.www.twoweek.bean.NewBase;
import com.dabin.www.twoweek.model.Model;
import com.dabin.www.twoweek.view.IView;

import java.util.List;

/**
 * Created by Dabin on 2017/11/11.
 */

public class Presenter implements Model.OnFinish{

    private final IView iView;
    private final Model model;

    public Presenter(IView iView) {
        this.iView = iView;
        model = new Model();
        model.setOnFinish(this);
    }

    public void setUrl(String url){
        model.getUrl(url);
    }

    @Override
    public void finish(List<NewBase.DataBean> data) {
        iView.getData(data);
    }
}
