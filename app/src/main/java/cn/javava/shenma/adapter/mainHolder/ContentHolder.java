package cn.javava.shenma.adapter.mainHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class ContentHolder extends RecyclerView.ViewHolder {

    public ContentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setData(Object obj){

    }
}
