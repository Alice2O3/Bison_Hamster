package C_Bison.Scanner;

import C_Flex.DFA_Types.*;

public interface IScanner {
    DFA_lexing_list scan(String code);
}
