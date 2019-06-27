package com.example.umengasmdemo;

import android.app.Application;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by zjy on 2019-05-13
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        MobclickAgent.setSecret(this, "5b867ea1f43e48532100031d");
        UMConfigure.setLogEnabled(true);
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(false);
    }
}
