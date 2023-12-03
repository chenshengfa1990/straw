package com.stream.straw.advertise_plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdLoadType;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.stream.straw.advertise_plugin.model.RewardBundleModel;
import com.stream.straw.advertise_plugin.util.TToast;

import java.lang.ref.WeakReference;

public class AdvertisePlayer {
    private static final String TAG = "AdvertisePlayer";
    private TTAdNative mTTAdNative;
    private FullScreenVideoAdLoadListener mFullScreenAdLoadListener;
    private RewardAdLoadListener rewardAdLoadListener;

    public static Context activityContext;
    private static class FullScreenVideoAdLoadListener implements TTAdNative.FullScreenVideoAdListener {

        private final Context context;

        private TTFullScreenVideoAd mAd;

        public FullScreenVideoAdLoadListener(Context context) {
            this.context = context;
        }

        @Override
        public void onError(int code, String message) {
            Log.e(TAG, "Callback --> onError: " + code + ", " + message);
            TToast.show(context, message);
        }

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
            Log.e(TAG, "Callback --> onFullScreenVideoAdLoad");
            TToast.show(context, "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
            handleAd(ad);
        }

        @Override

        public void onFullScreenVideoCached() {
            // 已废弃 请使用 onRewardVideoCached(TTRewardVideoAd ad) 方法
        }

        @Override

        public void onFullScreenVideoCached(TTFullScreenVideoAd ad) {
            Log.e(TAG, "Callback --> onFullScreenVideoCached");
            TToast.show(context, "FullVideoAd video cached");
            handleAd(ad);
        }

        /**
         * 处理广告对象
         */

        public void handleAd(TTFullScreenVideoAd ad) {
            if (mAd != null) {
                return;
            }
            mAd = ad;
            //【必须】广告展示时的生命周期监听

            mAd.setFullScreenVideoAdInteractionListener(new FullScreenVideoAdLifeListener(context));
            //【可选】监听下载状态
            mAd.setDownloadListener(new DownloadStatusListener());
            showAd(TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
        }

        /**
         * 触发展示广告
         */
        public void showAd(TTAdConstant.RitScenes ritScenes, String scenes) {
            if (mAd == null) {
                TToast.show(context, "当前广告未加载好，请先点击加载广告");
                return;
            }

            mAd.showFullScreenVideoAd((Activity) context, ritScenes, scenes);
            // 广告使用后应废弃
            mAd = null;
        }
    }

    private static class RewardAdLoadListener implements TTAdNative.RewardVideoAdListener {

        private final Activity mActivity;

        private TTRewardVideoAd mAd;

        public RewardAdLoadListener(Activity activity) {
            mActivity = activity;
        }

        /**
         * 广告加载过程中出错
         */
        @Override
        public void onError(int code, String message) {
            Log.e(TAG, "Callback --> onError: " + code + ", " + message);
            TToast.show(mActivity, message);
        }

        /**
         * 广告基础信息加载完成，此方法是回调后是广告可调用展示的最早时机
         *
         * @param ad 广告对象 在一次广告生命周期中onRewardVideoAdLoad与onRewardVideoCached回调中的ad是同一个对象
         */
        @Override

        public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
            Log.e(TAG, "Callback --> onRewardVideoAdLoad");

            TToast.show(mActivity, "rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
            handleAd(ad);
        }

        @Override

        public void onRewardVideoCached() {
            // 已废弃 请使用 onRewardVideoCached(TTRewardVideoAd ad) 方法
        }

        /**
         * 广告基础信息与素材缓存完成，此时调用广告展示流畅，是展示广告的最理想时机
         *
         * @param ad 广告对象 在一次广告生命周期中onRewardVideoAdLoad与onRewardVideoCached回调中的ad是同一个对象
         */
        @Override

        public void onRewardVideoCached(TTRewardVideoAd ad) {
            Log.e(TAG, "Callback --> onRewardVideoCached");

            TToast.show(mActivity, "rewardVideoAd cached 广告类型：" + getAdType(ad.getRewardVideoAdType()));
            handleAd(ad);
            showAd();
        }

        /**
         * 处理广告对象
         */

        public void handleAd(TTRewardVideoAd ad) {
            if (mAd != null) {
                return;
            }
            mAd = ad;
            //【必须】广告展示时的生命周期监听

            mAd.setRewardAdInteractionListener(new RewardAdLifeListener(mActivity));

            //【可选】监听下载状态
            mAd.setDownloadListener(new DownloadStatusListener());
        }

        /**
         * 触发展示广告
         */
        public void showAd() {
            if (mAd == null) {
                TToast.show(mActivity, "当前广告未加载好，请先点击加载广告");
                return;
            }

            mAd.showRewardVideoAd(mActivity);
            // 广告使用后应废弃
            mAd = null;
        }
    }

    private static String getAdType(int type) {
        switch (type) {

            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;

            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;

            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;

            case TTAdConstant.AD_TYPE_LIVE:
                return "直播流，type=" + type;
        }

        return "未知类型+type=" + type;
    }

    /**
     * 广告生命周期监听
     */
    private static class FullScreenVideoAdLifeListener implements TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {

        private final WeakReference<Context> mContextRef;

