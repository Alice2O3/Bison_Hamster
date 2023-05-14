package C_Bison.Language.Rules.Lexing;

import C_Bison.Grammar.Types.*;

public class Convert {
    public static String Grammar_List_to_str_full(Grammar_list grammar_list){
        StringBuilder ret = new StringBuilder();
        for(Grammar_rule rule : grammar_list.val){
            ret.append(String.format("<%d> -> ", rule.first));
            boolean flag = false;
            for(Grammar_expr expr : rule.second.val){
                if(flag){
                    ret.append(" | ");
                } else {
                    flag = true;
                }
                if(expr.val.isEmpty()){
                    ret.append("@");
                } else {
                    for(Grammar_pair p : expr.val){
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

    public static String Grammar_List_to_str(Grammar_list grammar_list){
        StringBuilder ret = new StringBuilder();
        for(Grammar_rule rule : grammar_list.val){
            ret.append(String.format("<%d> -> ", rule.first));
            boolean flag = false;
            for(Grammar_expr expr : rule.second.val){
                if(flag){
                    ret.append(" | ");
                } else {
                    flag = true;
                }
                if(expr.val.isEmpty()){
                    ret.append("@");
                } else {
                    for(Grammar_pair p : expr.val){
                        ret.append(String.format("<%d>", p.second));
                    }
                }
            }
            ret.append("\n");
        }
        return ret.toString();
    }
}
