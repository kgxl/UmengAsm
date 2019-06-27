package com.example.umengasmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.umengannotation.Lifecycle;
import com.example.umengannotation.Umeng;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Umeng(UmengKey = "aaa", UmengContent = "content", lifecycle = Lifecycle.BEFORE)
    private void Test() {
    }

    @Umeng(UmengKey = "222", UmengContent = "content", lifecycle = Lifecycle.BEFORE)
    private void Test2() {
    }
}