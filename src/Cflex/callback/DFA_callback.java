package Cflex.callback;

import Cflex.DFA;
import Cflex.DFA_event;
import Cflex.DFA_lexing;

public class DFA_callback {
    public static class Escape implements ICallback { //When meeting \n
        public void execute(DFA dfa, DFA_event e) {
            //debugInfo(e);

            //System.out.printf("'\\n' detected, using escape method%n");
            processEscape(dfa);
        }
    }

    public static class Escape_Accept implements ICallback { //When meeting \n
        private final Integer token_type;
        public Escape_Accept(Integer token_type_){
            token_type = token_type_;
        }
        public void execute(DFA dfa, DFA_event e) {
            //debugInfo(e);

            //System.out.printf("'\\n' detected, using escape method%n");
            processEscapeAccept(dfa, token_type);
        }
    }


    public static class Forward implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            //debugInfo(e);

            //System.out.printf("Moving forward%n");
            moveForward(dfa);
        }
    }

    public static class Accept implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            //debugInfo(e);

            //System.out.printf("Accepting new character%n");
            acceptForward(dfa, e.getChar());
        }
    }

    public static class Accept_Keep implements ICallback {
        private final Integer token_type;
        public Accept_Keep(Integer token_type_){
            token_type = token_type_;
        }

        public void execute(DFA dfa, DFA_event e) {
            //Util.debugInfo(e);

            acceptFinishedKeep(dfa, token_type);
        }
    }

    public static class Discard_Keep implements ICallback {
        public void execute(DFA dfa, DFA_event e) {
            //debugInfo(e);

            discardKeep(dfa);
        }
    }

    public static void debugInfo(DFA_event e){
        System.out.printf("Processing (%d --'%c'-> %d)%n", e.current_node.getIndex(), e.getChar(), e.next_node.getIndex());
    }

    public static void processEscape(DFA dfa){
        dfa.clearLexeme();
        dfa.gotoInitialState();
        dfa.keep = false;
        dfa.l_index = 0;
        dfa.r_index = 0;
        dfa.r_index_global++;
        dfa.l_index_global = dfa.r_index_global;
        dfa.line_index++;
    }

    public static void processEscapeAccept(DFA dfa, Integer token_type){
        dfa.clearLexeme();
        addTokenInfo(dfa, token_type);
        dfa.gotoInitialState();
        dfa.keep = false;
        dfa.l_index = 0;
        dfa.r_index = 0;
        dfa.r_index_global++;
        dfa.l_index_global = dfa.r_index_global;
        dfa.line_index++;
    }

    public static void moveForward(DFA dfa){
        dfa.keep = false;
        dfa.l_index++;
        dfa.r_index++;
        dfa.l_index_global++;
        dfa.r_index_global++;
    }

    public static void acceptForward(DFA dfa, Character c){
        dfa.pushChar(c);
        dfa.keep = false;
        dfa.r_index++;
        dfa.r_index_global++;
    }

    public static void acceptFinished(DFA dfa, Character c, Integer token_type){
        acceptForward(dfa, c);
        addTokenInfo(dfa, token_type);
        dfa.clearLexeme();
        dfa.gotoInitialState();
        dfa.keep = false;
        dfa.l_index = dfa.r_index;
        dfa.l_index_global = dfa.r_index_global;
    }

    public static void acceptFinishedKeep(DFA dfa, Integer token_type){
        addTokenInfo(dfa, token_type);
        dfa.clearLexeme();
        dfa.gotoInitialState();
        dfa.keep = true;
        dfa.l_index = dfa.r_index;
        dfa.l_index_global = dfa.r_index_global;
    }

    public static void discardKeep(DFA dfa){
        dfa.clearLexeme();
        dfa.gotoInitialState();
        dfa.keep = true;
        dfa.l_index = dfa.r_index;
        dfa.l_index_global = dfa.r_index_global;
    }

    public static void addTokenInfo(DFA dfa, Integer token_type){
        dfa.tokens_list.add(new DFA_lexing(dfa.l_index_global, dfa.r_index_global - 1, dfa.line_index, dfa.l_index, dfa.getLexeme(), token_type));
    }
}