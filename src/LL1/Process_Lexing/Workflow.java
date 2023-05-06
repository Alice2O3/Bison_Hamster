package LL1.Process_Lexing;

import Scanner.*;
import LL1.Types.*;
import LL1.Tokens;
import cflex.DFA_lexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workflow {
    private enum States {
        LEFT,
        ARROW,
        RIGHT_START,
        EXPRESSION,
        EMPTY,
        OR
    }
    public enum Process_Exception{
        NORMAL,
        ERROR
    }
    private States current_state = States.LEFT;
    private final Map<String, Integer> Symbol_Map = new HashMap<>();
    private final Map<String, Integer> Terminal_Map = new HashMap<>();
    private final Map<String, Integer> Non_Terminal_Map = new HashMap<>();
    private final List<String> Symbol_Index = new ArrayList<>();
    private Integer Symbol_cnt = 0;
    private final LL1_list LL1_List = new LL1_list();
    public LL1_list getLL1List(){
        return LL1_List;
    }
    private void data_clear(){
        Symbol_cnt = 0;
        Symbol_Map.clear();
        Terminal_Map.clear();
        Non_Terminal_Map.clear();
        Symbol_Index.clear();
    }
    private void record_tokens(DFA_lexing lexing){
        if(lexing.token_type == Tokens.NON_TERMINAL){
            if(!Symbol_Map.containsKey(lexing.token)){
                Non_Terminal_Map.put(lexing.token, Symbol_cnt);
                Symbol_Map.put(lexing.token, Symbol_cnt);
                Symbol_Index.add(lexing.token);
                Symbol_cnt++;
            }
        }
        else if(lexing.token_type == Tokens.TERMINAL){
            if(!Symbol_Map.containsKey(lexing.token)){
                Terminal_Map.put(lexing.token, Symbol_cnt);
                Symbol_Map.put(lexing.token, Symbol_cnt);
                Symbol_Index.add(lexing.token);
                Symbol_cnt++;
            }
        }
    }
    public Process_Exception process_tokens(List<DFA_lexing> lexing_list){ //true: Success, false: Error
        data_clear();
        LL1_rule rule = new LL1_rule();
        LL1_expr expr = new LL1_expr();
        for(DFA_lexing lexing : lexing_list){
            //System.out.printf("<'%s', %s>\n", lexing.token, Tokens.Map[lexing.token_type]);
            //Record the tokens
            record_tokens(lexing);
            String token = lexing.token;
            Integer token_type = lexing.token_type;
            switch(current_state){
                case LEFT:
                    if (token_type == Tokens.NON_TERMINAL) {
                        rule.first = Symbol_Map.get(token);
                        current_state = States.ARROW;
                    } else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case ARROW:
                    if (token_type == Tokens.ARROW) {
                        current_state = States.RIGHT_START;
                    } else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case RIGHT_START:
                    if (token_type == Tokens.TERMINAL) {
                        expr.val.add(new LL1_pair(true, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Tokens.NON_TERMINAL){
                        expr.val.add(new LL1_pair(false, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Tokens.EMPTY){
                        current_state = States.EMPTY;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case OR:
                    if (token_type == Tokens.TERMINAL) {
                        rule.second.val.add(expr);
                        expr = new LL1_expr();
                        expr.val.add(new LL1_pair(true, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Tokens.NON_TERMINAL){
                        rule.second.val.add(expr);
                        expr = new LL1_expr();
                        expr.val.add(new LL1_pair(false, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Tokens.EMPTY){
                        rule.second.val.add(expr);
                        expr = new LL1_expr();
                        current_state = States.EMPTY;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case EXPRESSION:
                    if (token_type == Tokens.TERMINAL) {
                        expr.val.add(new LL1_pair(true, Symbol_Map.get(token)));
                    }
                    else if (token_type == Tokens.NON_TERMINAL){
                        expr.val.add(new LL1_pair(false, Symbol_Map.get(token)));
                    }
                    else if (token_type == Tokens.OR){
                        current_state = States.OR;
                    }
                    else if (token_type == Tokens.ESCAPE){
                        rule.second.val.add(expr);
                        LL1_List.val.add(rule);
                        rule = new LL1_rule();
                        expr = new LL1_expr();
                        current_state = States.LEFT;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case EMPTY:
                    if (token_type == Tokens.OR){
                        current_state = States.OR;
                    }
                    else if (token_type == Tokens.ESCAPE){
                        rule.second.val.add(expr);
                        LL1_List.val.add(rule);
                        rule = new LL1_rule();
                        expr = new LL1_expr();
                        current_state = States.LEFT;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
            }
        }
        return Process_Exception.NORMAL;
    }

    public Process_Exception process_text(String text){
        return process_tokens(new Rule_Scanner().scan(text));
    }

    public String getSymbolInfo(){
        StringBuilder ret = new StringBuilder();
        ret.append("Symbols:\n");
        int symbol_cnt = 0;
        Boolean flag = false;
        for(String s : Symbol_Index){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%d, %s)", symbol_cnt, s));
            symbol_cnt++;
        }
        ret.append("\nTerminals:\n");
        flag = false;
        for(Map.Entry<String, Integer> p : Terminal_Map.entrySet()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", p.getKey(), p.getValue()));
        }
        ret.append("\nNon_Terminals:\n");
        flag = false;
        for(Map.Entry<String, Integer> p : Non_Terminal_Map.entrySet()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", p.getKey(), p.getValue()));
        }
        ret.append("\n");
        return ret.toString();
    }
}
