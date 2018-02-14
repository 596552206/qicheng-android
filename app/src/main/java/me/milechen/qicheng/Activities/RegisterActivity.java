package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.Beans.UserBean;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Services.PushService;
import me.milechen.qicheng.Services.QichengIntentService;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.UserNetUtil;
import me.milechen.qicheng.Utils.User.LoginUtil;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private EditText nick;
    private EditText phone;
    private EditText password;
    private Button submit;
    private TextView login;
    private TextView about;

    private UserNetUtil userNetUtil = new UserNetUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.welcome = (TextView) findViewById(R.id.register_welcome_tv_tv);
        this.nick = (EditText) findViewById(R.id.register_nick_et);
        this.phone = (EditText) findViewById(R.id.register_phone_et);
        this.password = (EditText) findViewById(R.id.register_password_et);
        this.submit = (Button) findViewById(R.id.register_submit_btn);
        this.login = (TextView) findViewById(R.id.register_login_tv);
        this.about = (TextView) findViewById(R.id.register_about_tv);

        login.setOnClickListener(this);
        nick.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nick = ((TextView) view).getText().toString();
                if (nick.length() != 0) {
                    welcome.setText("欢迎 " + nick + " 小朋友入坑");
                } else {
                    welcome.setText("欢迎小朋友入坑");
                }
            }
        });
        submit.setOnClickListener(this);


    }

    public void doRegister() {
        String phone = this.phone.getText().toString();
        String password = this.password.getText().toString();
        String nick = this.nick.getText().toString();
        if (password.length() == 0 || phone.length() == 0 || nick.length() == 0) {
            Toast.makeText(this, "请输入手机号、用户名及密码", Toast.LENGTH_LONG).show();
        } else if (!phone.substring(0, 1).equals("1") || phone.length() != 11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "密码至少需要六位", Toast.LENGTH_LONG).show();
        } else {
            userNetUtil.register(phone, nick, password, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                        @Override
                        public void onOK(int status, String detail, Object data) {
                            Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onErr(int status, String detail) {
                            Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void goLogin() {
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_submit_btn:
                doRegister();
                break;
            case R.id.register_login_tv:
                goLogin();
                break;
            default:

                break;
        }
    }
}
