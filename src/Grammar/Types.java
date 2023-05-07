package Grammar;

import java.util.List;
import java.util.ArrayList;

public class Types {
    public static class Grammar_pair {
        public Boolean first; //0: Non_Terminal 1: Terminal
        public Integer second;
        public Grammar_pair(Boolean first_, Integer second_){
            first = first_;
            second = second_;
        }
    }
    public static class Grammar_expr {
        public List<Grammar_pair> val = new ArrayList<>();
    }
    public static class Grammar_expr_list {
        public List<Grammar_expr> val = new ArrayList<>();
    }
    public static class Grammar_rule {
        public Integer first = 0;
        public Grammar_expr_list second = new Grammar_expr_list();
    }
    public static class Grammar_list {
        public List<Grammar_rule> val = new ArrayList<>();
    }
}
