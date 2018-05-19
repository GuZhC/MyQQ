package com.bysj.myqq.Bean;

import com.bysj.myqq.activity.RegistActivity;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author GuZhC
 * @create 2018/5/19
 * @Describe
 */
public class User extends BmobUser {
    String wyToken;
    String workNumber;
    String sex;
    String school;
    boolean isTeacher;
    BmobFile headimage;

    public BmobFile getHeadimage() {
        return headimage;
    }

    public void setHeadimage(BmobFile headimage) {
        this.headimage = headimage;
    }

    public String getWyToken() {
        return wyToken;
    }

    public void setWyToken(String wyToken) {
        this.wyToken = wyToken;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }


}
