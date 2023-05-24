prog -> prog_start_p EOF
prog_start_p -> prog_start | @
prog_start -> statement statement_p
statement_p -> statement statement_p | @
expression -> atom | infix_expression
infix_expression -> atom expression_operator expression
atom -> const | Identifier
const -> Integer
statement -> block_statement | simple_statement
simple_statement -> identifier_declare_statement | fuction_declare_statement | assign_statement | expression_statement | if_statement | loop_statement | return_statement
block_statement -> Left_big_bracket statement_p Right_big_bracket statement_end_p
statement_end_p -> Statement_end | @
identifier_declare_statement -> identifier_declare_statement_assign | identifier_declare_statement_noassign
identifier_declare_statement_assign -> type Identifier declare_operator expression Statement_end
identifier_declare_statement_noassign -> type Identifier Statement_end
fuction_declare_statement -> type_func Identifier Left_small_bracket param_list Right_small_bracket block_statement
param_list -> param param_p
param_p -> Comma param param_p | @
param -> type Identifier
assign_statement -> assign_statement_sub Statement_end
assign_statement_sub -> Identifier assign_operator expression
expression_statement -> expression Statement_end
if_statement -> If Left_small_bracket expression Right_small_bracket statement
loop_statement -> For Left_small_bracket assign_statement_sub Statement_end expression Statement_end assign_statement_sub Right_small_bracket statement
return_statement -> Return expression Statement_end
type -> Int
type_func -> Int | Void
expression_operator -> Plus | Minus | Equalto | Greater | Smaller | GreaterEqual | SmallerEqual
declare_operator -> Assign
assign_operator -> Assign | AssignAdd | AssignMinus