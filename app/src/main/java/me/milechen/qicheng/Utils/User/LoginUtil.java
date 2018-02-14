package me.milechen.qicheng.Utils.User;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import me.milechen.qicheng.Beans.UserBean;
import me.milechen.qicheng.Utils.TimeUtil;

/**
 * Created by mile on 2017/7/17.
 */
public class LoginUtil {
    public static int isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        int id = sp.getInt("id",-1);
        if(id == -1){
            return 0; //无记录（未登录）
        }else {
            long lastLoginTime = sp.getLong("loginTime",-1);
            long nowTime = new Date().getTime();
            if((nowTime - lastLoginTime) > TimeUtil.WEEK){
                //一周未登录
                return 2;
            }else{
                return 1;
                //登录

            }
        }
    }

    public static void renewTime(Context context){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        sp.edit().putLong("loginTime",new Date().getTime()).commit();
    }

    public static void saveUser(Context context, UserBean user){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id",user.getId());
        editor.putString("phone",user.getPhone());
        editor.putString("password", user.getPassword());
        editor.putString("nick",user.getNick());
        editor.putBoolean("gender",user.getGender());
        editor.putString("avatar",user.getAvatar());
        editor.putInt("accessibility",user.getAccessibility());
        editor.putInt("zan",user.getZan());
        editor.putInt("level",user.getLevel());
        editor.putString("clientid",user.getClientid());
        editor.commit();

        renewTime(context);
    }

    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("id");
        editor.remove("phone");
        editor.remove("password");
        editor.remove("nick");
        editor.remove("gender");
        editor.remove("avatar");
        editor.remove("accessibility");
        editor.remove("zan");
        editor.remove("level");
        editor.remove("clientid");
        editor.commit();

    }


}
