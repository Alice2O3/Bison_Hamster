prog -> prog_start_p EOF
prog_start_p -> prog_start | @
prog_start -> statement statement_p
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
param -> type Identifier
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