package cn.javava.shenma.utils;

import android.content.res.Resources;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoCustomCommandCallback;
import com.zego.zegoliveroom.entity.ZegoUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.javava.shenma.R;
import cn.javava.shenma.app.App;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.bean.ConfigBean;
import cn.javava.shenma.http.HttpApis;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.BoardState;
import cn.javava.shenma.interf.CMDKey;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Copyright © 2017 Zego. All rights reserved.
 */
public class CMDCenter {

    private static final String LOG_TAG = "CMDCenter";

    /**
     * 发送CMD的最长重试时间为10s.
     */
    private static final long MAX_RETRY_TIME = 10000;

    /**
     * 发送CMD的重试间隔为2s.
     */
    private static final long RETRY_INTERVAl = 2000;

    /**
     * 发送CMD的最大重试次数为5次.
     */
    public static final int MAX_RETRY_TIMES = 5;

    /**
     * 预约上机, Client->Server.
     */
    private static final int CMD_APPLY = 513;

    /**
     * 预约结果, Server->Client.
     */
    public static final int CMD_APPLY_RESULT = 272;

    /**
     * 取消预约, Client->Server.
     */
    public static final int CMD_CANCEL_APPLY = 514;

    /**
     * 回复收到"取消预约", Server->Client
     */
    public static final int CMD_REPLY_CANCEL_APPLY = 274;

    /**
     * 准备游戏, Server->Client.
     */
    public static final int CMD_GAME_READY = 258;

    /**
     * 回复收到"准备游戏", Client->Server.
     */
    private static final int CMD_REPLY_RECV_GAME_READY = 516;

    /**
     * 确认上机或者取消, Client->Server.
     */
    private static final int CMD_CONFIRM_BOARD = 515;

    /**
     * 回复收到"确认上机", Server->Client.
     */
    public static final int CMD_CONFIRM_BOARD_REPLY = 273;

    /**
     * 左移.
     */
    private static final int CMD_MOVE_LEFT = 528;

    /**
     * 右移.
     */
    private static final int CMD_MOVE_RIGHT = 529;

    /**
     * 上移.
     */
    private static final int CMD_MOVE_FORWARD = 530;

    /**
     * 下移.
     */
    private static final int CMD_MOVE_BACKWARD = 531;

    /**
     * 抓.
     */
    private static final int CMD_GRUB = 532;

    /**
     * 结束移动.
     */
    private static final int CMD_STOP_MOVE = 533;

    /**
     * 通知游戏结果, Server->Client.
     */
    public static final int CMD_GAME_RESULT = 260;

    /**
     * 确认收到游戏结果, Client->Server,
     */
    private static final int CMD_CONFIRM_GAME_RESULT = 517;

    /**
     * 游戏信息更新, Server->Client.
     */
    public static final int CMD_GAME_INFO_UPDATE = 257;

    /**
     * 查询游戏信息, Client->Server.
     */
    private static final int CMD_QUERY_GAME_INFO = 518;

    /**
     * 得到游戏信息, Server->Client.
     */
    public static final int CMD_RESPONSE_GAME_INFO = 275;

    private static final int YES = 1;

    private static final int NO = 0;

    private static CMDCenter sInstance;

    private int mSeq;

    private ZegoLiveRoom mZegoLiveRoom;

    private ZegoUser mUserInfoOfWaWaJi;

    private BoardState mCurrentBoardSate;

    private Resources mResources;

    private boolean mConfirmBoard;

    private LinkedList<String> mListLog;

    private CountDownTimer mCountDownTimerRetryHttpRequest;

    private CountDownTimer mCountDownTimerMove;

    private String mSessionID;

    /**
     * 用户上机操作的时长, 默认30s.
     */
    private int mUserBoardingTime;

    private CMDCenter() {
        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
        mResources = App.getInstance().getResources();
        mSeq = 0;

        mUserInfoOfWaWaJi = null;
        mCurrentBoardSate = BoardState.Ended;
        mConfirmBoard = false;
        mListLog = new LinkedList<>();
        mCountDownTimerRetryHttpRequest = null;
        mSessionID = null;
        mUserBoardingTime = 30 * 1000;
    }

