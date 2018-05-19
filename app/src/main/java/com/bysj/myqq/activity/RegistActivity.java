package com.bysj.myqq.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bysj.myqq.Bean.User;
import com.bysj.myqq.R;
import com.bysj.myqq.base.BaseActivity;
import com.bysj.myqq.utils.ToastUtils;

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
                isTeacher = cbRegisterAgree.isChecked()? false :true ;
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
                    if (etRegisterPsd.getText().toString().equals(etRegisterPsdtwo.getText().toString()))
                        doRegister();
                    else
                        ToastUtils.showWarning(this, "两次密码不一致");
                }
                break;
        }
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
                    ToastUtils.showSuccess(RegistActivity.this, "注册成功");
                    finish();
                } else {
                    if (e.getErrorCode() == 209)
                        ToastUtils.showError(RegistActivity.this, "号码已经注册");
                    else if (e.getErrorCode() == 202)
                        ToastUtils.showError(RegistActivity.this, "用户名已经注册");
                    else{
                        Log.e(TAG, "done: "+e.getMessage() + e.getErrorCode()+"" );
                        ToastUtils.showError(RegistActivity.this, "失败：" + e.getMessage() + e.getErrorCode());
                    }
                }
            }
        });
    }
}
