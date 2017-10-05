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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.TaleListAdapter;
import me.milechen.qicheng.Beans.TaleBean;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.TaleNetUtil;
import me.milechen.qicheng.Utils.RecyclerViewDivider;
import me.milechen.qicheng.Utils.TimeManager;
import me.milechen.qicheng.Activities.MainActivity;
import me.milechen.qicheng.Activities.TaleActivity;
import me.milechen.qicheng.R;
import okhttp3.Call;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeatMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LatestMainFragment extends MainFragment implements OnItemClickListener, TaleListAdapter.MyItemClickListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TaleListAdapter taleListAdapter;
    private ArrayList<TaleBean> latestTalesList = new ArrayList<TaleBean>();
    private boolean isLoadingMore = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_heat_main, container, false);
        //convenientBanner = new ConvenientBanner(getContext());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fgheat_srl);
        recyclerView = (RecyclerView) view.findViewById(R.id.fgheat_heat_rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateData(TimeManager.getInstance().getServiceTime());

        swipeRefreshLayout.setColorSchemeResources(R.color.green,R.color.red,R.color.blue,R.color.violet);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.light);
        swipeRefreshLayout.setProgressViewEndTarget(true,100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateData(TimeManager.getInstance().getServiceTime());
                    }
                }).start();
            }
        });


        taleListAdapter = new TaleListAdapter(latestTalesList);
        taleListAdapter.setOnItemClickListener(this);

        final LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taleListAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(),LinearLayoutManager.HORIZONTAL,14,getResources().getColor(R.color.gray)));
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition ==taleListAdapter.getItemCount()-1){
                    if(isLoadingMore){
                        Log.i("ii","isLoading");
                    }
                    if(!isLoadingMore){
                        Log.i("ii","notLoading");
                        isLoadingMore = true;
                        handler.sendEmptyMessageDelayed(3,500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        ((MainActivity)getActivity()).handlerForLatestF = this.handler;
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
        //Toast.makeText(getContext(),heatTalesList.get(pos).getId()+"isClicked",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("id",latestTalesList.get(pos).getId());
        bundle.putInt("pos",pos);
        Log.i("ii","pos:"+pos);
        intent.setClass(getActivity(), TaleActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,3);
    }

    private void updateData(long timeBefore){
        latestTalesList.clear();
        final TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchNewTales(timeBefore, 10, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithTaleBeanData(s).investigate(new ResponseInvestigator<List<TaleBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<TaleBean> data) {
                        for (TaleBean tale:data) {
                            latestTalesList.add(tale);
                        }
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Log.i("ii", "onOK: ");
                    }
                });
            }
        });

    }

    private void loadMore(long timeBefore){
        TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchNewTales(timeBefore, 10, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithTaleBeanData(s).investigate(new ResponseInvestigator<List<TaleBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<TaleBean> data) {
                        for (TaleBean tale:data) {
                            latestTalesList.add(tale);
                            //Log.i("ii",tale.getTagSet().get(0).getName());
                        }
                        taleListAdapter.notifyDataSetChanged();
                        isLoadingMore = false;
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Log.i("ii", detail);
                        Toast.makeText(getContext(),"没有更多...",Toast.LENGTH_LONG).show();
                        isLoadingMore = false;
                    }
                });
            }
        });
    }

    private void updateOne(final int pos){
        TaleNetUtil taleNetUtil = new TaleNetUtil();
        taleNetUtil.fetchTale(latestTalesList.get(pos).getId(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithCertainTaleData(s).investigate(new ResponseInvestigator<TaleBean>() {
                    @Override
                    public void onOK(int status, String detail, TaleBean data) {
                        latestTalesList.set(pos,data);
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
                    //swipeRefreshLayout.setRefreshing(false);
                    taleListAdapter.notifyDataSetChanged();
                    //swipeRefreshLayout.setEnabled(false);
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 2:// Activity请求更新
                    updateData(TimeManager.getInstance().getServiceTime());
                    taleListAdapter.notifyDataSetChanged();
                    break;
                case 3://加载更多
                    loadMore(latestTalesList.get(latestTalesList.size()-1).getTime());
                    break;
                case 4://更新一条
                    updateOne(msg.arg1);
                default:
                    break;
            }
        }
    };
}
