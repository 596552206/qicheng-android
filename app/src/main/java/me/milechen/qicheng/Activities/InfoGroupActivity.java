package me.milechen.qicheng.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Adapters.UsersAdapter;
import me.milechen.qicheng.Beans.GroupBean;
import me.milechen.qicheng.Beans.QRCodeGroupData;
import me.milechen.qicheng.Beans.UserBean;
import me.milechen.qicheng.R;
import me.milechen.qicheng.Utils.Net.GroupNetUtil;
import me.milechen.qicheng.Utils.Net.ResponseInvestigator;
import me.milechen.qicheng.Utils.Net.ResponseUtil;
import me.milechen.qicheng.Utils.Net.UserNetUtil;
import me.milechen.qicheng.Utils.QRCodeTranslator;
import okhttp3.Call;
import okhttp3.Response;

public class InfoGroupActivity extends AppCompatActivity {

    private int groupId;
    private TextView idTV;
    private TextView nameTV;
    private TextView passwordTV;
    private TextView paranumTV;
    private TextView membernumTV;
    private RecyclerView membersRV;
    private UsersAdapter usersAdapter;
    private ImageView qrCodeIV;
    private ArrayList<UserBean> members = new ArrayList<UserBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);

        Intent receivedIntent = getIntent();
        this.groupId = receivedIntent.getIntExtra("groupId", -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title_tv);
        idTV = (TextView) findViewById(R.id.info_group_acv_id_tv);
        nameTV = (TextView) findViewById(R.id.info_group_acv_name_tv);
        passwordTV = (TextView) findViewById(R.id.info_group_acv_password_tv);
        paranumTV = (TextView) findViewById(R.id.info_group_acv_paranum_tv);
        membernumTV = (TextView) findViewById(R.id.info_group_acv_members_tv);
        membersRV = (RecyclerView) findViewById(R.id.info_group_acv_members_rv);
        qrCodeIV = (ImageView) findViewById(R.id.info_group_acv_qrcode_iv);

        title.setText("小组信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        usersAdapter = new UsersAdapter(members);
        RecyclerView.LayoutManager lm = new FullyGridLayoutManager(this, 4);
        membersRV.setLayoutManager(lm);
        membersRV.setAdapter(usersAdapter);

        fetchBasicinfo(groupId);
        fetchMembers(groupId);

    }

    private void fetchBasicinfo(int groupId) {
        new GroupNetUtil().fetchGroupView(groupId, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithCertainGroupData(s).investigate(new ResponseInvestigator<GroupBean>() {
                    @Override
                    public void onOK(int status, String detail, GroupBean data) {
                        idTV.setText(data.getId() + "");
                        nameTV.setText(data.getName());
                        passwordTV.setText(data.getPassword());
                        paranumTV.setText(data.getParanumber() + "");
                        membernumTV.setText(data.getMember() + "");
                        generateQRCode(data.getId(), data.getPassword());
                    }

                    @Override
                    public void onErr(int status, String detail) {

                    }
                });
            }
        });
    }

    private void fetchMembers(int groupId) {
        new GroupNetUtil().getGroupMembers(groupId, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ResponseUtil.decodeResponseWithUserListData(s).investigate(new ResponseInvestigator<List<UserBean>>() {
                    @Override
                    public void onOK(int status, String detail, List<UserBean> data) {
                        members.addAll(data);
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErr(int status, String detail) {

                    }
                });
            }
        });
    }

    private void generateQRCode(int id, String password) {
        QRCodeTranslator qrCodeTranslator = new QRCodeTranslator();
        QRCodeGroupData groupData = new QRCodeGroupData();
        groupData.setId(id);
        groupData.setPassword(Integer.parseInt(password));
        String groupDataString = groupData.generateGroupDataString();
        qrCodeTranslator.setBody(QRCodeTranslator.JOIN_GROUP);
        qrCodeTranslator.setData(groupDataString);
        String qrcodeString = qrCodeTranslator.encodeQRCodeString();
        Bitmap bitmap = QRCodeTranslator.generateBitmap(qrcodeString, 200, 200);
        qrCodeIV.setImageBitmap(bitmap);
    }



    public class FullyGridLayoutManager extends GridLayoutManager {
        public FullyGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public FullyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;
            int count = getItemCount();
            int span = getSpanCount();
            for (int i = 0; i < count; i++) {
                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    if (i % span == 0) {
                        width = width + mMeasuredDimension[0];
                    }
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    if (i % span == 0) {
                        height = height + mMeasuredDimension[1];
                    }
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }

            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            if (position < getItemCount()) {
                try {
                    View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException
                    if (view != null) {
                        RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                                getPaddingLeft() + getPaddingRight(), p.width);
                        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                                getPaddingTop() + getPaddingBottom(), p.height);
                        view.measure(childWidthSpec, childHeightSpec);
                        measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                        measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                        recycler.recycleView(view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
