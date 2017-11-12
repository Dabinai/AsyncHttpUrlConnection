package com.dabin.www.twoweek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dabin.www.twoweek.R;
import com.dabin.www.twoweek.bean.NewBase;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Created by Dabin on 2017/11/11.
 */

public class HomeAdapter extends XRecyclerView.Adapter{

    private Context context;
    private List<NewBase.DataBean> data;

    public HomeAdapter(Context context, List<NewBase.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void dianji(View view,int position);

    }
    public void setOnItemClickListener( OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        DraweeController placeHolderDraweeController = Fresco.newDraweeControllerBuilder()
                .setUri(data.get(position).getImg()) //为图片设置url
                .setTapToRetryEnabled(true)   //设置在加载失败后,能否重试
                .setOldController(myViewHolder.simpleDraweeView.getController())
                .build();
        myViewHolder.simpleDraweeView.setController(placeHolderDraweeController);
        myViewHolder.textView.setText(data.get(position).getTitle());
        if(onItemClickListener!= null){
           myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onItemClickListener.dianji(view,position);
               }
           });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView simpleDraweeView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.placeHolderImageDraweeView);
            textView = itemView.findViewById(R.id.texts);
        }
    }
}
