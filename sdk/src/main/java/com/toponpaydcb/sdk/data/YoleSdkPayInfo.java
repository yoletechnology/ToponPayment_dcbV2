package com.toponpaydcb.sdk.data;

public class YoleSdkPayInfo {
    private String appName = "";//游戏名称
    private String productName = "";//商品名称
    private double amount = 0.0;
    private String countryCode = "";//"RU"
    private String currency = "";//"RUB"
    private String orderNumber = "";

    private String orderDescription = "";



    public void setAppName(String value)
    {
        appName = value;
    }
    public void setProductName(String value)
    {
        productName = value;
    }
    public void setAmount(double value)
    {
        amount = value;
    }
    public void setCountryCode(String value)
    {
        countryCode = value;
    }
    public void setCurrency(String value)
    {
        currency = value;
    }
    public void setOrderId(String value)
    {
        orderNumber = value;
    }
    public void setOrderDescription(String value)
    {
         orderDescription = value;
    }

    public String getAppName()
    {
        return appName;
    }
    public String getProductName()
    {
        return productName;
    }
    public double getAmount()
    {
        return amount;
    }
    public String getCountryCode()
    {
        return countryCode;
    }
    public String getCurrency()
    {
        return currency;
    }
    public String getOrderId()
    {
        return orderNumber;
    }
    public String getOrderDescription()
    {
        return orderDescription;
    }


}
