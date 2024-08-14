package com.toponpaydcb.sdk;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.toponpaydcb.sdk.callback.PayCallBackFunction;
import com.toponpaydcb.sdk.tool.NetworkRequest;
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
    protected NetworkRequest request = new NetworkRequest();

    protected static YoleSdkBase instance = null;

    protected androidx.activity.ComponentActivity _activity = null;

    protected PayCallBackFunction payCallBack = null;
//    public PayCallBackFunction cpPayCallBack = null;
    protected String login = "";
    protected String password = "";
    protected String projectId = "";
    protected String source = "";

    public String appkey = "";
    public String secretkey = "";
    public String billingNumber = "";

    protected void initSDKApi(String PROJECT_ID, String LOGIN, String PASSWORD) {
//        String PROJECT_ID = "2090";
//        String LOGIN="AIMO";
//        String PASSWORD = "r&62Q#c9";
        SDKApiOptions options = new SDKApiOptions.Builder(PROJECT_ID, LOGIN, PASSWORD)
                .setSource(source)
                .setLogsUsing(true)
                .setTestMode(true)
                .setDefaultMessage(true)
                .build();
        SDKApi.initSDK(_activity, options);
        Gson gson = new Gson();
        SDKApi.listenerCallbackApi(new CallbackApiDCB() {
            @Override
            public void error(int code, @Nullable String s, @Nullable String s1) {
                Log.e(TAG, "error 0 =" + s);
                Log.e(TAG, "error 1 =" + s1);
                if (s != null && payCallBack != null) {
//                    InvoiceResponse temp = new InvoiceResponse();
//                    InvoiceResponse.InvoiceInfo info = new InvoiceResponse.InvoiceInfo();
//                    info.setMInvoiceID("");
//                    temp.setMInvoiceInfo(info);
                    payCallBack.onCallBack(false, s, "",s);

                    LoadingDialog.getInstance(_activity).hideDialog();
                }
            }

            @Override
            public void successPaymentResult(@NonNull InvoiceResponse invoiceResponse, int i) {
                Log.e(TAG, "successPaymentResult");

                if (payCallBack != null) {

                    String invoiceID = invoiceResponse.getMInvoiceInfo().getMInvoiceID();
                    String paymentStatus = invoiceResponse.getMInvoiceInfo().getMPaymentStatus();

                    payCallBack.onCallBack(true, "", invoiceID,paymentStatus);

                    LoadingDialog.getInstance(_activity).hideDialog();
                }

            }

            @Override
            public void pinStatus(@Nullable Integer integer) {
                Log.e(TAG, "pinStatus");
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

    protected void startPay(String _billingNumber, String _productName, String _appName,double _price, String _currency,PayCallBackFunction call) {

        payCallBack = call;
        String orderId = _billingNumber;//订单号
        String productName = _productName;//商品名称
        String appName = _appName;//游戏名称
        double number = _price;//价格
        String currency = _currency;//货币类型
        boolean includingVAT = true;
        SDKApi.startPayment(_activity, new DCBPrice(orderId, productName, appName, number, currency, includingVAT), false);
    }
}
