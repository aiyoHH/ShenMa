package cn.javava.shenma.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.javava.shenma.R;
import cn.javava.shenma.bean.UserInfoBean;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnLoadingListener;
import cn.javava.shenma.utils.MotorDrvUtil;
import cn.javava.shenma.utils.QRcodeUtil;
import cn.javava.shenma.utils.SystemUtil;
import rx.Subscriber;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/15
 * Todo {TODO}.
 */

public class RechargeFragment extends DialogFragment implements OnLoadingListener {


    private long TIME_LIMIT_LOGIN=2*60*1000;
    private CountDownTimer countDownTimer;

    Bitmap mBitmap;
    ImageView imageView;
    private String qrcode_url="http://weixin.javava.cn/jiayi/index.php/index/Login/index/machinenumber/";
    public static RechargeFragment getInstance(String msg){
        RechargeFragment secondFragment= new RechargeFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Key.DATA,msg);
        secondFragment.setArguments(bundle);
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyle);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.0f;
        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.width = 900; // 宽度
        attributes.height = 1000; // 高度
//        attributes.alpha = 0.7f; // 透明度
        window.setAttributes(attributes);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scan_login,container);
        imageView=view.findViewById(R.id.qr_code);
        RelativeLayout mIvRootView=view.findViewById(R.id.qr_rootview);
        mIvRootView.setBackgroundResource(R.mipmap.recharge);
        String text=getArguments().getString(Key.DATA);
        mBitmap=QRcodeUtil.createQRCode(text, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        mBitmap=QRcodeUtil.createQRCode(text);

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final String deviceId = SystemUtil.getDeviceId(getActivity());

        Log.e("lzh2018","qrcode_url="+qrcode_url +deviceId);

        Bitmap qrCode = QRcodeUtil.createQRCode(qrcode_url +deviceId);
        imageView.setImageBitmap(qrCode);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(params);
        margin.topMargin=280;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        imageView.setLayoutParams(layoutParams);



        countDownTimer= new CountDownTimer(TIME_LIMIT_LOGIN, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                checkLogin(deviceId);
            }

            @Override
            public void onFinish() {



                RechargeFragment.this.dismiss();
            }
        };

        countDownTimer.start();

    }


    public void checkLogin(final String deviceId) {
        //联网获取access_token
        HttpHelper.getInstance().obtainUserInfo(new Subscriber<UserInfoBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserInfoBean bean) {
                if(Key.SUCCESS.equals(bean.getStatus())){

                    UserInfoBean.DataBean data = bean.getData();
                    if(data.getBalance()>Session.balance){
                        Session.balance=data.getBalance();
                        countDownTimer.cancel();
                        RechargeFragment.this.dismiss();
                    }


                }
            }
        },deviceId);

    }


    @Override
    public void onFinish(String accessToken) {

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(countDownTimer!=null)countDownTimer.cancel();
        if(mDismissListener!=null)mDismissListener.onDisMiss();
    }

    public interface onDismissListener{
        void onDisMiss();
    }

    private onDismissListener mDismissListener;

    public void addOnDdismissListener(onDismissListener listener){
        this.mDismissListener=listener;
    }
}
