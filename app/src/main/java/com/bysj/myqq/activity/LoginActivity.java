package com.bysj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bysj.myqq.Bean.User;
import com.bysj.myqq.R;
import com.bysj.myqq.utils.SharedPreferenceUtil;
import com.bysj.myqq.utils.ToastUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etLoginPhone.setText( SharedPreferenceUtil.instance(this).getString("NAME"));
        etLoginPsw.setText( SharedPreferenceUtil.instance(this).getString("PSD"));
    }

    @OnClick({R.id.btn_login, R.id.tv_login_forget_psd, R.id.tv_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.tv_login_forget_psd:
                ToastUtils.showInfo(this,"敬请期待");
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(this,RegistActivity.class));
                break;
        }
    }

    private void Login() {
        if (TextUtils.isEmpty(etLoginPhone.getText().toString())||TextUtils.isEmpty(etLoginPsw.getText().toString())){
            ToastUtils.showUsual(this,"请填写完整信息");
            return;
        }
        SharedPreferenceUtil.instance(this).saveString("NAME",etLoginPhone.getText().toString());
        SharedPreferenceUtil.instance(this).saveString("PSD",etLoginPsw.getText().toString());
        User user = new User();
        user.setUsername(etLoginPhone.getText().toString());
        user.setPassword(etLoginPsw.getText().toString());
        user.login(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                        ToastUtils.showError(LoginActivity.this, "失败：" + e.getMessage() + e.getErrorCode());
                    }
            }
        });


    }
}
