package com.bysj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bysj.myqq.Bean.User;
import com.bysj.myqq.R;
import com.bysj.myqq.base.BaseActivity;
import com.bysj.myqq.utils.MyLoader;
import com.bysj.myqq.utils.SharedPreferenceUtil;
import com.bysj.myqq.utils.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.et_register_number)
    EditText etRegisterNumber;
    @BindView(R.id.et_register_school)
    EditText etRegisterSchool;
    @BindView(R.id.et_register_worknum)
    EditText etRegisterWorknum;
    @BindView(R.id.et_register_psd)
    EditText etRegisterPsd;
    @BindView(R.id.et_register_psdtwo)
    EditText etRegisterPsdtwo;
    @BindView(R.id.cb_register_isteacher)
    CheckBox cbRegisterIsteacher;
    @BindView(R.id.cb_register_agree)
    CheckBox cbRegisterAgree;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private String sex = "男";
    private boolean isTeacher = false;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                doRegister();
            } else {
                MyLoader.stopLoading();
                String str1 = msg.getData().getString("desc");//接受msg传递过来的参数
                ToastUtils.showError(RegistActivity.this, str1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        setBackBtn();
        setTitle("注册");
    }

    @OnClick({R.id.cb_register_isteacher, R.id.cb_register_agree, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_register_isteacher:
                isTeacher = cbRegisterAgree.isChecked() ? false : true;
                break;
            case R.id.cb_register_agree:
                sex = cbRegisterAgree.isChecked() ? "女" : "男";

                break;
            case R.id.btn_register:
                if (TextUtils.isEmpty(etRegisterName.getText().toString()) || TextUtils.isEmpty(etRegisterNumber.getText().toString()) ||
                        TextUtils.isEmpty(etRegisterSchool.getText().toString()) || TextUtils.isEmpty(etRegisterWorknum.getText().toString()) ||
                        TextUtils.isEmpty(etRegisterPsd.getText().toString()) || TextUtils.isEmpty(etRegisterPsdtwo.getText().toString())) {
                    ToastUtils.showWarning(this, "请填写完整信息");
                } else {
                    if (etRegisterPsd.getText().toString().equals(etRegisterPsdtwo.getText().toString())) {
                        try {
                            registerHx(etRegisterNumber.getText().toString().trim(), etRegisterPsd.getText().toString().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(2);
                        }
                    } else
                        ToastUtils.showWarning(this, "两次密码不一致");
                }
                break;
        }
    }

    private void registerHx(final String name, final String pwd) {
        // 执行请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(name, pwd);//同步方法
                    handler.sendEmptyMessage(1);
                } catch (HyphenateException e) {
                    Message msg = new Message();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    if (!TextUtils.isEmpty(e.getDescription())) {
                        bundle.putString("desc", e.getDescription());  //往Bundle中存放数据
                    } else {
                        bundle.putString("desc", "更换帐号稍后重试");  //往Bundle中存放数据
                    }
                    msg.setData(bundle);//mes利用Bundle传递数据
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    private void doRegister() {
        User user = new User();
        user.setUsername(etRegisterName.getText().toString());
        user.setMobilePhoneNumber(etRegisterNumber.getText().toString());
        user.setSchool(etRegisterSchool.getText().toString());
        user.setWorkNumber(etRegisterWorknum.getText().toString());
        user.setPassword(etRegisterPsd.getText().toString());
        user.setSex(sex);
        user.setTeacher(isTeacher);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    try {
                        MyLoader.stopLoading();
                        SharedPreferenceUtil.instance(RegistActivity.this).saveString("NAME", etRegisterName.getText().toString().trim());
                        SharedPreferenceUtil.instance(RegistActivity.this).saveString("PSD", etRegisterPsd.getText().toString());
                        ToastUtils.showSuccess(RegistActivity.this, "注册成功");
                        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e1) {
                        MyLoader.stopLoading();
                        e1.printStackTrace();
                        ToastUtils.showError(RegistActivity.this, "注册失败稍后再试");
                    }

                } else {
                    if (e.getErrorCode() == 209)
                        ToastUtils.showError(RegistActivity.this, "号码已经注册");
                    else if (e.getErrorCode() == 202)
                        ToastUtils.showError(RegistActivity.this, "用户名已经注册");
                    else {
                        Log.e(TAG, "done: " + e.getMessage() + e.getErrorCode() + "");
                        ToastUtils.showError(RegistActivity.this, "失败：" + e.getMessage() + e.getErrorCode());
                    }
                    MyLoader.stopLoading();
                }
            }
        });
    }

}
