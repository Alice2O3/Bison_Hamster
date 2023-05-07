package Grammar.LL1;

import java.util.*;

public class Types{
    public static class LL1_symbol_set{
        public Set<Integer> val = new HashSet<>();
    }

    public static class LL1_expr_map{
        public Map<Integer, Grammar.Types.Grammar_expr_list> val = new HashMap<>();
    }

    public static class LL1_symbol_map{
        public Map<Integer, LL1_symbol_set> val = new HashMap<>();
    }

    public static class LL1_flag{
        public Boolean val;
        public LL1_flag(Boolean v_){
            val = v_;
        }
    }

    public static class LL1_flag_map{
        public Map<Integer, LL1_flag> val = new HashMap<>();
    }

    public static class LL1_expr_select{
        public Grammar.Types.Grammar_expr first;
        public LL1_symbol_set second;
        public LL1_expr_select(Grammar.Types.Grammar_expr first_, LL1_symbol_set second_){
            first = first_;
            second = second_;
        }
    }

    public static class LL1_expr_select_list{
        public List<LL1_expr_select> val = new ArrayList<>();
    }

    public static class LL1_expr_select_list_map{
        public Map<Integer, LL1_expr_select_list> val = new HashMap<>();
    }

    public static class LL1_expr_pair{
        public Integer first;
        public Grammar.Types.Grammar_expr second;
        public LL1_expr_pair(Integer first_, Grammar.Types.Grammar_expr second_){
            first = first_;
            second = second_;
        }
    }

    public static class LL1_expr_pair_list{
        public List<LL1_expr_pair> val = new ArrayList<>();
    }

    public static class LL1_symbol_index{
        public Map<Integer, Integer> val = new HashMap<>();
    }

    public static class LL1_table{
        public Map<Integer, LL1_symbol_index> val = new HashMap<>();
    }

    public enum Pre_Process_Exception{
        NORMAL,
        INPUT_ERR,
        UNREACHABLE
    }

    public enum Resolve_Exception{
        NORMAL,
        HAS_LEFT_RECURSION,
        NOT_LL1
    }
}