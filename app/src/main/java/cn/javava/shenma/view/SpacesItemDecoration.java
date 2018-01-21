package cn.javava.shenma.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Li'O on 2017/10/24.
 * Description {TODO}.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {


        if (parent.getChildAdapterPosition(view) >1){
            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildAdapterPosition(view)%2==0){
                outRect.left= space;
            }
            outRect.right=space;
            outRect.top=space;

        }



    }
}
