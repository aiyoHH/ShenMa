package cn.javava.shenma.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.javava.shenma.app.App;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Li'O on 2017/7/26.
 * Description {TODO}.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mBinder;
    protected Context mContext;
    protected CompositeSubscription mCompositeSubscription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(initLayout());
        mBinder = ButterKnife.bind(this);
        //NotificationsUtils.setStatusBar(this, Color.WHITE);

        initEventAndData();
        App.getInstance().addActivity(this);
    }


    protected abstract int initLayout();

    protected abstract void initEventAndData();



    @Override
    protected void onStop() {
        super.onStop();
        unSubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
        App.getInstance().removeActivity(this);
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


}
