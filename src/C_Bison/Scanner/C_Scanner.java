package C_Bison.Scanner;

import C_Bison.Language.Easy_C.Easy_C_Tokens;
import C_Flex.DFA;
import C_Flex.DFA_Types.*;
import C_Flex.callback.Common;
import C_Flex.callback.ICallback;
import C_Flex.callback.Util;

public class C_Scanner implements IScanner {
    private final DFA dfa;

    private static class States {
        private static final int INITIAL = 0;
        private static final int IDENTIFIER = 1;
        private static final int INTEGER = 2;
        private static final int LEFT_BIG_BRACKET = 3;
        private static final int RIGHT_BIG_BRACKET = 4;
        private static final int LEFT_SMALL_BRACKET = 5;
        private static final int RIGHT_SMALL_BRACKET = 6;
        private static final int COMMA = 7;
        private static final int STATEMENT_END = 8;
        private static final int PLUS = 9;
        private static final int MINUS = 10;
        private static final int ASSIGN = 11;
        private static final int ASSIGNADD = 12;
        private static final int ASSIGNMINUS = 13;
        private static final int SMALLER = 14;
        private static final int GREATER = 15;
        private static final int SMALLEREQUAL = 16;
        private static final int GREATEREQUAL = 17;
        private static final int EQUALTO = 18;
        private static final int length = 19;
    }

    private static class Identifier_Keep implements ICallback{
        public void execute(DFA dfa, DFA_event e) {
            String lexeme = dfa.getLexeme();
            if(lexeme.equals("if")){
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.IF);
            }
            else if(lexeme.equals("for")){
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.FOR);
            }
            else if(lexeme.equals("return")){
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.RETURN);
            }
            else if(lexeme.equals("int")){
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.INT);
            }
            else if(lexeme.equals("void")){
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.VOID);
            }
            else {
                Util.acceptFinishedKeep(dfa, Easy_C_Tokens.IDENTIFIER);
            }
        }
    }

    private static class Callbacks{
        private static final ICallback ACCEPT = new Common.Accept();
        private static final ICallback FORWARD = new Common.Forward();
        private static final ICallback ESCAPE = new Common.Escape();
        private static final ICallback IDENTIFIER_KEEP = new Identifier_Keep();
        private static ICallback KEEP(Integer token_type) {
            return new Common.Accept_Keep(token_type);
        }
    }

    public C_Scanner(){
        dfa = new DFA();
        dfa.initSize(C_Scanner.States.length);
        //Set Rules
        setGeneralRules();
        setIdentifiers();
        setIntegers();
        setOperators();
    }

    @Override
    public DFA_lexing_list scan(String code) {
        dfa.processString(code);
        return dfa.tokens_list;
    }

    private void setGeneralRules(){
        dfa.addEdge(States.INITIAL, States.INITIAL, Callbacks.FORWARD);
        dfa.addEdge(States.IDENTIFIER, States.INITIAL, Callbacks.IDENTIFIER_KEEP);
        dfa.addEdge(States.INTEGER, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.INTEGER));
        dfa.addEdge(States.LEFT_BIG_BRACKET, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.LEFT_BIG_BRACKET));
        dfa.addEdge(States.RIGHT_BIG_BRACKET, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.RIGHT_BIG_BRACKET));
        dfa.addEdge(States.LEFT_SMALL_BRACKET, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.LEFT_SMALL_BRACKET));
        dfa.addEdge(States.RIGHT_SMALL_BRACKET, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.RIGHT_SMALL_BRACKET));
        dfa.addEdge(States.COMMA, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.COMMA));
        dfa.addEdge(States.STATEMENT_END, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.STATEMENT_END));
        dfa.addEdge(States.PLUS, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.PLUS));
        dfa.addEdge(States.MINUS, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.MINUS));
        dfa.addEdge(States.ASSIGN, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.ASSIGN));
        dfa.addEdge(States.ASSIGNADD, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.ASSIGNADD));
        dfa.addEdge(States.ASSIGNMINUS, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.ASSIGNMINUS));
        dfa.addEdge(States.SMALLER, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.SMALLER));
        dfa.addEdge(States.GREATER, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.GREATER));
        dfa.addEdge(States.SMALLEREQUAL, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.SMALLEREQUAL));
        dfa.addEdge(States.GREATEREQUAL, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.GREATEREQUAL));
        dfa.addEdge(States.EQUALTO, States.INITIAL, Callbacks.KEEP(Easy_C_Tokens.EQUALTO));

        dfa.addEdge(States.INITIAL, States.INITIAL, '\n', Callbacks.ESCAPE);
    }

    private void setIdentifiers(){
        dfa.addEdge(States.INITIAL, States.IDENTIFIER, '_', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.IDENTIFIER, 'A', 'Z', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.IDENTIFIER, 'a', 'z', Callbacks.ACCEPT);
        dfa.addEdge(States.IDENTIFIER, States.IDENTIFIER, '_', Callbacks.ACCEPT);
        dfa.addEdge(States.IDENTIFIER, States.IDENTIFIER, 'A', 'Z', Callbacks.ACCEPT);
        dfa.addEdge(States.IDENTIFIER, States.IDENTIFIER, 'a', 'z', Callbacks.ACCEPT);
    }

    private void setIntegers(){
        dfa.addEdge(States.INITIAL, States.INTEGER, '0', '9', Callbacks.ACCEPT);
        dfa.addEdge(States.INTEGER, States.INTEGER, '0', '9', Callbacks.ACCEPT);
    }

    private void setOperators(){
        dfa.addEdge(States.INITIAL, States.LEFT_BIG_BRACKET, '{', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.RIGHT_BIG_BRACKET, '}', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.LEFT_SMALL_BRACKET, '(', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.RIGHT_SMALL_BRACKET, ')', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.COMMA, ',', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.STATEMENT_END, ';', Callbacks.ACCEPT);

        dfa.addEdge(States.INITIAL, States.PLUS, '+', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.MINUS, '-', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.ASSIGN, '=', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.SMALLER, '<', Callbacks.ACCEPT);
        dfa.addEdge(States.INITIAL, States.GREATER, '>', Callbacks.ACCEPT);

        dfa.addEdge(States.PLUS, States.ASSIGNADD, '=', Callbacks.ACCEPT);
        dfa.addEdge(States.MINUS, States.ASSIGNMINUS, '=', Callbacks.ACCEPT);
        dfa.addEdge(States.SMALLER, States.SMALLEREQUAL, '=', Callbacks.ACCEPT);
        dfa.addEdge(States.GREATER, States.GREATEREQUAL, '=', Callbacks.ACCEPT);
        dfa.addEdge(States.ASSIGN, States.EQUALTO, '=', Callbacks.ACCEPT);
    }
}
