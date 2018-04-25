package cn.javava.shenma.act;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.BannerAdapter;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.fragment.CatchExitFragment;
import cn.javava.shenma.fragment.GameResultDialog;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.BoardState;
import cn.javava.shenma.interf.CMDKey;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.utils.CMDCenter;
import cn.javava.shenma.utils.ImageLoader;
import cn.javava.shenma.utils.MotorDrvUtil;
import cn.javava.shenma.utils.SwitchBannerTask;
import cn.javava.shenma.utils.SystemUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.utils.ZegoStream;
import rx.Subscriber;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/8
 * Todo {TODO}.
 */

public class PlayActivity extends AppCompatActivity {

    @BindView(R.id.play_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.play_textureview1)
    TextureView mTextureView1;
    @BindView(R.id.play_textureview2)
    TextureView mTextureView2;
    @BindView(R.id.play_textureview2_layout)
    FrameLayout mTextureView2Layout;
    @BindView(R.id.tv_stream_state)
    TextView mTvStreamSate;
    @BindView(R.id.play_up)
    ImageButton mBtnUp;
    @BindView(R.id.play_down)
    ImageButton mBtnDown;
    @BindView(R.id.play_right)
    ImageButton mBtnRight;
    @BindView(R.id.play_left)
    ImageButton mBtnLeft;
    @BindView(R.id.play_boarding_countdown)
    TextView mTvBoardingCountDown;
    @BindView(R.id.play_internet_status)
    TextView mTvInternetStatus;
    @BindView(R.id.play_apply)
    ImageButton btnApply;
    @BindView(R.id.play_fee)
    TextView mTvFee;
    @BindView(R.id.orientation_layout)
    RelativeLayout orientationLayou;
    @BindView(R.id.orientation_type_bg)
    ImageView mIvTypeBg;
    @BindView(R.id.orientation_type_bg_layout)
    RelativeLayout typeBgLayout;
    @BindView(R.id.play_cancel)
    ImageButton mBtnCancel;
    @BindView(R.id.play_confirm)
    ImageButton mBtnConfirm;
    @BindView(R.id.avatar)
    ImageView mIvAvatar;
    @BindView(R.id.play_queue_count)
    TextView mTvQueueCount;

    private Room mRoom;
    private List<ZegoStream> mListStream = new ArrayList<>();
    private List<BannerBean.DataBean> mListBanner;
    private boolean isFirst = true;
    private boolean isWaitResult = false;
    private ZegoLiveRoom mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
    /**
     * app是否在后台.
     */
    private boolean mIsAppInBackground = true;

    /**
     * 切换摄像头次数, 用于记录当前正在显示"哪一条流"
     */
    private int mSwitchCameraTimes = 0;
    /**
     * 是否继续玩.
     */
    private boolean mContinueToPlay = false;

    /**
     * 房间内的排队人数.
     */
    private int mUsersInQueue = 0;

    /**
     * "确认上机"对话框.
     */
    private AlertDialog mDialogConfirmGameReady;
    /**
     * 计时器.
     */
    private CountDownTimer mCountDownTimer;
    private GameResultDialog mDialogGameResult = new GameResultDialog();
    private SoundPool soundPool;
    int soundID_1;
    int soundID_2;
    SwitchBannerTask switchBannerTaskn;
    private AlertDialog mDialog;
    private Intent mIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent = getIntent();

