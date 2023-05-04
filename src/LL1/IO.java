package LL1;

import LL1.Types.*;
import java.util.List;

public class IO {
    static void push_vec(LL1_rule V){
        V.second.val.add(new LL1_expr());
    }

    static void push_char(LL1_rule V, Character c, boolean is_terminal){
        List<LL1_expr> l = V.second.val;
        l.get(l.size() - 1).val.add(new LL1_pair(is_terminal, (int)c));
    }

    static Boolean is_non_terminal(Character c){
        return 'A' <= c && c <= 'Z';
    }

    static Boolean is_empty(Character c){
        return c == '@';
    }

    static Boolean is_space(Character c){
        return c == ' ';
    }

    static Boolean is_arrow(Character c){
        return c == '-';
    }

    static Boolean is_arrow_2(Character c){
        return c == '>';
    }

    static Boolean is_or(Character c){
        return c == '|';
    }

    static Boolean is_end(Character c){
        return c == '#';
    }

    static Boolean is_terminal(Character c){
        return !(is_non_terminal(c) || is_empty(c) || is_space(c) || is_or(c) || is_end(c));
    }

    enum flag_state{
        LEFT,
        ARROW,
        ARROW_2,
        RIGHT_START,
        RIGHT,
        RIGHT_2,
        RIGHT_STR,
        RIGHT_EMPTY,
        OR
    }

    public static LL1_rule str_to_vec(String s){
        LL1_rule V = new LL1_rule();
        String s_ = s + "#";
        flag_state flag = flag_state.LEFT;
        for (Character tmp : s_.toCharArray())
        {
            //printf_s("flag: %d\n", flag);
            if (flag == flag_state.LEFT) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_non_terminal(tmp)) {
                    flag = flag_state.ARROW;
                    V.first = (int)tmp;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.ARROW) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_arrow(tmp)) {
                    flag = flag_state.ARROW_2;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.ARROW_2) {
                if (is_arrow_2(tmp)) {
                    flag = flag_state.RIGHT_START;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.RIGHT_START) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = flag_state.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = flag_state.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = flag_state.RIGHT_STR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.RIGHT) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_or(tmp)) {
                    flag = flag_state.OR;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = flag_state.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = flag_state.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = flag_state.RIGHT_STR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.RIGHT_2) {
                if (is_space(tmp) || is_end(tmp)) {
                    continue;
                }
                else if (is_or(tmp)) {
                    flag = flag_state.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.RIGHT_STR) {
                if (is_space(tmp) || is_end(tmp)) {
                    flag = flag_state.RIGHT_2;
                }
                else if (is_terminal(tmp)) {
                    push_char(V, tmp, true);
                }
                else if (is_non_terminal(tmp)) {
                    push_char(V, tmp, false);
                }
                else if (is_or(tmp)) {
                    flag = flag_state.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.RIGHT_EMPTY) {
                if (is_space(tmp) || is_end(tmp)) {
                    flag = flag_state.RIGHT_2;
                }
                else if (is_or(tmp)) {
                    flag = flag_state.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == flag_state.OR) {
                if (is_space(tmp)) {
                    flag = flag_state.RIGHT;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = flag_state.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = flag_state.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = flag_state.RIGHT_STR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
        }
        return V;
    }

    public static String vec_to_str(LL1_rule V){
        Integer vfirst = V.first;
        if (vfirst == -1) {
            return "<ERR>";
        }
        StringBuilder ret = new StringBuilder();
        ret.append(Character.toChars(vfirst));
        ret.append(" -> ");
        List<LL1_expr> Vs = V.second.val;
        int i = 0;
        int num = Vs.size();
        for (LL1_expr pi : Vs) {
            List<LL1_pair> Vj = pi.val;
            if(Vj.isEmpty()){
                ret.append("@");
            }
            else{
                for(LL1_pair pj : Vj){
                    ret.append(Character.toChars(pj.second));
                }
            }
            i++;
            if (i < num) {
                ret.append(" | ");
            }
        }
        return ret.toString();
    }
}
