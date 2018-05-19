package com.bysj.myqq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bysj.myqq.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/4/12.
 */

public class SharedPreferenceUtil {

    public static SharedPreferences mSharedPreferences;
    private final String PREFERENCE_NAME = Constant.PREFERENCE_NAME;
    private Context mContext;

    public String hint = "";
    private static SharedPreferenceUtil sp;
    private final SharedPreferences.Editor editor;
    private String TAG = "SharedPreferenceUtil";

    public SharedPreferenceUtil(Context mContext) {
        super();
        sp = this;
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public synchronized static SharedPreferenceUtil instance(Context context) {
        if (sp == null) {
            sp = new SharedPreferenceUtil(context);
        }
        return sp;
    }

    /**
     * 得到String类型的key--value
     */
    public String getString(String value) {
        return mSharedPreferences.getString(value, null);
    }

    public void saveString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public void saveLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public long getLongValue(String key, long defValue) {
        long value = mSharedPreferences.getLong(key, defValue);
        return value;
    }

    /**
     * 取出数据（int)
     * * @param key
     *
     * @param defValue 默认值
     * @return
     */
    public int getIntValue(String key, int defValue) {
        int value = mSharedPreferences.getInt(key, defValue);
        return value;
    }

    /**
     * 存储数据(Int)
     * * @param key
     *
     * @param value
     */
    public void putIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * 存储List<String>
     *
     * @param key     List<String>对应的key
     * @param strList 对应需要存储的List<String>
     */
    public void putStrListValue(String key, List<String> strList) {
        if (null == strList) {
            return;
        }
        // 保存之前先清理已经存在的数据，保证数据的唯一性
        removeStrList(key);
        int size = strList.size();
        putIntValue(key + "size", size);
        for (int i = 0; i < size; i++) {
            saveString(key + i, strList.get(i));
        }
    }

    public void removeStrList(String key) {
        int size = getIntValue(key + "size", 0);
        if (0 == size) {
            return;
        }
        clearOne(key + "size");
        for (int i = 0; i < size; i++) {
            clearOne(key + i);
        }
    }


    /**
     * 取出List<String>
     *
     * @param key List<String> 对应的key
     * @return List<String>
     */
    public List<String> getStrListValue(String key) {
        List<String> strList = new ArrayList<String>();
        int size = getIntValue(key + "size", 0);
        //Log.d("sp", "" + size);
        for (int i = 0; i < size; i++) {
            strList.add(getString(key + i, ""));
        }
        return strList;
    }

    /**
     * @param key List<String>对应的key
     * @Description
     */
    public void removeStrListItem(String key) {
        int size = getIntValue(key + "size", 0);
        if (0 == size) {
            return;
        }
        List<String> strList = getStrListValue(key);
        // 待删除的List<String>数据暂存
        clearOne(key + 0);
        putIntValue(key + "size", size - 1);
        strList.remove(0);
        // 删除元素重新建立索引写入数据
        putStrListValue(key, strList);
    }

    /**
     * 保存对象
     */
    public void saveBean(String key, Object object) {
        String value = new Gson().toJson(object);
        editor.putString(key, value).commit();
    }

    /**
     *  读取对象
     */

    public Object getBean(String key, Class cls) {
        String value = mSharedPreferences.getString(key, null);
        try {
            if (value != null) {
                Object o = new Gson().fromJson(value, cls);
                return o;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 保存经纬度
     *
     * @param key
     * @param value
     */
    public void saveFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    /**
     * 保存经纬度
     *
     * @param key
     */
    @SuppressWarnings("null")
    public float getFloat(String key) {
        Float defValue = null;
        return mSharedPreferences.getFloat(key, defValue);
    }

    /**
     * 根据传入的key得到String类型的变量
     */
    public String getString(String key, String... defValue) {
        if (defValue != null && defValue.length > 0)
            return mSharedPreferences.getString(key, defValue[0]);
        else
            return mSharedPreferences.getString(key, "");
    }

    /**
     * 根据传入的key得到boolean类型的变量?
     */
    public boolean getBoolean(String key, boolean... defValue) {
        if (defValue.length > 0)
            return mSharedPreferences.getBoolean(hint + key, defValue[0]);
        else
            return mSharedPreferences.getBoolean(hint + key, true);
    }

    /**
     * 保存key--value
     */
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(hint + key, value).commit();
    }


    /**
     * 清除数据
     */
    public void clear() {
        editor.clear().commit();
    }


    public void clearOne(String name) {
        editor.remove(name).commit();
    }
}
