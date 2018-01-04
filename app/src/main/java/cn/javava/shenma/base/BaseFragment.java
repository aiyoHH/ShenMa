package cn.javava.shenma.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Li'O on 2017/7/26.
 * Description {TODO}.
 */

public abstract class BaseFragment extends Fragment {


    private Unbinder mBinder;
    protected Context mContext;
    protected CompositeSubscription mCompositeSubscription;
    private Subscription mRxSub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(initLayout(),null);
        this.mContext=getContext();
        mBinder = ButterKnife.bind(this,view);
        initEventAndData();
        return view;
    }



    protected abstract int initLayout();

    protected abstract void initEventAndData();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

        unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
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
