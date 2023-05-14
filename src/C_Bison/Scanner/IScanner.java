package C_Bison.Scanner;

import C_Flex.DFA_lexing;

import java.util.List;

public interface IScanner {
    List<DFA_lexing> scan(String code);
}