    public static CMDCenter getInstance() {
        if (sInstance == null) {
            synchronized (CMDCenter.class) {
                if (sInstance == null) {
                    sInstance = new CMDCenter();
                }
            }
        }
        return sInstance;
    }

    public void reset() {
        mUserInfoOfWaWaJi = null;
        mCurrentBoardSate = BoardState.Ended;
        mConfirmBoard = false;
        mListLog = new LinkedList<>();
        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }
        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mSessionID = null;
        mUserBoardingTime = 30 * 1000;
    }

    public  void reinitGame(){
        mCurrentBoardSate = BoardState.Ended;
        mConfirmBoard = false;
        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }
        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mSessionID = null;
    }

    public void continueToPlay(){
        mCurrentBoardSate = BoardState.Ended;
        mConfirmBoard = false;
        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }
        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
    }

    private int getSeq() {
        mSeq++;
        return mSeq;
    }

    public int getCurrentSeq(){
        return mSeq;
    }

    public ZegoUser getUserInfoOfWaWaJi() {
        return mUserInfoOfWaWaJi;
    }

    public void setUserInfoOfWaWaJi(ZegoUser zegoUser) {
        mUserInfoOfWaWaJi = zegoUser;
    }

    private ZegoUser[] getUserListToSendCMD(){
        if (mUserInfoOfWaWaJi != null){
            ZegoUser[]userList = new ZegoUser[1];
            userList[0] = mUserInfoOfWaWaJi;
            return userList;
        }
        return null;
    }


    public void setCurrentBoardSate(BoardState state) {
        printLog("[setCurrentBoardSate], currentSate: " + mCurrentBoardSate + ", state: " + state);
        mCurrentBoardSate = state;
    }

    public BoardState getCurrentBoardSate() {
        return mCurrentBoardSate;
    }

    private SimpleDateFormat sDataFormat = new SimpleDateFormat("[hh:mm:ss.SSS]");
    public void printLog(String msg) {
        Log.e(LOG_TAG, msg);

        String now = sDataFormat.format(new Date());
        mListLog.addFirst(String.format("%s %s", now, msg));
        //PreferenceUtil.getInstance().setObjectToString(LogListActivity.KEY_LIST_LOG, mListLog);
    }

    public boolean isCommandFromAnchor(String userID) {
        if (mUserInfoOfWaWaJi != null)
            if (!TextUtils.isEmpty(mUserInfoOfWaWaJi.userID) && mUserInfoOfWaWaJi.userID.equals(userID)) {
                return true;
            }
        return false;
    }

    public boolean isConfirmBoard() {
        return mConfirmBoard;
    }

    public void setSessionID(String sessionID){
        printLog("[CommandUtil_setSessionID], sessionID: " + sessionID);
        mSessionID = sessionID;
    }

    public String getSessionID(){
       return mSessionID;
    }

    public void setUserBoardingTime(int time){
//        mUserBoardingTime = time;
    }

    public int getUserBoardingTime(){
        return mUserBoardingTime;
    }

    /**
     * 从业务后台获取加密的游戏配置信息.
     */
    public void getEntrptedConfig(){
//        final long timeStamp = System.currentTimeMillis();
//
//        String appID = ZegoApiManager.getInstance().getAppID() + "";
//        String url = String.format("http://wsliveroom%s-api.zego.im:8181/pay?" +
//                        "app_id=%s&id_name=%s&session_id=%s&confirm=1&time_stamp=%s&item_type=123&item_price=200"
//                , appID, appID, Session.userId, mSessionID, timeStamp);

        HttpHelper.getInstance().gainConfig(new Subscriber<ConfigBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ConfigBean bean) {

                    CMDCenter.getInstance().confirmBoard(true, bean.getConfig(), Long.valueOf(bean.getTime_stamp()), new OnCommandSendCallback() {
                        @Override
                        public void onSendFail() {
                            printLog("[CMDCenter_getEntrptedConfig] error, confirm board fail");
                        }
                    });
            }
        },Session.accessToken,mSessionID,1);

    }

    Map<String, Object> getCMDHeader(int seq, int cmd, String sessionID){
        Map<String, Object> header = new HashMap<>();
        header.put(CMDKey.SEQ, seq);
        header.put(CMDKey.CMD, cmd);
        if (!TextUtils.isEmpty(sessionID)){
            header.put(CMDKey.SESSION_ID, sessionID);
        }
        return header;
    }

    Map<String, Object> getDataMap(long timeStamp){
        Map<String, Object> data = new HashMap<>();
        data.put(CMDKey.TIME_STAMP, timeStamp);
        return data;
    }

    /**
     * 查询游戏信息.
     */
    public void queryGameInfo(){
        printLog("[CommandUtil_queryGameInfo] enter");

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_QUERY_GAME_INFO, null);
        Map<String, Object> mapData = getDataMap(System.currentTimeMillis());
        mapCMD.put(CMDKey.DATA, mapData);

        Gson gson = new Gson();
        String msg = gson.toJson(mapCMD);

        printLog("queryGameInfo_sendCustomCommand, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("queryGameInfo_onSendCustomCommand, errorCode: " + errorCode);
            }
        });
    }

    /**
     * 预约游戏.
     *
     * @param needContinue
     */
    public void apply(boolean needContinue, final OnCommandSendCallback callback) {
        printLog("[CommandUtil_apply], needContinue: " + needContinue + ", currentState: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Ended) {
            printLog("[CommandUtil_apply] error, state mismatch");
            return;
        }

        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }

        setCurrentBoardSate(BoardState.Applying);

        String sessionID = needContinue ? mSessionID : null;

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_APPLY, sessionID);

        Map<String, Object> mapData = getDataMap(System.currentTimeMillis());
        mapData.put(CMDKey.CONTINUE, needContinue ? YES : NO);

        mapCMD.put(CMDKey.DATA, mapData);

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog(mResources.getString(R.string.send_reply, "1") + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog(mResources.getString(R.string.rsp_reply, "1") + errorCode);
            }
        });

        mCountDownTimerRetryHttpRequest = new CountDownTimer(MAX_RETRY_TIME, RETRY_INTERVAl) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCurrentBoardSate == BoardState.Applying) {
                    // 第一次回调的时间间隔 < 2000ms, 不处理
                    if ((MAX_RETRY_TIME - millisUntilFinished) > RETRY_INTERVAl){

                        final int retryTime =  MAX_RETRY_TIMES - (int) ((millisUntilFinished / RETRY_INTERVAl));
                        printLog(mResources.getString(R.string.send_reply, retryTime + "") + msg);
                        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                            @Override
                            public void onSendCustomCommand(int errorCode, String roomID) {
                                printLog(mResources.getString(R.string.rsp_reply, retryTime + "") + errorCode);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mCurrentBoardSate == BoardState.Applying) {
                    if (callback != null) {
                        callback.onSendFail();
                    }
                }
            }
        }.start();
    }

    public void cancelApply(final OnCommandSendCallback callback){
        printLog("[CMDCenter_cancelApply], enter");

        if (mCurrentBoardSate != BoardState.WaitingBoard){
            printLog("[CMDCenter_cancelApply] error, state mismatch");
            return;
        }

        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_CANCEL_APPLY, mSessionID);
        Map<String, Object> mapData = getDataMap(System.currentTimeMillis());

        mapCMD.put(CMDKey.DATA, mapData);

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog(mResources.getString(R.string.send_cancel_reply, "1") + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog(mResources.getString(R.string.rsp_cancel_reply, "1") + errorCode);
            }
        });

        mCountDownTimerRetryHttpRequest = new CountDownTimer(MAX_RETRY_TIME, RETRY_INTERVAl) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCurrentBoardSate == BoardState.WaitingBoard) {
                    // 第一次回调的时间间隔 < 2000ms, 不处理
                    if ((MAX_RETRY_TIME - millisUntilFinished) > RETRY_INTERVAl){

                        final int retryTime =  MAX_RETRY_TIMES - (int) ((millisUntilFinished / RETRY_INTERVAl));
                        printLog(mResources.getString(R.string.send_cancel_reply, retryTime + "") + msg);
                        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                            @Override
                            public void onSendCustomCommand(int errorCode, String roomID) {
                                printLog(mResources.getString(R.string.rsp_cancel_reply, retryTime + "") + errorCode);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mCurrentBoardSate == BoardState.WaitingBoard) {
                    if (callback != null) {
                        callback.onSendFail();
                    }
                }
            }
        }.start();
    }

    /**
     * 通知服务器，客户端已经收到"准备游戏"的指令.
     */
    public void replyRecvGameReady(int rspSeq){
        printLog("[CMDCenter_replyRecvGameReady], currentState: " + mCurrentBoardSate);

        Map<String, Object> mapCMD = getCMDHeader(rspSeq, CMD_REPLY_RECV_GAME_READY, mSessionID);

        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog(mResources.getString(R.string.send_reply_recv_game_ready) + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog(mResources.getString(R.string.rsp_reply_recv_game_ready) + errorCode);
            }
        });
    }


    /**
     * 通知服务器，客户端"确认/取消"上机.
     *
     * @param confirmBoard true: 上机，false: 不上机
     */
    public void confirmBoard(boolean confirmBoard, String encryptedConfig, long timeStamp, final OnCommandSendCallback callback) {
        printLog("[CMDCenter_confirmBoard], confirmBoard: " + confirmBoard + ", currentState: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.WaitingBoard) {
            printLog("[CMDCenter_confirmBoard] error, state mismatch");
            return;
        }

        // 取消之前的定时器
        if (mCountDownTimerRetryHttpRequest != null){
            mCountDownTimerRetryHttpRequest.cancel();
        }

        mConfirmBoard = confirmBoard;

        if (mConfirmBoard){
            setCurrentBoardSate(BoardState.ConfirmBoard);
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_CONFIRM_BOARD, mSessionID);

        Map<String, Object> mapData = getDataMap(timeStamp);
        mapData.put(CMDKey.CONFIRM, (mConfirmBoard ? YES : NO));

        if (mConfirmBoard){
            mapData.put(CMDKey.CONFIG, encryptedConfig);
        }

        mapCMD.put(CMDKey.DATA, mapData);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        final String msg = gson.toJson(mapCMD);

        printLog(mResources.getString(R.string.send_confirm_board, "1") + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog(mResources.getString(R.string.rsp_confirm_board, "1") + errorCode);
            }
        });

        mCountDownTimerRetryHttpRequest = new CountDownTimer(MAX_RETRY_TIME, RETRY_INTERVAl) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCurrentBoardSate == BoardState.ConfirmBoard) {
                    // 第一次回调，时间间隔 < 2000ms, 不处理
                    if ((MAX_RETRY_TIME - millisUntilFinished) > RETRY_INTERVAl){

                        final int retryTime = MAX_RETRY_TIMES - (int) ((millisUntilFinished / RETRY_INTERVAl));
                        printLog(mResources.getString(R.string.send_confirm_board, retryTime + "") + msg);
                        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                            @Override
                            public void onSendCustomCommand(int errorCode, String roomID) {
                                printLog(mResources.getString(R.string.rsp_confirm_board, retryTime + "") + errorCode);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mCurrentBoardSate == BoardState.ConfirmBoard) {
                    if (callback != null) {
                        callback.onSendFail();
                    }
                }
            }
        }.start();
    }

    public void moveLeft() {
        printLog("[CMDCenter_moveLeft], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_moveLeft] error, state mismatch");
            return;
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_MOVE_LEFT, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog("sendCustomCommand_moveLeft, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("onSendCustomCommand_moveLeft, errorCode:" + errorCode);
            }
        });

        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mCountDownTimerMove = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                printLog("sendCustomCommand_moveLeft, msg: " + msg);
                mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                    @Override
                    public void onSendCustomCommand(int errorCode, String roomID) {
                        printLog("onSendCustomCommand_moveLeft, errorCode:" + errorCode);
                    }
                });
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void moveRight() {
        printLog("[CMDCenter_moveRight], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_moveRight], state mismatch");
            return;
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_MOVE_RIGHT, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog("sendCustomCommand_moveRight, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("onSendCustomCommand_moveRight, errorCode:" + errorCode);
            }
        });

        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mCountDownTimerMove = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                printLog("sendCustomCommand_moveRight, msg: " + msg);
                mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                    @Override
                    public void onSendCustomCommand(int errorCode, String roomID) {
                        printLog("onSendCustomCommand_moveRight, errorCode:" + errorCode);
                    }
                });
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void moveForward() {
        printLog("[CMDCenter_moveForward], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_moveForward], state mismatch");
            return;
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_MOVE_FORWARD, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog("sendCustomCommand_moveForward, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("onSendCustomCommand_moveForward, errorCode:" + errorCode);
            }
        });

        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mCountDownTimerMove = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                printLog("sendCustomCommand_moveForward, msg: " + msg);
                mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                    @Override
                    public void onSendCustomCommand(int errorCode, String roomID) {
                        printLog("onSendCustomCommand_moveForward, errorCode:" + errorCode);
                    }
                });

            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void moveBackward() {
        printLog("[CMDCenter_moveBackward], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_moveBackward], state mismatch");
            return;
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_MOVE_BACKWARD, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog("sendCustomCommand_moveBackward, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("onSendCustomCommand_moveBackward, errorCode:" + errorCode);
            }
        });

        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }
        mCountDownTimerMove = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                printLog("sendCustomCommand_moveBackward, msg: " + msg);
                mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
                    @Override
                    public void onSendCustomCommand(int errorCode, String roomID) {
                        printLog("onSendCustomCommand_moveBackward, errorCode:" + errorCode);
                    }
                });
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void grub() {
        printLog("[CMDCenter_grub], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_grub], state mismatch");
            return;
        }

        setCurrentBoardSate(BoardState.WaitingGameResult);

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_GRUB, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog(mResources.getString(R.string.send_grub, "1") + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog(mResources.getString(R.string.rsp_grub, "1") + errorCode);
            }
        });
    }

    public void stopMove(){
        printLog("[CMDCenter_stopMove], currentSate: " + mCurrentBoardSate);

        if (mCurrentBoardSate != BoardState.Boarding) {
            printLog("[CMDCenter_stopMove], state mismatch");
            return;
        }

        if (mCountDownTimerMove != null){
            mCountDownTimerMove.cancel();
        }

        Map<String, Object> mapCMD = getCMDHeader(getSeq(), CMD_STOP_MOVE, mSessionID);
        mapCMD.put(CMDKey.DATA, getDataMap(System.currentTimeMillis()));

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog( "sendCustomCommand_stopMove, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog( "onSendCustomCommand_stopMove, errorCode: " + errorCode);
            }
        });
    }

    public void confirmGameResult(int rspSeq, boolean needContinue) {
        printLog("[CMDCenter_confirmGameResult], needContinue: " + needContinue + ", currentSate: " + mCurrentBoardSate);

        Map<String, Object> mapCMD = getCMDHeader(rspSeq, CMD_CONFIRM_GAME_RESULT, mSessionID);

        Map<String, Object> mapData = getDataMap(System.currentTimeMillis());
        mapData.put(CMDKey.CONTINUE, (needContinue ? YES : NO));

        mapCMD.put(CMDKey.DATA, mapData);

        Gson gson = new Gson();
        final String msg = gson.toJson(mapCMD);

        printLog("sendCustomCommand_confirmGameResult, msg: " + msg);
        mZegoLiveRoom.sendCustomCommand(getUserListToSendCMD(), msg, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int errorCode, String roomID) {
                printLog("onSendCustomCommand_confirmGameResult, errorCode:" + errorCode);
            }
        });
    }

    public interface OnCommandSendCallback {
        void onSendFail();
    }
}
