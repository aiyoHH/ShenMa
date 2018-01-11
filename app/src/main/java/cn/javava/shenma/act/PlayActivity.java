package cn.javava.shenma.act;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoLivePlayerCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;
import com.zego.zegoliveroom.entity.ZegoUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.BannerAdapter;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.interf.BoardState;
import cn.javava.shenma.interf.CMDKey;
import cn.javava.shenma.utils.CMDCenter;
import cn.javava.shenma.utils.SystemUtil;
import cn.javava.shenma.utils.ZegoStream;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/8
 * Todo {TODO}.
 */

public class PlayActivity extends AppCompatActivity{


    @BindView(R.id.play_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.play_cancel)
    ImageButton mBtnCancel;
    @BindView(R.id.play_confirm)
    ImageButton mBtnConfirm;
    @BindView(R.id.play_textureview1)
    TextureView mTextureView1;
    @BindView(R.id.play_textureview2)
    TextureView mTextureView2;

    private Room mRoom;
    private List<ZegoStream> mListStream = new ArrayList<>();
    private List<String> mListBanner = new ArrayList<>();
    private ZegoLiveRoom mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
    /**
     * app是否在后台.
     */
    private boolean mIsAppInBackground = true;

    /**
     * 切换摄像头次数, 用于记录当前正在显示"哪一条流"
     */
    private int mSwitchCameraTimes = 0;

    public static void actionStart(Activity activity, Room room) {
        Intent intent = new Intent(activity, PlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent != null) {
            mRoom = (Room) intent.getSerializableExtra("room");

            //setTitle(mRoom.roomName);
            setContentView(R.layout.activity_play);

            ButterKnife.bind(this);

            mListBanner.add("https://app-cdn.siy8.com/6320/images-1514632576278.png");

            mViewPager.setAdapter(new BannerAdapter(this,mListBanner));

            initStreamList();
//            initViews();
            startPlay();

        } else {
            Toast.makeText(this, "房间信息初始化错误, 请重新开始", Toast.LENGTH_LONG).show();
            finish();
        }

        //从加速服务器拉流
        ZegoLiveRoom.setConfig(ZegoConstants.Config.PREFER_PLAY_ULTRA_SOURCE + "=1");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsAppInBackground){
            mIsAppInBackground = false;

            int currentShowIndex = mSwitchCameraTimes % 2;
            if (currentShowIndex == 0){
                mListStream.get(0).playStream();
                mListStream.get(1).playStream();
            }else {
                mListStream.get(0).playStream();
                mListStream.get(1).playStream();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!SystemUtil.isAppForeground()){
            mIsAppInBackground = true;

            Log.i("PlayActivity", "App goes to background");

            for(ZegoStream zegoStream : mListStream){
                zegoStream.stopPlayStream();
            }
        }
    }

    private void logout() {
        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Ended) {
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage("正在游戏中，确定要离开吗？").setTitle("提示").setPositiveButton("离开", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doLogout();
                    finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            dialog.show();
        }else{
            doLogout();
        }
    }

    private void doLogout() {

        // 回复从CDN拉流
        ZegoLiveRoom.setConfig(ZegoConstants.Config.PREFER_PLAY_ULTRA_SOURCE + "=0");

        for (ZegoStream zegoStream : mListStream) {
            zegoStream.stopPlayStream();
        }

        mZegoLiveRoom.logoutRoom();
        CMDCenter.getInstance().reset();
        finish();

//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("lzh2017","KeyCode="+keyCode);
//        Log.e("lzh2017","KeyEvent event="+event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast.makeText(this,"左........",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast.makeText(this,"........右",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Toast.makeText(this,"...$$上$$...",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Toast.makeText(this,"...#下#...",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_BUTTON_C:
                Toast.makeText(this,"...确定...",Toast.LENGTH_LONG).show();
                mBtnConfirm.performClick();
                startActivity(new Intent(PlayActivity.this,TestActivity.class));
                break;
            case KeyEvent.KEYCODE_BACK:
                //弹窗是否退出
                mBtnCancel.performClick();
                logout();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化流信息. 当前, 一个房间内只需要2条流用于播放，少了用"空流"替代.
     */
    private void initStreamList() {

        int streamSize = mRoom.streamList.size() >= 2 ? 2 : mRoom.streamList.size();

        for (int index = 0; index < streamSize; index++) {
            mListStream.add(constructStream(index, mRoom.streamList.get(index)));
        }

        if (mListStream.size() < 2) {
            for (int index = mListStream.size(); index < 2; index++) {
                mListStream.add(constructStream(index, null));
            }
        }
    }

    private ZegoStream constructStream(int index, String streamID) {
        String sateStrings[];
        TextureView textureView;
        if (index == 0) {
            sateStrings = getResources().getStringArray(R.array.video1_state);
            textureView = mTextureView1;
        } else {
            sateStrings = getResources().getStringArray(R.array.video2_state);
            textureView = mTextureView2;
        }
        return new ZegoStream(streamID, textureView, sateStrings);
    }



    private void startPlay() {
        mZegoLiveRoom.loginRoom(mRoom.roomID, ZegoConstants.RoomRole.Audience, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errCode, ZegoStreamInfo[] zegoStreamInfos) {
                CMDCenter.getInstance().printLog("[onLoginCompletion], roomID: " + mRoom.roomID + ", errorCode: " + errCode + ", streamCount: " + zegoStreamInfos.length);
                if (errCode == 0) {
                    for (ZegoStreamInfo streamInfo : zegoStreamInfos) {
                        CMDCenter.getInstance().printLog("[onLoginCompletion], streamInfo: " + streamInfo.toString());

                        if (!TextUtils.isEmpty(streamInfo.userID) &&
                                !TextUtils.isEmpty(streamInfo.userName) && streamInfo.userName.startsWith("WWJS")) {
                            ZegoUser zegoUser = new ZegoUser();
                            zegoUser.userID = streamInfo.userID;
                            zegoUser.userName = streamInfo.userName;
                            CMDCenter.getInstance().setUserInfoOfWaWaJi(zegoUser);
                            break;
                        }
                    }

                    if (CMDCenter.getInstance().getUserInfoOfWaWaJi() == null){
                        CMDCenter.getInstance().printLog("[onLoginCompletion] error, No UserInfo Of WaWaJi");
                    }

//                    mIBtnApply.setEnabled(true);
//                    mIbtnSwitchCamera.setEnabled(true);

                    // 查询游戏信息
                    CMDCenter.getInstance().queryGameInfo();
                }
            }
        });

