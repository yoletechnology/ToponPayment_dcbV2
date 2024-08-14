package com.toponpaydcb.sdk.callback;

import androidx.annotation.Nullable;

import ru.ifree.dcblibrary.api.request_data.InvoiceResponse;

public interface PayCallBackFunction {

    public void onCallBack(boolean result, String info, String invoiceId,  @Nullable String paymentStatus);
}
