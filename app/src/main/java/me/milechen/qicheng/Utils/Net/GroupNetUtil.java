package me.milechen.qicheng.Utils.Net;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.StringCallback;

/**
 * Created by mile on 2017/7/30.
 */
public class GroupNetUtil {
    public void fetchGroups(int user, StringCallback callback){
        OkHttpUtils.get(Urls.GET_GROUP_BY_USER)
                .tag(this)
                .params("user",user)
                .execute(callback);
    }

    public void fetchGroupView(int group,StringCallback callback){
        OkHttpUtils.get(Urls.GET_GROUP_VIEW)
                .tag(this)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void setActive(int user,int group){
        OkHttpUtils.get(Urls.SET_USER_ACTIVE_IN_GROUP)
                .tag(this)
                .params("user",user)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void unsetActive(int user,int group){
        OkHttpUtils.get(Urls.UNSET_USER_ACTIVE_IN_GROUP)
                .tag(this)
                .params("user",user)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void askPush(int user,int group){
        OkHttpUtils.get(Urls.ASK_FOR_PUSH_GROUP_STATUS)
                .tag(this)
                .params("user",user)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void setSilent(int group){
        OkHttpUtils.get(Urls.SET_GROUP_SILENT)
                .tag(this)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void unsetSilent(int group){
        OkHttpUtils.get(Urls.UNSET_GROUP_SILENT)
                .tag(this)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void fetchSpeechesOld(int group,long timeBefore,StringCallback callback){
        OkHttpUtils.get(Urls.GET_GROUP_SPEECH_OLD)
                .tag(this)
                .params("group",group)
                .params("timeBefore",timeBefore)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void fetchSpeechesNew(int group,long timeAfter,StringCallback callback){
        OkHttpUtils.get(Urls.GET_GROUP_SPEECH_NEW)
                .tag(this)
                .params("group",group)
                .params("timeAfter",timeAfter)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void addSpeech(int group, String content, int user,  int atuser, int atpara, long time, StringCallback callback){
        OkHttpUtils.post(Urls.ADD_GROUP_SPEECHES)
                .tag(this)
                .params("group",group)
                .params("content",content)
                .params("user",user)
                .params("time",time)
                .params("atuser",atuser)
                .params("atpara",atpara)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);

    }

    public void createGroup(String groupName,String password,int user,StringCallback callback){
        OkHttpUtils.post(Urls.CREATE_GROUP)
                .params("user",user)
                .params("name",groupName)
                .params("password",password)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void joinGroup(int group,int user,int password,StringCallback callback){
        OkHttpUtils.post(Urls.JOIN_GROUP)
                .params("user",user)
                .params("group",group)
                .params("password",password)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void quitGroup(int group,int user,StringCallback callback){
        OkHttpUtils.get(Urls.QUIT_GROUP)
                .params("user",user)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void getGroupMembers(int group,StringCallback callback){
        OkHttpUtils.get(Urls.GET_GROUP_MEMBERS)
                .params("group",group)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }
}
