package me.milechen.qicheng.Utils.Net;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.Utils.TimeManager;

/**
 * Created by mile on 2017/7/21.
 */
public class ParaNetUtil {
    public void fetchParas(int taleId, int paraNumAfter, int limit, StringCallback callback){
        OkHttpUtils.get(Urls.GET_PARAS_OF_A_TALE)
                .tag(this)
                .params("taleId",taleId)
                .params("paraNumAfter",paraNumAfter)
                .params("limit",limit)
                .cacheKey("TALE_PARAS")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void toggleZan(int paraId,int userId,StringCallback callback){
        OkHttpUtils.get(Urls.TOGGLE_ZAN)
                .tag(this)
                .params("paraId",paraId)
                .params("userId",userId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void isZaned(int paraId,int userId,StringCallback callback){
        OkHttpUtils.get(Urls.IS_USER_ZAN_PARA)
                .tag(this)
                .params("paraId",paraId)
                .params("userId",userId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void writePara(int userId,int taleId,String content,StringCallback callback){
        OkHttpUtils.post(Urls.WRITE_PARA)
                .tag(this)
                .params("userId",userId)
                .params("taleId",taleId)
                .params("content",content)
                .params("time", TimeManager.getInstance().getServiceTime())
                .execute(callback);
    }

    public void getZan(int paraId,StringCallback callback){
        OkHttpUtils.get(Urls.GET_PARA_ZAN_NUM)
                .tag(this)
                .params("paraId",paraId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    public void voteToDelete(int user ,int para ,StringCallback callback){
        OkHttpUtils.get(Urls.VOTE_TO_DELETE)
                .tag(this)
                .params("user",user)
                .params("para",para)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    /**
     * 一下是属于Group的Para
     **/

    public void fetchGPara(int groupId,int paraNumAfter,int limit,StringCallback callback){
        OkHttpUtils.get(Urls.GET_PARAS_OF_A_GROUP)
                .tag(this)
                .params("group",groupId)
                .params("paraNumAfter",paraNumAfter)
                .params("limit",limit)
                .cacheKey("GROUP_PARAS")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public void writeGPara(int userId,int groupId,String content,StringCallback callback){
        OkHttpUtils.post(Urls.WRITE_GROUP_PARA)
                .tag(this)
                .params("user",userId)
                .params("group",groupId)
                .params("content",content)
                .params("time", TimeManager.getInstance().getServiceTime())
                .execute(callback);
    }

}
