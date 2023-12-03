package com.stream.straw

import android.os.Bundle
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import com.stream.straw.advertise_plugin.AdvertisePlayer
import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {
    private val mCustomController: TTCustomController = object : TTCustomController() {
        override fun isCanUseLocation(): Boolean {
            return super.isCanUseLocation()
        }

        override fun getTTLocation(): LocationProvider {
            return super.getTTLocation()
        }

        override fun alist(): Boolean {
            return super.alist()
        }

        override fun isCanUsePhoneState(): Boolean {
            return super.isCanUsePhoneState()
        }

        override fun getDevImei(): String {
            return super.getDevImei()
        }

        override fun isCanUseWifiState(): Boolean {
            return super.isCanUseWifiState()
        }

        override fun getMacAddress(): String {
            return super.getMacAddress()
        }

        override fun isCanUseWriteExternal(): Boolean {
            return super.isCanUseWriteExternal()
        }

        override fun getDevOaid(): String {
            return super.getDevOaid()
        }

        override fun isCanUseAndroidId(): Boolean {
            return super.isCanUseAndroidId()
        }

        override fun getAndroidId(): String {
            return super.getAndroidId()
        }

        override fun isCanUsePermissionRecordAudio(): Boolean {
            return super.isCanUsePermissionRecordAudio()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdvertisePlayer.activityContext = this

        //初始化SDK
        TTAdSdk.init(
            this,
            TTAdConfig.Builder()
                .appId("5449682") //xxxxxxx为穿山甲媒体平台注册的应用ID
                .useTextureView(true) //默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
                .appName("刷赚宝")
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK) //落地页主题
                .allowShowNotify(true) //是否允许sdk展示通知栏提示,若设置为false则会导致通知栏不显示下载进度，存在违规风险，请勿随意更改
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(
                    TTAdConstant.NETWORK_STATE_WIFI,
                    TTAdConstant.NETWORK_STATE_3G,
                    TTAdConstant.NETWORK_STATE_4G,
                    TTAdConstant.NETWORK_STATE_5G
                ) //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
                .supportMultiProcess(false) //是否支持多进程，true支持
                .customController(mCustomController)
                .build()
        )

        //启动SDK
        TTAdSdk.start(object : TTAdSdk.Callback {
            override fun success() {
//                if (StrawApplication.context !is Activity) {
//                    return
//                }
                Log.i(StrawApplication.TAG, "success: " + TTAdSdk.isInitSuccess())
            }

            override fun fail(code: Int, msg: String) {
                Log.i(StrawApplication.TAG, "fail:  code = $code msg = $msg")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        AdvertisePlayer.activityContext = null
    }
}
