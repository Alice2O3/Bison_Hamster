package cflex.callback;

import cflex.DFA;
import cflex.DFA_event;
import cflex.DFA_lexing;

public class Util {
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

    public static void processEscapeKeep(DFA dfa, Integer token_type){
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
