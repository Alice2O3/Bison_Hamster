package LL1;

import LL1.Types.*;
import LL1.Process.Types.*;

import java.util.*;

public class Process {
    public static class Types{
        public static class LL1_symbol_set{
            public Set<Integer> val = new HashSet<>();
        }

        public static class LL1_expr_map{
            public Map<Integer, LL1_expr_list> val = new HashMap<>();
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
            public LL1_expr first;
            public LL1_symbol_set second;
            public LL1_expr_select(LL1_expr first_, LL1_symbol_set second_){
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
            public LL1_expr second;
            public LL1_expr_pair(Integer first_, LL1_expr second_){
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

    Integer Start_Symbol = 0;
    static Integer Error_Tag = -1;
    static Integer End_Tag = -1;
    LL1_symbol_set Terminal_Set = new LL1_symbol_set();
    LL1_symbol_set Non_Terminal_Set = new LL1_symbol_set();
    LL1_expr_map Non_Terminal_Map = new LL1_expr_map();
    LL1_expr_map Non_Terminal_Map_Filtered = new LL1_expr_map();
    LL1_flag_map Reach_Empty_Map = new LL1_flag_map();

    //First, Follow and Select
    LL1_symbol_map First_Pending = new LL1_symbol_map();
    LL1_symbol_map First_Map = new LL1_symbol_map();
    LL1_symbol_map Follow_Pending = new LL1_symbol_map();
    LL1_symbol_map Follow_Map = new LL1_symbol_map();
    LL1_expr_select_list_map Select_Map = new LL1_expr_select_list_map();

    //LL1 Table
    LL1_expr_pair_list Expr_Pair_Index = new LL1_expr_pair_list();
    LL1_table LL1_Table = new LL1_table();

    void append_rule(LL1_expr_list dest, LL1_expr_list src){
        dest.val.addAll(src.val);
    }

    void collect_symbols_expr(LL1_expr expr){
        for(LL1_pair p : expr.val){
            if(p.first){
                Terminal_Set.val.add(p.second);
            } else {
                Non_Terminal_Set.val.add(p.second);
            }
        }
    }

    void collect_symbols_rule(Integer s, LL1_expr_list e_list){
        Non_Terminal_Set.val.add(s);
        for(LL1_expr e : e_list.val){
            collect_symbols_expr(e);
        }
    }

    void collect_symbols(){
        for(Map.Entry<Integer, LL1_expr_list> e : Non_Terminal_Map.val.entrySet()){
            collect_symbols_rule(e.getKey(), e.getValue());
        }
    }

    Boolean has_terminals(LL1_expr expr){
        for(LL1_pair p : expr.val){
            if(p.first){
                return true;
            }
        }
        return false;
    }

    LL1_rule filter_terminal_rule(Integer rule_first, LL1_expr_list rule_second){
        LL1_rule ret = new LL1_rule();
        ret.first = rule_first;
        for(LL1_expr expr : rule_second.val){
            if(!has_terminals(expr)){
                ret.second.val.add(expr);
            }
        }
        return ret;
    }

    void resolve_reach_empty_expr(Integer left, LL1_expr expr, LL1_flag iter_flag){
        for(LL1_pair p : expr.val){
            if(!Reach_Empty_Map.val.get(p.second).val){
                return;
            }
        }
        Reach_Empty_Map.val.get(left).val = true;
        iter_flag.val = true;
    }

    void resolve_reach_empty_rule(Integer left, LL1_expr_list expr_list, LL1_flag iter_flag){
        if(Reach_Empty_Map.val.get(left).val){
            return;
        }
        for(LL1_expr e : expr_list.val){
            resolve_reach_empty_expr(left, e, iter_flag);
        }
    }

    void resolve_first_pending_expr(LL1_symbol_set S, Integer left, LL1_expr expr){
        for(LL1_pair p : expr.val){
            Integer s = p.second;
            if(p.first){
                First_Map.val.get(left).val.add(s);
                break;
            }
            S.val.add(s);
            if(!Reach_Empty_Map.val.get(s).val){
                break;
            }
        }
    }

    void resolve_first_pending(Integer left, LL1_expr_list expr_list){
        LL1_symbol_set pending_set = new LL1_symbol_set();
        First_Map.val.put(left, new LL1_symbol_set());
        for(LL1_expr expr : expr_list.val){
            resolve_first_pending_expr(pending_set, left, expr);
        }
        First_Pending.val.put(left, pending_set);
    }

    void merge_set(LL1_symbol_set dest, LL1_symbol_set src){
        dest.val.addAll(src.val);
    }

    Boolean merge_set_update(LL1_symbol_set dest, LL1_symbol_set src){
        int prev_cnt = dest.val.size();
        dest.val.addAll(src.val);
        return dest.val.size() > prev_cnt;
    }

    void update_first_sub(Integer left, LL1_symbol_set S, LL1_flag iter_flag){
        LL1_symbol_set S_ = new LL1_symbol_set();
        S_.val.addAll(S.val);
        for(Integer p : S_.val){
            if(First_Pending.val.get(p).val.isEmpty()){
                merge_set(First_Map.val.get(left), First_Map.val.get(p));
                S.val.remove(p);
                iter_flag.val = true;
            }
        }
    }

    void update_first(){
        LL1_flag iter_flag = new LL1_flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, LL1_symbol_set> p: First_Pending.val.entrySet()){
                LL1_symbol_set S = p.getValue();
                if(!S.val.isEmpty()){
                    update_first_sub(p.getKey(), S, iter_flag);
                }
            }
        }
    }

