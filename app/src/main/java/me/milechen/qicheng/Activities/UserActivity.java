package me.milechen.qicheng.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.milechen.qicheng.Fragments.SideNavFragment;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.ModuleConst;
import me.milechen.qicheng.Utils.User.LoginUtil;


public class UserActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView qi = (TextView) findViewById(R.id.toolbar_right_tv);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        Button logout = (Button) findViewById(R.id.user_logout_btn);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Title");

        qi.setText(R.string.qi);
        title.setText(R.string.forall);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        logout.setOnClickListener(this);
        logout.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_logout_btn:
                Toast.makeText(UserActivity.this, "长按以退出", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void doLogout() {
        LoginUtil.logout(this);
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.user_logout_btn:
                doLogout();
                break;
        }
        return false;
    }
}
