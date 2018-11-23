package com.netposa.commonres.utils;

import java.util.List;

public class TextMark {
    /**
     * 多关键字查询表红,避免后面的关键字成为特殊的HTML语言代码
     * @param str 		检索结果
     * @param inputs	关键字集合
     * @param resStr	表红后的结果
     */
    public static StringBuffer addChild(String str, List<String> inputs, StringBuffer resStr){
        int index=str.length();//用来做为标识,判断关键字的下标
        String next="";//保存str中最先找到的关键字
        for (int i = inputs.size() -1 ; i>= 0;i--) {
            String theNext=inputs.get(i);
            int theIndex=str.indexOf(theNext);
            if(theIndex==-1){//过滤掉无效关键字
                inputs.remove(i);
            }else if(theIndex<index){
                index=theIndex;//替换下标
                next=theNext;
            }
        }

        //如果条件成立,表示串中已经没有可以被替换的关键字,否则递归处理
        if(index==str.length()){
            resStr.append(str);
        }else{
            resStr.append(str.substring(0,index));
            resStr.append("<font color='#2D87F9'>"+str.substring(index,index+next.length())+"</font>");
            String str1=str.substring(index+next.length(),str.length());
            addChild(str1,inputs,resStr);//剩余的字符串继续替换
        }
        return resStr;
    }

}
