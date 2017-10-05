package me.milechen.qicheng.Utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

import me.milechen.qicheng.Beans.QRCodeGroupData;

/**
 * Created by mile on 2017/10/3.
 */
public class QRCodeTranslator {

    private String codeString;
    private String[] codeStringA;

    private String header = "qicheng";
    private String body;

    public static final String JOIN_GROUP = "join_group";

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    private String data;

    public QRCodeTranslator(){

    }

    public QRCodeTranslator(String codeString){
        this.codeString = codeString;
        this.codeStringA = codeString.split("\\.");
    }

    public QRCodeTranslator translate(){
            this.body = codeStringA[1];
            this.data = codeStringA[2];
        return this;
    }

    public boolean isAvailable(){
        if(codeStringA.length == 3 && codeStringA[0].equals(this.header) && isBodyAvailable()) {
            return true;
        }else {
            return false;
        }
    }
    private boolean isBodyAvailable(){
        if(codeStringA[1].equals(JOIN_GROUP))return true;
        //if(body == xxx)return true;
        return false;
    }


    public QRCodeGroupData decodeGroupData(){
        if(this.body.equals("join_group")){
            String[] groupDataStringA = this.data.split("x");
            QRCodeGroupData data = new QRCodeGroupData();
            data.id = Integer.parseInt(groupDataStringA[0]);
            data.password  = Integer.parseInt(groupDataStringA[1]);
            return data;
        }else {
            return null;
        }
    }
    public String encodeQRCodeString(){
        if(this.body != null && this.data != null){
            this.codeString = this.header+"."+this.body+"."+this.data;
            return codeString;
        }else {
            return null;
        }
    }


    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
