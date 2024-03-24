package com.toponpaydcb.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ComponentActivity;

import com.toponpaydcb.sdk.callback.CreateSdkCallBackFunction;
import com.toponpaydcb.sdk.callback.InitCallBackFunction;
import com.toponpaydcb.sdk.callback.PayCallBackFunction;
import com.toponpaydcb.sdk.tool.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ru.ifree.dcblibrary.CallbackApiDCB;
import ru.ifree.dcblibrary.DCBPrice;
import ru.ifree.dcblibrary.SDKApi;
import ru.ifree.dcblibrary.SDKApiOptions;
import ru.ifree.dcblibrary.api.request_data.AuthorizeCustomerContractResponse;
import ru.ifree.dcblibrary.api.request_data.BaseResponse;
import ru.ifree.dcblibrary.api.request_data.InvoiceResponse;

public class YoleSdkBase {
    private String TAG = "Yole_YoleSdkBase";

    /**
     * 各种网络接口
     **/
    public NetworkRequest request = new NetworkRequest();

    public static YoleSdkBase instance = null;

    public androidx.activity.ComponentActivity _activity = null;

    public PayCallBackFunction payCallBack = null;
//    public PayCallBackFunction cpPayCallBack = null;
    public String login = "";
    public String password = "";
    public String projectId = "";

    public void initSDKApi(String PROJECT_ID, String LOGIN, String PASSWORD) {
//        String PROJECT_ID = "2090";
//        String LOGIN="AIMO";
//        String PASSWORD = "r&62Q#c9";
        SDKApiOptions options = new SDKApiOptions.Builder(PROJECT_ID, LOGIN, PASSWORD)
                .setSource("")
                .setLogsUsing(true)
                .setTestMode(true)
                .setDefaultMessage(true)
                .build();
        SDKApi.initSDK(_activity, options);

        SDKApi.listenerCallbackApi(new CallbackApiDCB() {
            @Override
            public void successPaymentResult(@NonNull InvoiceResponse invoiceResponse, int i) {
                Log.e(TAG, "successPaymentResult");

                if (payCallBack != null) {
                    String InvoiceID = invoiceResponse.getMInvoiceInfo().getMInvoiceID();
                    payCallBack.onCallBack(true, "", InvoiceID);

                    LoadingDialog.getInstance(_activity).hideDialog();
                }

            }

            @Override
            public void pinStatus(@Nullable Integer integer) {
                Log.e(TAG, "pinStatus");
            }

            @Override
            public void error(int i, @Nullable String s) {
                Log.e(TAG, "error=" + s);
                if (payCallBack != null) {
                    payCallBack.onCallBack(false, s, "");

                    LoadingDialog.getInstance(_activity).hideDialog();
                }
            }

            @Override
            public void contractInfo(@Nullable String s, @Nullable String s1) {
                Log.e(TAG, "contractInfo");
            }

            @Override
            public void cancelContractResult(@NonNull BaseResponse baseResponse) {
                Log.e(TAG, "cancelContractResult");
            }

            @Override
            public void authorizationContractResult(@NonNull AuthorizeCustomerContractResponse authorizeCustomerContractResponse) {
                Log.e(TAG, "authorizationContractResult");
            }
        });
    }

    public void startPay(double _price, String billingNumber, PayCallBackFunction call) {

        payCallBack = call;
        String orderId = billingNumber;//订单号
        String productName = "商品名称";//商品名称
        String appName = "游戏名称";//游戏名称
        double number = _price;//价格
        String currency = "RUB";//货币类型
        boolean includingVAT = true;
        SDKApi.startPayment(_activity, new DCBPrice(orderId, productName, appName, number, currency, includingVAT), true);
    }
}
