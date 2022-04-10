package util;

import java.util.ArrayList;
import java.util.List;

public class NLPUtil {
    public static List<String> snakeSplit(String name){
        List<String>result = new ArrayList<>();
        String[]names = name.split("::");
        for(String n:names){
            String[]S = n.split("_");
            for(String s:S){
                if(!s.trim().equals("")){
                    result.add(s.trim());
                }
            }
        }
        return result;
    }

    public static List<String>camelSplit(List<String>names){
        List<String>result = new ArrayList<>();
        for(String name:names){
            int prev=0;
            for(int i=0;i<name.length();++i){
                if(i < name.length()-1 && Character.isLowerCase(name.charAt(i)) && Character.isUpperCase(name.charAt(i + 1))) {
                    result.add(name.substring(prev,i+1).toLowerCase());
                    prev=i+1;
                }
            }
            result.add(name.substring(prev).toLowerCase());
        }
        return result;
    }
}
