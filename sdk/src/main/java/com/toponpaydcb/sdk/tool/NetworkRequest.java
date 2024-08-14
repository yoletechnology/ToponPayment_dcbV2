package com.toponpaydcb.sdk.tool;


import android.util.Log;

import com.toponpaydcb.sdk.callback.CreateSdkCallBackFunction;
import com.toponpaydcb.sdk.callback.InitCallBackFunction;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkRequest {
    public String TAG = "Yole_NetworkRequest";

    public void init(String appKey, String secretkey, InitCallBackFunction initCallBackFunction) {


        JSONObject formBody = new JSONObject();
        try {
            EncodeBaseDataV2 data = NetUtil.RestApiRequest("", secretkey);
            formBody.put("appKey", appKey);
            formBody.put("sign", data.sign);
            formBody.put("content", "");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onlineInit-formBody-error:" + e.toString());
        }


        String res = NetUtil.sendPost("https://api.yolesdk.com/v2/api/ruBankCard/transaction/initBySdk", formBody);
        Log.d(TAG, "onlineInit" + res);

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if (status.indexOf("SUCCESS") == -1) {
                initCallBackFunction.onCallBack(false, "");
            } else {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);

                String content_sign = contentJsonObject.getString("sign");
                String content_content = contentJsonObject.getString("content");
                String decode_content = NetUtil.decodeBase64(content_content);

                initCallBackFunction.onCallBack(true, decode_content);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            initCallBackFunction.onCallBack(false, "");
        }


    }

    public void createBySdk(String appKey, String secretkey, double amount, String orderNumber,String countryCode, String currency, String orderDescription, CreateSdkCallBackFunction callBackFunction) {
        JSONObject requestBody = new JSONObject ();
        try {
            requestBody.put("amount",amount);
            requestBody.put("orderNumber",orderNumber);
            requestBody.put("countryCode",countryCode);
            requestBody.put("currency",currency);
            requestBody.put("orderDescription",orderDescription);
            Log.d(TAG, "createBySdk-content:"+requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "createBySdk-requestBody-error:"+e.toString());
        }

        JSONObject formBody = new JSONObject();
        try {
            EncodeBaseDataV2 data = NetUtil.RestApiRequest(requestBody.toString(), secretkey);
            formBody.put("appKey", appKey);
            formBody.put("sign", data.sign);
            formBody.put("content", data.content);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "createBySdk-formBody-error:" + e.toString());
        }


        String res = NetUtil.sendPost("https://api.yolesdk.com/v2/api/ruBankCard/transaction/createBySdk", formBody);
        Log.d(TAG, "createBySdk" + res);

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if (status.indexOf("SUCCESS") == -1) {
                callBackFunction.onCallBack(false, "");
            } else {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);

                String content_sign = contentJsonObject.getString("sign");
                String content_content = contentJsonObject.getString("content");
                String decode_content = NetUtil.decodeBase64(content_content);

                callBackFunction.onCallBack(true, decode_content);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callBackFunction.onCallBack(false, "");
        }
    }

    public void savePayRecordBySdk(String appKey, String secretkey,  String billingNumber, String invoiceId,String  paymentStatus) {

        JSONObject requestBody = new JSONObject ();
        try {
            requestBody.put("billingNumber",billingNumber);
            requestBody.put("invoiceId",invoiceId);
            requestBody.put("paymentStatus",paymentStatus);
            Log.d(TAG, "createBySdk-content:"+requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "savePayRecordBySdk-requestBody-error:"+e.toString());
        }

        JSONObject formBody = new JSONObject();
        try {
            EncodeBaseDataV2 data = NetUtil.RestApiRequest(requestBody.toString(), secretkey);
            formBody.put("appKey", appKey);
            formBody.put("sign", data.sign);
            formBody.put("content", data.content);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "savePayRecordBySdk-formBody-error:" + e.toString());
        }


        String res = NetUtil.sendPost("https://api.yolesdk.com/v2/api/ruBankCard/transaction/savePayRecordBySdk", formBody);
        Log.d(TAG, "savePayRecordBySdk" + res);

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            Log.d(TAG, "savePayRecordBySdk-status:"+status+";errorCode="+errorCode+";message="+message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void saveSdkResult(String appKey, String secretkey,  String billingNumber, String invoiceResponse) {

        JSONObject requestBody = new JSONObject ();
        try {
            requestBody.put("InvoiceResponse",invoiceResponse);
            Log.d(TAG, "saveSdkResult-content:"+requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "saveSdkResult-requestBody-error:"+e.toString());
        }

        JSONObject formBody = new JSONObject();
        try {
            EncodeBaseDataV2 data = NetUtil.RestApiRequest(requestBody.toString(), secretkey);
            formBody.put("appKey", appKey);
            formBody.put("sign", data.sign);
            formBody.put("content", data.content);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "saveSdkResult-formBody-error:" + e.toString());
        }


        String res = NetUtil.sendPost("https://api.yolesdk.com/v2/api/ruBankCard/transaction/saveSdkResult", formBody);
        Log.d(TAG, "saveSdkResult" + res);

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            Log.d(TAG, "saveSdkResult-status:"+status+";errorCode="+errorCode+";message="+message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
