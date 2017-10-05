package me.milechen.qicheng.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.Net.GroupNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import okhttp3.Call;
import okhttp3.Response;

public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private int user;
    private EditText groupId;
    private EditText password;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        title.setText("加入小组");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        groupId = (EditText) findViewById(R.id.search_acv_groupid_tv);
        password = (EditText) findViewById(R.id.search_acv_password_tv);
        submit = (Button) findViewById(R.id.search_acv_submit_btn);

        submit.setOnClickListener(this);

        this.user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_acv_submit_btn){
            if(groupId.getText().length()>0 && password.getText().length()==4){
                new GroupNetUtil().joinGroup(Integer.parseInt(groupId.getText().toString()), user, Integer.parseInt(password.getText().toString()), new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                            @Override
                            public void onOK(int status, String detail, String data) {
                                Toast.makeText(SearchGroupActivity.this,detail,Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onErr(int status, String detail) {
                                Toast.makeText(SearchGroupActivity.this,detail,Toast.LENGTH_LONG).show();
                                //finish();
                            }
                        });
                    }
                });
            }else{
                Toast.makeText(SearchGroupActivity.this,"请输入正确的信息",Toast.LENGTH_LONG).show();
            }
        }
    }
}
