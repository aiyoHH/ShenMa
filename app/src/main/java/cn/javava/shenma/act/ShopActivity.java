package cn.javava.shenma.act;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.ShopAdapter;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.ShopBean;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/9
 * Todo {TODO}.
 */

public class ShopActivity extends BaseActivity {


    @BindView(R.id.shop_recycler)
    RecyclerView mRecyclerView;  

    List<ShopBean> mShopBeanList;

    int[] resIds={R.mipmap.test_1,R.mipmap.test_2,R.mipmap.test_5,R.mipmap.test_10,
            R.mipmap.test_1,R.mipmap.test_2,R.mipmap.test_5,R.mipmap.test_10};

    @Override
    protected int initLayout() {
        return R.layout.activity_shop;
    }

    @Override
    protected void initEventAndData() {
        mShopBeanList=new ArrayList<>();

        for (int i = 0; i < 8; i++) {
           ShopBean bean=new ShopBean();
           bean.resId=resIds[i];
            mShopBeanList.add(bean);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ShopAdapter(this,mShopBeanList));
    }
}
