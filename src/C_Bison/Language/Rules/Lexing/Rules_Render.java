package C_Bison.Language.Rules.Lexing;

import C_Bison.Grammar.Grammar_Types.*;

import java.util.Map;

public class Rules_Render {
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
                    ret.append("<EMPTY>");
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

    public static String get_Symbol_Info(Rules_Workflow workflow){
        StringBuilder ret = new StringBuilder();
        ret.append("Symbols:\n");
        int symbol_cnt = 0;
        Boolean flag = false;
        for(String s : workflow.getSymbol_list()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", s, symbol_cnt));
            symbol_cnt++;
        }
        ret.append("\n\nTerminals:\n");
        flag = false;
        for(Map.Entry<String, Integer> p : workflow.getTerminal_map().entrySet()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", p.getKey(), p.getValue()));
        }
        ret.append("\n\nNon_Terminals:\n");
        flag = false;
        for(Map.Entry<String, Integer> p : workflow.getNon_terminal_map().entrySet()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", p.getKey(), p.getValue()));
        }
        ret.append("\n");
        return ret.toString();
    }
}
