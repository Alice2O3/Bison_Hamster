package C_Bison.Grammar.LL1;

import C_Bison.Grammar.LL1.LL1_Types.*;
import C_Bison.Grammar.Grammar_Types.*;
import Util.StringIO;

import java.util.Map;

public class LL1_Render {
    private final static String empty_tag = "@";
    private final static String end_tag = "$";

    private static String render_symbol(Integer s){
        if(s.equals(LL1_Process.End_Tag)){
            return end_tag;
        }
        return String.format("<%d>", s);
    }

    private static String render_symbol_simple(Integer s){
        if(s.equals(LL1_Process.End_Tag)){
            return end_tag;
        }
        return StringIO.int_to_str(s);
    }

    public static String expr_pair_to_str(LL1_expr_pair expr_pair){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<%d> -> ", expr_pair.first));
        Grammar_expr expr = expr_pair.second;
        if(expr.val.isEmpty()){
            sb.append(empty_tag);
        }
        else {
            for(Grammar_pair pk : expr.val){
                sb.append(String.format("<%d>", pk.second));
            }
        }
        return sb.toString();
    }

    public static String expr_pair_to_str_simple(LL1_expr_pair expr_pair){
        StringBuilder sb = new StringBuilder();
        sb.append(StringIO.int_to_str(expr_pair.first));
        sb.append(" -> ");
        Grammar_expr expr = expr_pair.second;
        if(expr.val.isEmpty()){
            sb.append(empty_tag);
        }
        else {
            for(Grammar_pair pk : expr.val){
                sb.append(StringIO.int_to_str(pk.second));
            }
        }
        return sb.toString();
    }

    public static String LL1_Table_Render(LL1_Process ll1, Integer table_space){
        StringBuilder sb = new StringBuilder();
        LL1_expr_pair_list expr_pair_index = ll1.get_expr_pair_index();
        LL1_symbol_set terminal_set = ll1.get_terminals();
        LL1_table LL1_Table = ll1.get_LL1_table();
        sb.append(StringIO.str_align_center("LL1 Table", table_space));
        for(Integer s : terminal_set.val){
            sb.append(StringIO.str_align_center(render_symbol(s), table_space));
        }
        sb.append("\n");
        for(Map.Entry<Integer, LL1_symbol_index> pi: LL1_Table.val.entrySet()){
            sb.append(StringIO.str_align_center(String.format("<%d>", pi.getKey()), table_space));
            LL1_symbol_index S = pi.getValue();
            for(Integer pj : terminal_set.val){
                if(S.val.containsKey(pj)){
                    Integer ind = S.val.get(pj);
                    LL1_expr_pair expr_pair = expr_pair_index.val.get(ind);
                    sb.append(StringIO.str_align_center(expr_pair_to_str(expr_pair), table_space));
                }
                else {
                    sb.append(StringIO.str_align_center("-", table_space));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String LL1_Table_Render_Simple(LL1_Process ll1, Integer table_space){
        StringBuilder sb = new StringBuilder();
        LL1_expr_pair_list expr_pair_index = ll1.get_expr_pair_index();
        LL1_symbol_set terminal_set = ll1.get_terminals();
        LL1_table LL1_Table = ll1.get_LL1_table();
        sb.append(String.format("%s", StringIO.str_align_center("LL1 Table", table_space)));
        for(Integer s : terminal_set.val){
            sb.append(StringIO.str_align_center(render_symbol_simple(s), table_space));
        }
        sb.append("\n");
        for(Map.Entry<Integer, LL1_symbol_index> pi: LL1_Table.val.entrySet()){
            sb.append(String.format("%s", StringIO.str_align_center(StringIO.int_to_str(pi.getKey()), table_space)));
            LL1_symbol_index S = pi.getValue();
            for(Integer pj : terminal_set.val){
                if(S.val.containsKey(pj)){
                    Integer ind = S.val.get(pj);
                    LL1_expr_pair expr_pair = expr_pair_index.val.get(ind);
                    sb.append(String.format("%s", StringIO.str_align_center(expr_pair_to_str_simple(expr_pair), table_space)));
                }
                else {
                    sb.append(String.format("%s", StringIO.str_align_center("-", table_space)));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String LL1_Table_Render_Map_Simple(LL1_Process ll1){
        StringBuilder sb = new StringBuilder();
        sb.append("Map_Info:\n\nReach_Empty:\n");
        LL1_flag_map reach_empty_map = ll1.get_reach_empty_map();
        for(Map.Entry<Integer, LL1_flag> p : reach_empty_map.val.entrySet()){
            sb.append("Reach_Empty(").append(StringIO.int_to_str(p.getKey())).append(") = ").append(p.getValue().val).append("\n");
        }

        sb.append("\nFirst_Set:\n");
        LL1_symbol_map first_map = ll1.get_first_map();
        for(Map.Entry<Integer, LL1_symbol_set> pi : first_map.val.entrySet()){
            Integer left = pi.getKey();
            sb.append("First(").append(StringIO.int_to_str(left)).append(") = { ");
            LL1_symbol_set S = pi.getValue();
            boolean flag = false;
            for(Integer pj : S.val){
                if(flag){
                    sb.append(", ");
                } else {
                    flag = true;
                }
                sb.append(render_symbol_simple(pj));
            }
            if(reach_empty_map.val.get(left).val){
                sb.append(", ").append(empty_tag);
            }
            sb.append(" }\n");
        }

        sb.append("\nFollow_Set:\n");
        LL1_symbol_map follow_map = ll1.get_follow_map();
        for(Map.Entry<Integer, LL1_symbol_set> pi : follow_map.val.entrySet()){
            sb.append("Follow(").append(StringIO.int_to_str(pi.getKey())).append(") = { ");
            LL1_symbol_set S = pi.getValue();
            boolean flag = false;
            for(Integer pj : S.val){
                if(flag){
                    sb.append(", ");
                } else {
                    flag = true;
                }
                sb.append(render_symbol_simple(pj));
            }
            sb.append(" }\n");
        }

        sb.append("\nSelect_Set:\n");
        LL1_expr_select_list_map select_map = ll1.get_select_map();
        int max_length = 0;
        for(Map.Entry<Integer, LL1_expr_select_list> pi : select_map.val.entrySet()){
            Integer left = pi.getKey();
            LL1_expr_select_list V = pi.getValue();
            for(LL1_expr_select pj : V.val){
                LL1_expr_pair expr = new LL1_expr_pair(left, pj.first);
                int current_length = String.format("Select(%s)", expr_pair_to_str_simple(expr)).length();
                if(max_length < current_length){
                    max_length = current_length;
                }
            }
        }

        for(Map.Entry<Integer, LL1_expr_select_list> pi : select_map.val.entrySet()){
            Integer left = pi.getKey();
            LL1_expr_select_list V = pi.getValue();
            for(LL1_expr_select pj : V.val){
                LL1_expr_pair expr = new LL1_expr_pair(left, pj.first);
                sb.append(StringIO.str_align_left(String.format("Select(%s)", expr_pair_to_str_simple(expr)), max_length));
                sb.append(" = { ");
                boolean flag = false;
                for(Integer pk : pj.second.val){
                    if(flag){
                        sb.append(", ");
                    } else {
                        flag = true;
                    }
                    sb.append(render_symbol_simple(pk));
                }
                sb.append(" }\n");
            }
        }
        return sb.toString();
    }
}
