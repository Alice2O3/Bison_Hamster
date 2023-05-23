# Bison_Hamster

<div align=center>
    <img src="img/Bison_Hamster_Avatar.jpeg" width = "400" height = "400">
</div>

GNU has Bison for LR grammars, and we have Bison Hamster for LL(1) grammars!

This hamster has more intelligence on LL(1) than GPT-4 and will help you analyze LL(1) grammars and parse Simple C Program into AST trees! (I want to give her a full life~)

**Image Source**: Avatar of anime artist 
Bison倉鼠 (Pixiv: [https://www.pixiv.net/users/333556](https://www.pixiv.net/users/333556), support her if possible~)

## Usage

This project is build with Intellij IDEA, so just build as follows:

1. Fetch the project

```bash
git clone https://github.com/Alice2O3/Bison_Hamster.git
```

2. Open the project with Intellij IDEA
3. Run Main.java to see how the hamster will parse the code~ >ω<
4. If you want to go deeper, you can run the Debug tests in folder `"src/Debug"` >ω<

## Grammar Format

```txt
<Non_Terminal> -> <Expr> | <Expr> | ... | <Expr>
<Non_Terminal> -> <Expr> | <Expr> | ... | <Expr>
...
```

In this format, `<Expr>` can be expressed as `<Terminal>/<Non_Terminal> ... <Terminal>/<Non_Terminal>` or `@` (Empty), and `<Terminal>` can be expressed as `[A-Z][A-Za-z_]*` in regex (First character is **Upper Case**), `<Non_Terminal>` can be expressed as `[a-z][A-Za-z_]*` in regex (First character is **Lower Case**).

### Sample Format

Grammar used to parse simple C program into AST trees is located in `Grammars/Easy_C.bison`:

```txt
prog -> statement statement_p
statement_p -> statement statement_p | @
expression -> atom infix_expression
infix_expression -> operator_expression | assign_expression | @
operator_expression -> expression_operator expression
assign_expression -> assign_operator expression
atom -> const | Identifier
const -> Integer
statement -> block_statement | simple_statement
simple_statement -> declare_statement | expression_statement | if_statement | loop_statement | return_statement
block_statement -> Left_big_bracket statement_p Right_big_bracket statement_end_p
statement_end_p -> Statement_end | @
declare_statement -> identifier_declare_statement | type_func Identifier fuction_declare
identifier_declare_statement -> type Identifier identifier_declare_statement_p
identifier_declare_statement_p -> fuction_declare | identifier_declare_statement_assign | Statement_end
identifier_declare_statement_assign -> declare_operator expression Statement_end
expression_statement -> expression Statement_end
fuction_declare -> Left_small_bracket param_list Right_small_bracket block_statement
param_list -> param param_p
param_p -> Comma param param_p | @
param -> type Identifier | @
if_statement -> If Left_small_bracket expression Right_small_bracket statement
loop_statement -> For Left_small_bracket for_expression_l Statement_end for_expression_m Statement_end for_expression_r Right_small_bracket statement
for_expression_l -> Identifier assign_expression
for_expression_m -> expression
for_expression_r -> Identifier assign_expression
return_statement -> Return expression Statement_end
type -> Int
type_func -> Void
expression_operator -> Plus | Minus | Equalto | Greater | Smaller | GreaterEqual | SmallerEqual
declare_operator -> Assign
assign_operator -> Assign | AssignAdd | AssignMinus
```

The symbol list and index are as follows:

```txt
Terminals:
(Return, 45), (For, 41), (Statement_end, 26), (SmallerEqual, 54), (Left_big_bracket, 23), (AssignMinus, 57), (AssignAdd, 56), (Int, 46), (Equalto, 50), (Integer, 15), (Comma, 39), (GreaterEqual, 53), (Right_small_bracket, 36), (Identifier, 14), (Left_small_bracket, 34), (Right_big_bracket, 24), (Greater, 51), (Assign, 55), (Smaller, 52), (Void, 47), (Plus, 48), (EOF, 2), (If, 40), (Minus, 49)

Non_Terminals:
(const, 13), (identifier_declare_statement_p, 31), (prog_start_p, 1), (operator_expression, 9), (type_func, 28), (for_expression_r, 44), (simple_statement, 17), (param_p, 38), (type, 30), (fuction_declare, 29), (param, 37), (statement, 4), (statement_end_p, 25), (block_statement, 16), (declare_statement, 18), (identifier_declare_statement, 27), (declare_operator, 33), (return_statement, 22), (expression, 6), (expression_statement, 19), (assign_operator, 12), (identifier_declare_statement_assign, 32), (prog, 0), (param_list, 35), (infix_expression, 8), (statement_p, 5), (expression_operator, 11), (if_statement, 20), (loop_statement, 21), (assign_expression, 10), (for_expression_m, 43), (prog_start, 3), (atom, 7), (for_expression_l, 42)
```

## LL(1) grammar support

This hamaster only recognizes LL(1) grammar, so make sure the grammar is LL(1) and if it has any left recursion or overlapped SELECT set, she will tell you and throw the error >ω<

Try to revise the grammar to meet her need >ω<

## AST Format

AST node has two types, the attributes are as follows:

- Non_Terminals: "type" : String, "children" : List
- Terminals: "value" : String

### Sample Format

```json
{
    "type": "xxx",
    "children":
    [
        {
            "type": "xxx",
            "children":
            [
                {
                    "type": "xxx",
                    "children":
                    [
                        {
                            "value": "xxx"
                        }
                    ]
                },
                {
                    "type": "xxx",
                    "children":
                    [
                        {
                            "type": "xxx",
                            "children":
                            [
                                {
                                    "type": "xxx",
                                    "children":
                                    [
                                        {
                                            "type": "xxx",
                                            "children":
                                            [
                                                {
                                                    "value": "xxx"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "xxx",
                            "children":
                            [
                                {
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "xxx",
                    "children":
                    [
                        {
                            "value": "xxx"
                        }
                    ]
                }
            ]
        }
    ]
}
```

## Limitations

- This parser only works on LL(1) grammar, so you have to revise your grammar into LL(1) type >ω<
- Since she don't have time to write regex expressions into DFA, the lexer has to be written by ourselves and the symbol index is hard coded (see src/C_Flex). But the lexer is written with general DFA and once you get hang of it, you can quickly modify them. Also, you just need to consider the mapping of token_id and the corresponding terminal string in LL(1), and the hamster will do the rest for you >ω<

```txt
         ^      
        <0>     
     _   v  _   
    / \  / / \  
   / _ \--/   \ 
   \/ >   <   / 
   |    ω    /  
   \        |   
    |       |   
```
