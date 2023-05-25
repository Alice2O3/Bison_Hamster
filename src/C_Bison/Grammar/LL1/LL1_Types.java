package C_Bison.Grammar.LL1;

import java.util.*;
import C_Bison.Grammar.Grammar_Types.*;

public class LL1_Types {
    public static class LL1_Symbol_Set {
        public Set<Integer> val = new HashSet<>();
    }

    public static class LL1_Expr_Map {
        public Map<Integer, Grammar_expr_list> val = new HashMap<>();
    }

    public static class LL1_Symbol_Map {
        public Map<Integer, LL1_Symbol_Set> val = new HashMap<>();
    }

    public static class LL1_Flag {
        public Boolean val;
        public LL1_Flag(Boolean v_){
            val = v_;
        }
    }

    public static class LL1_Flag_Map {
        public Map<Integer, LL1_Flag> val = new HashMap<>();
    }

    public static class LL1_Expr_Select {
        public Grammar_expr first;
        public LL1_Symbol_Set second;
        public LL1_Expr_Select(Grammar_expr first_, LL1_Symbol_Set second_){
            first = first_;
            second = second_;
        }
    }

    public static class LL1_Expr_Select_List {
        public List<LL1_Expr_Select> val = new ArrayList<>();
    }

    public static class LL1_Expr_Select_List_Map {
        public Map<Integer, LL1_Expr_Select_List> val = new HashMap<>();
    }

    public static class LL1_Expr_Pair {
        public Integer first;
        public Grammar_expr second;
        public LL1_Expr_Pair(Integer first_, Grammar_expr second_){
            first = first_;
            second = second_;
        }
    }

    public static class LL1_Expr_Pair_List {
        public List<LL1_Expr_Pair> val = new ArrayList<>();
    }

    public static class LL1_Symbol_Index {
        public Map<Integer, Integer> val = new HashMap<>();
    }

    public static class LL1_Table {
        public Map<Integer, LL1_Symbol_Index> val = new HashMap<>();
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

    public enum Parser_Exception{
        NORMAL,
        READ_ERROR,
        INPUT_ERROR,
        HAS_LEFT_RECURSION,
        NOT_LL1,
        PARSE_ERROR
    }
}