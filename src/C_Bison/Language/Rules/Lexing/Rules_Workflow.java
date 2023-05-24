package C_Bison.Language.Rules.Lexing;

import C_Bison.Scanner.*;
import C_Bison.Grammar.Grammar_Types.*;
import C_Bison.Language.Rules.Rules_Tokens;
import C_Flex.DFA_Types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rules_Workflow {
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
    private final List<String> Symbol_List = new ArrayList<>();
    private Integer Symbol_cnt = 0;
    private final Grammar_list Grammar_List = new Grammar_list();
    private void data_clear(){
        Symbol_cnt = 0;
        Symbol_Map.clear();
        Terminal_Map.clear();
        Non_Terminal_Map.clear();
        Symbol_List.clear();
    }
    private void record_tokens(DFA_lexing lexing){
        if(lexing.token_type == Rules_Tokens.NON_TERMINAL){
            if(!Symbol_Map.containsKey(lexing.token)){
                Non_Terminal_Map.put(lexing.token, Symbol_cnt);
                Symbol_Map.put(lexing.token, Symbol_cnt);
                Symbol_List.add(lexing.token);
                Symbol_cnt++;
            }
        }
        else if(lexing.token_type == Rules_Tokens.TERMINAL){
            if(!Symbol_Map.containsKey(lexing.token)){
                Terminal_Map.put(lexing.token, Symbol_cnt);
                Symbol_Map.put(lexing.token, Symbol_cnt);
                Symbol_List.add(lexing.token);
                Symbol_cnt++;
            }
        }
    }
    private Process_Exception process_tokens(DFA_lexing_list lexing_list){ //true: Success, false: Error
        data_clear();
        Grammar_rule rule = new Grammar_rule();
        Grammar_expr expr = new Grammar_expr();
        for(DFA_lexing lexing : lexing_list.val){
            //Record the tokens
            record_tokens(lexing);
            String token = lexing.token;
            Integer token_type = lexing.token_type;
            switch(current_state){
                case LEFT:
                    if (token_type == Rules_Tokens.NON_TERMINAL) {
                        rule.first = Symbol_Map.get(token);
                        current_state = States.ARROW;
                    } else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case ARROW:
                    if (token_type == Rules_Tokens.ARROW) {
                        current_state = States.RIGHT_START;
                    } else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case RIGHT_START:
                    if (token_type == Rules_Tokens.TERMINAL) {
                        expr.val.add(new Grammar_pair(true, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        expr.val.add(new Grammar_pair(false, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.EMPTY){
                        current_state = States.EMPTY;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case OR:
                    if (token_type == Rules_Tokens.TERMINAL) {
                        rule.second.val.add(expr);
                        expr = new Grammar_expr();
                        expr.val.add(new Grammar_pair(true, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        rule.second.val.add(expr);
                        expr = new Grammar_expr();
                        expr.val.add(new Grammar_pair(false, Symbol_Map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.EMPTY){
                        rule.second.val.add(expr);
                        expr = new Grammar_expr();
                        current_state = States.EMPTY;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case EXPRESSION:
                    if (token_type == Rules_Tokens.TERMINAL) {
                        expr.val.add(new Grammar_pair(true, Symbol_Map.get(token)));
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        expr.val.add(new Grammar_pair(false, Symbol_Map.get(token)));
                    }
                    else if (token_type == Rules_Tokens.OR){
                        current_state = States.OR;
                    }
                    else if (token_type == Rules_Tokens.ESCAPE){
                        rule.second.val.add(expr);
                        Grammar_List.val.add(rule);
                        rule = new Grammar_rule();
                        expr = new Grammar_expr();
                        current_state = States.LEFT;
                    }
                    else {
                        return Process_Exception.ERROR;
                    }
                    break;
                case EMPTY:
                    if (token_type == Rules_Tokens.OR){
                        current_state = States.OR;
                    }
                    else if (token_type == Rules_Tokens.ESCAPE){
                        rule.second.val.add(expr);
                        Grammar_List.val.add(rule);
                        rule = new Grammar_rule();
                        expr = new Grammar_expr();
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
        return process_tokens(new Grammar_Scanner().scan(text));
    }

    public String getSymbolInfo(){
        StringBuilder ret = new StringBuilder();
        ret.append("Symbols:\n");
        int symbol_cnt = 0;
        Boolean flag = false;
        for(String s : Symbol_List){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%d, %s)", symbol_cnt, s));
            symbol_cnt++;
        }
        ret.append("\n\nTerminals:\n");
        flag = false;
        for(Map.Entry<String, Integer> p : Terminal_Map.entrySet()){
            if(flag){
                ret.append(", ");
            } else {
                flag = true;
            }
            ret.append(String.format("(%s, %d)", p.getKey(), p.getValue()));
        }
        ret.append("\n\nNon_Terminals:\n");
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

    public Map<String, Integer> getSymbolMap(){
        return Symbol_Map;
    }
    public Grammar_list getGrammarList(){
        return Grammar_List;
    }
    public List<String> getSymbolList() { return Symbol_List; }
}