    void resolve_follow_pending_expr(Integer left, LL1_expr expr){
        List<LL1_pair> V = expr.val;
        ListIterator<LL1_pair> i = V.listIterator();
        while(i.hasNext()){
            LL1_pair pi = i.next();
            if(!pi.first){
                Integer si = pi.second;
                LL1_symbol_set S = Follow_Map.val.get(si);
                boolean flag = true;
                ListIterator<LL1_pair> j = V.listIterator(i.nextIndex());
                while(j.hasNext()){
                    LL1_pair pj = j.next();
                    Integer sj = pj.second;
                    if(pj.first){
                        S.val.add(sj);
                        flag = false;
                        break;
                    } else {
                       merge_set(S, First_Map.val.get(sj));
                       if(!Reach_Empty_Map.val.get(sj).val){
                           flag = false;
                           break;
                       }
                    }
                }
                if(flag){
                    Follow_Pending.val.get(si).val.add(left);
                }
            }
        }
    }

    void resolve_follow_pending(Integer left, LL1_expr_list expr_list){
        for(LL1_expr expr : expr_list.val){
            resolve_follow_pending_expr(left, expr);
        }
    }

    void update_follow_sub(Integer left, LL1_symbol_set S, LL1_flag iter_flag){
        for(Integer s : S.val){
            if(merge_set_update(Follow_Map.val.get(left), Follow_Map.val.get(s))){
                iter_flag.val = true;
            }
        }
    }

    void update_follow(){
        LL1_flag iter_flag = new LL1_flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, LL1_symbol_set> p : Follow_Pending.val.entrySet()){
                update_follow_sub(p.getKey(), p.getValue(), iter_flag);
            }
        }
    }

    void resolve_select_expr(LL1_expr_select_list V, Integer left, LL1_expr expr){
        LL1_symbol_set S = new LL1_symbol_set();
        boolean can_reach_empty = true;
        for(LL1_pair p : expr.val){
            Integer s = p.second;
            if(p.first){
                S.val.add(s);
                can_reach_empty = false;
                break;
            }
            else {
                merge_set(S, First_Map.val.get(s));
                if(!Reach_Empty_Map.val.get(s).val){
                    can_reach_empty = false;
                    break;
                }
            }
        }
        if(can_reach_empty){
            merge_set(S, Follow_Map.val.get(left));
        }
        V.val.add(new LL1_expr_select(expr, S));
    }

    void resolve_select_rule(Integer left, LL1_expr_list expr_list){
        LL1_expr_select_list tmp = new LL1_expr_select_list();
        for(LL1_expr expr : expr_list.val){
            resolve_select_expr(tmp, left, expr);
        }
        Select_Map.val.put(left, tmp);
    }

    Boolean check_LL1(){
        for(Map.Entry<Integer, LL1_expr_select_list> pi : Select_Map.val.entrySet()){
            LL1_symbol_set S_cnt = new LL1_symbol_set();
            Set<Integer> S_cnt_v = S_cnt.val;
            LL1_expr_select_list S = pi.getValue();
            for(LL1_expr_select pj : S.val){
                LL1_symbol_set select_set = pj.second;
                for(Integer pk : select_set.val){
                    if(S_cnt_v.contains(pk)){
                        return false;
                    }
                    S_cnt_v.add(pk);
                }
            }
        }
        return true;
    }

