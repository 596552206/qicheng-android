package me.milechen.qicheng.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.GActivity;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.GParasAdapter;
import me.milechen.qicheng.Adapters.ParasAdapter;
import me.milechen.qicheng.Beans.GParaBean;
import me.milechen.qicheng.Beans.ParaBean;
import me.milechen.qicheng.Fragments.DiscussFragment;
import me.milechen.qicheng.QichengApplication;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;
import me.milechen.qicheng.Utils.Net.GroupNetUtil;
import me.milechen.qicheng.Utils.Net.ParaNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import okhttp3.Call;
import okhttp3.Response;



public class GraleActivity extends AppCompatActivity implements GParasAdapter.OnGroupParaClickListener {

    private int groupId;
    private String groupName;
    private int user;
    private ArrayList<GParaBean> paras = new ArrayList<>();
    private GParasAdapter parasAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ParaNetUtil paraNetUtil = new ParaNetUtil();
    private int status;
    private GroupNetUtil groupNetUtil = new GroupNetUtil();

    private boolean isLoadingMore = false;
    private boolean isNoMore = false;


    private Toolbar toolbar;
    private TextView title;
    private TextView tucao;
    public DiscussFragment discussFragment;
    private RecyclerView recyclerView;
    private LinearLayout hintBar;
    private TextView groupIdTV;
    private TextView groupInfoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grale);
        Intent receivedIntent = getIntent();
        this.groupId = receivedIntent.getIntExtra("groupId",-1);
        this.groupName = receivedIntent.getStringExtra("groupName");

        this.user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);

        //Toast.makeText(this,bundle.get("id")+"",Toast.LENGTH_LONG).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tucao = (TextView) findViewById(R.id.toolbar_right_tv);
        recyclerView = (RecyclerView) findViewById(R.id.grale_paras_rv);
        hintBar = (LinearLayout) findViewById(R.id.grale_hintbar_l);
        groupIdTV = (TextView) findViewById(R.id.grale_id_tv);
        groupInfoTV = (TextView) findViewById(R.id.grale_info_tv);
        title = (TextView) findViewById(R.id.toolbar_title_tv);


        tucao.setText(R.string.discuss);
        title.setText(R.string.group);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        parasAdapter = new GParasAdapter(paras);
        parasAdapter.setOnGroupParaClickListener(this);
        recyclerView.setAdapter(parasAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition ==parasAdapter.getItemCount()-1){
                    if(isLoadingMore){
                    }
                    if(!isLoadingMore){
                        fetchParas(paras.get(paras.size()-1).getParanum(),3);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        showBar();
        //fetchParas(0,10);

        ((QichengApplication)getApplication()).handlerForGraleActivity = this.graleHandler;

        discussFragment = new DiscussFragment();
        Bundle bundleForFg = new Bundle();
        bundleForFg.putInt("module", ModuleConst.M_GROUP);
        bundleForFg.putInt("moduleId",groupId);
        discussFragment.setBundle(bundleForFg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.grale_discuss_fg,discussFragment);
        fragmentTransaction.commit();
    }

    private void setActive(){
        groupNetUtil.setActive(user,groupId);
    }

    private void unsetActive(){
        groupNetUtil.unsetActive(user,groupId);
    }

    public void showBar(){
        groupIdTV.setText(groupName);
        groupInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"更多信息",Toast.LENGTH_LONG).show();
                Intent intent2Grale = new Intent();
                intent2Grale.putExtra("groupId",groupId);
                intent2Grale.setClass(GraleActivity.this,InfoGroupActivity.class);
                startActivity(intent2Grale);
            }
        });
    }

    public void fetchParas(final int paraNumAfter, int limit) {
        paraNetUtil.fetchGPara(groupId, paraNumAfter, limit, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithGroupParaBeanListData(s).investigate(new ResponseInvestigator<List<GParaBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<GParaBean> data) {
                        paras.addAll(data);
                        parasAdapter.notifyDataSetChanged();
                        if (paraNumAfter != 0) recyclerView.smoothScrollToPosition(paraNumAfter-1);
                        isLoadingMore = false;
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        isNoMore = true;
                        isLoadingMore = false;
                    }
                });
            }
        });
    }

    @Override
    public void onGroupParaLongClick(int pos, int paraNum) {
        doReview(paraNum);
    }

    private void doReview(int paraNum){
        Log.i("ii","review");
        discussFragment.atPara(paraNum);
        tucao.callOnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActive();
        freshStatus();
        Log.i("ii","active");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetActive();
        Log.i("ii","not active");
    }


    private void freshStatus(){
        Log.i("ii","TaleActivity正在向服务器寻求状态推送");
        groupNetUtil.askPush(user,groupId);
    }

    public Handler graleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            status = msg.what;
            switch (status){
                case 550:
                    //readyTime();
                    Log.i("ii","GraleAct收到550(就绪)");
                    hint_Ready();
                    break;
                case 551:
                    //silentTime();
                    Log.i("ii","GraleAct收到551（静默）");
                    hint_silent();
                    break;
                case 555:
                    //fresh reviews();
                    Log.i("ii","GraleAct收到555（刷新评论）");
                    fresh_reviews();
                    break;
            }
        }
    };

    private void hint_Ready(){
        hintBar.removeAllViews();
        final TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.light));
        tv.setTextSize(20);
        tv.setText("就绪。");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight=2;
        tv.setLayoutParams(params);
        hintBar.addView(tv);

        final Button btn = new Button(this);
        btn.setText("承");
        btn.setTextColor(getResources().getColor(R.color.light));
        btn.setBackgroundColor(getResources().getColor(R.color.migold));
        btn.setGravity(Gravity.CENTER);
        //btn.setBackgroundResource(R.drawable.round_btn_bg_light_line_migold_fill);
        btn.setTextSize(22);
        btn.setPadding(0,0,0,0);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.weight = 1;
        btn.setLayoutParams(params2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setClass(GraleActivity.this,ChengGroupActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("para",(!paras.isEmpty())?(paras.get(paras.size()-1).getParanum()+1):(1));
                bundle1.putInt("group",groupId);
                intent1.putExtras(bundle1);
                GraleActivity.this.startActivityForResult(intent1,2, ActivityOptions.makeSceneTransitionAnimation(GraleActivity.this).toBundle());
            }
        });

        hintBar.addView(btn);

        fetchParas((paras.size()==0)?0:(paras.get(paras.size()-1).getParanum()),10);
    }

    private void hint_silent(){
        hintBar.removeAllViews();
        final TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.light));
        tv.setTextSize(20);
        tv.setText("静默。");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        hintBar.addView(tv);
    }

    private void fresh_reviews(){
        discussFragment.fetchSpeechesNew();
        tucao.setBackground(getDrawable(R.drawable.redspot));
        graleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tucao.setBackgroundColor(getResources().getColor(R.color.none));
            }
        },10000);
    }
}
