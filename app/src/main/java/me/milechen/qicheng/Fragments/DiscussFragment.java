package me.milechen.qicheng.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.milechen.qicheng.Activities.TaleActivity;
import me.milechen.qicheng.Adapters.SpeechAdapter;
import me.milechen.qicheng.Beans.SpeechBean;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;
import me.milechen.qicheng.Utils.Net.GroupNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import me.milechen.qicheng.Utils.TimeManager;
import okhttp3.Call;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussFragment extends Fragment implements SpeechAdapter.MyItemLongClickListener {

    private int myUserId;
    private int module;
    private int moduleId;
    private List<SpeechBean> speeches = new ArrayList();
    private RecyclerView recyclerView;
    private SpeechAdapter speechAdapter;
    private EditText editText;
    private EditText atTv;
    private Button submit;
    private boolean isLoadingMore;
    private boolean isNoMore = false;
    public int atuser = -1;
    public int atpara = -1;

    public DiscussFragment() {
        // Required empty public constructor
    }

    public void setBundle(Bundle bundle){
        this.module = bundle.getInt("module");
        this.moduleId = bundle.getInt("moduleId");
        Log.i("ii","getModuleId"+moduleId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discuss, container, false);;
        this.recyclerView = (RecyclerView) view.findViewById(R.id.fgdiscuss_speeches_rv);
        this.editText = (EditText) view.findViewById(R.id.fgdiscuss_speeches_et);
        this.atTv = (EditText) view.findViewById(R.id.fgdiscuss_at_tv);
        this.submit = (Button) view.findViewById(R.id.fgdiscuss_speeches_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myUserId = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getInt("id",-1);


        speechAdapter = new SpeechAdapter(speeches,myUserId);
        speechAdapter.setOnLongClickListener(this);
        recyclerView.setAdapter(speechAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isScollUp = true;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int topPos = ((LinearLayoutManager)linearLayoutManager).findFirstVisibleItemPosition();
                if(isScollUp && !isNoMore && newState == RecyclerView.SCROLL_STATE_IDLE && topPos == 0){
                    if(isLoadingMore){
                        Log.i("ii","正在拉取对话中");
                    }
                    if(!isLoadingMore){
                        Log.i("ii","准备拉取对话");
                        isLoadingMore = true;
                        fetchSpeechesOld(speeches.get(0).getTime());
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0)isScollUp = true;
                if (dy>0)isScollUp = false;
            }
        });


        atTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                atTv.setText("");
                atPara(-1);
                atUser("",-1);
                switch2SingleLine();
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if(content.length() > 0 ){
                    doSubmit(content,atuser,atpara);
                }
            }
        });
        fetchSpeechesOld(TimeManager.getInstance().getServiceTime());
        Log.i("ii","正在拉取会话");
    }

    public void fetchSpeechesOld(long timeBefore){
        isLoadingMore = true;
        if(module == ModuleConst.M_TALE){
            //故事模块
            new TaleNetUtil().fetchSpeechesOld(moduleId, timeBefore, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithSpeechBeanListData(s).investigate(new ResponseInvestigator<List<SpeechBean>>() {
                        @Override
                        public void onOK(int status, String detail, List<SpeechBean> data) {
                            if(status == 200){
                                Collections.reverse(data);
                                speeches.addAll(0,data);
                                speechAdapter.notifyDataSetChanged();
                                isLoadingMore = false;
                            }else if(status == 203){
                                isNoMore = true;
                                isLoadingMore = false;
                            }
                        }

                        @Override
                        public void onErr(int status, String detail) {

                            isLoadingMore = false;
                        }
                    });
                }
            });
        }else if(module == ModuleConst.M_GROUP){
            //小组模块
            new GroupNetUtil().fetchSpeechesOld(moduleId,timeBefore, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithSpeechBeanListData(s).investigate(new ResponseInvestigator<List<SpeechBean>>() {
                        @Override
                        public void onOK(int status, String detail, List<SpeechBean> data) {
                            if(status == 200){
                                Collections.reverse(data);
                                speeches.addAll(0,data);
                                speechAdapter.notifyDataSetChanged();
                                isLoadingMore = false;
                            }else if(status == 203){
                                isNoMore = true;
                                isLoadingMore = false;
                            }
                        }

                        @Override
                        public void onErr(int status, String detail) {

                            isLoadingMore = false;
                        }
                    });
                }
            });
        }
    }

    public void fetchSpeechesNew(){
        isLoadingMore = true;
        if(module == ModuleConst.M_TALE){
            //故事模块
            new TaleNetUtil().fetchSpeechesNew(moduleId, speeches.get(speeches.size()-1).getTime(), new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithSpeechBeanListData(s).investigate(new ResponseInvestigator<List<SpeechBean>>() {
                        @Override
                        public void onOK(int status, String detail, List<SpeechBean> data) {
                            if(status == 200){
                                Collections.reverse(data);
                                speeches.addAll(data);
                                speechAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(speechAdapter.getItemCount()-1);
                                isLoadingMore = false;
                            }else if(status == 203){
                                isLoadingMore = false;
                            }
                        }

                        @Override
                        public void onErr(int status, String detail) {

                            isLoadingMore = false;
                        }
                    });
                }
            });
        }else if(module == ModuleConst.M_GROUP){
            //小组模块
            new GroupNetUtil().fetchSpeechesNew(moduleId, (!speeches.isEmpty())?(speeches.get(speeches.size()-1).getTime()):(0), new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithSpeechBeanListData(s).investigate(new ResponseInvestigator<List<SpeechBean>>() {
                        @Override
                        public void onOK(int status, String detail, List<SpeechBean> data) {
                            if(status == 200){
                                Collections.reverse(data);
                                speeches.addAll(data);
                                speechAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(speechAdapter.getItemCount()-1);
                                isLoadingMore = false;
                            }else if(status == 203){
                                isLoadingMore = false;
                            }
                        }

                        @Override
                        public void onErr(int status, String detail) {

                            isLoadingMore = false;
                        }
                    });
                }
            });
        }
    }


    public void doSubmit(String content , int atUser, int atPara){
        editText.setText("");
        atTv.setText("");
        this.atpara = -1;
        this.atuser = -1;
        switch2SingleLine();
        final long time = TimeManager.getInstance().getServiceTime();
        if(module == ModuleConst.M_TALE){
            new TaleNetUtil().addSpeech(moduleId,content,myUserId,atUser,atPara,time, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                        @Override
                        public void onOK(int status, String detail, Object data) {
                            //fetchSpeeches(time+1);
                            Toast.makeText(getContext(),"发表成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErr(int status, String detail) {
                            Toast.makeText(getContext(),"发表失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if(module == ModuleConst.M_GROUP){
            new GroupNetUtil().addSpeech(moduleId,content,myUserId,atUser,atPara,time, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResponseUtil.decodeResponseWithoutData(s).investigate(new ResponseInvestigator() {
                        @Override
                        public void onOK(int status, String detail, Object data) {
                            //fetchSpeeches(time+1);
                            Toast.makeText(getContext(),"发表成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErr(int status, String detail) {
                            Toast.makeText(getContext(),"发表失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void  atUser(String nick,int user){
        switch2multiLine();
        this.atuser = user;
        if(user != -1)this.atTv.setText(this.atTv.getText()+"@"+nick+"·");
    }
    public void atPara(int para){
        switch2multiLine();
        this.atpara = para;
        if(para != -1)this.atTv.setText(this.atTv.getText()+"@第"+para+"段·");
    }

    @Override
    public void onItemLongClick(View view, int pos) {
        atUser(speeches.get(pos).getNick(),speeches.get(pos).getUserid());
    }

    private void switch2multiLine(){
        int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        int space = getResources().getDimensionPixelOffset(R.dimen.common_space);

        atTv.setVisibility(View.VISIBLE);

        editText.setPadding(space,height,height,0);
        editText.setMinHeight(height*2);
    }

    private void switch2SingleLine(){
        int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        int space = getResources().getDimensionPixelOffset(R.dimen.common_space);
        atTv.setVisibility(View.INVISIBLE);

        editText.setPadding(space,0,space,0);
        editText.setMinHeight(height);
    }
}
