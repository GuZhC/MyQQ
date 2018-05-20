package com.bysj.myqq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bysj.myqq.R;
import com.bysj.myqq.activity.LoginActivity;
import com.bysj.myqq.activity.WelcomeActivity;
import com.bysj.myqq.base.BaseFragment;
import com.bysj.myqq.utils.SharedPreferenceUtil;
import com.bysj.myqq.utils.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MeFragment extends BaseFragment {


    @BindView(R.id.btn_me_out)
    Button btnMeOut;
    Unbinder unbinder;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_me_out)
    public void onViewClicked() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                SharedPreferenceUtil.instance(getContext()).saveString("NAME","");
                SharedPreferenceUtil.instance(getContext()).saveString("PSD","");
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                startActivity(new Intent(getContext(), LoginActivity.class));
                ToastUtils.showError(getContext(),message);
                getActivity().finish();
            }
        });

    }
}
