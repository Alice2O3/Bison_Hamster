expression -> term term_z
term_z -> Add term term_z | Minus term term_z | @
term -> term_p term_y
term_y -> Mul term_p term_y | Divide term_p term_y | @
term_p -> term_f term_x
term_x -> Exp term_f term_x | @
term_f -> Minus term_f | Add term_f | Atom | Left_Bracket expression Right_Bracket
