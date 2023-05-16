package C_Bison.Scanner;

import C_Bison.Language.Easy_C.Tokens;
import C_Flex.DFA;
import C_Flex.Types.*;
import C_Flex.callback.Common;
import C_Flex.callback.ICallback;
import C_Flex.callback.Util;

import java.util.List;

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
                Util.acceptFinishedKeep(dfa, Tokens.IF);
            }
            else if(lexeme.equals("for")){
                Util.acceptFinishedKeep(dfa, Tokens.FOR);
            }
            else if(lexeme.equals("return")){
                Util.acceptFinishedKeep(dfa, Tokens.RETURN);
            }
            else if(lexeme.equals("int")){
                Util.acceptFinishedKeep(dfa, Tokens.INT);
            }
            else if(lexeme.equals("void")){
                Util.acceptFinishedKeep(dfa, Tokens.VOID);
            }
            else {
                Util.acceptFinishedKeep(dfa, Tokens.IDENTIFIER);
            }
        }
    }

    private static class Callbacks{
        private static final ICallback ACCEPT = new Common.Accept();
        private static final ICallback FORWARD = new Common.Forward();
        private static final ICallback ESCAPE = new Common.Escape();
        private static final ICallback IDENTIFIER_KEEP = new Identifier_Keep();
        private static final ICallback DISCARD_KEEP = new Common.Discard_Keep();
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
        Util.moveForward(dfa);
        dfa.setLexeme("EOF");
        Util.addTokenInfo(dfa, Tokens.EOF);
        return dfa.tokens_list;
    }

    private void setGeneralRules(){
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.INITIAL, 0, 127, Callbacks.FORWARD));
        dfa.addEdge(new DFA_edgeInterval(States.IDENTIFIER, States.INITIAL, 0, 127, Callbacks.IDENTIFIER_KEEP));
        dfa.addEdge(new DFA_edgeInterval(States.INTEGER, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.INTEGER)));

        dfa.addEdge(new DFA_edgeInterval(States.LEFT_BIG_BRACKET, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.LEFT_BIG_BRACKET))); //{
        dfa.addEdge(new DFA_edgeInterval(States.RIGHT_BIG_BRACKET, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.RIGHT_BIG_BRACKET))); //}
        dfa.addEdge(new DFA_edgeInterval(States.LEFT_SMALL_BRACKET, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.LEFT_SMALL_BRACKET))); //(
        dfa.addEdge(new DFA_edgeInterval(States.RIGHT_SMALL_BRACKET, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.RIGHT_SMALL_BRACKET))); //)
        dfa.addEdge(new DFA_edgeInterval(States.COMMA, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.COMMA))); //,
        dfa.addEdge(new DFA_edgeInterval(States.STATEMENT_END, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.STATEMENT_END))); //;

        dfa.addEdge(new DFA_edgeInterval(States.PLUS, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.PLUS))); //+
        dfa.addEdge(new DFA_edgeInterval(States.MINUS, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.MINUS))); //-
        dfa.addEdge(new DFA_edgeInterval(States.ASSIGN, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.ASSIGN))); //=
        dfa.addEdge(new DFA_edgeInterval(States.ASSIGNADD, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.ASSIGNADD))); //+=
        dfa.addEdge(new DFA_edgeInterval(States.ASSIGNMINUS, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.ASSIGNMINUS))); //-=
        dfa.addEdge(new DFA_edgeInterval(States.SMALLER, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.SMALLER))); //<
        dfa.addEdge(new DFA_edgeInterval(States.GREATER, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.GREATER))); //>
        dfa.addEdge(new DFA_edgeInterval(States.SMALLEREQUAL, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.SMALLEREQUAL))); //<=
        dfa.addEdge(new DFA_edgeInterval(States.GREATEREQUAL, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.GREATEREQUAL))); //>=
        dfa.addEdge(new DFA_edgeInterval(States.EQUALTO, States.INITIAL, 0, 127, Callbacks.KEEP(Tokens.EQUALTO))); //==

        dfa.addEdge(new DFA_edge(States.INITIAL, States.INITIAL, '\n', Callbacks.ESCAPE));
    }

    private void setIdentifiers(){
        dfa.addEdge(new DFA_edge(States.INITIAL, States.IDENTIFIER, '_', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.IDENTIFIER, 'A', 'Z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.IDENTIFIER, 'a', 'z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.IDENTIFIER, States.IDENTIFIER, '_', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.IDENTIFIER, States.IDENTIFIER, 'A', 'Z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.IDENTIFIER, States.IDENTIFIER, 'a', 'z', Callbacks.ACCEPT));
    }

    private void setIntegers(){
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.INTEGER, '0', '9', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.INTEGER, States.INTEGER, '0', '9', Callbacks.ACCEPT));
    }

    private void setOperators(){
        dfa.addEdge(new DFA_edge(States.INITIAL, States.LEFT_BIG_BRACKET, '{', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.RIGHT_BIG_BRACKET, '}', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.LEFT_SMALL_BRACKET, '(', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.RIGHT_SMALL_BRACKET, ')', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.COMMA, ',', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.STATEMENT_END, ';', Callbacks.ACCEPT));

        dfa.addEdge(new DFA_edge(States.INITIAL, States.PLUS, '+', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.MINUS, '-', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.ASSIGN, '=', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.SMALLER, '<', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.GREATER, '>', Callbacks.ACCEPT));

        dfa.addEdge(new DFA_edge(States.PLUS, States.ASSIGNADD, '=', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.MINUS, States.ASSIGNMINUS, '=', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.SMALLER, States.SMALLEREQUAL, '=', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.GREATER, States.GREATEREQUAL, '=', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.ASSIGN, States.EQUALTO, '=', Callbacks.ACCEPT));
    }
}
