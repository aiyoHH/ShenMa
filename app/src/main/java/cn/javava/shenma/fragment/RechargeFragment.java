package cn.javava.shenma.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.javava.shenma.R;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnLoadingListener;
import cn.javava.shenma.utils.QRcodeUtil;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/15
 * Todo {TODO}.
 */

public class RechargeFragment extends DialogFragment implements OnLoadingListener {

    Bitmap mBitmap;
    ImageView imageView;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scan_qrcode,container);
        imageView=view.findViewById(R.id.qr_code);
        String text=getArguments().getString(Key.DATA);
//        mBitmap=QRcodeUtil.createQRCode(text, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        mBitmap=QRcodeUtil.createQRCode(text);

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("lzh2017","onViewCreated text=");
        imageView.setBackground(new BitmapDrawable(mBitmap));
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.dimAmount=0.0f;
//        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(attributes);
//    }


    @Override
    public void onFinish(String accessToken) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

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
