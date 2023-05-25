package Util;

public class StringIO {
    public static String int_to_str(Integer s){
        return Character.toString((char) s.intValue());
    }

    public static String space_n(Integer n){
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < n; i++){
            ret.append(' ');
        }
        return ret.toString();
    }

    public static String str_align_left(String S, Integer table_space){
        int len = table_space - S.length();
        if(len <= 0){
            return S;
        }
        return S + space_n(len);
    }

    public static String str_align_center(String S, Integer table_space){
        int len = table_space - S.length();
        if(len <= 0){
            return S;
        }
        int len_l = len / 2, len_r = len - len_l;
        return space_n(len_l) + S + space_n(len_r);
    }

    public static String tab_n(Integer n){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++){
            sb.append("\t");
        }
        return sb.toString();
    }
}
