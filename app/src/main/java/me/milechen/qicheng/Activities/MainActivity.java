package me.milechen.qicheng.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.FragmentAdapter;
import me.milechen.qicheng.Fragments.HeatMainFragment;
import me.milechen.qicheng.Fragments.LatestMainFragment;
import me.milechen.qicheng.Fragments.MainFragment;
import me.milechen.qicheng.Fragments.SideNavFragment;
import me.milechen.qicheng.Fragments.TagsMainFragment;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;

public class MainActivity extends AppCompatActivity
        implements  MainFragment.OnFragmentInteractionListener ,View.OnClickListener,SideNavFragment.OnSideNavFragmentInteractionListener{

    private List<MainFragment> fragmentList = new ArrayList<MainFragment>();
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private TextView heat_tv , latest_tv , tags_tv;
    private ImageView tabLine;
    private CoordinatorLayout snackbar;
    private DrawerLayout drawer;

    private HeatMainFragment heatMainFragment;
    private LatestMainFragment latestMainFragment;
    private TagsMainFragment tagsMainFragment;

    private int currentIndex;
    private int screenWidth;

    public Handler handlerForHeatF;
    public Handler handlerForLatestF;
    public Handler handlerForTagF;

    //private long time = TimeManager.getInstance().getServiceTime();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if(LoginActivity.instance != null)LoginActivity.instance.finish();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView qi = (TextView) findViewById(R.id.toolbar_right_tv);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        heat_tv = (TextView) findViewById(R.id.main_heat_tv);
        latest_tv = (TextView) findViewById(R.id.main_latest_tv);
        //tags_tv = (TextView) findViewById(R.id.main_tags_tv);
        tabLine = (ImageView) findViewById(R.id.id_tab_line_iv);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        snackbar = (CoordinatorLayout) findViewById(R.id.col_main_main);
        drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        fragInit();

        qi.setText(R.string.qi);
        title.setText(R.string.forall);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SideNavFragment snf = new SideNavFragment();
        snf.setCheckedModuleConst(ModuleConst.M_TALE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fg_side_nav,snf);
        fragmentTransaction.commit();

        qi.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void fragInit(){


        currentIndex = 0;


        heatMainFragment = new HeatMainFragment();
        latestMainFragment = new LatestMainFragment();
        //tagsMainFragment = new TagsMainFragment();

        fragmentList.add(heatMainFragment);
        fragmentList.add(latestMainFragment);
        //fragmentList.add(tagsMainFragment);

        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(),fragmentList);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(currentIndex);

        initTabLine();
        //moveToPos(currentIndex,0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                moveToPos(position,positionOffset);
                //Log.i("ii","OFSET::"+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                lightUp(position);
                currentIndex = position;
                Log.i("ii","滑动到第"+position+"页");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        heat_tv.setOnClickListener(this);
        latest_tv.setOnClickListener(this);
        //tags_tv.setOnClickListener(this);
    }

    public void resetLights(){
        heat_tv.setTextColor(getResources().getColor(R.color.migold));
        latest_tv.setTextColor(getResources().getColor(R.color.migold));
        //tags_tv.setTextColor(getResources().getColor(R.color.migold));
    }

    public void lightUp(int pos){
        resetLights();
        switch (pos){
            case 0:
                heat_tv.setTextColor(getResources().getColor(R.color.brown));
                break;
            case 1:
                latest_tv.setTextColor(getResources().getColor(R.color.brown));
                break;
            //case 2:
            //    tags_tv.setTextColor(getResources().getColor(R.color.brown));
            //    break;
        }
    }

    public void moveToPos(int position, float offset) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabLine
                .getLayoutParams();

        lp.leftMargin = (int) (position * (screenWidth / 2) + (offset) * (screenWidth / 2));

        tabLine.setLayoutParams(lp);
    }

    public void initTabLine(){
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabLine
                .getLayoutParams();
        lp.width = screenWidth / 2;
        tabLine.setLayoutParams(lp);
    }





    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.main_heat_tv:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_latest_tv:
                viewPager.setCurrentItem(1);
                break;
            //case R.id.main_tags_tv:
            //    viewPager.setCurrentItem(2);
            //    break;
            case R.id.toolbar_right_tv:
                jumpToQi();
        }
    }

    @Override
    public void onFragmentInteractionBannerClicked(int position) {
        //TODO:首页banner点击事件
        Toast.makeText(this,position+"is clicked",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMenuItemClick(int moduleConst) {
        if(moduleConst == ModuleConst.M_TALE){

        }else if(moduleConst == ModuleConst.M_GROUP){
            startActivity(new Intent(MainActivity.this,GroupActivity.class));
            finish();
        }else if(moduleConst == ModuleConst.M_NOTICE){

        }else if(moduleConst == ModuleConst.M_MINE){

        }else if(moduleConst == ModuleConst.M_SEARCH){

        }else if(moduleConst == ModuleConst.M_ABOUT){

        }else if(moduleConst == ModuleConst.M_REWARD){

        }
        drawer.closeDrawer(Gravity.LEFT);
    }


    private void jumpToQi(){
        Intent intent = new Intent(MainActivity.this,QiActivity.class);
        MainActivity.this.startActivityForResult(intent,1,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i("ii","requestCode:"+requestCode+",resultCode:"+resultCode+",data:"+data);
        if(requestCode == 1 && resultCode == 1){//从QiActivity返回的数据
            if(data != null){
                handlerForHeatF.sendEmptyMessage(2);
                handlerForLatestF.sendEmptyMessage(2);
                viewPager.setCurrentItem(1);
                Snackbar.make(snackbar,data.getStringExtra("message")+currentIndex,Snackbar.LENGTH_SHORT).show();
            }
        }
        else if(resultCode == 3) {//从TaleActivity返回的数据
            //Log.i("ii","shou dao 3 from taleA");
            if(data != null){
                Message message = new Message();
                message.what = 4;
                message.arg1 = (int) data.getExtras().get("pos");
                if(currentIndex == 0)handlerForHeatF.sendMessage(message);
                if(currentIndex == 1)handlerForLatestF.sendMessage(message);
            }
        }
    }
}
