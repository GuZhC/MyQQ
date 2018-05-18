package com.bysj.myqq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bysj.myqq.R;
import com.bysj.myqq.base.BaseActivity;

public class RegistActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        setBackBtn();
        setTitle("注册");
    }
}
