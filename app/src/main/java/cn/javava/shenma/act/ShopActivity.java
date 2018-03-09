package cn.javava.shenma.act;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.ShopAdapter;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.PayResultBean;
import cn.javava.shenma.bean.ShopBean;
import cn.javava.shenma.fragment.QRCodeFragment;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.OnPositionClickListener;
import cn.javava.shenma.utils.QRcodeUtil;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/9
 * Todo {TODO}.
 */

public class ShopActivity extends BaseActivity implements OnPositionClickListener {


    @BindView(R.id.shop_recycler)
    RecyclerView mRecyclerView;
    QRCodeFragment mQRCodeFragment;

    List<ShopBean> mShopBeanList;

    int[] resIds={R.mipmap.test_1,R.mipmap.test_2,R.mipmap.test_5,R.mipmap.test_10,
            R.mipmap.test_1,R.mipmap.test_2,R.mipmap.test_5,R.mipmap.test_10};
    int[] moneyS={100,200,500,1000};

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
        mRecyclerView.setAdapter(new ShopAdapter(this,mShopBeanList,this));
    }

    @Override
    public void onClick(int position) {


        obtainQRText(moneyS[position]);

    }

    private void obtainQRText(int money){

        Subscriber subscriber=new Subscriber<PayResultBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PayResultBean bean) {
                if(mQRCodeFragment!=null)mQRCodeFragment.dismiss();
                Log.e("lzh2017","onNext text="+bean.getCode_url());
                 mQRCodeFragment = QRCodeFragment.getInstance(bean.getCode_url());
                 mQRCodeFragment.setCancelable(true);
                 mQRCodeFragment.show(getFragmentManager(), "pay");

            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().obtainQRCodePay(subscriber,"9527",money);
    }
}
