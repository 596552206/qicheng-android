package me.milechen.qicheng.Utils.Net;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

/**
 * Created by mile on 2017/7/17.
 */
public class UserNetUtil {
    public void fetchUserAvatarByPhone(String phone, StringCallback callback){
        OkHttpUtils.get(Urls.GET_AVATAR_BY_PHONE)
                .tag(this)
                .params("phone",phone)
                .execute(callback);
    }

    public void fetchUserNickByPhone(String phone, StringCallback callback){
        OkHttpUtils.get(Urls.GET_NICK_BY_PHONE)
                .tag(this)
                .params("phone",phone)
                .execute(callback);
    }

    public void login(String phone,String password,StringCallback callback){
        OkHttpUtils.get(Urls.LOGIN)
                .tag(this)
                .params("phone",phone)
                .params("password",password)
                .execute(callback);
    }

    public void updateClientId(String clientId,int userId,StringCallback callback){
        OkHttpUtils.get(Urls.UPDATE_CLIENT_ID)
                .tag(this)
                .params("user",userId)
                .params("clientid",clientId)
                .execute(callback);
    }
}
