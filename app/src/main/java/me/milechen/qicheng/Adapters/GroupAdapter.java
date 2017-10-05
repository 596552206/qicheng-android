package me.milechen.qicheng.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.milechen.qicheng.Beans.GroupBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/7/30.
 */
public class GroupAdapter extends RecyclerView.Adapter {

    private List<GroupBean> groups;

    public GroupAdapter(List<GroupBean> groups){this.groups = groups;}

    private OnGroupItemClickListener listener;

    public void setOnItemClickListener(OnGroupItemClickListener onItemClickListener){this.listener = onItemClickListener;}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grouplist,parent,false),this.listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MViewHolder)holder).name.setText(groups.get(position).getName());
        ((MViewHolder)holder).id.setText("ID:"+groups.get(position).getId());
        ((MViewHolder)holder).member.setText("组员:"+groups.get(position).getMember()+"/12");
        ((MViewHolder)holder).paranum.setText(groups.get(position).getParanumber()+" 段");
        ((MViewHolder)holder).paraone.setText(groups.get(position).getParaone());
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }


    class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView name;
        public TextView id;
        public TextView member;
        public TextView paranum;
        public TextView paraone;
        public OnGroupItemClickListener onGroupItemClickListener;

        public MViewHolder(View itemView ,OnGroupItemClickListener onGroupItemClickListener) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.item_grouplist_name_tv);
            this.id = (TextView) itemView.findViewById(R.id.item_grouplist_id_tv);
            this.member = (TextView) itemView.findViewById(R.id.item_grouplist_member_tv);
            this.paranum = (TextView) itemView.findViewById(R.id.item_grouplist_paranum_tv);
            this.paraone = (TextView) itemView.findViewById(R.id.item_grouplist_content_tv);
            this.onGroupItemClickListener = onGroupItemClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(this.onGroupItemClickListener != null)this.onGroupItemClickListener.onGroupItemClick(groups.get(getPosition()).getId(),getPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(this.onGroupItemClickListener != null)this.onGroupItemClickListener.onGroupItemLongClick(groups.get(getPosition()).getId(),getPosition());
            return true;
        }
    }

    public interface OnGroupItemClickListener{
        void onGroupItemClick(int groupId,int pos);
        void onGroupItemLongClick(int groupId,int pos);
    }
}
