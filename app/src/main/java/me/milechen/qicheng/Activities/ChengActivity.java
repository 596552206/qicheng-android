package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.Net.ParaNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import okhttp3.Call;
import okhttp3.Response;


public class ChengActivity extends AppCompatActivity {

    int tale;
    int user;
    int paraNum;
    Toolbar toolbar;
    TextView title;
    TextView cheng;
    EditText content;
    ParaNetUtil paraNetUtil = new ParaNetUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(new Explode().setDuration(800));
        getWindow().setExitTransition(new Explode().setDuration(800));
        getWindow().setReenterTransition(new Explode().setDuration(800));
        setContentView(R.layout.activity_cheng);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.tale = bundle.getInt("tale");
        this.paraNum = bundle.getInt("para");
        this.user = getSharedPreferences("user", MODE_PRIVATE).getInt("id", -1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cheng = (TextView) findViewById(R.id.toolbar_right_tv);
        title = (TextView) findViewById(R.id.toolbar_title_tv);
        content = (EditText) findViewById(R.id.cheng_content_et);

        cheng.setText(R.string.cheng);
        title.setText("续写第" + this.paraNum + "段");
        cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.getText().toString().length()>=1){
                    doCheng();
                }else{
                    Toast.makeText(ChengActivity.this,"至少写一个字吧...",Toast.LENGTH_SHORT).show();
                }
            }
        });
        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChengActivity.this.finishAfterTransition();
            }
        });

    }

    private void doCheng() {
        paraNetUtil.writePara(user, tale, content.getText().toString(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        goBack();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(ChengActivity.this,"出了点问题...",Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                });
            }
        });
    }

    private void goBack(){
        //setResult(2);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new TaleNetUtil().setSilent(tale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new TaleNetUtil().unsetSilent(tale);
    }
}