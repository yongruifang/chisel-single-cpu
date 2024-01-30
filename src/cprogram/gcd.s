	.file	"gcd.c"
	.option nopic
	.option checkconstraints
	.attribute arch, "rv32i2p0"
	.attribute unaligned_access, 0
	.attribute stack_align, 16
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-48
	sw	s0,44(sp)
	addi	s0,sp,48
	li	a5,12
	sw	a5,-28(s0)
	li	a5,20
	sw	a5,-32(s0)
	lw	a5,-28(s0)
	sw	a5,-20(s0)
	lw	a5,-32(s0)
	sw	a5,-24(s0)
	lw	a4,-20(s0)
	lw	a5,-24(s0)
	bge	a4,a5,.L3
	lw	a5,-32(s0)
	sw	a5,-20(s0)
	lw	a5,-28(s0)
	sw	a5,-24(s0)
	j	.L3
.L5:
	lw	a4,-20(s0)
	lw	a5,-24(s0)
	sub	a5,a4,a5
	sw	a5,-36(s0)
	lw	a4,-24(s0)
	lw	a5,-36(s0)
	ble	a4,a5,.L4
	lw	a5,-24(s0)
	sw	a5,-20(s0)
	lw	a5,-36(s0)
	sw	a5,-24(s0)
	j	.L3
.L4:
	lw	a5,-36(s0)
	sw	a5,-20(s0)
.L3:
	lw	a4,-20(s0)
	lw	a5,-24(s0)
	bne	a4,a5,.L5
	li	a5,0
	mv	a0,a5
	lw	s0,44(sp)
	addi	sp,sp,48
	jr	ra
	.size	main, .-main
	.ident	"GCC: (GNU) 9.2.0"
