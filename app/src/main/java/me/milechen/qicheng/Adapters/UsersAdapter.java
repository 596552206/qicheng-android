package me.milechen.qicheng.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.milechen.qicheng.Beans.UserBean;
import me.milechen.qicheng.R;

/**
 * Created by mile on 2017/10/2.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<UserBean> users = new ArrayList<>();

    public UsersAdapter(ArrayList<UserBean> userBeenList){
        this.users = userBeenList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_square,parent,false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MViewHolder)holder).nick.setText(users.get(position).getNick());
        ((MViewHolder)holder).avatar.setImageURI(users.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        TextView nick;
        SimpleDraweeView avatar;

        public MViewHolder(View itemView) {
            super(itemView);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.item_user_avatar_sdv);
            nick = (TextView) itemView.findViewById(R.id.item_user_nick_tv);
        }
    }
}
