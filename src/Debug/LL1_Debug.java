package Debug;

import LL1.Simple_Read.FileIO;
import LL1.Simple_Read.IO;
import LL1.Process;
import LL1.Process.Types.*;
import LL1.Types.*;
import java.io.File;
import java.util.Map;

public class LL1_Debug {
    private static String input_file = "Compile_Test/LL1_read_test/LL1_read_test.txt";
    private static String empty_tag = "@";
    private static String end_tag = "#";
    private static Integer table_space = 12;

    private static String str_repeat(Character ch, Integer n){
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < n; i++){
            ret.append(ch);
        }
        return ret.toString();
    }

    private static String str_align(String S){
        int len = table_space - S.length();
        if(len <= 0){
            return S;
        }
        int len_l = len / 2, len_r = len - len_l;
        return str_repeat(' ', len_l) + S + str_repeat(' ', len_r);
    }

    public static String int_to_str(Integer s){
        return Character.toString((char) s.intValue());
    }

    public static String expr_pair_to_str(LL1_expr_pair expr_pair){
        StringBuilder sb = new StringBuilder();
        sb.append(int_to_str(expr_pair.first));
        sb.append(" -> ");
        LL1_expr expr = expr_pair.second;
        if(expr.val.isEmpty()){
            sb.append(empty_tag);
        }
        else {
            for(LL1_pair pk : expr.val){
                sb.append(int_to_str(pk.second));
            }
        }
        return sb.toString();
    }

    public static void get_LL1_table_test(){
        LL1_list V = FileIO.read_from_file(new File(input_file));
        if(V == null){
            System.out.print("\nInput Error!\n");
            return;
        }
        for(LL1_rule rule : V.val){
            System.out.printf("%s\n", IO.vec_to_str(rule));
        }
        Process ll1 = new Process();
        if(ll1.pre_process(V) != Pre_Process_Exception.NORMAL){
            System.out.print("\nInput Error!\n");
            return;
        }
        Resolve_Exception e = ll1.resolve_LL1_table();
        if(e == Resolve_Exception.HAS_LEFT_RECURSION){
            System.out.print("\nGrammar has left recursion!\n");
            return;
        }
        else if(e == Resolve_Exception.NOT_LL1){
            System.out.print("\nGrammar is not LL1!\n");
            return;
        }
        LL1_expr_pair_list expr_pair_index = ll1.get_expr_pair_index();
        LL1_symbol_set terminal_set = ll1.get_terminals();
        LL1_table LL1_Table = ll1.get_LL1_table();
        System.out.printf("\n%s", str_align("LL1 Table"));
        for(Integer s : terminal_set.val){
            if(s.equals(ll1.get_end_tag())){
                System.out.printf("%s", str_align(end_tag));
            } else {
                System.out.printf("%s", str_align(int_to_str(s)));
            }
        }
        System.out.print("\n");
        for(Map.Entry<Integer, LL1_symbol_index> pi: LL1_Table.val.entrySet()){
            System.out.printf("%s", str_align(int_to_str(pi.getKey())));
            LL1_symbol_index S = pi.getValue();
            for(Integer pj : terminal_set.val){
                if(S.val.containsKey(pj)){
                    Integer ind = S.val.get(pj);
                    LL1_expr_pair expr_pair = expr_pair_index.val.get(ind);
                    System.out.printf("%s", str_align(expr_pair_to_str(expr_pair)));
                }
                else {
                    System.out.printf("%s", str_align("-"));
                }
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args){
        get_LL1_table_test();
    }
}
