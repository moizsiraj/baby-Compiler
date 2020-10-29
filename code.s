.data
A:	.asciiz	"hello world hello world \n"
B:	.asciiz	"hi world  \n"
C:	.asciiz	"a \n"

.text
main:
	li	$v0,	4	
	la	$a0	A
	syscall
	li	$v0,	4	
	la	$a0	B
	syscall
	li	$v0,	4	
	la	$a0	C
	syscall
	li	$v0,	10	
	syscall