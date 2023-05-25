package C_Bison.Grammar.LL1;

import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Grammar.LL1.LL1_Types.*;

import java.util.*;

public class LL1_Process {
    private final static Integer End_Tag = -1;
    public static Integer getEnd_Tag(){
        return End_Tag;
    }
    private final LL1_Symbol_Set terminal_set = new LL1_Symbol_Set();
    private final LL1_Symbol_Set non_terminal_set = new LL1_Symbol_Set();
    private final LL1_Expr_Map non_terminal_map = new LL1_Expr_Map();
    private final LL1_Expr_Map non_terminal_map_filtered = new LL1_Expr_Map();
    private final LL1_Flag_Map reach_empty_map = new LL1_Flag_Map();

    //First, Follow and Select
    private final LL1_Symbol_Map first_pending = new LL1_Symbol_Map();
    private final LL1_Symbol_Map first_map = new LL1_Symbol_Map();
    private final LL1_Symbol_Map follow_pending = new LL1_Symbol_Map();
    private final LL1_Symbol_Map follow_map = new LL1_Symbol_Map();
    private final LL1_Expr_Select_List_Map select_map = new LL1_Expr_Select_List_Map();

    //LL1 Table
    private final LL1_Expr_Pair_List expr_pair_index = new LL1_Expr_Pair_List();
    private final LL1_Table ll1_table = new LL1_Table();
    private Integer start_symbol = 0;

    private void append_rule(Grammar_expr_list dest, Grammar_expr_list src){
        dest.val.addAll(src.val);
    }

    private void collect_symbols_expr(Grammar_expr expr){
        for(Grammar_pair p : expr.val){
            if(p.first){
                terminal_set.val.add(p.second);
            } else {
                non_terminal_set.val.add(p.second);
            }
        }
    }

    private void collect_symbols_rule(Integer s, Grammar_expr_list e_list){
        non_terminal_set.val.add(s);
        for(Grammar_expr e : e_list.val){
            collect_symbols_expr(e);
        }
    }

    private void collect_symbols(){
        for(Map.Entry<Integer, Grammar_expr_list> e : non_terminal_map.val.entrySet()){
            collect_symbols_rule(e.getKey(), e.getValue());
        }
    }

    private Boolean has_terminals(Grammar_expr expr){
        for(Grammar_pair p : expr.val){
            if(p.first){
                return true;
            }
        }
        return false;
    }

    private Grammar_rule filter_terminal_rule(Integer rule_first, Grammar_expr_list rule_second){
        Grammar_rule ret = new Grammar_rule();
        ret.first = rule_first;
        for(Grammar_expr expr : rule_second.val){
            if(!has_terminals(expr)){
                ret.second.val.add(expr);
            }
        }
        return ret;
    }

    private void resolve_reach_empty_expr(Integer left, Grammar_expr expr, LL1_Flag iter_flag){
        for(Grammar_pair p : expr.val){
            if(!reach_empty_map.val.get(p.second).val){
                return;
            }
        }
        reach_empty_map.val.get(left).val = true;
        iter_flag.val = true;
    }

    private void resolve_reach_empty_rule(Integer left, Grammar_expr_list expr_list, LL1_Flag iter_flag){
        if(reach_empty_map.val.get(left).val){
            return;
        }
        for(Grammar_expr e : expr_list.val){
            resolve_reach_empty_expr(left, e, iter_flag);
        }
    }

    private void resolve_first_pending_expr(LL1_Symbol_Set S, Integer left, Grammar_expr expr){
        for(Grammar_pair p : expr.val){
            Integer s = p.second;
            if(p.first){
                first_map.val.get(left).val.add(s);
                return;
            }
            S.val.add(s);
            if(!reach_empty_map.val.get(s).val){
                return;
            }
        }
    }

    private void resolve_first_pending(Integer left, Grammar_expr_list expr_list){
        LL1_Symbol_Set pending_set = new LL1_Symbol_Set();
        first_map.val.put(left, new LL1_Symbol_Set());
        for(Grammar_expr expr : expr_list.val){
            resolve_first_pending_expr(pending_set, left, expr);
        }
        first_pending.val.put(left, pending_set);
    }

