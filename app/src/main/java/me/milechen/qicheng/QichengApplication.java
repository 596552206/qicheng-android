package me.milechen.qicheng;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;

import me.milechen.qicheng.Utils.TimeManager;

/**
 * Created by mile on 2017/7/8.
 */
public class QichengApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        HttpHeaders headers = new HttpHeaders();//设置公共header所有的 header 都 不支持 中文
        //headers.put("Cookie", "__test=c3c35e69c0a3bbbf5cb67174f52f987a; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");  //这是用来解决免费空间服务商Byet.host的检查插件，具体见https://stackoverflow.com/questions/31912000/byethost-server-passing-html-values-checking-your-browser-with-json-string
        /**
        这一段注释来发表一下感慨：2017年十月五日凌晨三点左右，我成功注册免费milez.ml域名及qicheng.milez.ml子域名，并且成功申请免费空间byet.host。
        次日（及2017年十月六日）凌晨一点左右，我将服务器代码上传到主机，但出现一个严重的问题：原本应该返回json的操作返回一段html而不是json。
        尝试各种方法，没有进展。最后google，在stackoverflow找到解决方案。
        地址https://stackoverflow.com/questions/31912000/byethost-server-passing-html-values-checking-your-browser-with-json-string
        公元2017年10月6日，凌晨2:18.
         I:e003a1f9b83314ed8fdd568c2d86fad8
         ii：40ec623f288f01e8e5ed51e8c85b2e1a
         iii:647fa91486476aa40be2f1b8aa7e2af0
         **/

        HttpParams params = new HttpParams();
        //所有的 params 都 支持 中文
        //params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //OkHttpUtils.getInstance().setCookieStore(new PersistentCookieStore());
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
        OkHttpUtils.getInstance().addInterceptor(new TimeManager.TimeCalibrationInterceptor());

        Fresco.initialize(this);


    }

    public Handler handlerForTaleActivity;
    public Handler handlerForGraleActivity;
    public void sendMessageToTaleActivity(Message message){
        if(handlerForTaleActivity!=null){
            Log.i("ii","QichengApplication已收到，正在转发给TaleActivity，消息：code="+message.what+" , data="+message.obj);
            handlerForTaleActivity.sendMessage(message);
        }
    }
    public void sendMessageToGraleActivity(Message message){
        if(handlerForGraleActivity != null){
            Log.i("ii","QichengApplication已收到，正在转发给GraleActivity，消息：code="+message.what+" , data="+message.obj);
            handlerForGraleActivity.sendMessage(message);
        }
    }
}
