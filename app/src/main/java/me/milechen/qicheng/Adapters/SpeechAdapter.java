package me.milechen.qicheng.Adapters;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.milechen.qicheng.Beans.SpeechBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/8/6.
 */
public class SpeechAdapter extends RecyclerView.Adapter {

    private List<SpeechBean> speeches;
    private int myId;
    private boolean sdkEnable;
    private MyItemLongClickListener onLongClickListener;

    public void setOnLongClickListener(MyItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public SpeechAdapter(List<SpeechBean> speechBeanList,int myId){
        this.speeches = speechBeanList;
        this.myId = myId;
        this.sdkEnable = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discuss,parent,false),this.onLongClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MViewHolder)holder).nick.setText(speeches.get(position).getNick());
        StringBuilder sb = new StringBuilder(new String());
        if(speeches.get(position).getAtuser() != -1){
            sb.append("<font color='#b39f89'>@"+speeches.get(position).getAtusernick()+"</font>·");
        }
        if(speeches.get(position).getAtpara() != -1){
            sb.append("<font color='#b39f89'>@第"+speeches.get(position).getAtpara()+"段</font>·");
        }
        sb.append(speeches.get(position).getContent());
        if (sdkEnable) {
            ((MViewHolder)holder).content.setText(Html.fromHtml(sb.toString(),Html.FROM_HTML_MODE_COMPACT));
        }else {
            ((MViewHolder)holder).content.setText(Html.fromHtml(sb.toString()));
        }
        if(speeches.get(position).getUserid() == this.myId){
            ((MViewHolder)holder).nick.setGravity(Gravity.RIGHT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            ((MViewHolder)holder).content.setLayoutParams(lp);
            ((MViewHolder)holder).content.setBackgroundResource(R.drawable.text_bubble_right);
        }
    }

    @Override
    public int getItemCount() {
        return this.speeches.size();
    }


    private class MViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView nick;
        TextView content;
        MyItemLongClickListener myItemLongClickListener;

        public MViewHolder(View itemView,MyItemLongClickListener myItemLongClickListener) {
            super(itemView);
            nick = (TextView) itemView.findViewById(R.id.item_discuss_nick_tv);
            content = (TextView) itemView.findViewById(R.id.item_discuss_content_tv);
            this.myItemLongClickListener = myItemLongClickListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (this.myItemLongClickListener != null){
                this.myItemLongClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }else{
                return false;
            }
        }
    }

    public interface MyItemLongClickListener{
        void onItemLongClick(View view ,int pos);
    }

}
