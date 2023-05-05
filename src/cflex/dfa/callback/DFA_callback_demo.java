package cflex.dfa.callback;

import cflex.dfa.DFA;
import cflex.dfa.DFA_event;

public class DFA_callback_demo {
    public static class Default implements ICallback {
        @Override
        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);
            
            Util.acceptForward(dfa, e.getChar());
        }
    }

    public static class Accepted implements ICallback {
        @Override
        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);

            System.out.printf("[Accepted] Word detected, return to initial state%n");
            Util.acceptFinished(dfa, e.getChar(), "Word");
        }
    }
}
