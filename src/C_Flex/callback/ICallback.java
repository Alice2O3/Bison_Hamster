package C_Flex.callback;

import C_Flex.DFA;
import C_Flex.DFA_Types.*;

public interface ICallback {
    void execute(DFA dfa, DFA_event e);
}