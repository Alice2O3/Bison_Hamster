package C_Bison.Scanner;

import C_Bison.Language.Rules.Rules_Tokens;
import C_Flex.DFA;
import C_Flex.DFA_Types.*;
import C_Flex.callback.Common;
import C_Flex.callback.ICallback;

public class Grammar_Scanner implements IScanner {

    private final DFA dfa;
    private static class States {
        private static final int INITIAL = 0;
        private static final int TERMINAL = 1;

        private static final int NON_TERMINAL = 2;
        private static final int ARROW_L = 3;
        private static final int ARROW_R = 4;
        private static final int OR = 5;
        private static final int EMPTY = 6;
        private static final int length = 7;
    }
    private static class Callbacks {
        private static final ICallback ACCEPT = new Common.Accept();
        private static final ICallback FORWARD = new Common.Forward();
        private static final ICallback ESCAPE_ACCEPT = new Common.Escape_Accept(Rules_Tokens.ESCAPE);
        private static final ICallback DISCARD_KEEP = new Common.Discard_Keep();

        private static ICallback KEEP(Integer token_type) {
            return new Common.Accept_Keep(token_type);
        }
    }
    public Grammar_Scanner(){
        dfa = new DFA();
        dfa.initSize(States.length);
        //Set Rules
        setGeneralRules();
        setIdentifiers();
        setOperators();
    }
    @Override
    public DFA_lexing_list scan(String code) {
        dfa.processString(code);
        return dfa.tokens_list;
    }

    private void setGeneralRules(){
        //Finish Rules
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.INITIAL, 0, 127, Callbacks.FORWARD));
        dfa.addEdge(new DFA_edgeInterval(States.TERMINAL, States.INITIAL, 0, 127, Callbacks.KEEP(Rules_Tokens.TERMINAL))); //1: TERMINAL
        dfa.addEdge(new DFA_edgeInterval(States.NON_TERMINAL, States.INITIAL, 0, 127, Callbacks.KEEP(Rules_Tokens.NON_TERMINAL))); //2: NON_TERMINAL
        dfa.addEdge(new DFA_edgeInterval(States.ARROW_L, States.INITIAL, 0, 127, Callbacks.DISCARD_KEEP));
        dfa.addEdge(new DFA_edgeInterval(States.ARROW_R, States.INITIAL, 0, 127, Callbacks.KEEP(Rules_Tokens.ARROW))); //3: ARROW
        dfa.addEdge(new DFA_edgeInterval(States.OR, States.INITIAL, 0, 127, Callbacks.KEEP(Rules_Tokens.OR))); //4: OR
        dfa.addEdge(new DFA_edgeInterval(States.EMPTY, States.INITIAL, 0, 127, Callbacks.KEEP(Rules_Tokens.EMPTY))); //5: EMPTY

        //Process Escapes
        dfa.addEdge(new DFA_edge(States.INITIAL, States.INITIAL, '\n', Callbacks.ESCAPE_ACCEPT));
    }

    private void setIdentifiers(){
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.TERMINAL, 'A', 'Z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.INITIAL, States.NON_TERMINAL, 'a', 'z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.NON_TERMINAL, '_', Callbacks.ACCEPT));

        dfa.addEdge(new DFA_edgeInterval(States.TERMINAL, States.TERMINAL, 'a', 'z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.TERMINAL, States.TERMINAL, 'A', 'Z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.TERMINAL, States.TERMINAL, '_', Callbacks.ACCEPT));

        dfa.addEdge(new DFA_edgeInterval(States.NON_TERMINAL, States.NON_TERMINAL, 'a', 'z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edgeInterval(States.NON_TERMINAL, States.NON_TERMINAL, 'A', 'Z', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.NON_TERMINAL, States.NON_TERMINAL, '_', Callbacks.ACCEPT));
    }

    private void setOperators(){
        dfa.addEdge(new DFA_edge(States.INITIAL, States.ARROW_L, '-', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.ARROW_L, States.ARROW_R, '>', Callbacks.ACCEPT));

        dfa.addEdge(new DFA_edge(States.INITIAL, States.OR, '|', Callbacks.ACCEPT));
        dfa.addEdge(new DFA_edge(States.INITIAL, States.EMPTY, '@', Callbacks.ACCEPT));
    }
}
