package com.toponpaydcb.sdk.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    //手机号正则表达式
    public static final String PHONE = "^((\\+7|7|8)+9([0-9]){9})$";
/**
 * 简单介绍一下正则表达式的结构
 *  ^：表示匹配开始的位置
 * 	[38][0-9]：手机号前两位为13和18，[0-9]表示第三位为0-9任意数字，如130、180。（下同）
 * 	|：表示多个匹配模式，因手机号第二位第三位并未全部使用数字0-9，比如没有141开头的手机号
 * 	\d{8}：匹配8个数字
 * 	$：结束符
 */
    /**
     * 正则表达式匹配判断
     * @param patternStr 匹配规则
     * @param input 需要做匹配操作的字符串
     * @return true if matched, else false
     */
    //此处传入两个值，第一个是手机号的正则表达式，第二个是输入的手机号
    public static boolean isMatchered(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
