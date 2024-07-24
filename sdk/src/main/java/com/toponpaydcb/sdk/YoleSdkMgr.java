package com.toponpaydcb.sdk;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.toponpaydcb.sdk.callback.CreateSdkCallBackFunction;
import com.toponpaydcb.sdk.callback.InitCallBackFunction;
import com.toponpaydcb.sdk.callback.PayCallBackFunction;
import com.toponpaydcb.sdk.data.YoleSdkPayInfo;

import org.json.JSONException;
import org.json.JSONObject;

import ru.ifree.dcblibrary.DCBPrice;
import ru.ifree.dcblibrary.SDKApi;

public class YoleSdkMgr extends YoleSdkBase {

    private String TAG = "Yole_YoleSdkMgr";
    private static YoleSdkMgr _instance = null;

    public static final String RETURN_INFO = "com.toponpaydcb.sdk.info";


    public String appkey = "";
    public String secretkey = "";
    public String billingNumber = "";

    public static YoleSdkMgr getsInstance() {
        if (YoleSdkMgr._instance == null) {
            YoleSdkMgr._instance = new YoleSdkMgr();
        }
        return YoleSdkMgr._instance;
    }

    private YoleSdkMgr() {
        Log.e(TAG, "YoleSdkMgr");
    }

    public void initSdk(androidx.activity.ComponentActivity _var1, String Appkey, String Secretkey) {
        _activity = _var1;
        appkey = Appkey;
        secretkey = Secretkey;
        final boolean[] isRequestSuccess = {false};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request.init(appkey, secretkey, new InitCallBackFunction() {

                        @Override
                        public void onCallBack(boolean result, String content) {
                            try {
                                isRequestSuccess[0] = result;
                                if (result == true) {
                                    JSONObject decode_contentJsonObject = new JSONObject(content);
                                    login = decode_contentJsonObject.getString("login");
                                    password = decode_contentJsonObject.getString("password");
                                    projectId = decode_contentJsonObject.getString("projectId");
                                    source = decode_contentJsonObject.getString("source");
                                    Log.e(TAG, "initBySdk 成功：login=" + login + ";password=" + password + ";projectId=" + projectId + ";source=" + source);
                                } else {
                                    Log.e(TAG, "initBySdk 失败");
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        try {
            thread.join(); // 等待新线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (isRequestSuccess[0] == true) {
            super.initSDKApi(projectId, login, password);
        }
    }

    private void failCallBack(String stt,PayCallBackFunction call)
    {
        call.onCallBack(false,stt,"");
        LoadingDialog.getInstance(_activity).hideDialog();
    }
    public void startPay(YoleSdkPayInfo _orderInfo,PayCallBackFunction call) {

        LoadingDialog.getInstance(_activity).showDialog();

        String appName = _orderInfo.getAppName();//游戏名称
        if(appName.length() <= 0)
        {
            failCallBack("parameter error：appName",call);
            return;
        }
        String productName = _orderInfo.getProductName();//商品名称
        if(productName.length() <= 0)
        {
            failCallBack("parameter error：productName",call);
            return;
        }
        double amount = _orderInfo.getAmount();
        if(amount <= 0)
        {
            failCallBack("parameter error：amount",call);
            return;
        }
        String countryCode = _orderInfo.getCountryCode();//"RU"
        if(countryCode.length() <= 0)
        {
            failCallBack("parameter error：countryCode",call);
            return;
        }
        String currency = _orderInfo.getCurrency();//"RUB"
        if(currency.length() <= 0)
        {
            failCallBack("parameter error：currency",call);
            return;
        }
        String orderNumber = _orderInfo.getOrderId();
        if(orderNumber.length() <= 0)
        {
            failCallBack("parameter error：orderNumber",call);
            return;
        }
        String orderDescription = _orderInfo.getOrderDescription();
        if(orderDescription.length() <= 0)
        {
            failCallBack("parameter error：orderDescription",call);
            return;
        }


        final boolean[] isRequestSuccess = {false};
        Thread thread = new Thread(() -> {
            request.createBySdk(appkey, secretkey, amount,orderNumber, countryCode, currency, orderDescription, new CreateSdkCallBackFunction() {
                @Override
                public void onCallBack(boolean result, String content) {
                    try {
                        isRequestSuccess[0] = result;
                        if (result == true) {
                            JSONObject decode_contentJsonObject = new JSONObject(content);
                            billingNumber = decode_contentJsonObject.getString("billingNumber");
                            Log.e(TAG, "createBySdk 成功：orderNumber=" + billingNumber);
                        } else {
                            Log.e(TAG, "createBySdk 失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        thread.start();

        try {
            thread.join(); // 等待新线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (isRequestSuccess[0] == true) {
            super.startPay(billingNumber,productName,appName,amount, currency, new PayCallBackFunction(){

                @Override
                public void onCallBack(boolean result, String info, String invoiceId) {

                    call.onCallBack(result,info,invoiceId);

//                    if(result == true)
                    {
                        Thread thread1 = new Thread(() -> {
                            request.savePayRecordBySdk(appkey, secretkey, billingNumber, invoiceId);
                        });
                        thread1.start();
                    }
                }
            });
        }
        else
        {
            failCallBack("Server failed to retrieve order",call);
        }
    }
}
