package cflex.dfa.callback;

import cflex.dfa.DFA;
import cflex.dfa.DFA_event;

public class DFA_callback {
    public static class Escape implements ICallback { //When meeting \n
        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);

            System.out.printf("'\\n' detected, using escape method%n");
            Util.processEscape(dfa);
        }
    }

    public static class Forward implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            Util.debugInfo(e);

            System.out.printf("Moving forward%n");
            Util.moveForward(dfa);
        }
    }

    public static class Accept implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            Util.debugInfo(e);

            System.out.printf("Accepting new character%n");
            Util.acceptForward(dfa, e.getChar());
        }
    }

    public static class Accept_Keep implements ICallback {
        private final String token_type;
        public Accept_Keep(String token_type_){
            token_type = token_type_;
        }

        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);

            Util.acceptFinishedKeep(dfa, token_type);
        }
    }

    public static class Accept_Operator_Keep implements ICallback {
        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);

            Util.acceptFinishedKeep(dfa, String.format("'%s'", dfa.getLexeme()));
        }
    }

    public static class Discard_Keep implements ICallback {
        public void execute(DFA dfa, DFA_event e) {
            Util.debugInfo(e);

            Util.discardKeep(dfa, dfa.getLexeme());
        }
    }
}