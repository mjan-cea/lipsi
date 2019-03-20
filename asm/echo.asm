# 
# Echo IO
# read input, add 1, write to output
#

loop:
io 0x1
addi 0x01
br loop
