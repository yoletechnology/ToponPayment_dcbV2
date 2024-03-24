package com.toponpaydcb.sdk.tool;

public class PhoneMccMnc {
    private static String[] _mcc = new String[]{"",""};
    private static String[] _mnc = new String[]{"",""};

    public PhoneMccMnc() {
    }

    public void setPhoneMccMnc(String[] mcc, String[] mnc) {
        this._mcc = mcc;
        this._mnc = mnc;
    }

    public String getMccWithMnc(int sole) {
        if(this._mcc[sole].length() <= 0 || this._mnc[sole].length() <= 0)
        {
            return "";
        }
        return (this._mcc[sole] + "-" + this._mnc[sole]);
    }

    public String getMcc(int sole) {
        return this._mcc[sole];
    }

    public String getMnc(int sole) {
        return this._mnc[sole];
    }
}