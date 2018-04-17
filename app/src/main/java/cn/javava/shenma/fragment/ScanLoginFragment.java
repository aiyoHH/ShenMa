package cn.javava.shenma.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
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

public class ScanLoginFragment extends DialogFragment  {
    ImageView mIvQRcode;

    private long TIME_LIMIT_LOGIN=2*60*1000;
    private CountDownTimer countDownTimer;
    private SoundPool soundPool;
    int soundID_1;
    int  soundID_2;


    private String qrcode_url="http://weixin.javava.cn/jiayi/index.php/index/Login/index/machinenumber/";

    public static ScanLoginFragment getInstance(String msg){
        ScanLoginFragment secondFragment= new ScanLoginFragment();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scan_login,container);
         mIvQRcode=view.findViewById(R.id.qr_code);
        RelativeLayout mIvRootView=view.findViewById(R.id.qr_rootview);
        mIvRootView.setBackgroundResource(R.drawable.login_bg);

        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundID_1=soundPool.load(getActivity(), R.raw.login_success, 1);
        soundID_2= soundPool.load(getActivity(), R.raw.login_failure, 1);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String deviceId = SystemUtil.getDeviceId(getActivity());

        Log.e("lzh2018","qrcode_url="+qrcode_url +deviceId);

        Bitmap qrCode = QRcodeUtil.createQRCode(qrcode_url +deviceId);
        mIvQRcode.setImageBitmap(qrCode);
        ViewGroup.LayoutParams params = mIvQRcode.getLayoutParams();
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(params);
        margin.topMargin=280;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        mIvQRcode.setLayoutParams(layoutParams);


        countDownTimer= new CountDownTimer(TIME_LIMIT_LOGIN, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                checkLogin(deviceId);
            }

            @Override
            public void onFinish() {
                //提示登录失败
                soundPool.play(soundID_2, 0.8f, 0.8f,1, 0, 1.0f);

                ScanLoginFragment.this.dismiss();
            }
        };

        countDownTimer.start();

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount=0.0f;
        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.width = 800; // 宽度
        attributes.height = 1000; // 高度
//        attributes.alpha = 0.7f; // 透明度
        window.setAttributes(attributes);
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
                    if("success".equals(bean.getStatus())){
                        countDownTimer.cancel();
                        soundPool.play(soundID_1, 0.8f, 0.8f,1, 0, 1.0f);
                        UserInfoBean.DataBean data = bean.getData();
                        Session.login=true;
                        Session.openid=data.getOpen_id();
                        Session.memberid=data.getMember_id();
                        Session.nickname=data.getName();
                        Session.token=data.getToken();
                        Session.headimgurl=data.getProfile_photo();
                        Session.balance=data.getBalance();
                        ScanLoginFragment.this.dismiss();
                        if("open".equals(data.getOpen_all_case())){
                            MotorDrvUtil.pushAllCase(getActivity());
                        }
                    }
                }
            },deviceId);

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e("lzh2017",".....onDismiss..");
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
