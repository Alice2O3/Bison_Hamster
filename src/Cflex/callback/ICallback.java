package Cflex.callback;

import Cflex.DFA;
import Cflex.DFA_event;

public interface ICallback {
    void execute(DFA dfa, DFA_event e);
}