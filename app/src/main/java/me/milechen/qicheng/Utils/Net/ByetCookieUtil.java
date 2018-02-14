package me.milechen.qicheng.Utils.Net;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;

import java.util.ArrayList;

/**
 * Created by mile on 2017/10/22.
 */
public class ByetCookieUtil {
    public void copeByetHost(WebView webView){
        class MyWebviewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                Log.i("ii","cookie string:"+cookieStr);
                String ch = getCookie(cookieStr);
                Log.i("ii", "Byet Cookies = " + ch);
                Log.i("ii", "Trying to set common header for Byet Cookies");
                setCh(ch);
                super.onPageFinished(view, url);
            }
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(Urls.ROOT);
        webView.setWebViewClient(new MyWebviewClient());
    }
    public String getCookie(String s){
        s = s.replace(" ", "");//好吧终于找到原因了，因为这前面又一个，，，空格。。。。MDZZ！！！
        String[] cookies = s.split(";");
        String out = null;
        for (String str : cookies) {
            String[] cookie = str.split("=");
            if (cookie[0].toString().equals("__test")) {
                out = cookie[1].toString();
            } else {
                Log.i("ii", str + "不是需要的cookie");
            }
        }
        return out;
    }
    public void setCh(String s){
        HttpHeaders headers = new HttpHeaders();//设置公共header所有的 header 都 不支持 中文
        headers.put("Cookie", "__test="+s+"; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
        OkHttpUtils.getInstance().addCommonHeaders(headers);
    }
}