        mZegoLiveRoom.setZegoLivePlayerCallback(new IZegoLivePlayerCallback() {
            @Override
            public void onPlayStateUpdate(int errCode, String streamID) {

                int currentShowIndex = mSwitchCameraTimes % 2;

                if (errCode != 0) {
                    // 设置流的状态
                    for (ZegoStream zegoStream : mListStream) {
                        if (zegoStream.getStreamID().equals(streamID)) {
                            zegoStream.setStreamSate(ZegoStream.StreamState.PlayFail);
                            break;
                        }
                    }

                    ZegoStream currentShowStream = mListStream.get(currentShowIndex);
                    if (currentShowStream.getStreamID().equals(streamID)) {
//                        mTvStreamSate.setText(currentShowStream.getStateString());
//                        mTvStreamSate.setVisibility(View.VISIBLE);
                    }
                }
                CMDCenter.getInstance().printLog("[onPlayStateUpdate], streamID: " + streamID + " ,errorCode: " + errCode + ", currentShowIndex: " + currentShowIndex);
            }

            @Override
            public void onPlayQualityUpdate(String streamID, ZegoStreamQuality zegoStreamQuality) {
                // 当前显示的流质量
                if (mListStream.get(mSwitchCameraTimes % 2).getStreamID().equals(streamID)) {
                    switch (zegoStreamQuality.quality) {
                        case 0:
//                            mTvQuality.setText("网络优秀");
//                            mIvQuality.setImageResource(R.mipmap.excellent);
//                            mTvQuality.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            break;
                        case 1:
//                            mTvQuality.setText("网络流畅");
//                            mIvQuality.setImageResource(R.mipmap.good);
//                            mTvQuality.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                            break;
                        case 2:
//                            mTvQuality.setText("网络缓慢");
//                            mIvQuality.setImageResource(R.mipmap.average);
//                            mTvQuality.setTextColor(getResources().getColor(R.color.bg_yellow_p));
                            break;
                        case 3:
//                            mTvQuality.setText("网络拥堵");
//                            mIvQuality.setImageResource(R.mipmap.pool);
//                            mTvQuality.setTextColor(getResources().getColor(R.color.text_red));
                            break;
                    }
//                    mIvQuality.setVisibility(View.VISIBLE);
//                    mTvQuality.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onInviteJoinLiveRequest(int i, String s, String s1, String s2) {

            }

            @Override
            public void onRecvEndJoinLiveCommand(String s, String s1, String s2) {

            }

            @Override
            public void onVideoSizeChangedTo(String streamID, int i, int i1) {
                for (ZegoStream zegoStream : mListStream) {
                    if (zegoStream.getStreamID().equals(streamID)) {
                        zegoStream.setStreamSate(ZegoStream.StreamState.PlaySuccess);
                        break;
                    }
                }

                int currentShowIndex = mSwitchCameraTimes % 2;
                ZegoStream currentShowStream = mListStream.get(currentShowIndex);
                if (currentShowStream.getStreamID().equals(streamID)) {
//                    mTvStreamSate.setVisibility(View.GONE);
                    currentShowStream.show();
                }

                CMDCenter.getInstance().printLog("[onVideoSizeChanged], streamID: " + streamID + ", currentShowIndex: " + currentShowIndex);
            }
        });

        mZegoLiveRoom.setZegoRoomCallback(new IZegoRoomCallback() {
            @Override
            public void onKickOut(int reason, String roomID) {

            }

            @Override
            public void onDisconnect(int errorCode, String roomID) {
            }

            @Override
            public void onReconnect(int i, String s) {

            }

            @Override
            public void onTempBroken(int i, String s) {

            }

            @Override
            public void onStreamUpdated(final int type, final ZegoStreamInfo[] listStream, final String roomID) {
            }

            @Override
            public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {

            }

            @Override
            public void onRecvCustomCommand(String userID, String userName, String content, String roomID) {
                // 只接收当前房间，当前主播发来的消息
                if (CMDCenter.getInstance().isCommandFromAnchor(userID) && mRoom.roomID.equals(roomID)) {
                    if (!TextUtils.isEmpty(content)) {
                        //handleRecvCustomCMD(content);
                    }
                }
            }
        });
    }

//    /**
//     * 处理来自服务器的消息.
//     */
//    private void handleRecvCustomCMD(String msg) {
//        CMDCenter.getInstance().printLog("[handleRecvCustomCMD], msg: " + msg);
//
//        Map<String, Object> map = getMapFromJson(msg);
//        if (map == null) {
//            CMDCenter.getInstance().printLog("[handleRecvCustomCMD] error, map is null");
//            return;
//        }
//
//        int cmd = ((Double) map.get(CMDKey.CMD)).intValue();
//        int rspSeq = ((Double) map.get(CMDKey.SEQ)).intValue();
//        String sessionID = (String) map.get(CMDKey.SESSION_ID);
//
//        Map<String, Object> data = (Map<String, Object>) map.get("data");
//        if (data == null){
//            CMDCenter.getInstance().printLog("[handleRecvCustomCMD] error, data is null");
//            return;
//        }
//
//        switch (cmd) {
//            case CMDCenter.CMD_APPLY_RESULT:
//                handleApplyResult(rspSeq, data);
//                break;
//            case CMDCenter.CMD_REPLY_CANCEL_APPLY:
//                handleReplyCancelApply(rspSeq, sessionID, data);
//                break;
//            case CMDCenter.CMD_GAME_READY:
//                handleGameReady(rspSeq, sessionID, data);
//                break;
//            case CMDCenter.CMD_CONFIRM_BOARD_REPLY:
//                handleConfirmBoardReply(rspSeq, sessionID, data);
//                break;
//            case CMDCenter.CMD_GAME_RESULT:
//                handleGameResult(rspSeq, sessionID, data);
//                break;
//            case CMDCenter.CMD_RESPONSE_GAME_INFO:
//                handleResponseGameInfo(rspSeq, data);
//                break;
//            case CMDCenter.CMD_GAME_INFO_UPDATE:
//                handleGameInfoUpdate(data);
//                break;
//        }
//    }
//
//    /**
//     * 处理服务器返回的"预约结果".
//     */
//    private void handleApplyResult(int rspSeq, Map<String, Object> data) {
//        CMDCenter.getInstance().printLog("[handleApplyResult] enter");
//
//        if (!checkSeq(rspSeq)){
//            return;
//        }
//
//        if (!checkIsMyMsg((LinkedTreeMap<String, String>) data.get(CMDKey.PLAYER))){
//            return;
//        }
//
//        CMDCenter.getInstance().printLog("[handleApplyResult], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());
//        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Applying) {
//            CMDCenter.getInstance().printLog("[handleApplyResult] error, state mismatch");
//            return;
//        }
//
//        int result = ((Double) data.get(CMDKey.RESULT)).intValue();
//        if (result == 0) {
//            String sessionID = (String) data.get(CMDKey.SESSION_ID);
//            if(TextUtils.isEmpty(sessionID)){
//                CMDCenter.getInstance().printLog("[handleApplyResult] error, sessionID is null");
//                return;
//            }
//
//            CMDCenter.getInstance().setSessionID(sessionID);
//            CMDCenter.getInstance().setCurrentBoardSate(BoardState.WaitingBoard);
//
//            int myPostion = ((Double) data.get(CMDKey.INDEX)).intValue();
//            if (!mContinueToPlay){
//                String text = getString(R.string.cancel_apply) + "\n" + getString(R.string.my_position, (myPostion + ""));
//                showApplyBtn(true, R.mipmap.cancel, text, getString(R.string.cancel_apply).length());
//            }
//        } else {
//            Toast.makeText(PlayActivity.this, getString(R.string.apply_faile), Toast.LENGTH_SHORT).show();
//            reinitGame();
//        }
//    }

}
