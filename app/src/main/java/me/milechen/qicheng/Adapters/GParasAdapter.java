package me.milechen.qicheng.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.milechen.qicheng.Beans.GParaBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/8/19.
 */
public class GParasAdapter extends RecyclerView.Adapter {

    private ArrayList<GParaBean> paras = new ArrayList<>();
    private OnGroupParaClickListener listener;

    public GParasAdapter(ArrayList<GParaBean> paras){
        this.paras = paras;
    }

    public void setOnGroupParaClickListener(OnGroupParaClickListener onGroupParaClickListener){
        this.listener = onGroupParaClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paras_group,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MViewHolder)holder).info.setText("第"+paras.get(position).getParanum()+"段·"+paras.get(position).getUsernick()+"·"+paras.get(position).getSmartTime());
        ((MViewHolder)holder).content.setText(paras.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return paras.size();
    }


    class MViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView info;
        public TextView content;
        public OnGroupParaClickListener onGroupParaClickListener;

        public MViewHolder(View itemView) {
            super(itemView);
            this.info = (TextView) itemView.findViewById(R.id.item_paras_group_user);
            this.content = (TextView) itemView.findViewById(R.id.item_paras_group_content);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onGroupParaLongClick(getPosition(),paras.get(getPosition()).getParanum());
            return true;
        }
    }

    public interface OnGroupParaClickListener{
        void onGroupParaLongClick(int pos,int paraNum);
    }
}