        public FullScreenVideoAdLifeListener(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        @Override

        public void onAdShow() {
            Log.d(TAG, "Callback --> FullVideoAd show");
            TToast.show(mContextRef.get(), "FullVideoAd show");
        }

        @Override

        public void onAdVideoBarClick() {
            Log.d(TAG, "Callback --> FullVideoAd bar click");
            TToast.show(mContextRef.get(), "FullVideoAd bar click");
        }

        @Override

        public void onAdClose() {
            Log.d(TAG, "Callback --> FullVideoAd close");
            TToast.show(mContextRef.get(), "FullVideoAd close");
        }

        @Override
        public void onVideoComplete() {
            Log.d(TAG, "Callback --> FullVideoAd complete");
            TToast.show(mContextRef.get(), "FullVideoAd complete");
        }

        @Override
        public void onSkippedVideo() {
            Log.d(TAG, "Callback --> FullVideoAd skipped");
            TToast.show(mContextRef.get(), "FullVideoAd skipped");
        }
    }

    private static class RewardAdLifeListener implements TTRewardVideoAd.RewardAdInteractionListener {

        private final WeakReference<Context> mContextRef;

        public RewardAdLifeListener(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        @Override

        public void onAdShow() {
            // 广告展示
            Log.d(TAG, "Callback --> rewardVideoAd show");
            TToast.show(mContextRef.get(), "rewardVideoAd show");
        }

        @Override

        public void onAdVideoBarClick() {
            // 广告中产生了点击行为
            Log.d(TAG, "Callback --> rewardVideoAd bar click");
            TToast.show(mContextRef.get(), "rewardVideoAd bar click");
        }

        @Override

        public void onAdClose() {
            // 广告整体关闭
            Log.d(TAG, "Callback --> rewardVideoAd close");
            TToast.show(mContextRef.get(), "rewardVideoAd close");
        }

        //视频播放完成回调
        @Override
        public void onVideoComplete() {
            // 广告素材播放完成，例如视频未跳过，完整的播放了
            Log.d(TAG, "Callback --> rewardVideoAd complete");
            TToast.show(mContextRef.get(), "rewardVideoAd complete");
        }

        @Override
        public void onVideoError() {
            // 广告素材展示时出错
            Log.e(TAG, "Callback --> rewardVideoAd error");
            TToast.show(mContextRef.get(), "rewardVideoAd error");
        }

        @Override

        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errorMsg) {
            // 已废弃 请使用 onRewardArrived(boolean isRewardValid, int rewardType, Bundle extraInfo)
        }

        @Override

        public void onRewardArrived(boolean isRewardValid, int rewardType, Bundle extraInfo) {
            // 用户的观看行为满足了奖励条件
            RewardBundleModel rewardBundleModel = new RewardBundleModel(extraInfo);
            Log.e(TAG, "Callback --> rewardVideoAd has onRewardArrived " +
                    "\n奖励是否有效：" + isRewardValid +
                    "\n奖励类型：" + rewardType +
                    "\n奖励名称：" + rewardBundleModel.getRewardName() +
                    "\n奖励数量：" + rewardBundleModel.getRewardAmount() +
                    "\n建议奖励百分比：" + rewardBundleModel.getRewardPropose());
            TToast.show(mContextRef.get(), "ad onRewardArrived valid:" + isRewardValid +
                    " type:" + rewardType + " errorCode:" + rewardBundleModel.getServerErrorCode());
            if (!isRewardValid) {
                Log.d(TAG, "发送奖励失败 code：" + rewardBundleModel.getServerErrorCode() +
                        "\n msg：" + rewardBundleModel.getServerErrorMsg());
                return;
            }


            if (rewardType == TTRewardVideoAd.REWARD_TYPE_DEFAULT) {
                Log.d(TAG, "普通奖励发放，name:" + rewardBundleModel.getRewardName() +
                        "\namount:" + rewardBundleModel.getRewardAmount());
            }
        }

        @Override
        public void onSkippedVideo() {
            // 用户在观看素材时点击了跳过
            Log.e(TAG, "Callback --> rewardVideoAd has onSkippedVideo");
            TToast.show(mContextRef.get(), "rewardVideoAd has onSkippedVideo");
        }
    }

    /**
     * 【可选】下载状态监听器
     */
    private static class DownloadStatusListener implements TTAppDownloadListener {

        @Override
        public void onIdle() {
        }

        @Override
        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onInstalled(String fileName, String appName) {
            Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
        }
    }

    void init() {
//        if (mTTAdNative == null) {
            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdSdk.getAdManager();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限
            ttAdManager.requestPermissionIfNecessary(activityContext);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            mTTAdNative = ttAdManager.createAdNative(activityContext);
//        }
    }

    /**
     * 加载广告
     */
    void loadAd(final String codeId) {
        //step5:创建广告请求参数AdSlot

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) // 广告代码位Id
                .setAdLoadType(TTAdLoadType.LOAD) // 本次广告用途：TTAdLoadType.LOAD实时；TTAdLoadType.PRELOAD预请求
                .build();
        //step6:注册广告加载生命周期监听，请求广告

        rewardAdLoadListener = new RewardAdLoadListener((Activity) activityContext);

//        mTTAdNative.loadFullScreenVideoAd(adSlot, mAdLoadListener);
        mTTAdNative.loadRewardVideoAd(adSlot, rewardAdLoadListener);
    }
}
