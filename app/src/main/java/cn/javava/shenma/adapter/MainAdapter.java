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
        if(holder instanceof VideoHolder){
            ((VideoHolder) holder).setData(mContext);
        }

    }

    @Override
    public int getItemCount() {
        return additional+4;
    }

    @Override
    public int getItemViewType(int position) {

//        switch (position) {
//            case 0:
//                return Key.Type.banner;
//            case 1:
//                return Key.Type.video;
//                default:
//                    return Key.Type.content;
//        }

        if(position==0){
            return Key.Type.video;
        }else{
            return Key.Type.content;
        }
    }



}