        if (mIntent != null) {
            mRoom = (Room) mIntent.getSerializableExtra("selectRoom");


            //setTitle(mRoom.roomName);
            setContentView(R.layout.activity_play);

            ButterKnife.bind(this);

            mListBanner = Session.bannerList;

            mViewPager.setAdapter(new BannerAdapter(this, mListBanner));
            mTvFee.setText(mRoom.balance + "币/次");

            switchBannerTaskn = new SwitchBannerTask();
            switchBannerTaskn.start(mViewPager);


            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            soundID_2 = soundPool.load(this, R.raw.bg_music, 1);
            soundID_1 = soundPool.load(this, R.raw.catch_down, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(soundID_2, 0.8f, 0.8f, -1, -1, 1.0f);

                }
            });


            showControlPannel(false);


            initStreamList();
            initViews();
            startPlay();
            UIUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startRead();
                }
            }, 2000);


        } else {
            Toast.makeText(this, "房间信息初始化错误, 请重新开始", Toast.LENGTH_LONG).show();
            setResult(5, mIntent);
            finish();
        }

        //从加速服务器拉流
        ZegoLiveRoom.setConfig(ZegoConstants.Config.PREFER_PLAY_ULTRA_SOURCE + "=1");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsAppInBackground) {
            mIsAppInBackground = false;

            int currentShowIndex = mSwitchCameraTimes % 2;
            if (currentShowIndex == 0) {
                mListStream.get(0).playStream();
                mListStream.get(1).playStream();
            } else {
                mListStream.get(0).playStream();
                mListStream.get(1).playStream();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!SystemUtil.isAppForeground()) {
            mIsAppInBackground = true;
            for (ZegoStream zegoStream : mListStream) {
                zegoStream.stopPlayStream();
            }
            switchBannerTaskn.stop();
            CMDCenter.getInstance().moveBackward();
        }
        doLogout();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.pause(soundID_1);
            soundPool.pause(soundID_2);
            soundPool.release();
        }
        switchBannerTaskn.stop();
        Session.isZhua = 0;
    }

    protected void initViews() {
        ImageLoader.loadCircular(this, Session.headimgurl, mIvAvatar);
    }


    private void startRead() {
        if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Ended) {
            CMDCenter.getInstance().apply(PlayActivity.this, false, new CMDCenter.OnCommandSendCallback() {
                @Override
                public void onSendFail() {
                    sendCMDFail("Apply");
                }
            });
        } else {
            if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingBoard) {
                CMDCenter.getInstance().cancelApply(new CMDCenter.OnCommandSendCallback() {
                    @Override
                    public void onSendFail() {
                        Toast.makeText(PlayActivity.this, getString(R.string.cancel_apply_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean isFeeing;

    //预约上机
    public void apply() {
        if (isFeeing)
            return;
        isFeeing = true;
        isFirst = false;
        //扣费环节
        Subscriber<NoneDataBean> subscriber = new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {
                isFeeing = false;
            }

            @Override
            public void onError(Throwable e) {
                doLogout();
            }

            @Override
            public void onNext(NoneDataBean roomO) {
                if (Key.SUCCESS.equals(roomO.getStatus())) {
                    Session.balance = roomO.getData().getBalance();
                    Session.isZhua = roomO.getData().getIs_zhua();
                } else {
                    doLogout();
                }
            }
        };
        HttpHelper.getInstance().feeDeduction(subscriber, mRoom.balance);

    }

    private void logout() {
        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Ended) {

            final CatchExitFragment catchExitFragment = CatchExitFragment.getInstance();
            catchExitFragment.setGameResultCallback(new GameResultDialog.OnGameResultCallback() {
                @Override
                public void onGiveUpPlaying() {
                    catchExitFragment.dismiss();
                    doLogout();
                }

                @Override
                public void onContinueToPlay() {
                    catchExitFragment.dismiss();
                }
            });

            catchExitFragment.show(getFragmentManager(), "");

        } else {
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
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        setResult(5, mIntent);
        finish();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnLeft.setActivated(true);
                    if (mSwitchCameraTimes % 2 == 0) {
                        CMDCenter.getInstance().moveLeft();
                    } else {
                        CMDCenter.getInstance().moveForward();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mBtnLeft.setActivated(false);
                    CMDCenter.getInstance().stopMove();
                }

                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnRight.setActivated(true);
                    if (mSwitchCameraTimes % 2 == 0) {
                        CMDCenter.getInstance().moveRight();
                    } else {
                        CMDCenter.getInstance().moveBackward();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mBtnRight.setActivated(false);
                    CMDCenter.getInstance().stopMove();
                }

                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnUp.setActivated(true);
                    if (mSwitchCameraTimes % 2 == 0) {
                        CMDCenter.getInstance().moveBackward();
                    } else {
                        CMDCenter.getInstance().moveLeft();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mBtnUp.setActivated(false);
                    CMDCenter.getInstance().stopMove();
                }

                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mBtnDown.setActivated(true);
                    if (mSwitchCameraTimes % 2 == 0) {
                        CMDCenter.getInstance().moveForward();
                    } else {
                        CMDCenter.getInstance().moveRight();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mBtnDown.setActivated(false);
                    CMDCenter.getInstance().stopMove();
                }

                break;
            case KeyEvent.KEYCODE_BUTTON_C:
                mBtnConfirm.setActivated(true);

                if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Boarding) {
                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }
                    CMDCenter.getInstance().grub();
                    soundPool.play(soundID_1, 0.8f, 0.8f, 1, 0, 1.0f);
                    showControlPannel(false);
                    UIUtils.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitResult();
                        }
                    }, 3000);
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                //弹窗是否退出
                mBtnCancel.setActivated(true);
                if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Applying) {
                    logout();
                }
                mBtnCancel.setActivated(false);
                return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 初始化流信息.
     */
    private void initStreamList() {

        //当前, 一个房间内只需要2条流用于播放，少了用"空流"替代.
        int streamSize = mRoom.streamList.size() >= 2 ? 2 : mRoom.streamList.size();

        for (int index = 0; index < streamSize; index++) {
            mListStream.add(constructStream(index, mRoom.streamList.get(index)));
        }

        if (mListStream.size() < 2) {
            for (int index = mListStream.size(); index < 2; index++) {
                mListStream.add(constructStream(index, null));
            }

            mTextureView2.setVisibility(View.INVISIBLE);
            mTextureView2Layout.setVisibility(View.INVISIBLE);
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
                        CMDCenter.getInstance().printLog("[onLoginCompletion], streamInfo: userId=" + streamInfo.userID
                                + " |userName=" + streamInfo.userName);

                        if (!TextUtils.isEmpty(streamInfo.userID) &&
                                !TextUtils.isEmpty(streamInfo.userName) && streamInfo.userName.startsWith("WWJS")) {
                            ZegoUser zegoUser = new ZegoUser();
                            zegoUser.userID = streamInfo.userID;
                            zegoUser.userName = streamInfo.userName;
                            CMDCenter.getInstance().setUserInfoOfWaWaJi(zegoUser);
                            break;
                        }
                    }

                    if (CMDCenter.getInstance().getUserInfoOfWaWaJi() == null) {
                        CMDCenter.getInstance().printLog("[onLoginCompletion] error, No UserInfo Of WaWaJi");
                    }

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

                            mTvInternetStatus.setText("网络优秀");
                            //                            mTvInternetStatus.setImageResource(R.mipmap.excellent);
                            mTvInternetStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            break;
                        case 1:
                            mTvInternetStatus.setText("网络流畅");
                            //                            mTvInternetStatus.setImageResource(R.mipmap.good);
                            mTvInternetStatus.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                            break;
                        case 2:
                            mTvInternetStatus.setText("网络缓慢");
                            //                            mTvInternetStatus.setImageResource(R.mipmap.average);
                            mTvInternetStatus.setTextColor(getResources().getColor(R.color.colorYellow));
                            break;
                        case 3:
                            mTvInternetStatus.setText("网络拥堵");
                            //                            mIvQuality.setImageResource(R.mipmap.pool);
                            mTvInternetStatus.setTextColor(getResources().getColor(R.color.colorRed));
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
                    mTvStreamSate.setVisibility(View.GONE);
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
                        handleRecvCustomCMD(content);
                    }
                }
            }
        });
    }

    private void sendCMDFail(String cmd) {
        Toast.makeText(PlayActivity.this, getString(R.string.send_cmd_error), Toast.LENGTH_SHORT).show();
        reinitGame();
    }

    /**
     * 处理来自服务器的消息.
     */
    private void handleRecvCustomCMD(String msg) {
        CMDCenter.getInstance().printLog("[handleRecvCustomCMD], msg: " + msg);

        Map<String, Object> map = getMapFromJson(msg);
        if (map == null) {
            CMDCenter.getInstance().printLog("[handleRecvCustomCMD] error, map is null");
            return;
        }

        int cmd = ((Double) map.get(CMDKey.CMD)).intValue();
        int rspSeq = ((Double) map.get(CMDKey.SEQ)).intValue();
        String sessionID = (String) map.get(CMDKey.SESSION_ID);

        Map<String, Object> data = (Map<String, Object>) map.get("data");
        if (data == null) {
            CMDCenter.getInstance().printLog("[handleRecvCustomCMD] error, data is null");
            return;
        }

        switch (cmd) {
            case CMDCenter.CMD_APPLY_RESULT:
                handleApplyResult(rspSeq, data);
                break;
            case CMDCenter.CMD_REPLY_CANCEL_APPLY:
                handleReplyCancelApply(rspSeq, sessionID, data);
                break;
            case CMDCenter.CMD_GAME_READY:
                handleGameReady(rspSeq, sessionID, data);
                break;
            case CMDCenter.CMD_CONFIRM_BOARD_REPLY:
                handleConfirmBoardReply(rspSeq, sessionID, data);
                break;
            case CMDCenter.CMD_GAME_RESULT:
                synchronized (this) {
                    handleGameResult(rspSeq, sessionID, data);
                }
                break;
            case CMDCenter.CMD_RESPONSE_GAME_INFO:
                handleResponseGameInfo(rspSeq, data);
                break;
            case CMDCenter.CMD_GAME_INFO_UPDATE:
                handleGameInfoUpdate(data);
                break;
        }
    }

    /**
     * 房间人数更新.
     */
    private void handleGameInfoUpdate(Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleGameInfoUpdate] enter");
        showGameInfo(data);
    }

    /**
     * 处理服务器返回的"预约结果".
     */
    private void handleApplyResult(int rspSeq, Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleApplyResult] enter");

        if (!checkSeq(rspSeq)) {
            return;
        }

        if (!checkIsMyMsg((LinkedTreeMap<String, String>) data.get(CMDKey.PLAYER))) {
            return;
        }

        CMDCenter.getInstance().printLog("[handleApplyResult], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());
        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Applying) {
            CMDCenter.getInstance().printLog("[handleApplyResult] error, state mismatch");
            return;
        }

        int result = ((Double) data.get(CMDKey.RESULT)).intValue();
        if (result == 0) {
            String sessionID = (String) data.get(CMDKey.SESSION_ID);
            if (TextUtils.isEmpty(sessionID)) {
                CMDCenter.getInstance().printLog("[handleApplyResult] error, sessionID is null");
                return;
            }

            CMDCenter.getInstance().setSessionID(sessionID);
            CMDCenter.getInstance().setCurrentBoardSate(BoardState.WaitingBoard);
            if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.Ended || CMDCenter.getInstance().getCurrentBoardSate() != BoardState.WaitingBoard) {
                if (isFirst) {
                    apply();
                }
            }
            int myPostion = ((Double) data.get(CMDKey.INDEX)).intValue();
            if (!mContinueToPlay) {
                String text = getString(R.string.my_position, (myPostion + ""));
                showApplyBtn(true, R.mipmap.cancel, text, getString(R.string.cancel_apply).length());
            }
        } else {
            Toast.makeText(PlayActivity.this, getString(R.string.apply_faile), Toast.LENGTH_SHORT).show();
            reinitGame();
        }
    }

    private void handleReplyCancelApply(final int rspSeq, String sessionID, Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleReplyCancelApply] enter");

        if (!checkSeq(rspSeq)) {
            return;
        }

        if (!checkSessionID(sessionID)) {
            return;
        }

        CMDCenter.getInstance().printLog("[handleReplyCancelApply], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());

        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.WaitingBoard) {
            CMDCenter.getInstance().printLog("[handleReplyCancelApply] error, state mismatch");
            return;
        }

        Toast.makeText(this, getString(R.string.cancel_apply_success), Toast.LENGTH_SHORT).show();

        reinitGame();
    }

    /**
     * 处理服务器返回的"准备游戏"的指令.
     */
    private void handleGameReady(final int rspSeq, String sessionID, Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleGameReady] enter");

        if (!checkSessionID(sessionID)) {
            return;
        }

        if (!checkIsMyMsg((LinkedTreeMap<String, String>) data.get(CMDKey.PLAYER))) {
            return;
        }

        CMDCenter.getInstance().printLog("[handleGameReady], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());

        if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Applying) {
            CMDCenter.getInstance().printLog("[handleGameReady], fix state");
            CMDCenter.getInstance().setCurrentBoardSate(BoardState.WaitingBoard);
        }

        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.WaitingBoard) {
            CMDCenter.getInstance().printLog("[handleGameReady] error, state mismatch");
            return;
        }
        // 通知服务器，客户端已经收到GameReady指令
        CMDCenter.getInstance().replyRecvGameReady(rspSeq);

        // 继续玩，直接确认上机
        if (mContinueToPlay) {
            mContinueToPlay = false;
            CMDCenter.getInstance().printLog("[handleGameReady], continue to play");
            CMDCenter.getInstance().getEntrptedConfig();
            return;
        }

        if (mDialogConfirmGameReady != null && mDialogConfirmGameReady.isShowing()) {
            CMDCenter.getInstance().printLog("[handleGameReady], confirm dialog is showing");
            return;
        }

        readingGo();

    }

    private void waitResult() {
        View inflate = View.inflate(this, R.layout.wait_result_dialog_layout, null);
        mDialog = new AlertDialog.Builder(this, R.style.dialog_show_style)
                .setCancelable(false)
                .setView(inflate).create();
        mDialog.show();
    }

    private void readingGo() {
        View inflate = View.inflate(this, R.layout.dialog_star_play, null);
        final TextView dialogMsg = inflate.findViewById(R.id.dialog_star_play_hint);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.dialog_show_style)
                .setCancelable(false)
                .setView(inflate).create();
        dialog.show();

        mCountDownTimer = new CountDownTimer(5000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingBoard) {
                    dialogMsg.setText(getString(R.string.confirm_board, ((millisUntilFinished / 1000) + 1) + ""));
                }
            }

            @Override
            public void onFinish() {
                if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingBoard) {
                    CMDCenter.getInstance().getEntrptedConfig();
                    dialog.dismiss();
                }
            }
        }.start();

    }

    /**
     * 处理服务器返回的"收到上机选择"指令.
     */
    private void handleConfirmBoardReply(int rspSeq, String sessionID, Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleConfirmBoardReply] enter");

        if (!checkSeq(rspSeq)) {
            return;
        }

        if (!checkSessionID(sessionID)) {
            return;
        }

        CMDCenter.getInstance().printLog("[handleConfirmBoardReply], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());

        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.ConfirmBoard) {
            CMDCenter.getInstance().printLog("[handleConfirmBoardReply] error, state mismatch");
            return;
        }

        int result = ((Double) data.get(CMDKey.RESULT)).intValue();
        if (result != 0) {
            CMDCenter.getInstance().printLog("[handleConfirmBoardReply] error, confirm board fail");
            Toast.makeText(this, "上机校验失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        if (CMDCenter.getInstance().isConfirmBoard()) {

            CMDCenter.getInstance().setCurrentBoardSate(BoardState.Boarding);
            startGame();

            mCountDownTimer = new CountDownTimer(CMDCenter.getInstance().getUserBoardingTime(), 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Boarding) {
                        mTvBoardingCountDown.setVisibility(View.VISIBLE);
                        mTvBoardingCountDown.setText("游戏倒计时：" + ((millisUntilFinished / 1000)) + "s");
                    }
                }

                @Override
                public void onFinish() {
                    if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Boarding) {
                        //                        enbleControl(false);
                        CMDCenter.getInstance().grub();
                    }
                }
            }.start();
        }
    }


    /**
     * 处理服务器返回的"游戏结果".
     */
    private void handleGameResult(final int rspSeq, String sessionID, Map<String, Object> data) {

        CMDCenter.getInstance().printLog("[handleGameResult] enter");
        if (!checkSessionID(sessionID))
            return;

        if (!checkIsMyMsg((LinkedTreeMap<String, String>) data.get(CMDKey.PLAYER)))
            return;
        CMDCenter.getInstance().printLog("[handleGameResult], currentSate: " + CMDCenter.getInstance().getCurrentBoardSate());

        if (CMDCenter.getInstance().getCurrentBoardSate() != BoardState.WaitingGameResult) {
            // 服务器可能没有收到客户端发送的"确认收到游戏结果"消息，会继续发送"游戏结果"到客户端
            CMDCenter.getInstance().confirmGameResult(rspSeq, mContinueToPlay);
            CMDCenter.getInstance().printLog("[handleGameResult], remain handleGameResult from Server");
            return;
        }

        if (mDialogGameResult != null && mDialogGameResult.isVisible()) {
            CMDCenter.getInstance().printLog("[handleGameResult], confirm dialog is visible");
            return;
        }

        if (isWaitResult)
            return;

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }


        if (mDialogGameResult != null && mDialogGameResult.isVisible())
            return;
        isWaitResult = true;
        CMDCenter.getInstance().confirmGameResult(rspSeq, false);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            showControlPannel(true);
        }
        int result = ((Double) data.get(CMDKey.RESULT)).intValue();
        mDialogGameResult.setRspSeq(rspSeq);

        if (result == 1) {
            mDialogGameResult.setBackGround(true);
            MotorDrvUtil.openMotor(this, mRoom.number);
        } else {
            mDialogGameResult.setBackGround(false);
            MotorDrvUtil.registerGood("0");
        }

        mDialogGameResult.setCancelable(false);
        if (result != 1) {
            mDialogGameResult.setGameResultCallback(new GameResultDialog.OnGameResultCallback() {
                @Override
                public void onGiveUpPlaying() {
                    mCountDownTimer.cancel();
                    mDialogGameResult.dismiss();
                    isWaitResult = false;
                    mTvBoardingCountDown.setText("");
                    reinitGame();
                    setResult(5, mIntent);
                    finish();
                }

                @Override
                public void onContinueToPlay() {
                    mCountDownTimer.cancel();
                    mDialogGameResult.dismiss();
                    isWaitResult = false;
                    mTvBoardingCountDown.setText("");
                    kouFee(rspSeq);
                }
            });
        }

        mDialogGameResult.show(getFragmentManager(), "GameResultDialog");
        mCountDownTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingGameResult) {
                    mDialogGameResult.setContinueText(getString(R.string.continue_to_play, (millisUntilFinished / 1000) + ""));
                }
            }

            @Override
            public void onFinish() {
                if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingGameResult) {
                    mDialogGameResult.dismiss();
                    isWaitResult = false;
                    reinitGame();
                    setResult(5, mIntent);
                    finish();
                }
            }
        }.start();

    }


    private void kouFee(final int rspSeq) {
        //扣费环节
        if (isFeeing)
            return;
        isFeeing = true;
        Subscriber<NoneDataBean> subscriber = new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {
                isFeeing = false;
            }

            @Override
            public void onError(Throwable e) {
                doLogout();
            }

            @Override
            public void onNext(NoneDataBean roomO) {

                if (Key.SUCCESS.equals(roomO.getStatus())) {

                    CMDCenter.getInstance().confirmGameResult(rspSeq, true);
                    continueToPlay();
                    Session.isZhua = roomO.getData().getIs_zhua();
                    Session.balance = roomO.getData().getBalance();
                } else {
                    doLogout();
                }
            }
        };
        HttpHelper.getInstance().feeDeduction(subscriber, mRoom.balance);

    }

    /**
     * 处理服务器返回的"游戏信息".
     */
    private void handleResponseGameInfo(int rspSeq, Map<String, Object> data) {
        CMDCenter.getInstance().printLog("[handleResponseGameInfo] enter");

        if (!checkSeq(rspSeq)) {
            return;
        }

        int gameTime = ((Double) data.get(CMDKey.GAME_TIME)).intValue();
        CMDCenter.getInstance().setUserBoardingTime(gameTime);

        showGameInfo(data);
    }

    private void startGame() {
        showControlPannel(true);
    }

    private void continueToPlay() {
        mContinueToPlay = true;
        CMDCenter.getInstance().continueToPlay();
        showControlPannel(false);
        if (mUsersInQueue == 0) {
            String text = getString(R.string.apply_grub) + "\n" + getString(R.string.current_queue_count, mUsersInQueue + "");
            showApplyBtn(false, R.mipmap.ic_room1, text, getString(R.string.start_game).length());
        } else {
            String text = getString(R.string.apply_grub) + "\n" + getString(R.string.current_queue_count, mUsersInQueue + "");
            showApplyBtn(false, R.mipmap.ic_room2, text, getString(R.string.apply_grub).length());
        }


        CMDCenter.getInstance().getEntrptedConfig();

        CMDCenter.getInstance().apply(this, true, new CMDCenter.OnCommandSendCallback() {
            @Override
            public void onSendFail() {
                sendCMDFail("Apply");
            }
        });


    }


    private void showGameInfo(Map<String, Object> data) {

        int total = ((Double) data.get(CMDKey.TOTAL)).intValue();
        //        mTvRoomUserCount.setText(getString(R.string.room_user_count, total + ""));

        ArrayList queueList = (ArrayList) data.get(CMDKey.QUEUE);
        int myPosition = 0;
        if (queueList != null) {
            mUsersInQueue = queueList.size();

        }

        if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Ended ||
                CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Applying) {

            boolean enable = true;
            if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.Applying) {
                enable = false;
            }

            if (mUsersInQueue == 0) {
                String text = getString(R.string.current_queue_count, mUsersInQueue + "");
                showApplyBtn(enable, R.mipmap.ic_room1, text, text.length());
            } else {
                String text = getString(R.string.current_queue_count, mUsersInQueue + "");
                showApplyBtn(enable, R.mipmap.ic_room2, text, getString(R.string.apply_grub).length());
            }

        } else if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.WaitingBoard
                || CMDCenter.getInstance().getCurrentBoardSate() == BoardState.ConfirmBoard) {

            boolean enable = true;
            if (CMDCenter.getInstance().getCurrentBoardSate() == BoardState.ConfirmBoard) {
                enable = false;
            }

            String text = getString(R.string.my_position, myPosition + "");
            showApplyBtn(enable, R.mipmap.cancel, text, getString(R.string.cancel_apply).length());
        }
    }


    private void reinitGame() {
        mContinueToPlay = false;
        CMDCenter.getInstance().reinitGame();
        showControlPannel(false);

        if (mUsersInQueue == 0) {
            String text = getString(R.string.current_queue_count, mUsersInQueue + "");

            showApplyBtn(true, R.mipmap.ic_room1, text, getString(R.string.start_game).length());
        } else {
            String text = getString(R.string.current_queue_count, mUsersInQueue + "");
            showApplyBtn(true, R.mipmap.ic_room2, text, getString(R.string.apply_grub).length());
        }
    }

    private boolean checkSeq(int rspSeq) {
        if (CMDCenter.getInstance().getCurrentSeq() != rspSeq) {
            CMDCenter.getInstance().printLog("Error, seq mismatch, rspSeq: " + rspSeq + ", currentSeq: " + CMDCenter.getInstance().getCurrentSeq());
            return false;
        }

        return true;
    }

    private void showApplyBtn(boolean enable, int background, String text, int mainTextLen) {
        mTvQueueCount.setText(text);
    }

    //控制按钮控制台状态
    private void showControlPannel(boolean enable) {
        mBtnCancel.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        mBtnConfirm.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        orientationLayou.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        typeBgLayout.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        if (!enable) {
            if (mIvTypeBg != null)
                ImageLoader.load(this, mRoom.roomIcon, mIvTypeBg);
        }

        btnApply.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        mTvFee.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);


    }

    private boolean checkSessionID(String sessionID) {
        if (TextUtils.isEmpty(sessionID) || !sessionID.equals(CMDCenter.getInstance().getSessionID())) {
            CMDCenter.getInstance().printLog("Error, sessionID not equal, my sessionID: " + CMDCenter.getInstance().getSessionID() + ", sessionID: "
                    + sessionID);
            return false;
        }

        return true;
    }

    private boolean checkIsMyMsg(LinkedTreeMap<String, String> mapPlayer) {
        if (mapPlayer == null) {
            CMDCenter.getInstance().printLog("Error, player is null");
            return false;
        }


        return true;
    }

    private Map<String, Object> getMapFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<>();
            map = gson.fromJson(json, map.getClass());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
