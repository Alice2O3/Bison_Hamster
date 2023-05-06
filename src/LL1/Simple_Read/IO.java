package LL1.Simple_Read;

import LL1.Types.*;
import java.util.List;

public class IO {
    private static void push_vec(LL1_rule V){
        V.second.val.add(new LL1_expr());
    }

    private static void push_char(LL1_rule V, Character c, boolean is_terminal){
        List<LL1_expr> l = V.second.val;
        l.get(l.size() - 1).val.add(new LL1_pair(is_terminal, (int)c));
    }

    private static Boolean is_non_terminal(Character c){
        return 'A' <= c && c <= 'Z';
    }

    private static Boolean is_empty(Character c){
        return c == '@';
    }

    private static Boolean is_space(Character c){
        return c == ' ';
    }

    private static Boolean is_arrow(Character c){
        return c == '-';
    }

    private static Boolean is_arrow_2(Character c){
        return c == '>';
    }

    private static Boolean is_or(Character c){
        return c == '|';
    }

    private static Boolean is_end(Character c){
        return c == '#';
    }

    private static Boolean is_terminal(Character c){
        return !(is_non_terminal(c) || is_empty(c) || is_space(c) || is_or(c) || is_end(c));
    }

    private enum States {
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
        States flag = States.LEFT;
        for (Character tmp : s_.toCharArray())
        {
            //printf_s("flag: %d\n", flag);
            if (flag == States.LEFT) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_non_terminal(tmp)) {
                    flag = States.ARROW;
                    V.first = (int)tmp;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.ARROW) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_arrow(tmp)) {
                    flag = States.ARROW_2;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.ARROW_2) {
                if (is_arrow_2(tmp)) {
                    flag = States.RIGHT_START;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.RIGHT_START) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = States.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = States.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = States.RIGHT_STR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.RIGHT) {
                if (is_space(tmp)) {
                    continue;
                }
                else if (is_or(tmp)) {
                    flag = States.OR;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = States.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = States.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = States.RIGHT_STR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.RIGHT_2) {
                if (is_space(tmp) || is_end(tmp)) {
                    continue;
                }
                else if (is_or(tmp)) {
                    flag = States.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.RIGHT_STR) {
                if (is_space(tmp) || is_end(tmp)) {
                    flag = States.RIGHT_2;
                }
                else if (is_terminal(tmp)) {
                    push_char(V, tmp, true);
                }
                else if (is_non_terminal(tmp)) {
                    push_char(V, tmp, false);
                }
                else if (is_or(tmp)) {
                    flag = States.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.RIGHT_EMPTY) {
                if (is_space(tmp) || is_end(tmp)) {
                    flag = States.RIGHT_2;
                }
                else if (is_or(tmp)) {
                    flag = States.OR;
                }
                else {
                    V.first = -1;
                    break;
                }
            }
            else if (flag == States.OR) {
                if (is_space(tmp)) {
                    flag = States.RIGHT;
                }
                else if (is_empty(tmp)) {
                    push_vec(V);
                    flag = States.RIGHT_EMPTY;
                }
                else if (is_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, true);
                    flag = States.RIGHT_STR;
                }
                else if (is_non_terminal(tmp)) {
                    push_vec(V);
                    push_char(V, tmp, false);
                    flag = States.RIGHT_STR;
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
        Integer first = V.first;
        if (first == -1) {
            return "<ERR>";
        }
        StringBuilder ret = new StringBuilder();
        ret.append(Character.toChars(first));
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