    //Public Functions
    public Pre_Process_Exception pre_process(LL1_list V){
        if(V.val.isEmpty()){
            return Pre_Process_Exception.INPUT_ERR;
        }
        Terminal_Set.val.clear();
        Non_Terminal_Set.val.clear();
        Non_Terminal_Map.val.clear();
        for(LL1_rule rule : V.val){
            Integer s = rule.first;
            if(s.equals(Error_Tag)){
                return Pre_Process_Exception.INPUT_ERR;
            }
            else if(Non_Terminal_Map.val.containsKey(s)){
                append_rule(Non_Terminal_Map.val.get(s), rule.second);
            }
            else {
                LL1_expr_list expr_list = new LL1_expr_list();
                append_rule(expr_list, rule.second);
                Non_Terminal_Map.val.put(s, expr_list);
            }
        }
        Start_Symbol = V.val.get(0).first;
        Terminal_Set.val.add(End_Tag);
        collect_symbols();
        if(Non_Terminal_Map.val.size() < Non_Terminal_Set.val.size()){
            return Pre_Process_Exception.UNREACHABLE;
        }
        return Pre_Process_Exception.NORMAL;
    }

    public void filter_terminal_expr(){
        Non_Terminal_Map_Filtered.val.clear();
        for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map.val.entrySet()){
            LL1_rule rule = filter_terminal_rule(p.getKey(), p.getValue());
            if(!rule.second.val.isEmpty()){
                Non_Terminal_Map_Filtered.val.put(rule.first, rule.second);
            }
        }
    }

    public void resolve_reach_empty(){
        filter_terminal_expr();
        for(Integer s : Non_Terminal_Set.val){
            Reach_Empty_Map.val.put(s, new LL1_flag(false));
        }
        LL1_flag iter_flag = new LL1_flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map_Filtered.val.entrySet()){
                resolve_reach_empty_rule(p.getKey(), p.getValue(), iter_flag);
            }
        }
    }

    public Resolve_Exception resolve_first(){
        resolve_reach_empty();
        First_Map.val.clear();
        First_Pending.val.clear();
        for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map.val.entrySet()){
            resolve_first_pending(p.getKey(), p.getValue());
        }
        update_first();
        for(Map.Entry<Integer, LL1_symbol_set> p : First_Pending.val.entrySet()){
            if(!p.getValue().val.isEmpty()){
                return Resolve_Exception.HAS_LEFT_RECURSION;
            }
        }
        return Resolve_Exception.NORMAL;
    }

    public Resolve_Exception resolve_follow(){
        Resolve_Exception e = resolve_first();
        if(e != Resolve_Exception.NORMAL){
            return e;
        }
        Follow_Map.val.clear();
        Follow_Pending.val.clear();
        for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map.val.entrySet()){
            Integer left = p.getKey();
            Follow_Map.val.put(left, new LL1_symbol_set());
            Follow_Pending.val.put(left, new LL1_symbol_set());
        }
        Follow_Map.val.get(Start_Symbol).val.add(End_Tag);
        for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map.val.entrySet()){
            resolve_follow_pending(p.getKey(), p.getValue());
        }
        update_follow();
        return Resolve_Exception.NORMAL;
    }

    public Resolve_Exception resolve_select(){
        Resolve_Exception e = resolve_follow();
        if(e != Resolve_Exception.NORMAL){
            return e;
        }
        Select_Map.val.clear();
        for(Map.Entry<Integer, LL1_expr_list> p : Non_Terminal_Map.val.entrySet()){
            resolve_select_rule(p.getKey(), p.getValue());
        }
        if(!check_LL1()){
            return Resolve_Exception.NOT_LL1;
        }
        return Resolve_Exception.NORMAL;
    }

    public Resolve_Exception resolve_LL1_table(){
        Resolve_Exception e = resolve_select();
        if(e != Resolve_Exception.NORMAL){
            return e;
        }
        int cnt = 0;
        Expr_Pair_Index.val.clear();
        LL1_Table.val.clear();
        for(Map.Entry<Integer, LL1_expr_select_list> pi : Select_Map.val.entrySet()){
            Integer left = pi.getKey();
            LL1_symbol_index S = new LL1_symbol_index();
            for(LL1_expr_select pj : pi.getValue().val){
                LL1_expr expr = pj.first;
                Expr_Pair_Index.val.add(new LL1_expr_pair(left, expr));
                for(Integer pk : pj.second.val){
                    S.val.put(pk, cnt);
                }
                cnt++;
            }
            LL1_Table.val.put(left, S);
        }
        return Resolve_Exception.NORMAL;
    }

    public LL1_expr_pair_list get_expr_pair_index(){
        return Expr_Pair_Index;
    }

    public LL1_symbol_set get_terminals(){
        return Terminal_Set;
    }

    public LL1_table get_LL1_table(){
        return LL1_Table;
    }

    public Integer get_end_tag(){
        return End_Tag;
    }
}
