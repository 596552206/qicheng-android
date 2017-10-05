package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.igexin.sdk.PushManager;

import me.milechen.qicheng.Services.PushService;
import me.milechen.qicheng.Services.QichengIntentService;
import me.milechen.qicheng.Utils.User.LoginUtil;
import me.milechen.qicheng.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        switch (LoginUtil.isLogin(this)){
            case 0 :
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToLogin(false);
                    }
                },1500);
                break;
            case 1 :
                LoginUtil.renewTime(this);

                //初始化推送服务
                PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
                PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),QichengIntentService.class);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToMain();
                    }
                },1500);
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToLogin(false);
                    }
                },1500);
                break;
        }


    }



    private void goToMain(){
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        SplashActivity.this.finish();
    }

    private void goToLogin(boolean showMessage){
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
        intent.putExtra("show",showMessage);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
