package com.bysj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.bysj.myqq.Bean.User;
import com.bysj.myqq.R;
import com.bysj.myqq.utils.MyLoader;
import com.bysj.myqq.utils.SharedPreferenceUtil;
import com.bysj.myqq.utils.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WelcomeActivity extends AppCompatActivity {
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                MyLoader.stopLoading();
//                String str1 = msg.getData().getString("desc");//接受msg传递过来的参数
                String str1 ="号码已被占用";//接受msg传递过来的参数
                ToastUtils.showError(WelcomeActivity.this, str1);
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String name = SharedPreferenceUtil.instance(WelcomeActivity.this).getString("NAME");
                String psd = SharedPreferenceUtil.instance(WelcomeActivity.this).getString("PSD");
                String phone = SharedPreferenceUtil.instance(WelcomeActivity.this).getString("PHONE");
                if (name =="" ||
                        psd=="" ||
                        phone=="") {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Login(name,psd,phone);
                }
            }
        }, 2000);
    }

    private void Login(String name, final String psd, final String phone) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(psd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    HxLogin(phone,psd);
                } else {
                    ToastUtils.showError(WelcomeActivity.this, "登录过期");
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void HxLogin(String account, String token) {
        EMClient.getInstance().login(account, token, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLoader.stopLoading();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                MyLoader.stopLoading();
                Message msg = new Message();
                msg.what = 2;
                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(message)) {
                    bundle.putString("desc", message);  //往Bundle中存放数据
                } else {
                    bundle.putString("desc", "更换帐号稍后重试");  //往Bundle中存放数据
                }
                msg.setData(bundle);//mes利用Bundle传递数据
                handler.sendMessage(msg);
            }
        });
    }
}
