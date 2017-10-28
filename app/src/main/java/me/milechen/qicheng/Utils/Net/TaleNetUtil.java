package me.milechen.qicheng.Utils.Net;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;

/**
 * Created by mile on 2017/7/1.
 */
public class TaleNetUtil {


    public void fetchHotTales(long timeBefore,int limit,StringCallback callback){
        OkHttpUtils.get(Urls.GET_HOT_TALE_VIEW)     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .params("timeBefore",timeBefore)
                .params("limit",limit)
                .cacheKey("HOT_TALE_VIEW")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(callback);
    }

    public void fetchNewTales(long timeBefore,int limit,StringCallback callback){
        OkHttpUtils.get(Urls.GET_LATEST_TALE_VIEW)     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .params("timeBefore",timeBefore)
                .params("limit",limit)
                .cacheKey("LATEST_TALE_VIEW")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(callback);
    }

    public void newTale(int sponsorId,ArrayList<Integer> tagsList,long time,String content,StringCallback callback){
        OkHttpUtils.post(Urls.NEW_TALE)
                .tag(this)
                .params("sponsorId",sponsorId)
                .params("tags",new Gson().toJson(tagsList))
                .params("time",time)
                .params("content",content)
                .execute(callback);
    }

    public void fetchTale(int id,StringCallback callback){
        OkHttpUtils.get(Urls.GET_TALE)
                .tag(this)
                .params("taleId",id)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void isUserFocusTale(int userId,int taleId,StringCallback callback){
        OkHttpUtils.get(Urls.IS_USER_FOCUS_TALE)
                .tag(this)
                .params("userId",userId)
                .params("taleId",taleId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void toggleFocus(int userId,int taleId,StringCallback callback){
        OkHttpUtils.get(Urls.TOGGLE_FOCUS)
                .tag(this)
                .params("userId",userId)
                .params("taleId",taleId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void setActive(int user,int tale){
        OkHttpUtils.get(Urls.SET_USER_ACTIVE_IN_TALE)
                .tag(this)
                .params("user",user)
                .params("tale",tale)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void unsetActive(int user,int tale){
        OkHttpUtils.get(Urls.UNSET_USER_ACTIVE_IN_TALE)
                .tag(this)
                .params("user",user)
                .params("tale",tale)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void askPush(int tale,int user){
        OkHttpUtils.get(Urls.ASK_FOR_PUSH_TALE_STATUS)
                .tag(this)
                .params("tale",tale)
                .params("user",user)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void setSilent(int tale){
        OkHttpUtils.get(Urls.SET_TALE_SILENT)
                .tag(this)
                .params("tale",tale)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void unsetSilent(int tale){
        OkHttpUtils.get(Urls.UNSET_TALE_SILENT)
                .tag(this)
                .params("tale",tale)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(null);
    }

    public void fetchSpeechesOld(int tale,long timeBefore,StringCallback callback){
        OkHttpUtils.get(Urls.GET_TALE_SPEECHES_OLD)
                .tag(this)
                .params("tale",tale)
                .params("timeBefore",timeBefore)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void fetchSpeechesNew(int tale,long timeAfter,StringCallback callback){
        OkHttpUtils.get(Urls.GET_TALE_SPEECHES_NEW)
                .tag(this)
                .params("tale",tale)
                .params("timeAfter",timeAfter)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void addSpeech(int tale, String content, int user,  int atuser, int atpara, long time, StringCallback callback){
        OkHttpUtils.post(Urls.ADD_TALE_SPEECHES)
                .tag(this)
                .params("tale",tale)
                .params("content",content)
                .params("user",user)
                .params("time",time)
                .params("atuser",atuser)
                .params("atpara",atpara)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);

    }

}
