# DominosaSolver
This is a JavaScript implementation of the Dominosa Solver,  originally written in Java by @Monikae. 

## Usage
To get a solution for a particular board:

    dominosa_solver.DominosaSolver.getSolution(rows, columns, highestNumber, numbersOnBoard[][])

Prints the solution in the browser's debug console and returns the solution positions as an array (null if board is unsolveable).

dominosa_solver namespace also gives the user access to another other object in the library. 

## Quick Start 
Simply download solver.html, load it in the browser and enter the command above
in the browser's debug console. 

## Library 
DominosaSolver.js and DominosaSolver.ts are compiled, standalone libraries that can be used in any JS or TS project. 

## Modifying Source
This library was created with the [Jsweet transpiler](https://github.com/cincheo/jsweet "Jsweet transpiler"). To modify the library, it's recommended to clone the repository, edit the source files and recompile.

To recompile, you must first have Git, Maven and NodeJS installed and available in your path.  From there, execute the command 
`mvn generate-sources`
in the repository's root directory.   This will re-transpile the library and place it in the target/js/build.js directory. 

For compatability purposes, the library itself avoids using many Java utilities in favor of replacing them with JavaScript functions provided by the JSweet API. Please see the [Jsweet Language Specification](https://github.com/cincheo/jsweet/blob/master/doc/jsweet-language-specifications.md#functional-types "Jsweet Language Specification") for more information. 

