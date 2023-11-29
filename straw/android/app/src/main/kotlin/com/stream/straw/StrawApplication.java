package com.stream.straw;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bytedance.sdk.openadsdk.LocationProvider;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.live.ILiveAdCustomConfig;



@SuppressWarnings("unused")
public class StrawApplication extends Application {

    public static String PROCESS_NAME_STRAW = "com.stream.straw";
    public static final String TAG = "StrawApp";
    private static Context context;

    private TTCustomController mCustomController = new TTCustomController() {

        @Override
        public boolean isCanUseLocation() {
            return super.isCanUseLocation();
        }

        @Override
        public LocationProvider getTTLocation() {
            return super.getTTLocation();
        }

        @Override
        public boolean alist() {
            return super.alist();
        }

        @Override
        public boolean isCanUsePhoneState() {
            return super.isCanUsePhoneState();
        }

        @Override
        public String getDevImei() {
            return super.getDevImei();
        }

        @Override
        public boolean isCanUseWifiState() {
            return super.isCanUseWifiState();
        }

        @Override
        public String getMacAddress() {
            return super.getMacAddress();
        }

        @Override
        public boolean isCanUseWriteExternal() {
            return super.isCanUseWriteExternal();
        }

        @Override
        public String getDevOaid() {
            return super.getDevOaid();
        }

        @Override
        public boolean isCanUseAndroidId() {
            return super.isCanUseAndroidId();
        }

        @Override
        public String getAndroidId() {
            return super.getAndroidId();
        }

        @Override
        public boolean isCanUsePermissionRecordAudio() {
            return super.isCanUsePermissionRecordAudio();
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TTAdSdk.init(context,
                new TTAdConfig.Builder()
                        .appId("5449682")//xxxxxxx为穿山甲媒体平台注册的应用ID
                        .useTextureView(true) //默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName("刷赚宝")
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)//落地页主题
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示,若设置为false则会导致通知栏不显示下载进度，存在违规风险，请勿随意更改
                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G, TTAdConstant.NETWORK_STATE_4G, TTAdConstant.NETWORK_STATE_5G) //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
                        .supportMultiProcess(false) //是否支持多进程，true支持
                        .customController(mCustomController)
                        .build());

        TTAdSdk.start(new TTAdSdk.Callback() {
            @Override
            public void success() {
                if (!(context instanceof Activity)) {
                    return;
                }
                Log.i(TAG, "success: " + TTAdSdk.isInitSuccess());
            }

            @Override
            public void fail(int code, String msg) {
                Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
            }
        });


    }

    public static Context getAppContext() {
        return StrawApplication.context;
    }


















































































































}
