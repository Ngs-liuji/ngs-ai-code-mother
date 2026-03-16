package com.ngsliuji.ngsaicodemother;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public static void main(String[] args) {
        String s = "abcabcbb";
        Map<Character,Integer> dict = new HashMap<>();
        int i=-1,j=0,n=s.length(),res=0;
        for(;j<n;j++){

            if(dict.containsKey(s.charAt(j))){
               i++;
               j=i;
               res = Math.max(res,j-i);
            }
            dict.put(s.charAt(j),j);
        }
    }

}
