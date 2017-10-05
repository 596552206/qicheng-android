package me.milechen.qicheng.Utils.Net;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.StringCallback;

/**
 * Created by mile on 2017/7/13.
 */
public class TagNetUtil {
    public void fetchTags(StringCallback callback){
        OkHttpUtils.get(Urls.GET_TAGS)     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("GET_TAGS")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(callback);
    }
}
