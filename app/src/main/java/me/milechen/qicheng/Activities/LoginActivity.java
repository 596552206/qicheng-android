package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.igexin.sdk.PushManager;
import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.Beans.UserBean;
import me.milechen.qicheng.Services.PushService;
import me.milechen.qicheng.Services.QichengIntentService;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.UserNetUtil;
import me.milechen.qicheng.Utils.User.LoginUtil;
import me.milechen.qicheng.R;
import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public SimpleDraweeView avatar;
    public TextView nick;
    public EditText phone;
    public EditText password;
    public Button submit;
    public TextView forget;
    public TextView register;

    private UserNetUtil userNetUtil = new UserNetUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode().setDuration(800));
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        boolean show = intent.getBooleanExtra("show",false);
        if(show) {
            Toast.makeText(this,"登陆过期，请重新登陆", Toast.LENGTH_SHORT).show();
        }

        phone = (EditText) findViewById(R.id.login_phone_et);
        password = (EditText) findViewById(R.id.login_password_et);
        submit = (Button) findViewById(R.id.login_submit_btn);
        forget = (TextView) findViewById(R.id.login_forget_tv);
        register = (TextView) findViewById(R.id.login_register_tv);
        avatar = (SimpleDraweeView) findViewById(R.id.login_avatar_sdv);
        nick = (TextView) findViewById(R.id.login_nick_tv);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 11){
                    fetchAvatar(editable.toString());
                    fetchNick(editable.toString());
                }
            }
        });

        submit.setOnClickListener(this);

    }

    private void fetchAvatar(String phone){
        userNetUtil.fetchUserAvatarByPhone(phone, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator<Object>() {
                    @Override
                    public void onOK(int status, String detail, Object data) {
                        Log.i("ii","succeed in fetching user avatar"+detail);
                        avatar.setImageURI(Uri.parse(detail));

                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Log.i("ii","fail in fetching user avatar"+detail);
                    }
                });
            }
        });
    }

    private void fetchNick(String phone){
        userNetUtil.fetchUserNickByPhone(phone, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator<Object>() {
                    @Override
                    public void onOK(int status, String detail, Object data) {
                        Log.i("ii","succeed in fetching user nick"+detail);
                        nick.setText("欢迎回来，"+detail+"！");

                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Log.i("ii","fail in fetching user nick"+detail);
                    }
                });
            }
        });
    }

    public void doLogin(){
        String phone = this.phone.getText().toString();
        String password = this.password.getText().toString();
        if(password == null || phone == null){
            Toast.makeText(this,"请输入手机号和密码",Toast.LENGTH_LONG).show();
        }else if (!phone.substring(0,1).equals("1")){
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
        }else{
            userNetUtil.login(phone, password, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithUserData(s).investigate(new ResponseInvestigator<UserBean>() {
                        @Override
                        public void onOK(int status, String detail, UserBean data) {
                            LoginUtil.saveUser(LoginActivity.this,data);

                            //初始化推送服务
                            PushManager.getInstance().initialize(getApplicationContext(), PushService.class);
                            PushManager.getInstance().registerPushIntentService(getApplicationContext(),QichengIntentService.class);

                            Toast.makeText(getApplicationContext(),detail,Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }

                        @Override
                        public void onErr(int status, String detail) {
                            Toast.makeText(getApplicationContext(),detail,Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_submit_btn:
                doLogin();
                break;
            default:

                break;
        }
    }
}
