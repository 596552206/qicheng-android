package me.milechen.qicheng.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.milechen.qicheng.Beans.ParaBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/7/21.
 */
public class ParasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ParaBean> paras;

    public void setOnLongClickListener(MyItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    private MyItemLongClickListener onLongClickListener;

    public ParasAdapter(ArrayList<ParaBean> paras){
        this.paras = paras;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MViewHolder view = new MViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paras,parent,false),onLongClickListener);
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MViewHolder){
            ((MViewHolder)holder).content.setText(paras.get(position).getContent());
            ((MViewHolder)holder).user.setText("Paragraph\u0020"+paras.get(position).getParanum()+"·By\u0020"+paras.get(position).getUsernick()+"\u0020·"+paras.get(position).getSmartTime());
            ((MViewHolder)holder).zan.setText(paras.get(position).getZan()+"·赞");
        }
    }

    @Override
    public int getItemCount() {
        return paras.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        TextView content;
        TextView user;
        TextView zan;
        MyItemLongClickListener itemLongClickListener;

        public MViewHolder(View itemView ,MyItemLongClickListener itemLongClickListener) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.item_paras_content_tv);
            user = (TextView) itemView.findViewById(R.id.item_paras_user_tv);
            zan = (TextView) itemView.findViewById(R.id.item_paras_zan_tv);
            this.itemLongClickListener = itemLongClickListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (this.itemLongClickListener != null){
                this.itemLongClickListener.onItemLongClick(view,getAdapterPosition());
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
