package C_Flex.callback;

import C_Flex.DFA;
import C_Flex.DFA_Types.*;

public class Common {
    public static class Escape implements ICallback { //When meeting \n
        public void execute(DFA dfa, DFA_event e) {
            Util.processEscape(dfa);
        }
    }

    public static class Escape_Accept implements ICallback { //When meeting \n
        private final Integer token_type;
        public Escape_Accept(Integer token_type_){
            token_type = token_type_;
        }
        public void execute(DFA dfa, DFA_event e) {
            Util.processEscapeAccept(dfa, token_type);
        }
    }


    public static class Forward implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            Util.moveForward(dfa);
        }
    }

    public static class Accept implements ICallback {
        public void execute(DFA dfa, DFA_event e){
            Util.acceptForward(dfa, e.getChar());
        }
    }

    public static class Accept_Keep implements ICallback {
        private final Integer token_type;
        public Accept_Keep(Integer token_type_){
            token_type = token_type_;
        }

        public void execute(DFA dfa, DFA_event e) {
            Util.acceptFinishedKeep(dfa, token_type);
        }
    }

    public static class Discard_Keep implements ICallback {
        public void execute(DFA dfa, DFA_event e) {
            Util.discardKeep(dfa);
        }
    }
}