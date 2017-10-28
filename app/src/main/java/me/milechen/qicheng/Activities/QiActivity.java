package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.milechen.qicheng.Beans.TagBean;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TagNetUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import me.milechen.qicheng.Utils.TimeManager;
import okhttp3.Call;
import okhttp3.Response;


public class QiActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<TagBean> tagList;
    Toolbar toolbar;
    TextView qi,title;
    EditText editText;
    TagFlowLayout tagFlowLayout;
    private int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode().setDuration(800));
        getWindow().setExitTransition(new Explode().setDuration(800));
        getWindow().setReenterTransition(new Explode().setDuration(800));
        setContentView(R.layout.activity_qi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        qi = (TextView) findViewById(R.id.toolbar_right_tv);
        title = (TextView) findViewById(R.id.toolbar_title_tv);
        editText = (EditText) findViewById(R.id.qi_content_et);

        user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);

        qi.setText(R.string.qi);
        title.setText(R.string.new_tale);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QiActivity.this.finishAfterTransition();
            }
        });

        qi.setOnClickListener(this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });

        getTags();





    }

    public void flowLayoutAct(){

        tagFlowLayout = (TagFlowLayout) findViewById(R.id.qi_tag_tfv);

        final LayoutInflater inflater = LayoutInflater.from(QiActivity.this);
        tagFlowLayout.setAdapter(new TagAdapter<TagBean>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean tag) {
                Log.i("ii",tag.getName());
                TextView tv = (TextView) inflater.inflate(R.layout.item_tag,tagFlowLayout,false);
                tv.setText(tag.getName());
                return tv;
            }
        });


        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                //Toast.makeText(QiActivity.this,"select:"+selectPosSet,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTags(){
        TagNetUtil tagNetUtil = new TagNetUtil();
        tagNetUtil.fetchTags(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithTagBeanData(s).investigate(new ResponseInvestigator<List<TagBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<TagBean> data) {
                        tagList = (ArrayList<TagBean>) data;
                        flowLayoutAct();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(QiActivity.this,"网络错误",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_right_tv :
                Toast.makeText(QiActivity.this,"发布中 ^ - ^",Toast.LENGTH_SHORT).show();
                upload();
            break;
        }
    }

    private void upload(){
        String paraOne = editText.getText().toString();
        ArrayList<Integer> tagsIdList = new ArrayList<>();
        Set<Integer> selectTagsPosSet = tagFlowLayout.getSelectedList();
        ArrayList<Integer> tagsPosList = new ArrayList<>(selectTagsPosSet);
        for (Integer pos : tagsPosList) {
            tagsIdList.add(tagList.get(pos).getId());
        }
        TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.newTale(user, tagsIdList, TimeManager.getInstance().getServiceTime(), paraOne, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                QiActivity.this.setResult(1,new Intent().putExtra("message","成功发起新故事 ↖(^ω^)↗"));
                QiActivity.this.finishAfterTransition();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                QiActivity.this.setResult(1,new Intent().putExtra("message","出了点问题哦 ≡(▔﹏▔)≡"));
                QiActivity.this.finishAfterTransition();
            }
        });

    }
}
