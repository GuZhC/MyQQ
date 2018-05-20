package com.bysj.myqq;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import cn.bmob.v3.Bmob;

/**
 * @author GuZhC
 * @create 2018/5/19
 * @Describe
 */
public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Bmob.initialize(this, "c3e54e7b00d12b57b5f04eb128c53ebb");

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载


//初始化
        EMClient.getInstance().init(INSTANCE, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private static Myapp INSTANCE;

    public static synchronized Myapp getInstance() {
        return INSTANCE;
    }


}
