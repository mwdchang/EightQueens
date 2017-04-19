# EightQueens 
An Eight-Queens "solver" using genetic algorithm approach as outlined in *Artificial Intelligence: A Modern Approach*. 

This is more of an execuse to practice/learn Scala than any real attempt at making genetic algorithm work efficient for the Eight-Queens problem. 

In practice the solver performs quite poorly. There is no guarantee that, after the first few initial generations, the selection-reproduction stage in subsequent generations will yield progressively better states. In another words, the population converges to something close to a solution state after a few iterations - it then depends on random luck to actually reach a solution state.

The program uses an integer array of length 8 to encode the state, each element represent a column while the value represent the row. The variable **size** controls the population size. By default program terminates after 2000 iterations if no solution states are generated.




