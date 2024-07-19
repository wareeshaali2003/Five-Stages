// Branch control ( Assignment )
package singlecycle
import chisel3._ 
import chisel3.util._

class LM_IO_Interface_BranchControl extends Bundle {
val aluop = Input ( UInt (5. W ) )
val branch = Input ( Bool () )
val arg_x = Input ( SInt (32. W ) )
val arg_y = Input ( SInt (32. W ) )
val br_taken = Output ( Bool () )
}
object aluop {
// ALU Operations , may expand / modify in future
val beq = 0. U (4. W )
val bne = 1. U (4. W )
val blt = 2. U (4. W )
val bge = 3. U (4. W )
val bltu = 4. U (4. W )
val bgeu = 5. U (4. W )
}
import aluop._
class BranchControl extends Module {
val io = IO ( new LM_IO_Interface_BranchControl )
// Start Coding here
io . br_taken  := 0.U
switch ( io.aluop ) {
    is ( beq ) {
        val con = io.arg_x === io.arg_y
    io . br_taken  := io . branch && con 
}
is ( bne ) {
    val con = io.arg_x =/= io.arg_y
    io . br_taken  := io . branch && con 
}
is (blt) {
    val con = io.arg_x > io.arg_y
    io . br_taken  := io . branch && con 
}
is (bge) {
    val con = io.arg_x < io.arg_y
    io . br_taken  := io . branch && con 
}
is (bltu) {
    val con = io.arg_x <= io.arg_y
    io . br_taken  := io . branch && con 
}
is (bgeu) {
    val con = io.arg_x >= io.arg_y 
    io . br_taken  := io . branch && con 
}
}}
// End your code here
// Well , you can actually write classes too . So , technically you have no