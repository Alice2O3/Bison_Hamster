package LL1.Process_Lexing;

import LL1.Process;
import LL1.Process.Types.*;
import LL1.Types.*;

import java.util.Map;

public class Convert {
    public static String LL1_List_to_str_full(LL1_list ll1_list){
        StringBuilder ret = new StringBuilder();
        for(LL1_rule rule : ll1_list.val){
            ret.append(String.format("<%d> -> ", rule.first));
            boolean flag = false;
            for(LL1_expr expr : rule.second.val){
                if(flag){
                    ret.append(" | ");
                } else {
                    flag = true;
                }
                if(expr.val.isEmpty()){
                    ret.append("@");
                } else {
                    for(LL1_pair p : expr.val){
                        String s;
                        if(p.first){
                            s = "TERMINAL";
                        } else {
                            s = "NON_TERMINAL";
                        }
                        ret.append(String.format("<%s, %d>", s, p.second));
                    }
                }
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    public static String LL1_List_to_str(LL1_list ll1_list){
        StringBuilder ret = new StringBuilder();
        for(LL1_rule rule : ll1_list.val){
            ret.append(String.format("<%d> -> ", rule.first));
            boolean flag = false;
            for(LL1_expr expr : rule.second.val){
                if(flag){
                    ret.append(" | ");
                } else {
                    flag = true;
                }
                if(expr.val.isEmpty()){
                    ret.append("@");
                } else {
                    for(LL1_pair p : expr.val){
                        ret.append(String.format("<%d>", p.second));
                    }
                }
            }
            ret.append("\n");
        }
        return ret.toString();
    }
}
