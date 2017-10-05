package me.milechen.qicheng.Activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.GTServiceManager;
import com.igexin.sdk.PushManager;
import com.lzy.okhttputils.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.ParasAdapter;
import me.milechen.qicheng.Beans.ParaBean;
import me.milechen.qicheng.Beans.TagBean;
import me.milechen.qicheng.Beans.TaleBean;
import me.milechen.qicheng.Fragments.DiscussFragment;
import me.milechen.qicheng.QichengApplication;
import me.milechen.qicheng.Services.PushService;
import me.milechen.qicheng.Services.QichengIntentService;
import me.milechen.qicheng.Utils.ModuleConst;
import me.milechen.qicheng.Utils.Net.ParaNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import me.milechen.qicheng.Utils.TimeManager;
import me.milechen.qicheng.R;
import okhttp3.Call;
import okhttp3.Response;



public class TaleActivity extends AppCompatActivity implements ParasAdapter.MyItemLongClickListener, View.OnClickListener{

    private int id;
    private int pos;
    private int user;
    private TaleBean tale;
    private Toolbar toolbar;
    private TagFlowLayout tagFlowLayout;
    private TaleNetUtil taleNetUtil;
    private ParaNetUtil paraNetUtil;
    private Button focus;
    private TextView sponsor;
    private TextView time;
    private TextView zanAndPara;
    private RecyclerView recyclerView;
    private ParasAdapter parasAdapter;
    public ArrayList<ParaBean> paras = new ArrayList<>();
    private boolean isLoadingMore = false;
    //private CoordinatorLayout coor;
    private LinearLayout hintBar;
    private Dialog dialog;
    private View dialogView;
    private TextView zanDialog;
    private TextView voteDialog;
    private TextView reviewDialog;
    private int longClickedParaId;
    private int longClickedParaPos;
    private boolean isNoMore = false;
    private int status;
    private DiscussFragment discussFragment;
    private TextView tucao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        this.id = bundle.getInt("id");
        this.pos = bundle.getInt("pos");
        this.user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);

        //Toast.makeText(this,bundle.get("id")+"",Toast.LENGTH_LONG).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tucao = (TextView) findViewById(R.id.toolbar_right_tv);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);

        tucao.setText(R.string.discuss);
        title.setText(R.string.tale);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        taleNetUtil = new TaleNetUtil();
        paraNetUtil = new ParaNetUtil();
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.tale_tag_tfv);
        focus = (Button) findViewById(R.id.tale_focus_btn);
        sponsor = (TextView) findViewById(R.id.tale_sponsornick_tv);
        time = (TextView) findViewById(R.id.tale_time_tv);
        zanAndPara = (TextView) findViewById(R.id.tale_zanandpara_tv);
        recyclerView = (RecyclerView) findViewById(R.id.tale_paras_rv);
        //coor = (CoordinatorLayout) findViewById(R.id.tale_coor);
        hintBar = (LinearLayout) findViewById(R.id.tale_hintbar_l);

        dialog = new Dialog(this,R.style.BottomDialog);
        dialogView = LayoutInflater.from(this).inflate(R.layout.paras_popup_dialog,null);
        zanDialog = (TextView) dialogView.findViewById(R.id.paras_popup_zan_tv);
        zanDialog.setOnClickListener(this);
        reviewDialog = (TextView) dialogView.findViewById(R.id.paras_popup_review_tv);
        reviewDialog.setOnClickListener(this);
        voteDialog = (TextView) dialogView.findViewById(R.id.paras_popup_vote_tv);
        voteDialog.setOnClickListener(this);
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.setCanceledOnTouchOutside(true);
        //dialog.show();

        parasAdapter = new ParasAdapter(paras);
        parasAdapter.setOnLongClickListener(this);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(parasAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                Log.i("ii",lastPosition+"");
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition ==parasAdapter.getItemCount()-1){
                    if(isLoadingMore){
                        Log.i("ii","isLoading");
                    }
                    if(!isLoadingMore){
                        Log.i("ii","notLoading");
                        fetchParas(paras.get(paras.size()-1).getParanum(),3);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        fetchBar(true);
        fetchParas(0,10);

        ((QichengApplication)getApplication()).handlerForTaleActivity = this.taleHandler;

        discussFragment = new DiscussFragment();
        Bundle bundleForFg = new Bundle();
        bundleForFg.putInt("module", ModuleConst.M_TALE);
        bundleForFg.putInt("moduleId",id);
        discussFragment.setBundle(bundleForFg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.tale_discuss_fg,discussFragment);
        fragmentTransaction.commit();

    }

    public void setActive(){
        taleNetUtil.setActive(user,id);
    }

    public void unsetActive(){
        taleNetUtil.unsetActive(user,id);
    }

    public void fetchBar(final boolean showFocus){
        taleNetUtil.fetchTale(this.id, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithCertainTaleData(s).investigate(new ResponseInvestigator<TaleBean>() {
                    @Override
                    public void onOK(int status, String detail, TaleBean data) {
                        showBar(data,showFocus);
                    }
                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(TaleActivity.this,detail,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void fetchParas(final int paraNumAfter, int limit){
            isLoadingMore = true;
            paraNetUtil.fetchParas(id, paraNumAfter, limit, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithParaBeanData(s).investigate(new ResponseInvestigator<List<ParaBean>>() {
                        @Override
                        public void onOK(int status, String detail, List<ParaBean> data) {
                            paras.addAll(data);
                            parasAdapter.notifyDataSetChanged();
                            if(paraNumAfter!=0)recyclerView.smoothScrollToPosition(paraNumAfter-1);
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


    public void showBar(TaleBean tale,boolean showFocus){
        this.tale = tale;
        showTags(tale.getTagSet());
        if(showFocus)showFocus();
        showSponsor(tale.getSponsorNick());
        showTime(tale.getSmartTime());
        showZanAndPara(tale.getZan(),tale.getParaNumber());
    }

    private void showTags(final List<TagBean> tags){
        final LayoutInflater layoutInflater = LayoutInflater.from(TaleActivity.this);
        tagFlowLayout.setAdapter(new TagAdapter<TagBean>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean o) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.item_tag,tagFlowLayout,false);
                tv.setText(tags.get(position).getName());
                return tv;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(TaleActivity.this,tags.get(position).getName()+"被点击",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void showFocus(){
        updateFocus();
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFocus();
            }
        });
    }

    private void updateFocus(){
        taleNetUtil.isUserFocusTale(user, id, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        if(data.equals("yes")){//已关注
                            focus.setText("-关注");
                            focus.setBackground(getDrawable(R.drawable.round_btn_bg_filled));
                            focus.setTextColor(getResources().getColor(R.color.light));
                        }else if(data.equals("no")){
                            focus.setText("+关注");
                            focus.setBackground(getDrawable(R.drawable.round_btn_bg_unfilled));
                            focus.setTextColor(getResources().getColor(R.color.migold));
                        }
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(TaleActivity.this,detail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void toggleFocus(){
        taleNetUtil.toggleFocus(user, id, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                    @Override
                    public void onOK(int status, String detail, Object data) {
                        updateFocus();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        updateFocus();
                    }
                });
            }
        });
    }

    private void showSponsor(String sponsorNick){
        sponsor.setText(sponsorNick);
        sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaleActivity.this,"ID为"+id+"的用户",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showTime(String smartTime){
        time.setText(smartTime);
    }
    private void showZanAndPara(int zan,int para){
        zanAndPara.setText(para+"段\u0020|\u0020"+zan+"赞");
    }

    @Override
    public void onItemLongClick(View view, int pos) {
        //Toast.makeText(TaleActivity.this,pos+"is Clicked",Toast.LENGTH_SHORT).show();
        if(status == 502 && pos == paras.size()-1 && isNoMore == true){
            voteDialog.setEnabled(true);
        }else {
            voteDialog.setEnabled(false);
        }
        mShowDialog(paras.get(pos).getId(),pos);
    }

    private void mShowDialog(int paraId,int paraPos){

        this.longClickedParaPos = paraPos;
        this.longClickedParaId = paraId;

        paraNetUtil.isZaned(paraId, user, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        Log.i("ii",data);
                        if(data.equals("yes")){
                            Log.i("ii","quixiaozan");
                            zanDialog.setText("取消赞");
                        }else{
                            zanDialog.setText("赞");
                        }
                        dialog.show();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(TaleActivity.this,detail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.paras_popup_zan_tv:
                doZan(this.longClickedParaId);
                break;
            case R.id.paras_popup_review_tv:
                doReview(paras.get(this.longClickedParaPos).getParanum());
                break;
            case R.id.paras_popup_vote_tv:
                doVote(this.longClickedParaId);
                break;
        }
    }

    private void doZan(final int paraId){
        paraNetUtil.toggleZan(paraId, user, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        fetchBar(false);
                        paraNetUtil.getZan(paraId, new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                                    @Override
                                    public void onOK(int status, String detail, Object data) {
                                        paras.get(longClickedParaPos).setZan(Integer.valueOf(detail));
                                        parasAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onErr(int status, String detail) {

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(TaleActivity.this,detail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.dismiss();
    }
    private void doReview(int paraNum){
        Log.i("ii","review");
        dialog.dismiss();
        discussFragment.atPara(paraNum);
        tucao.callOnClick();
    }
    private void doVote(final int paraId){
        Log.i("ii","toushan");
        paraNetUtil.voteToDelete(user, paraId, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                    @Override
                    public void onOK(int status, String detail, Object data) {
                        if(status == 503){
                            //投票成功
                            Toast.makeText(TaleActivity.this,detail,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onErr(int status, String detail) {
                        return;
                    }
                });
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Toast.makeText(TaleActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.dismiss();
    }


//    private void refreshAll(int limit){
//        //Log.i("ii","toushanhoushuaxin");
//        paraNetUtil.fetchParas(id, 0, limit, new StringCallback() {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//                ResponseUtil.decodeResponseWithParaBeanData(s).investigate(new ResponseInvestigator<List<ParaBean>>() {
//                    @Override
//                    public void onOK(int status, String detail, List<ParaBean> data) {
//                        paras.clear();
//                        paras.addAll(data);
//                        parasAdapter.notifyDataSetChanged();
//                        recyclerView.smoothScrollToPosition(parasAdapter.getItemCount()-1);
//                    }
//
//                    @Override
//                    public void onErr(int status, String detail) {
//
//                    }
//                });
//            }
//        });
//        isNoMore = false;
//        isLoadingMore = false;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 2 && resultCode == 2){//ChengActivity
//            int lastPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//            isNoMore = false;
//            fetchParas(paras.get(lastPos).getParanum(),1);
//            recyclerView.scrollToPosition(lastPos+1);
//            fetchBar();
//        }
//    }

    @Override
    public void onBackPressed() {

        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        setResult(3,new Intent().putExtras(bundle));
        super.onBackPressed();
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
        taleNetUtil.askPush(id,user);
    }

    public Handler taleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            status = msg.what;
            switch (status){
                case 500:
                    //readyTime();
                    Log.i("ii","TaleAct收到500(就绪)");
                    hint_Ready();
                    break;
                case 501:
                    //silentTime();
                    Log.i("ii","TaleAct收到501（静默）");
                    hint_silent();
                    break;
                case 502:
                    //coldenTime();
                    Log.i("ii","TaleAct收到502（冷却），冷却时间："+msg.obj);
                    hint_colden((String) msg.obj);
                    break;
                case 504:
                    //deleteRenewTime();
                    Log.i("ii","TaleAct收到504（删除最后一段）");
                    hint_deleted();
                    break;
                case 505:
                    //new review(s);
                    Log.i("ii","TaleAct收到505（收到一条新评论）");
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
                intent1.setClass(TaleActivity.this,ChengActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("para",paras.get(paras.size()-1).getParanum()+1);
                bundle1.putInt("tale",id);
                intent1.putExtras(bundle1);
                TaleActivity.this.startActivityForResult(intent1,2, ActivityOptions.makeSceneTransitionAnimation(TaleActivity.this).toBundle());
            }
        });

        hintBar.addView(btn);
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

    private void hint_colden(String createTime){
        fetchBar(false);

        hintBar.removeAllViews();
        TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.light));
        tv.setTextSize(20);
        tv.setText("冷却。");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 3;
        tv.setLayoutParams(params);
        int lastTime = Integer.valueOf(createTime);
        long lastTimeM = lastTime*1000;
        long nowTime = TimeManager.getInstance().getServiceTime();
        int deltaTime = (int) (nowTime - lastTimeM);
        int colenTime = 20000 - deltaTime;
        hintBar.addView(tv);
        if(isNoMore) {
            fetchParas(paras.get(paras.size()-1).getParanum(),1);

            Button btn = new Button(this);
            btn.setText("投删");
            btn.setTextColor(getResources().getColor(R.color.light));
            btn.setBackgroundColor(getResources().getColor(R.color.none));
            btn.setGravity(Gravity.CENTER);
            //btn.setBackgroundResource(R.drawable.round_btn_bg_light_line_migold_fill);
            btn.setTextSize(18);
            btn.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params2.weight = 1;
            btn.setLayoutParams(params2);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(TaleActivity.this, "长按以投删 Y(^_^)Y", Toast.LENGTH_SHORT).show();
                }
            });
            btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    doVote(paras.get(paras.size() - 1).getId());
                    return true;
                }
            });

            hintBar.addView(btn);
        }

        taleHandler.postDelayed(myFreshStatusRunnable,colenTime);
    }
    private Runnable myFreshStatusRunnable = new Runnable() {
        @Override
        public void run() {
            freshStatus();
        }
    };

    private void hint_deleted(){
        hintBar.removeAllViews();
        final TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.light));
        tv.setTextSize(20);
        tv.setText("末段被民主删除。");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        hintBar.addView(tv);
        taleHandler.removeCallbacks(myFreshStatusRunnable);
        taleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hint_Ready();
            }
        },5000);
        if(isNoMore){
            paras.remove(paras.size()-1);
            parasAdapter.notifyDataSetChanged();
        }
        fetchBar(false);
    }

    private void fresh_reviews(){
        discussFragment.fetchSpeechesNew();
        tucao.setBackground(getDrawable(R.drawable.redspot));
        taleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tucao.setBackgroundColor(getResources().getColor(R.color.none));
            }
        },10000);
    }


}


