package me.milechen.qicheng.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import me.milechen.qicheng.Activities.UserActivity;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;


public class SideNavFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private int selectedModule = -1;

    private NavigationView navigationView;
    private SimpleDraweeView avatarView;
    private TextView nickView;
    private String userNick;
    private String userAvatar;


    private OnSideNavFragmentInteractionListener listener;

    public SideNavFragment() {
    }

    public void setCheckedModuleConst(int moduleConst){
        this.selectedModule = moduleConst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_side_nav, container, false);
        this.navigationView = (NavigationView) view.findViewById(R.id.fg_side_nav_view);
        View header = navigationView.getHeaderView(0);
        this.avatarView = (SimpleDraweeView) header.findViewById(R.id.side_nav_face_sdv);
        this.nickView = (TextView) header.findViewById(R.id.side_nav_nick_tv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        this.userAvatar = sp.getString("avatar",null);
        this.userNick = sp.getString("nick",null);

        this.avatarView.setImageURI(this.userAvatar);
        this.nickView.setText(this.userNick);
        this.nickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                getActivity().startActivity(intent);
            }
        });

        this.navigationView.setNavigationItemSelectedListener(this);
        this.navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(),UserActivity.class));
            }
        });
        if(this.selectedModule == ModuleConst.M_TALE){
            this.navigationView.setCheckedItem(R.id.nav_forall);
        }else if(this.selectedModule == ModuleConst.M_GROUP){
            this.navigationView.setCheckedItem(R.id.nav_group);
        }else if(this.selectedModule == ModuleConst.M_NOTICE){
            this.navigationView.setCheckedItem(R.id.nav_notice);
        }else if(this.selectedModule == ModuleConst.M_MINE){
            this.navigationView.setCheckedItem(R.id.nav_mine);
        }else if(this.selectedModule == ModuleConst.M_SEARCH){
            this.navigationView.setCheckedItem(R.id.nav_search);
        }else if(this.selectedModule == ModuleConst.M_ABOUT){
            this.navigationView.setCheckedItem(R.id.nav_about);
        }else if(this.selectedModule == ModuleConst.M_REWARD){
            this.navigationView.setCheckedItem(R.id.nav_reward);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnSideNavFragmentInteractionListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnSideNavFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_forall) {
            this.listener.onMenuItemClick(ModuleConst.M_TALE);
        } else if (id == R.id.nav_group) {
            this.listener.onMenuItemClick(ModuleConst.M_GROUP);
        } else if (id == R.id.nav_notice) {
            this.listener.onMenuItemClick(ModuleConst.M_NOTICE);
        } else if (id == R.id.nav_mine) {
            this.listener.onMenuItemClick(ModuleConst.M_MINE);
        } else if (id == R.id.nav_search) {
            this.listener.onMenuItemClick(ModuleConst.M_SEARCH);
        } else if (id == R.id.nav_about) {
            this.listener.onMenuItemClick(ModuleConst.M_ABOUT);
        } else if (id == R.id.nav_reward){
            this.listener.onMenuItemClick(ModuleConst.M_REWARD);
        }
        return true;
    }

    public interface OnSideNavFragmentInteractionListener{
        void onMenuItemClick(int moduleConst);
    }


}
