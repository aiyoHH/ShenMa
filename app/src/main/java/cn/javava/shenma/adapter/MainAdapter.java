package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cn.javava.shenma.R;
import cn.javava.shenma.adapter.mainHolder.ContentHolder;
import cn.javava.shenma.adapter.mainHolder.VideoHolder;
import cn.javava.shenma.interf.Key;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class MainAdapter extends RecyclerView.Adapter {

    private final int additional=1;

    Context mContext;
    LayoutInflater mInflater;
    List<String> mList;

    public MainAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==Key.Type.video){
            return new VideoHolder(mInflater.inflate(R.layout.item_main_video,parent,false));
        }else{
            return new ContentHolder(mInflater.inflate(R.layout.item_main_content,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return additional+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return Key.Type.video;
        }else{
            return Key.Type.content;
        }
    }



}
