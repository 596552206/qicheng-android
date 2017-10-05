package me.milechen.qicheng.Adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.milechen.qicheng.Beans.TaleBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/6/7.
 */
public class TaleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private ArrayList<TaleBean> arrayList;
    private View mHeaderView;
    private MyItemClickListener onClickListener;

    public TaleListAdapter(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)//HEADER
        {
            return new MViewHolder(mHeaderView,onClickListener);
        } else {
            MViewHolder holder = new MViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_talelist, parent,
                    false),onClickListener);
            return holder;
        }
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setOnItemClickListener(MyItemClickListener myItemClickListener){
        this.onClickListener = myItemClickListener;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof MViewHolder) {
                int posForArr = getPosInArray(position);
                ((MViewHolder) holder).sponsorNick.setText(arrayList.get(posForArr).getSponsorNick());
                ((MViewHolder) holder).time.setText(arrayList.get(posForArr).getSmartTime() + "");
                ((MViewHolder) holder).paranum.setText(arrayList.get(posForArr).getParaNumber() + "段\u0020|\u0020"+arrayList.get(posForArr).getZan() + "赞");
                ((MViewHolder) holder).tags.setText(arrayList.get(posForArr).getTagString());
                ((MViewHolder) holder).content.setText(arrayList.get(posForArr).getParaOne());
                ((MViewHolder) holder).avatar.setImageURI(Uri.parse(arrayList.get(posForArr).getSponsoravatar()));
                return;
            }
            return;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }
    }

    private int getPosInArray(int pos){
        if(mHeaderView != null){
            return pos-1;
        }else {
            return pos;
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return arrayList.size();
        } else {
            return arrayList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            return TYPE_NORMAL;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }

    }


    class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sponsorNick;
        TextView time;
        TextView paranum;
        TextView tags;
        TextView content;
        SimpleDraweeView avatar;
        MyItemClickListener onItemClickListener;

        public MViewHolder(View itemView,MyItemClickListener onItemClickListener) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }

            sponsorNick = (TextView) itemView.findViewById(R.id.item_talelist_nick_tv);
            time = (TextView) itemView.findViewById(R.id.item_talelist_time_tv);
            paranum = (TextView) itemView.findViewById(R.id.item_talelist_paranum_tv);
            tags = (TextView) itemView.findViewById(R.id.item_talelist_tags_tv);
            content = (TextView) itemView.findViewById(R.id.item_talelist_content_tv);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.item_talelist_avatar_sdv);

            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (this.onItemClickListener != null)this.onItemClickListener.onItemClick(view,getPosInArray(getPosition()));
        }
    }

    public interface MyItemClickListener{
        public void onItemClick(View view,int pos);
    }

}


