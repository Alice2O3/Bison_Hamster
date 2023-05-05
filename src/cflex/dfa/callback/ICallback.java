package cflex.dfa.callback;

import cflex.dfa.DFA;
import cflex.dfa.DFA_event;

public interface ICallback {
    void execute(DFA dfa, DFA_event e);
}