package LL1.Process_Lexing;

import LL1.Process;
import LL1.Process.Types.*;
import LL1.Types.*;

import java.util.Map;

public class Render {
    private final static String empty_tag = "@";
    private final static String end_tag = "#";

    private static String space_n(Integer n){
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < n; i++){
            ret.append(' ');
        }
        return ret.toString();
    }

    private static String str_align(String S, Integer table_space){
        int len = table_space - S.length();
        if(len <= 0){
            return S;
        }
        int len_l = len / 2, len_r = len - len_l;
        return space_n(len_l) + S + space_n(len_r);
    }

    private static String expr_pair_to_str(LL1_expr_pair expr_pair){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<%d> -> ", expr_pair.first));
        LL1_expr expr = expr_pair.second;
        if(expr.val.isEmpty()){
            sb.append(empty_tag);
        }
        else {
            for(LL1_pair pk : expr.val){
                sb.append(String.format("<%d>", pk.second));
            }
        }
        return sb.toString();
    }

    public static String LL1_Table_Render(Process ll1, Integer table_space){
        StringBuilder sb = new StringBuilder();
        Process.Types.LL1_expr_pair_list expr_pair_index = ll1.get_expr_pair_index();
        Process.Types.LL1_symbol_set terminal_set = ll1.get_terminals();
        Process.Types.LL1_table LL1_Table = ll1.get_LL1_table();
        sb.append(str_align("LL1 Table", table_space));
        for(Integer s : terminal_set.val){
            if(s.equals(ll1.get_end_tag())){
                sb.append(str_align(end_tag, table_space));
            } else {
                sb.append(str_align(String.format("<%d>", s), table_space));
            }
        }
        sb.append("\n");
        for(Map.Entry<Integer, Process.Types.LL1_symbol_index> pi: LL1_Table.val.entrySet()){
            sb.append(str_align(String.format("<%d>", pi.getKey()), table_space));
            Process.Types.LL1_symbol_index S = pi.getValue();
            for(Integer pj : terminal_set.val){
                if(S.val.containsKey(pj)){
                    Integer ind = S.val.get(pj);
                    Process.Types.LL1_expr_pair expr_pair = expr_pair_index.val.get(ind);
                    sb.append(str_align(expr_pair_to_str(expr_pair), table_space));
                }
                else {
                    sb.append(str_align("-", table_space));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
