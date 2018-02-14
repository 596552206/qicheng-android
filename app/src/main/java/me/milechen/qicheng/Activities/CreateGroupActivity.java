package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupName;
    private EditText password;
    private Button submit;
    private Button ramdomize;
    private int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        title.setText("创建小组");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        groupName = (EditText) findViewById(R.id.create_group_acv_group_name_et);
        password = (EditText) findViewById(R.id.create_group_acv_group_password_et);
        submit = (Button) findViewById(R.id.create_group_acv_submit_btn);
        ramdomize = (Button) findViewById(R.id.create_group_acv_group_password_randomize_btn);

        submit.setOnClickListener(this);
        ramdomize.setOnClickListener(this);

        this.user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);
    }

    private void createGroup(final String groupname, String password){
        new GroupNetUtil().createGroup(groupname, password, user, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        Toast.makeText(CreateGroupActivity.this,"创建成功，小组ID为:"+data,Toast.LENGTH_SHORT).show();
                        Intent intent2Grale = new Intent();
                        intent2Grale.putExtra("groupId",Integer.parseInt(data));
                        intent2Grale.putExtra("groupName",groupname);
                        intent2Grale.setClass(CreateGroupActivity.this,GraleActivity.class);
                        startActivity(intent2Grale);
                        CreateGroupActivity.this.finish();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(CreateGroupActivity.this,detail,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.create_group_acv_submit_btn){
            if(groupName.getText().length()==0||password.getText().length()==0){
                Toast.makeText(CreateGroupActivity.this,"请输入小组名和口令",Toast.LENGTH_LONG).show();
            }else{
                createGroup(groupName.getText().toString(),password.getText().toString());
            }
        }else if(view.getId() == R.id.create_group_acv_group_password_randomize_btn){
            int rand = (int) ((Math.round(Math.random()*10)*1000)+(Math.round(Math.random()*10)*100)+(Math.round(Math.random()*10)*10)+(Math.round(Math.random()*10)));
            password.setText(new DecimalFormat("0000").format(rand));
        }
    }
}