    private void merge_set(LL1_Symbol_Set dest, LL1_Symbol_Set src){
        dest.val.addAll(src.val);
    }

    private Boolean merge_set_update(LL1_Symbol_Set dest, LL1_Symbol_Set src){
        int prev_cnt = dest.val.size();
        dest.val.addAll(src.val);
        return dest.val.size() > prev_cnt;
    }

    private void update_first_sub(Integer left, LL1_Symbol_Set S, LL1_Flag iter_flag){
        Iterator<Integer> it = S.val.iterator();
        while(it.hasNext()){
            Integer p = it.next();
            if(first_pending.val.get(p).val.isEmpty()){
                merge_set(first_map.val.get(left), first_map.val.get(p));
                it.remove();
                iter_flag.val = true;
            }
        }
    }

    private void update_first(){
        LL1_Flag iter_flag = new LL1_Flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, LL1_Symbol_Set> p: first_pending.val.entrySet()){
                LL1_Symbol_Set S = p.getValue();
                if(!S.val.isEmpty()){
                    update_first_sub(p.getKey(), S, iter_flag);
                }
            }
        }
    }

    private void resolve_follow_pending_expr(Integer left, Grammar_expr expr){
        List<Grammar_pair> V = expr.val;
        ListIterator<Grammar_pair> i = V.listIterator();
        while(i.hasNext()){
            Grammar_pair pi = i.next();
            if(!pi.first){
                Integer si = pi.second;
                LL1_Symbol_Set S = follow_map.val.get(si);
                boolean flag = true;
                ListIterator<Grammar_pair> j = V.listIterator(i.nextIndex());
                while(j.hasNext()){
                    Grammar_pair pj = j.next();
                    Integer sj = pj.second;
                    if(pj.first){
                        S.val.add(sj);
                        flag = false;
                        break;
                    } else {
                       merge_set(S, first_map.val.get(sj));
                       if(!reach_empty_map.val.get(sj).val){
                           flag = false;
                           break;
                       }
                    }
                }
                if(flag){
                    follow_pending.val.get(si).val.add(left);
                }
            }
        }
    }

    private void resolve_follow_pending(Integer left, Grammar_expr_list expr_list){
        for(Grammar_expr expr : expr_list.val){
            resolve_follow_pending_expr(left, expr);
        }
    }

    private void update_follow_sub(Integer left, LL1_Symbol_Set S, LL1_Flag iter_flag){
        for(Integer s : S.val){
            if(merge_set_update(follow_map.val.get(left), follow_map.val.get(s))){
                iter_flag.val = true;
            }
        }
    }

    private void update_follow(){
        LL1_Flag iter_flag = new LL1_Flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, LL1_Symbol_Set> p : follow_pending.val.entrySet()){
                update_follow_sub(p.getKey(), p.getValue(), iter_flag);
            }
        }
    }

    private void resolve_select_expr(LL1_Expr_Select_List V, Integer left, Grammar_expr expr){
        LL1_Symbol_Set S = new LL1_Symbol_Set();
        boolean can_reach_empty = true;
        for(Grammar_pair p : expr.val){
            Integer s = p.second;
            if(p.first){
                S.val.add(s);
                can_reach_empty = false;
                break;
            }
            else {
                merge_set(S, first_map.val.get(s));
                if(!reach_empty_map.val.get(s).val){
                    can_reach_empty = false;
                    break;
                }
            }
        }
        if(can_reach_empty){
            merge_set(S, follow_map.val.get(left));
        }
        V.val.add(new LL1_Expr_Select(expr, S));
    }

    private void resolve_select_rule(Integer left, Grammar_expr_list expr_list){
        LL1_Expr_Select_List tmp = new LL1_Expr_Select_List();
        for(Grammar_expr expr : expr_list.val){
            resolve_select_expr(tmp, left, expr);
        }
        select_map.val.put(left, tmp);
    }

    private Boolean check_LL1(){
        for(Map.Entry<Integer, LL1_Expr_Select_List> pi : select_map.val.entrySet()){
            LL1_Symbol_Set S_cnt = new LL1_Symbol_Set();
            Set<Integer> S_cnt_v = S_cnt.val;
            LL1_Expr_Select_List S = pi.getValue();
            for(LL1_Expr_Select pj : S.val){
                LL1_Symbol_Set select_set = pj.second;
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
    public Pre_Process_Exception pre_process(Grammar_list V){
        if(V.val.isEmpty()){
            return Pre_Process_Exception.INPUT_ERR;
        }
        terminal_set.val.clear();
        non_terminal_set.val.clear();
        non_terminal_map.val.clear();
        for(Grammar_rule rule : V.val){
            Integer s = rule.first;
            if(non_terminal_map.val.containsKey(s)){
                append_rule(non_terminal_map.val.get(s), rule.second);
            }
            else {
                Grammar_expr_list expr_list = new Grammar_expr_list();
                append_rule(expr_list, rule.second);
                non_terminal_map.val.put(s, expr_list);
            }
        }
        start_symbol = V.val.get(0).first;
        terminal_set.val.add(getEnd_Tag());
        collect_symbols();
        if(non_terminal_map.val.size() < non_terminal_set.val.size()){
            return Pre_Process_Exception.UNREACHABLE;
        }
        return Pre_Process_Exception.NORMAL;
    }

    private void filter_terminal_expr(){
        non_terminal_map_filtered.val.clear();
        for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map.val.entrySet()){
            Grammar_rule rule = filter_terminal_rule(p.getKey(), p.getValue());
            if(!rule.second.val.isEmpty()){
                non_terminal_map_filtered.val.put(rule.first, rule.second);
            }
        }
    }

    public void resolve_reach_empty(){
        filter_terminal_expr();
        for(Integer s : non_terminal_set.val){
            reach_empty_map.val.put(s, new LL1_Flag(false));
        }
        LL1_Flag iter_flag = new LL1_Flag(true);
        while(iter_flag.val){
            iter_flag.val = false;
            for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map_filtered.val.entrySet()){
                resolve_reach_empty_rule(p.getKey(), p.getValue(), iter_flag);
            }
        }
    }

    public Resolve_Exception resolve_first(){
        resolve_reach_empty();
        first_map.val.clear();
        first_pending.val.clear();
        for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map.val.entrySet()){
            resolve_first_pending(p.getKey(), p.getValue());
        }
        update_first();
        for(Map.Entry<Integer, LL1_Symbol_Set> p : first_pending.val.entrySet()){
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
        follow_map.val.clear();
        follow_pending.val.clear();
        for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map.val.entrySet()){
            Integer left = p.getKey();
            follow_map.val.put(left, new LL1_Symbol_Set());
            follow_pending.val.put(left, new LL1_Symbol_Set());
        }
        follow_map.val.get(start_symbol).val.add(getEnd_Tag());
        for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map.val.entrySet()){
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
        select_map.val.clear();
        for(Map.Entry<Integer, Grammar_expr_list> p : non_terminal_map.val.entrySet()){
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
        expr_pair_index.val.clear();
        ll1_table.val.clear();
        for(Map.Entry<Integer, LL1_Expr_Select_List> pi : select_map.val.entrySet()){
            Integer left = pi.getKey();
            LL1_Symbol_Index S = new LL1_Symbol_Index();
            for(LL1_Expr_Select pj : pi.getValue().val){
                Grammar_expr expr = pj.first;
                expr_pair_index.val.add(new LL1_Expr_Pair(left, expr));
                for(Integer pk : pj.second.val){
                    S.val.put(pk, cnt);
                }
                cnt++;
            }
            ll1_table.val.put(left, S);
        }
        return Resolve_Exception.NORMAL;
    }

    public LL1_Expr_Pair_List get_expr_pair_index(){
        return expr_pair_index;
    }

    public LL1_Symbol_Set get_terminals(){
        return terminal_set;
    }

    public LL1_Types.LL1_Table get_LL1_table(){
        return ll1_table;
    }

    public Integer get_start_symbol(){
        return start_symbol;
    }

    public LL1_Flag_Map get_reach_empty_map(){
        return reach_empty_map;
    }

    public LL1_Symbol_Map get_first_map(){
        return first_map;
    }

    public LL1_Symbol_Map get_follow_map(){
        return follow_map;
    }

    public LL1_Expr_Select_List_Map get_select_map(){
        return select_map;
    }
}
