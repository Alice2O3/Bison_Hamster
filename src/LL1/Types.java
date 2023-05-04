package LL1;

import java.util.List;
import java.util.ArrayList;

public class Types {
    public static class LL1_pair{
        public Boolean first;
        public Integer second;
        public LL1_pair(Boolean first_, Integer second_){
            first = first_;
            second = second_;
        }
    }
    public static class LL1_expr{
        public List<LL1_pair> val = new ArrayList<>();
    }
    public static class LL1_expr_list{
        public List<LL1_expr> val = new ArrayList<>();
    }
    public static class LL1_rule{
        public Integer first = 0;
        public LL1_expr_list second = new LL1_expr_list();
    }
    public static class LL1_list{
        public List<LL1_rule> val = new ArrayList<>();
    }
}
