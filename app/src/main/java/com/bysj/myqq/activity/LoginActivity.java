package com.bysj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bysj.myqq.Bean.User;
import com.bysj.myqq.R;
import com.bysj.myqq.utils.MyLoader;
import com.bysj.myqq.utils.SharedPreferenceUtil;
import com.bysj.myqq.utils.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_login_forget_psd)
    TextView tvLoginForgetPsd;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    private String TAG = "LOGIN";
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                MyLoader.stopLoading();
                String str1 = msg.getData().getString("desc");//接受msg传递过来的参数
                ToastUtils.showError(LoginActivity.this, str1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etLoginPhone.setText(SharedPreferenceUtil.instance(this).getString("NAME"));
        etLoginPsw.setText(SharedPreferenceUtil.instance(this).getString("PSD"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        etLoginPhone.setText(SharedPreferenceUtil.instance(this).getString("NAME"));
        etLoginPsw.setText(SharedPreferenceUtil.instance(this).getString("PSD"));
    }

    @OnClick({R.id.btn_login, R.id.tv_login_forget_psd, R.id.tv_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.tv_login_forget_psd:
                ToastUtils.showInfo(this, "敬请期待");
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(this, RegistActivity.class));
                break;
        }
    }

    private void Login() {
        MyLoader.showLoading(this);
        if (TextUtils.isEmpty(etLoginPhone.getText().toString()) || TextUtils.isEmpty(etLoginPsw.getText().toString())) {
            ToastUtils.showUsual(this, "请填写完整信息");
            MyLoader.stopLoading();
            return;
        }
        SharedPreferenceUtil.instance(this).saveString("NAME", etLoginPhone.getText().toString());
        SharedPreferenceUtil.instance(this).saveString("PSD", etLoginPsw.getText().toString());
        User user = new User();
        user.setUsername(etLoginPhone.getText().toString());
        user.setPassword(etLoginPsw.getText().toString());
        user.login(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    SharedPreferenceUtil.instance(LoginActivity.this).saveString("PHONE", s.getMobilePhoneNumber());
                    HxLogin(s.getMobilePhoneNumber(), etLoginPsw.getText().toString());
                } else {
                    ToastUtils.showError(LoginActivity.this, "帐号或者密码错误");
                    MyLoader.stopLoading();
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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
