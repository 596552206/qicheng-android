package me.milechen.qicheng.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.lzy.okhttputils.callback.StringCallback;

import me.milechen.qicheng.QichengApplication;
import me.milechen.qicheng.Utils.Net.UserNetUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by mile on 2017/8/9.
 */
public class QichengIntentService extends GTIntentService {


    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ii","OnBind");
        return super.onBind(intent);
    }

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        //Log.e("gt", "onReceiveClientId -> " + "clientid = " + s);
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        String savedClientId = sp.getString("clientid",null);
        if(!s.equals(savedClientId)){
            //client发生变动
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("clientid",s);
            editor.commit();//更改本地clientId
            UserNetUtil userNetUtil = new UserNetUtil();
            userNetUtil.updateClientId(s, sp.getInt("id",-1), new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.i("ii","服务器已更新clientId");
                }
            });//服务器同步clientId
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String messageS = new String(gtTransmitMessage.getPayload());
        int code = Integer.valueOf(messageS.substring(0,3));
        String data = new String();
        if(messageS.length()>3){
            data = messageS.substring(3);
        }else {
            data = null;
        }
        Log.i("ii","QichengIntentService收到透传信息：code="+code+" , data="+data);
        sendMessage(code,data);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    private void sendMessage(int code,String data){
        if(code>=500&&code<=549) {
            Log.i("ii","QichengIntentService正在派送消息给QichengApplication");
            Message message = new Message();
            message.what = code;
            message.obj = data;
            ((QichengApplication)getApplication()).sendMessageToTaleActivity(message);

        }else if(code>=550&&code<=599){
            //GroupActivity
            Log.i("ii","QichengIntentService正在派送消息给QichengApplication");
            Message message = new Message();
            message.what = code;
            message.obj = data;
            ((QichengApplication)getApplication()).sendMessageToGraleActivity(message);
        }
    }

}
