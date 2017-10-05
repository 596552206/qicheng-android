package me.milechen.qicheng.Utils;

import android.os.SystemClock;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpDate;

/**
 * Created by mile on 2017/8/2.
 */
public class TimeManager {
    private static TimeManager instance;
    private long differenceTime;        //以前服务器时间 - 以前服务器时间的获取时刻的系统启动时间
    private boolean isServerTime;       //是否是服务器时间

    private TimeManager() {
    }
    public static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    /**
     * 获取当前时间
     *
     * @return the time
     */
    public synchronized long getServiceTime() {
        if (!isServerTime) {
            //todo 这里可以加上触发获取服务器时间操作
            return System.currentTimeMillis();
        }

        //时间差加上当前手机启动时间就是准确的服务器时间了
        return differenceTime + SystemClock.elapsedRealtime();
    }

    /**
     * 时间校准
     *
     * @param lastServiceTime 当前服务器时间
     * @return the long
     */
    public synchronized long initServerTime(long lastServiceTime) {
        //记录时间差
        differenceTime = lastServiceTime - SystemClock.elapsedRealtime();
        isServerTime = true;
        return lastServiceTime;
    }

    /**
     * Created by mile on 2017/8/2.
     */
    public static class TimeCalibrationInterceptor implements Interceptor {
        long minResponseTime = Long.MAX_VALUE;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.nanoTime();
            Response response = chain.proceed(request);
            long responseTime = System.nanoTime() - startTime;

            Headers headers = response.headers();
            calibration(responseTime, headers);
            return response;
        }

        private void calibration(long responseTime, Headers headers) {
            if (headers == null) {
                return;
            }

            //如果这一次的请求响应时间小于上一次，则更新本地维护的时间
            if (responseTime >= minResponseTime) {
                return;
            }

            String standardTime = headers.get("Date");
            if (!TextUtils.isEmpty(standardTime)) {
                Date parse = HttpDate.parse(standardTime);
                if (parse != null) {
                    // 客户端请求过程一般大于比收到响应时间耗时，所以没有简单的除2 加上去，而是直接用该时间
                    getInstance().initServerTime(parse.getTime());
                    minResponseTime = responseTime;
                }
            }
        }
    }
}