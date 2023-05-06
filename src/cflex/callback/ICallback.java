package cflex.callback;

import cflex.DFA;
import cflex.DFA_event;

public interface ICallback {
    void execute(DFA dfa, DFA_event e);
}