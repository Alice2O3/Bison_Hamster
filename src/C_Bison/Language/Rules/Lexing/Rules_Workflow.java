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
    private final Map<String, Integer> symbol_map = new HashMap<>();
    private final Map<String, Integer> terminal_map = new HashMap<>();
    private final Map<String, Integer> non_terminal_map = new HashMap<>();
    private final List<String> symbol_list = new ArrayList<>();
    private Integer symbol_cnt = 0;
    private final Grammar_list grammar_list = new Grammar_list();
    private void data_clear(){
        symbol_cnt = 0;
        symbol_map.clear();
        terminal_map.clear();
        non_terminal_map.clear();
        symbol_list.clear();
        grammar_list.val.clear();
    }

    private void record_tokens(DFA_lexing lexing){
        if(lexing.token_type == Rules_Tokens.NON_TERMINAL){
            if(!symbol_map.containsKey(lexing.token)){
                non_terminal_map.put(lexing.token, symbol_cnt);
                symbol_map.put(lexing.token, symbol_cnt);
                symbol_list.add(lexing.token);
                symbol_cnt++;
            }
        }
        else if(lexing.token_type == Rules_Tokens.TERMINAL){
            if(!symbol_map.containsKey(lexing.token)){
                terminal_map.put(lexing.token, symbol_cnt);
                symbol_map.put(lexing.token, symbol_cnt);
                symbol_list.add(lexing.token);
                symbol_cnt++;
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
                        rule.first = symbol_map.get(token);
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
                        expr.val.add(new Grammar_pair(true, symbol_map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        expr.val.add(new Grammar_pair(false, symbol_map.get(token)));
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
                        expr.val.add(new Grammar_pair(true, symbol_map.get(token)));
                        current_state = States.EXPRESSION;
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        rule.second.val.add(expr);
                        expr = new Grammar_expr();
                        expr.val.add(new Grammar_pair(false, symbol_map.get(token)));
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
                        expr.val.add(new Grammar_pair(true, symbol_map.get(token)));
                    }
                    else if (token_type == Rules_Tokens.NON_TERMINAL){
                        expr.val.add(new Grammar_pair(false, symbol_map.get(token)));
                    }
                    else if (token_type == Rules_Tokens.OR){
                        current_state = States.OR;
                    }
                    else if (token_type == Rules_Tokens.ESCAPE){
                        rule.second.val.add(expr);
                        grammar_list.val.add(rule);
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
                        grammar_list.val.add(rule);
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

    public Map<String, Integer> getSymbol_map(){
        return symbol_map;
    }
    public Map<String, Integer> getTerminal_map(){
        return terminal_map;
    }
    public Map<String, Integer> getNon_terminal_map(){
        return non_terminal_map;
    }
    public Grammar_list getGrammar_list(){
        return grammar_list;
    }
    public List<String> getSymbol_list() { return symbol_list; }
}
