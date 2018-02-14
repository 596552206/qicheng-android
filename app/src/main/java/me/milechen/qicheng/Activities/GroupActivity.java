package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.GroupAdapter;
import me.milechen.qicheng.Beans.GroupBean;
import me.milechen.qicheng.Beans.QRCodeGroupData;
import me.milechen.qicheng.Fragments.SideNavFragment;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;
import me.milechen.qicheng.Utils.Net.GroupNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.QRCodeTranslator;
import okhttp3.Call;
import okhttp3.Response;


public class GroupActivity extends AppCompatActivity implements SideNavFragment.OnSideNavFragmentInteractionListener, GroupAdapter.OnGroupItemClickListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private int user;
    private CoordinatorLayout snackbar;
    private GroupNetUtil gnu = new GroupNetUtil();
    private List<GroupBean> groups = new ArrayList<>();
    private GroupAdapter groupAdapter;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private TextView plus;
    private int backFromGraleId = -1;
    private int backFromGraldPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        plus = (TextView) findViewById(R.id.toolbar_right_tv);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        snackbar = (CoordinatorLayout) findViewById(R.id.group_acv_snackbar_coord);
        recyclerView = (RecyclerView) findViewById(R.id.group_groups_rv);

        plus.setText("+");
        plus.setOnClickListener(this);
        title.setText(R.string.group);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_group);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SideNavFragment snf = new SideNavFragment();
        snf.setCheckedModuleConst(ModuleConst.M_GROUP);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.group_container_fg_side_nav,snf);
        fragmentTransaction.commit();

        //plus.setOnClickListener(this);

        groupAdapter = new GroupAdapter(groups);
        groupAdapter.setOnItemClickListener(this);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        this.user = getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);
        fetchGroups();
    }

    private void fetchGroups(){
        gnu.fetchGroups(this.user, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithGroupBeanListData(s).investigate(new ResponseInvestigator<List<GroupBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<GroupBean> data) {
                        if(status == 200){
                            groups.clear();
                            groups.addAll(data);

                        }else if(status == 203){
                            if(!groups.isEmpty())groups.clear();
                            Snackbar.make(snackbar,"君还一个小组都没加入哦 (･･;)",Snackbar.LENGTH_LONG).show();
                        }
                        groupAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErr(int status, String detail) {

                    }
                });
            }
        });
    }

    private void freshGroup(int groupId, final int pos){
        gnu.fetchGroupView(groupId, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithCertainGroupData(s).investigate(new ResponseInvestigator<GroupBean>() {
                    @Override
                    public void onOK(int status, String detail, GroupBean data) {
                        groups.set(pos,data);
                        groupAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErr(int status, String detail) {

                    }
                });
            }
        });
    }


    private void quitGroup(int groupid){
        gnu.quitGroup(groupid, user, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                    @Override
                    public void onOK(int status, String detail, String data) {
                        Toast.makeText(GroupActivity.this,detail,Toast.LENGTH_LONG).show();
                        fetchGroups();
                    }

                    @Override
                    public void onErr(int status, String detail) {
                        Toast.makeText(GroupActivity.this,detail,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(backFromGraleId == -2){//等于-2刷新全部
            fetchGroups();
            backFromGraleId=-1;
            backFromGraldPos=-1;
        }else if(backFromGraleId == -1){//等于-1无操作

        }else{//其他则为更新特定故事简介
            freshGroup(backFromGraleId,backFromGraldPos);
            backFromGraleId = -1;
            backFromGraldPos = -1;
        }
    }

    @Override
    public void onMenuItemClick(int moduleConst) {
        if(moduleConst == ModuleConst.M_TALE){
            startActivity(new Intent(GroupActivity.this,MainActivity.class));
            finish();
        }else if(moduleConst == ModuleConst.M_GROUP){

        }else if(moduleConst == ModuleConst.M_NOTICE){

        }else if(moduleConst == ModuleConst.M_MINE){

        }else if(moduleConst == ModuleConst.M_SEARCH){

        }else if(moduleConst == ModuleConst.M_ABOUT){

        }else if(moduleConst == ModuleConst.M_REWARD){

        }
        drawer.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onGroupItemClick(int groupId,int pos) {
        //Snackbar.make(snackbar,groupId+"is clicked",Snackbar.LENGTH_SHORT).show();
        this.backFromGraleId = groupId;
        this.backFromGraldPos = pos;

        Intent intent2Grale = new Intent();
        intent2Grale.putExtra("groupId",groupId);
        intent2Grale.putExtra("groupName",groups.get(pos).getName());
        intent2Grale.setClass(GroupActivity.this,GraleActivity.class);
        startActivity(intent2Grale);
    }

    @Override
    public void onGroupItemLongClick(final int groupId, int pos) {
        Snackbar.make(snackbar,"退出小组「"+groupId+"」？",Snackbar.LENGTH_LONG).setAction("退出", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitGroup(groupId);
            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.toolbar_right_tv){
            Log.i("ii","plus clicked");
            PopupMenu popupMenu = new PopupMenu(this,plus);
            popupMenu.inflate(R.menu.group_plus);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.group_plus_menu_scan:
                if(ContextCompat.checkSelfPermission(GroupActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                final int REQ_CODE_PERMISSION = 0x1111;
                ActivityCompat.requestPermissions(GroupActivity.this, new String[]{android.Manifest.permission.CAMERA},REQ_CODE_PERMISSION);
                Toast.makeText(GroupActivity.this,"无法打开相机，请申请权限，然后重试",Toast.LENGTH_LONG).show();
            }else {
                startActivityForResult(new Intent(GroupActivity.this, CaptureActivity.class), CaptureActivity.REQ_CODE);
            }
                break;
            case R.id.group_plus_menu_search:
                startActivity(new Intent(GroupActivity.this,SearchGroupActivity.class));
                backFromGraleId=-2;
                backFromGraldPos=-2;
                break;
            case R.id.group_plus_menu_make:
                startActivity(new Intent(GroupActivity.this,CreateGroupActivity.class));
                backFromGraleId=-2;
                backFromGraldPos=-2;
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CaptureActivity.REQ_CODE){
            switch (resultCode) {
                case RESULT_OK:
                    QRCodeTranslator qrCodeTranslator = new QRCodeTranslator(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                    if(qrCodeTranslator.isAvailable()){
                        QRCodeGroupData groupData = qrCodeTranslator.translate().decodeGroupData();
                        new GroupNetUtil().joinGroup(groupData.getId(), user,groupData.getPassword(), new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                ResponseUtil.decodeResponseWithStringData(s).investigate(new ResponseInvestigator<String>() {
                                    @Override
                                    public void onOK(int status, String detail, String data) {
                                        Toast.makeText(GroupActivity.this,detail,Toast.LENGTH_LONG).show();
                                        //finish();
                                        fetchGroups();
                                    }

                                    @Override
                                    public void onErr(int status, String detail) {
                                        Toast.makeText(GroupActivity.this,detail,Toast.LENGTH_LONG).show();
                                        //finish();
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(GroupActivity.this,"这个二维码没有用哦╮(╯▽╰)╭",Toast.LENGTH_LONG).show();
                    }
                    break;
                case RESULT_CANCELED:
                    if (data != null) {
                        // for some reason camera is not working correctly
                        Toast.makeText(GroupActivity.this,data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT),Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }
}
