package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.javava.shenma.R;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.bean.ShopBean;
import cn.javava.shenma.utils.ImageLoader;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/9
 * Todo {TODO}.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopItemHolder> {

    Context mContext;
    LayoutInflater mInflater;
    List<ShopBean> mList;

    public ShopAdapter(Context context, List<ShopBean> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public ShopAdapter.ShopItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopItemHolder(mInflater.inflate(R.layout.item_shop_food,parent,false));
    }

    @Override
    public void onBindViewHolder(ShopItemHolder holder, int position) {
        holder.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mList.size()>0)return mList.size();
        return 0;
    }

    public class ShopItemHolder extends RecyclerView.ViewHolder{
        ImageView mIvShow;
        public ShopItemHolder(View itemView) {
            super(itemView);
             mIvShow=itemView.findViewById(R.id.item_shop_show);
        }

        public void setData(ShopBean bean){
            ImageLoader.load(mContext,bean.resId,mIvShow);
        }


    }
}
