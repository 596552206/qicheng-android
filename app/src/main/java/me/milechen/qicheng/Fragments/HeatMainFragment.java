package me.milechen.qicheng.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lzy.okhttputils.callback.StringCallback;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.milechen.qicheng.Activities.MainActivity;
import me.milechen.qicheng.Activities.TaleActivity;
import me.milechen.qicheng.Adapters.TaleListAdapter;
import me.milechen.qicheng.Beans.TaleBean;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import me.milechen.qicheng.Utils.RecyclerViewDivider;
import me.milechen.qicheng.Utils.TimeManager;
import okhttp3.Call;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnScrollListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeatMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HeatMainFragment extends MainFragment implements OnItemClickListener, TaleListAdapter.MyItemClickListener {
    

    private ConvenientBanner convenientBanner;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private TaleListAdapter taleListAdapter;
    private ArrayList<String> heatTitleList = new ArrayList<String>();
    private ArrayList<TaleBean> heatTalesList = new ArrayList<TaleBean>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_heat_main, container, false);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.fgheat_srl);
        recyclerView = (RecyclerView) view.findViewById(R.id.fgheat_heat_rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateData(TimeManager.getInstance().getServiceTime());

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                Log.i("ii", "正在刷新");
                updateData(TimeManager.getInstance().getServiceTime());
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                Log.i("ii", "正在加载");
                handler.sendEmptyMessageDelayed(3, 500);
            }
        });


        taleListAdapter = new TaleListAdapter(heatTalesList);
        taleListAdapter.setOnItemClickListener(this);


        final LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        setHeaderView(recyclerView);

        recyclerView.setAdapter(taleListAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 14, getResources().getColor(R.color.gray)));

        ((MainActivity)getActivity()).handlerForHeatF = this.handler;

    }

    private void setHeaderView(RecyclerView view){
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_rv_heat_main, view, false);
        convenientBanner = (ConvenientBanner) header.findViewById(R.id.fgheat_heat_cb);
        convenientBanner.setPages(
                new CBViewHolderCreator<BannerHeatHolderView>() {
                    @Override
                    public BannerHeatHolderView createHolder() {
                        return new BannerHeatHolderView();
                    }
                }, heatTitleList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.banner_page_indicator, R.drawable.banner_page_indicator_selected})
                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(this)//监听翻页事件
                .setOnItemClickListener(this);
        taleListAdapter.setHeaderView(header);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //super.mListener.onFragmentInteractionTell();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        super.mListener = null;
    }

    @Override
    public void onItemClick(int position) {
        super.mListener.onFragmentInteractionBannerClicked(position);
    }

    @Override
    public void onItemClick(View view, int pos) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("id",heatTalesList.get(pos).getId());
        bundle.putInt("pos",pos);
        Log.i("ii","pos:"+pos);
        intent.setClass(getActivity(), TaleActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,3);
    }



    class BannerHeatHolderView implements Holder{
        private TextView content;
        private TextView leftQuote;
        private TextView rightQuote;
        private LinearLayout linearLayout;
        @Override
        public View createView(Context context) {
            linearLayout = new LinearLayout(context);
            content = new TextView(context);
            leftQuote = new TextView(context);
            rightQuote = new TextView(context);
            linearLayout.addView(leftQuote);
            linearLayout.addView(content);
            linearLayout.addView(rightQuote);
            return linearLayout;
        }

        @Override
        public void UpdateUI(Context context, int position, Object data) {
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            leftQuote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            leftQuote.setGravity(Gravity.LEFT);
            leftQuote.setText("“");
            leftQuote.setTextSize(50);
            leftQuote.setTextColor(getResources().getColor(R.color.light));
            leftQuote.setPadding(100,0,0,0);

            rightQuote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rightQuote.setGravity(Gravity.RIGHT);
            rightQuote.setText("”");
            rightQuote.setTextSize(50);
            rightQuote.setTextColor(getResources().getColor(R.color.light));
            rightQuote.setPadding(0,0,100,0);

            content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));;
            content.setText((String) data);
            content.setTextColor(getResources().getColor(R.color.light));
            content.setGravity(Gravity.CENTER);
            content.setTextSize(24);

        }
    }

    private void updateBanner() {
        if (heatTitleList.isEmpty()) {
            if (heatTalesList.size() > 0) {
                heatTitleList.clear();
                for (int i = 0; i < 4; i++) {
                    String p1 = heatTalesList.get(i).getParaOne();
                    String title = (p1.length() >= 20) ? p1.substring(0, 20) + "..." : p1;
                    heatTitleList.add(i, title);
                }
                Log.i("ii", "更新了banner" + heatTitleList.size());
                convenientBanner.notifyDataSetChanged();
            }
        }

    }

    private void updateData(long timeBefore){
        heatTalesList.clear();
        final TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchHotTales(timeBefore, 10, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                //Log.i("ii","header:"+response.header("Cookie","def"));
                ResponseUtil.decodeResponseWithTaleBeanData(s).investigate(new ResponseInvestigator<List<TaleBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<TaleBean> data) {
                        heatTalesList.addAll(data);
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(getContext(),detail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void loadMore(long timeBefore){
        TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchHotTales(timeBefore, 8, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.i("ii", response.headers().toString());
                ResponseUtil.decodeResponseWithTaleBeanData(s).investigate(new ResponseInvestigator<List<TaleBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<TaleBean> data) {
                        for (TaleBean tale:data) {
                            heatTalesList.add(tale);
                        }
                        taleListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Log.i("ii", detail);
                        Toast.makeText(getContext(),"没有更多...",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void updateOne(final int pos){
        TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchTale(heatTalesList.get(pos).getId(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithCertainTaleData(s).investigate(new ResponseInvestigator<TaleBean>() {
                    @Override
                    public void onOK(int status, String detail, TaleBean data) {
                        heatTalesList.set(pos,data);
                        taleListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErr(int status, String detail) {

                    }
                });
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://下拉
                    taleListAdapter.notifyDataSetChanged();
                    updateBanner();
                    break;
                case 2:// Activity请求更新
                    updateData(TimeManager.getInstance().getServiceTime());
                    taleListAdapter.notifyDataSetChanged();
                    break;
                case 3://加载更多
                    loadMore(heatTalesList.get(heatTalesList.size()-1).getTime());
                    break;
                case 4://更新一条
                    updateOne(msg.arg1);
                default:
                    break;
            }
        }
    };
}
