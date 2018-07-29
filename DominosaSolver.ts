/* Generated from Java with JSweet 2.2.0-SNAPSHOT - http://www.jsweet.org */
namespace dominosa_solver {
    export enum Orientation {
        HORIZONTAL, VERTICAL
    }

    /** @ignore */
    export class Orientation_$WRAPPER {
        /*private*/ c;

        /*private*/ adverb;

        constructor(protected _$ordinal : number, protected _$name : string, c, adverb) {
            if(this.c===undefined) this.c = null;
            if(this.adverb===undefined) this.adverb = null;
            this.c = c;
            this.adverb = adverb;
        }

        public getSymbol() : string {
            return "" + this.c;
        }

        public getAdverb() : string {
            return this.adverb;
        }
        public name() : string { return this._$name; }
        public ordinal() : number { return this._$ordinal; }
    }
    Orientation["__class"] = "dominosa_solver.Orientation";
    Orientation["__interfaces"] = ["java.lang.Comparable","java.io.Serializable"];

    Orientation["_$wrappers"] = [new Orientation_$WRAPPER(0, "HORIZONTAL", 'H', "horizontally"), new Orientation_$WRAPPER(1, "VERTICAL", 'V', "vertically")];

}
namespace dominosa_solver {
    export enum SolveStatus {
        SOLVED, INCOMPLETE, CONFLICT
    }
}
namespace dominosa_solver {
    export class UnsolvableException extends Error {
        public constructor(message? : any) {
            if(((typeof message === 'string') || message === null)) {
                let __args = Array.prototype.slice.call(arguments);
                super(message); this.message=message;
                (<any>Object).setPrototypeOf(this, UnsolvableException.prototype);
            } else if(message === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                super();
                (<any>Object).setPrototypeOf(this, UnsolvableException.prototype);
            } else throw new Error('invalid overload');
        }
    }
    UnsolvableException["__class"] = "dominosa_solver.UnsolvableException";
    UnsolvableException["__interfaces"] = ["java.io.Serializable"];


}
namespace dominosa_solver {
    export class DominosaSolver {
        public static getSolution(numRows : number, numColumns : number, highestNumber : number, numbersOnBoard : number[][]) : Array<dominosa_solver.Position> {
            dominosa_solver.Board.printSteps = true;
            let board : dominosa_solver.Board = dominosa_solver.Board.createInitialBoard(highestNumber, numRows, numColumns, numbersOnBoard);
            let solveStatus : dominosa_solver.SolveStatus = board.seek();
            if(solveStatus !== dominosa_solver.SolveStatus.SOLVED) {
                return null;
            } else {
                console.log("End - final directions:");
                dominosa_solver.PotentialDirections.printFinalDirections(board.finalDirections);
                return DominosaSolver.getPositions(board.potentialPositionsForTiles, highestNumber);
            }
        }

        /*private*/ static getPositions(solution : dominosa_solver.PositionList[][], highestNumber : number) : Array<dominosa_solver.Position> {
            let positions : Array<dominosa_solver.Position> = <any>(new Array<dominosa_solver.Position>());
            for(let i : number = 0; i <= highestNumber; i++) {
                for(let j : number = i; j <= highestNumber; j++) {
                    let pos : dominosa_solver.Position = solution[i][j].getFirst();
                    positions.push(pos);
                };
            };
            return positions;
        }

        /*private*/ static leadingZeroes(number : number, leadingZeroes : boolean) : string {
            if(leadingZeroes === false) return "" + number;
            if(number <= 9) return "0" + number; else return "" + number;
        }
    }
    DominosaSolver["__class"] = "dominosa_solver.DominosaSolver";

}
namespace dominosa_solver {
    export class Position {
        row : number;

        column : number;

        orientation : dominosa_solver.Orientation;

        /**
         * 
         * @return {string}
         */
        public toString() : string {
            return this.row + "," + this.column + " " + this.orientation;
        }

        public constructor(row : number, column : number, orientation : dominosa_solver.Orientation) {
            if(this.row===undefined) this.row = 0;
            if(this.column===undefined) this.column = 0;
            if(this.orientation===undefined) this.orientation = null;
            this.row = row;
            this.column = column;
            this.orientation = orientation;
        }

        public getOverlappingPositions() : Array<Position> {
            let list : Array<Position> = <any>(new Array<any>());
            {
                let array122 = this.getSquaresAbove();
                for(let index121=0; index121 < array122.length; index121++) {
                    let a = array122[index121];
                    {
                        list.push(new Position(a.row, a.column, dominosa_solver.Orientation.VERTICAL));
                    }
                }
            }
            {
                let array124 = this.getSquaresLeft();
                for(let index123=0; index123 < array124.length; index123++) {
                    let l = array124[index123];
                    {
                        list.push(new Position(l.row, l.column, dominosa_solver.Orientation.HORIZONTAL));
                    }
                }
            }
            {
                let array126 = this.getSquaresBelow();
                for(let index125=0; index125 < array126.length; index125++) {
                    let b = array126[index125];
                    {
                        list.push(new Position(b.row - 1, b.column, dominosa_solver.Orientation.VERTICAL));
                    }
                }
            }
            {
                let array128 = this.getSquaresRight();
                for(let index127=0; index127 < array128.length; index127++) {
                    let r = array128[index127];
                    {
                        list.push(new Position(r.row, r.column - 1, dominosa_solver.Orientation.HORIZONTAL));
                    }
                }
            }
            return list;
        }

        public getSmallerNumber() : number {
            if(this.getFirstNumber() <= this.getSecondNumber()) return this.getFirstNumber();
            return this.getSecondNumber();
        }

        public getBiggerNumber() : number {
            if(this.getFirstNumber() <= this.getSecondNumber()) return this.getSecondNumber();
            return this.getFirstNumber();
        }

        /*private*/ getFirstNumber() : number {
            return dominosa_solver.Board.numbersOnBoard[this.row][this.column];
        }

        /*private*/ getSecondNumber() : number {
            return dominosa_solver.Board.numbersOnBoard[this.secondRow()][this.secondColumn()];
        }

        public getSquares() : dominosa_solver.Square[] {
            let squares : dominosa_solver.Square[] = [null, null];
            squares[0] = new dominosa_solver.Square(this.row, this.column);
            if(this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares[1] = new dominosa_solver.Square(this.row, this.column + 1);
            } else if(this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column);
            }
            return squares;
        }

        public secondRow() : number {
            return this.getSquares()[1].row;
        }

        public secondColumn() : number {
            return this.getSquares()[1].column;
        }

        /**
         * 
         * @return {Array} the square or squares above, if any
         */
        public getSquaresAbove() : dominosa_solver.Square[] {
            let squares : dominosa_solver.Square[] = [];
            if(this.row === 0) return squares;
            if(this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row - 1, this.column);
                squares[1] = new dominosa_solver.Square(this.row - 1, this.column + 1);
            } else if(this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row - 1, this.column);
            }
            return squares;
        }

        /**
         * 
         * @return {Array} the square or squares below, if any
         */
        public getSquaresBelow() : dominosa_solver.Square[] {
            let squares : dominosa_solver.Square[] = [];
            if(this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                if(this.row === dominosa_solver.Board.numRows - 1) return squares;
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row + 1, this.column);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column + 1);
            } else if(this.orientation === dominosa_solver.Orientation.VERTICAL) {
                if(this.row === dominosa_solver.Board.numRows - 2) return squares;
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row + 2, this.column);
            }
            return squares;
        }

        /**
         * 
         * @return {Array} the square or squares to the left, if any
         */
        public getSquaresLeft() : dominosa_solver.Square[] {
            let squares : dominosa_solver.Square[] = [];
            if(this.column === 0) return squares;
            if(this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row, this.column - 1);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column - 1);
            } else if(this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row, this.column - 1);
            }
            return squares;
        }

        /**
         * 
         * @return {Array} the square or squares to the right, if any
         */
        public getSquaresRight() : dominosa_solver.Square[] {
            let squares : dominosa_solver.Square[] = [];
            if(this.orientation === dominosa_solver.Orientation.VERTICAL) {
                if(this.column === dominosa_solver.Board.numColumns - 1) return squares;
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row, this.column + 1);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column + 1);
            } else if(this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                if(this.column === dominosa_solver.Board.numColumns - 2) return squares;
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row, this.column + 2);
            }
            return squares;
        }

        /**
         * 
         * @return {number}
         */
        public hashCode() : number {
            let prime : number = 31;
            let result : number = 1;
            result = prime * result + ((this.orientation == null)?0:dominosa_solver.Orientation["_$wrappers"][this.orientation].hashCode());
            result = prime * result + this.row;
            result = prime * result + this.column;
            return result;
        }

        /**
         * 
         * @param {*} obj
         * @return {boolean}
         */
        public equals(obj : any) : boolean {
            if(this === obj) return true;
            if(obj == null) return false;
            if((<any>this.constructor) !== (<any>obj.constructor)) return false;
            let other : Position = <Position>obj;
            if(this.orientation !== other.orientation) return false;
            if(this.row !== other.row) return false;
            if(this.column !== other.column) return false;
            return true;
        }
    }
    Position["__class"] = "dominosa_solver.Position";

}
namespace dominosa_solver {
    export enum InputModeEnum {
        DEFAULT, SPACES, LETTERS, ZEROES
    }
}
namespace dominosa_solver {
    export enum DirectionEnum {
        LEFT, RIGHT, UP, DOWN, NOT_SET
    }

    /** @ignore */
    export class DirectionEnum_$WRAPPER {
        /*private*/ depiction;

        constructor(protected _$ordinal : number, protected _$name : string, value) {
            if(this.depiction===undefined) this.depiction = null;
            this.depiction = value;
        }

        /**
         * 
         * @return {string}
         */
        public toString() : string {
            return "" + this.depiction;
        }

        public value() : string {
            return this.depiction;
        }
        public name() : string { return this._$name; }
        public ordinal() : number { return this._$ordinal; }
    }
    DirectionEnum["__class"] = "dominosa_solver.DirectionEnum";
    DirectionEnum["__interfaces"] = ["java.lang.Comparable","java.io.Serializable"];

    DirectionEnum["_$wrappers"] = [new DirectionEnum_$WRAPPER(0, "LEFT", '<'), new DirectionEnum_$WRAPPER(1, "RIGHT", '>'), new DirectionEnum_$WRAPPER(2, "UP", '^'), new DirectionEnum_$WRAPPER(3, "DOWN", 'v'), new DirectionEnum_$WRAPPER(4, "NOT_SET", '?')];

}
namespace dominosa_solver {
    export class Board {
        public static numColumns : number = 0;

        public static numRows : number = 0;

        public static highestNumber : number = 0;

        public static numbersOnBoard : number[][] = null;

        potentialDirections : dominosa_solver.PotentialDirections[][];

        finalDirections : string[][];

        potentialPositionsForTiles : dominosa_solver.PositionList[][];

        static printSteps : boolean = false;

        public constructor(b? : any) {
            if(((b != null && b instanceof <any>dominosa_solver.Board) || b === null)) {
                let __args = Array.prototype.slice.call(arguments);
                if(this.potentialDirections===undefined) this.potentialDirections = null;
                if(this.finalDirections===undefined) this.finalDirections = null;
                if(this.potentialPositionsForTiles===undefined) this.potentialPositionsForTiles = null;
                if(this.potentialDirections===undefined) this.potentialDirections = null;
                if(this.finalDirections===undefined) this.finalDirections = null;
                if(this.potentialPositionsForTiles===undefined) this.potentialPositionsForTiles = null;
                (() => {
                    this.potentialDirections = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.numRows, Board.numColumns]);
                    this.finalDirections = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.numRows, Board.numColumns]);
                    this.potentialPositionsForTiles = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.highestNumber + 1, Board.highestNumber + 1]);
                    Board.copy(b.potentialDirections, this.potentialDirections, b.finalDirections, this.finalDirections, b.potentialPositionsForTiles, this.potentialPositionsForTiles);
                })();
            } else if(b === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                if(this.potentialDirections===undefined) this.potentialDirections = null;
                if(this.finalDirections===undefined) this.finalDirections = null;
                if(this.potentialPositionsForTiles===undefined) this.potentialPositionsForTiles = null;
                if(this.potentialDirections===undefined) this.potentialDirections = null;
                if(this.finalDirections===undefined) this.finalDirections = null;
                if(this.potentialPositionsForTiles===undefined) this.potentialPositionsForTiles = null;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {dominosa_solver.SolveStatus} solved / incomplete / conflict
         * @private
         */
        /*private*/ seekUnique() : dominosa_solver.SolveStatus {
            let solveStatus : dominosa_solver.SolveStatus = dominosa_solver.SolveStatus.INCOMPLETE;
            try {
                let changed : boolean = true;
                while((changed === true)) {
                    changed = false;
                    for(let i : number = 0; i <= Board.highestNumber; i++) {
                        for(let j : number = i; j <= Board.highestNumber; j++) {
                            if(this.potentialPositionsForTiles[i][j].hasExactlyOne() && this.potentialPositionsForTiles[i][j].done === false) {
                                if(Board.printSteps) console.log("Only one potential position: ");
                                this.potentialPositionsForTiles[i][j].done = true;
                                changed = true;
                                let position : dominosa_solver.Position = this.potentialPositionsForTiles[i][j].getFirst();
                                this.setUniquePosition$int$int$dominosa_solver_Position(i, j, position);
                            }
                        };
                    };
                    for(let r : number = 0; r < Board.numRows; r++) {
                        for(let c : number = 0; c < Board.numColumns; c++) {
                            let potDir : dominosa_solver.PotentialDirections = this.potentialDirections[r][c];
                            if(potDir.done === false && potDir.hasOnlyOneDirection() === true) {
                                if(Board.printSteps) console.log("Only one potential direction: ");
                                potDir.done = true;
                                changed = true;
                                let top : number = -1;
                                let left : number = -1;
                                let dir : dominosa_solver.Orientation = null;
                                if(potDir.right) {
                                    top = r;
                                    left = c;
                                    dir = dominosa_solver.Orientation.HORIZONTAL;
                                }
                                if(potDir.left) {
                                    top = r;
                                    left = c - 1;
                                    dir = dominosa_solver.Orientation.HORIZONTAL;
                                }
                                if(potDir.up) {
                                    top = r - 1;
                                    left = c;
                                    dir = dominosa_solver.Orientation.VERTICAL;
                                }
                                if(potDir.down) {
                                    top = r;
                                    left = c;
                                    dir = dominosa_solver.Orientation.VERTICAL;
                                }
                                this.setUniquePosition$dominosa_solver_Position(new dominosa_solver.Position(top, left, dir));
                            }
                        };
                    };
                };
                solveStatus = this.isDone();
            } catch(ex) {
                solveStatus = dominosa_solver.SolveStatus.CONFLICT;
            };
            return solveStatus;
        }

        /**
         * 
         * @return {dominosa_solver.SolveStatus} solved / incomplete / conflict
         */
        public seek() : dominosa_solver.SolveStatus {
            let seekUniqueSolveStatus : dominosa_solver.SolveStatus = this.seekUnique();
            if(seekUniqueSolveStatus === dominosa_solver.SolveStatus.SOLVED) {
                return dominosa_solver.SolveStatus.SOLVED;
            }
            if(seekUniqueSolveStatus === dominosa_solver.SolveStatus.CONFLICT) {
                return dominosa_solver.SolveStatus.CONFLICT;
            }
            if(seekUniqueSolveStatus === dominosa_solver.SolveStatus.INCOMPLETE) {
                let newBoard : Board = null;
                let clonedBoardSolveStatus : dominosa_solver.SolveStatus = null;
                let seekUniqueSolveStatus2 : dominosa_solver.SolveStatus = null;
                while((true)) {
                    let guess : dominosa_solver.Position = this.guess();
                    if(guess == null) {
                        return dominosa_solver.SolveStatus.CONFLICT;
                    }
                    newBoard = new Board(this);
                    if(Board.printSteps) console.log("--> Making a guess: ");
                    newBoard.applyGuess(guess);
                    clonedBoardSolveStatus = newBoard.seek();
                    if(clonedBoardSolveStatus === dominosa_solver.SolveStatus.SOLVED) {
                        if(Board.printSteps) console.info("This guess worked.");
                        this.applyState(newBoard);
                        return dominosa_solver.SolveStatus.SOLVED;
                    }
                    if(clonedBoardSolveStatus === dominosa_solver.SolveStatus.CONFLICT) {
                        if(Board.printSteps) console.info("<-- This guess didn\'t work.");
                        this.markGuessAsImpossible(guess);
                        seekUniqueSolveStatus2 = this.seekUnique();
                        switch((seekUniqueSolveStatus2)) {
                        case dominosa_solver.SolveStatus.SOLVED:
                            return dominosa_solver.SolveStatus.SOLVED;
                        case dominosa_solver.SolveStatus.CONFLICT:
                            if(Board.printSteps) console.info("<-- This guess didn\'t work. The alternative didn\'t work either.");
                            return dominosa_solver.SolveStatus.CONFLICT;
                        case dominosa_solver.SolveStatus.INCOMPLETE:
                            if(Board.printSteps) console.info("<-- This guess didn\'t work, but helped us narrow down the possibilities.");
                            break;
                        }
                    }
                };
            }
            return seekUniqueSolveStatus;
        }

        /**
         * doesn't change the board
         * @return {dominosa_solver.Position} null if it was not possible to make a guess
         * @private
         */
        /*private*/ guess() : dominosa_solver.Position {
            let positionListLength2 : dominosa_solver.PositionList = null;
            let lastPositionListLength3orMore : dominosa_solver.PositionList = null;
            out: for(let row : number = 0; row < this.potentialPositionsForTiles.length; row++) {
                for(let column : number = 0; column < this.potentialPositionsForTiles[row].length; column++) {
                    let positionList : dominosa_solver.PositionList = this.potentialPositionsForTiles[row][column];
                    let size : number = positionList.size();
                    if(size === 0 || size === 1) {
                        continue;
                    }
                    if(size === 2) {
                        positionListLength2 = positionList;
                        break out;
                    }
                    if(size >= 3) {
                        lastPositionListLength3orMore = positionList;
                    }
                };
            };
            let position : dominosa_solver.Position = null;
            if(positionListLength2 != null) {
                position = positionListLength2.getFirst();
            } else if(lastPositionListLength3orMore != null) {
                position = lastPositionListLength3orMore.getFirst();
            } else {
                return null;
            }
            return position;
        }

        public static createInitialBoard(highestNumber : number, numRows : number, numColumns : number, numbersOnBoard : number[][]) : Board {
            Board.highestNumber = highestNumber;
            Board.numRows = numRows;
            Board.numColumns = numColumns;
            Board.numbersOnBoard = numbersOnBoard;
            let board : Board = new Board();
            board.initializeFirstBoard();
            return board;
        }

        /*private*/ initializeFirstBoard() {
            this.finalDirections = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.numRows, Board.numColumns]);
            for(let i : number = 0; i < this.finalDirections.length; i++) {
                for(let j : number = 0; j < this.finalDirections[i].length; j++) {
                    this.finalDirections[i][j] = '?';
                };
            };
            this.potentialDirections = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.numRows, Board.numColumns]);
            dominosa_solver.PotentialDirections.initialize(this.potentialDirections);
            for(let i : number = 0; i < Board.numColumns; i++) {
                this.potentialDirections[0][i].up = false;
                this.potentialDirections[Board.numRows - 1][i].down = false;
            };
            for(let i : number = 0; i < Board.numRows; i++) {
                this.potentialDirections[i][0].left = false;
                this.potentialDirections[i][Board.numColumns - 1].right = false;
            };
            this.potentialPositionsForTiles = <any> (function(dims) { let allocate = function(dims) { if(dims.length==0) { return null; } else { let array = []; for(let i = 0; i < dims[0]; i++) { array.push(allocate(dims.slice(1))); } return array; }}; return allocate(dims);})([Board.highestNumber + 1, Board.highestNumber + 1]);
            for(let i : number = 0; i <= Board.highestNumber; i++) {
                for(let j : number = i; j <= Board.highestNumber; j++) {
                    this.potentialPositionsForTiles[i][j] = new dominosa_solver.PositionList();
                };
            };
            for(let i : number = 0; i < Board.numbersOnBoard.length; i++) {
                for(let j : number = 0; j < Board.numbersOnBoard[i].length - 1; j++) {
                    let numberOnLeftTile : number = Board.numbersOnBoard[i][j];
                    let numberOnRightTile : number = Board.numbersOnBoard[i][j + 1];
                    if(numberOnLeftTile <= numberOnRightTile) this.potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.HORIZONTAL)); else this.potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.HORIZONTAL));
                };
            };
            for(let i : number = 0; i < Board.numbersOnBoard.length - 1; i++) {
                for(let j : number = 0; j < Board.numbersOnBoard[i].length; j++) {
                    let numberOnTopTile : number = Board.numbersOnBoard[i][j];
                    let numberOnBottomTile : number = Board.numbersOnBoard[i + 1][j];
                    if(numberOnTopTile <= numberOnBottomTile) this.potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.VERTICAL)); else this.potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.VERTICAL));
                };
            };
        }

        /**
         * like removePotentialTile() without the exception
         * @param {dominosa_solver.Position} guess
         * @private
         */
        /*private*/ markGuessAsImpossible(guess : dominosa_solver.Position) {
            try {
                this.removePotentialTile(guess);
            } catch(ex) {
            };
        }

        /*private*/ removePotentialTile(position : dominosa_solver.Position) {
            this.potentialPositionsForTiles[position.getSmallerNumber()][position.getBiggerNumber()].remove$dominosa_solver_Position(position);
            let potDir1 : dominosa_solver.PotentialDirections = this.potentialDirections[position.row][position.column];
            let potDir2 : dominosa_solver.PotentialDirections = this.potentialDirections[position.secondRow()][position.secondColumn()];
            if(position.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                potDir1.clearRight();
                potDir2.clearLeft();
            } else if(position.orientation === dominosa_solver.Orientation.VERTICAL) {
                potDir1.clearDown();
                potDir2.clearUp();
            }
        }

        /**
         * like setUniquePosition() but no exception possible
         * @param {dominosa_solver.Position} guess
         * @private
         */
        /*private*/ applyGuess(guess : dominosa_solver.Position) {
            try {
                this.setUniquePosition$dominosa_solver_Position(guess);
            } catch(ex) {
            };
        }

        /*private*/ setUniquePosition$dominosa_solver_Position(uniquePosition : dominosa_solver.Position) {
            let smallerNumber : number = uniquePosition.getSmallerNumber();
            let biggerNumber : number = uniquePosition.getBiggerNumber();
            this.setUniquePosition$int$int$dominosa_solver_Position(smallerNumber, biggerNumber, uniquePosition);
        }

        public setUniquePosition$int$int$dominosa_solver_Position(smallerNumber : number, biggerNumber : number, uniquePosition : dominosa_solver.Position) {
            if(Board.printSteps) console.info("Placing tile [" + smallerNumber + " " + biggerNumber + "] at (" + uniquePosition.row + "/" + uniquePosition.column + ") " + dominosa_solver.Orientation["_$wrappers"][uniquePosition.orientation].getAdverb() + " (row/column) (counting from 0).");
            let removedPositions : Array<dominosa_solver.Position> = this.potentialPositionsForTiles[smallerNumber][biggerNumber].setUniquePosition(uniquePosition);
            this.potentialPositionsForTiles[smallerNumber][biggerNumber].done = true;
            for(let index129=0; index129 < removedPositions.length; index129++) {
                let removedPosition = removedPositions[index129];
                {
                    this.removePotentialTile(removedPosition);
                }
            }
            let row : number = uniquePosition.row;
            let column : number = uniquePosition.column;
            if(uniquePosition.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                this.finalDirections[row][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value();
                this.finalDirections[row][column + 1] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value();
                this.potentialDirections[row][column].clearAllExceptRight();
                this.potentialDirections[row][column + 1].clearAllExceptLeft();
            } else if(uniquePosition.orientation === dominosa_solver.Orientation.VERTICAL) {
                this.finalDirections[row][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value();
                this.finalDirections[row + 1][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value();
                this.potentialDirections[row][column].clearAllExceptDown();
                this.potentialDirections[row + 1][column].clearAllExceptUp();
            }
            let overlappingPositions : Array<dominosa_solver.Position> = uniquePosition.getOverlappingPositions();
            for(let index130=0; index130 < overlappingPositions.length; index130++) {
                let overlapPos = overlappingPositions[index130];
                {
                    this.removePotentialTile(overlapPos);
                }
            }
        }

        public setUniquePosition(smallerNumber? : any, biggerNumber? : any, uniquePosition? : any) : any {
            if(((typeof smallerNumber === 'number') || smallerNumber === null) && ((typeof biggerNumber === 'number') || biggerNumber === null) && ((uniquePosition != null && uniquePosition instanceof <any>dominosa_solver.Position) || uniquePosition === null)) {
                return <any>this.setUniquePosition$int$int$dominosa_solver_Position(smallerNumber, biggerNumber, uniquePosition);
            } else if(((smallerNumber != null && smallerNumber instanceof <any>dominosa_solver.Position) || smallerNumber === null) && biggerNumber === undefined && uniquePosition === undefined) {
                return <any>this.setUniquePosition$dominosa_solver_Position(smallerNumber);
            } else throw new Error('invalid overload');
        }

        public isDone() : dominosa_solver.SolveStatus {
            let potentialDirectionsUnique : boolean = true;
            let finalDirectionsSet : boolean = true;
            let potentialPositionsUnique : boolean = true;
            let zeroDirectionExists : boolean = false;
            let zeroPositionExists : boolean = false;
            out: for(let index131=0; index131 < this.potentialDirections.length; index131++) {
                let potentialDirectionsRow = this.potentialDirections[index131];
                {
                    for(let index132=0; index132 < potentialDirectionsRow.length; index132++) {
                        let potentialDirectionsSquare = potentialDirectionsRow[index132];
                        {
                            if(potentialDirectionsSquare.hasOnlyOneDirection() === false) {
                                potentialDirectionsUnique = false;
                            }
                            if(potentialDirectionsSquare.hasNoDirections()) {
                                zeroDirectionExists = true;
                                break out;
                            }
                        }
                    }
                }
            }
            for(let index133=0; index133 < this.finalDirections.length; index133++) {
                let cs = this.finalDirections[index133];
                {
                    for(let index134=0; index134 < cs.length; index134++) {
                        let c = cs[index134];
                        {
                            if((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.NOT_SET].value())) {
                                finalDirectionsSet = false;
                            }
                        }
                    }
                }
            }
            out: for(let i : number = 0; i < this.potentialPositionsForTiles.length; i++) {
                for(let j : number = i; j < this.potentialPositionsForTiles[i].length; j++) {
                    if(this.potentialPositionsForTiles[i][j].hasExactlyOne() === false) {
                        potentialPositionsUnique = false;
                    }
                    if(this.potentialPositionsForTiles[i][j].isEmpty()) {
                        zeroPositionExists = true;
                        break out;
                    }
                };
            };
            let returnStatus : dominosa_solver.SolveStatus = dominosa_solver.SolveStatus.INCOMPLETE;
            if(zeroDirectionExists || zeroPositionExists) {
                returnStatus = dominosa_solver.SolveStatus.CONFLICT;
            } else if(potentialDirectionsUnique || finalDirectionsSet || potentialPositionsUnique) {
                returnStatus = dominosa_solver.SolveStatus.SOLVED;
                if(!potentialDirectionsUnique || !finalDirectionsSet || !potentialPositionsUnique) {
                    console.info("Something is wrong, either all three criteria should say it\'s solved or none!");
                }
            }
            return returnStatus;
        }

        /*private*/ applyState(otherBoard : Board) {
            this.potentialDirections = otherBoard.potentialDirections;
            this.finalDirections = otherBoard.finalDirections;
            this.potentialPositionsForTiles = otherBoard.potentialPositionsForTiles;
        }

        /*private*/ static copy(potentialDirections : dominosa_solver.PotentialDirections[][], newPotentialDirections : dominosa_solver.PotentialDirections[][], finalDirections : string[][], newFinalDirections : string[][], potentialPositionsForTiles : dominosa_solver.PositionList[][], newPotentialPositionsForTiles : dominosa_solver.PositionList[][]) {
            for(let i : number = 0; i < potentialDirections.length; i++) {
                for(let j : number = 0; j < potentialDirections[i].length; j++) {
                    newPotentialDirections[i][j] = new dominosa_solver.PotentialDirections(potentialDirections[i][j]);
                };
            };
            for(let i : number = 0; i < finalDirections.length; i++) {
                for(let j : number = 0; j < finalDirections[i].length; j++) {
                    newFinalDirections[i][j] = finalDirections[i][j];
                };
            };
            for(let i : number = 0; i < potentialPositionsForTiles.length; i++) {
                for(let j : number = i; j < potentialPositionsForTiles[i].length; j++) {
                    newPotentialPositionsForTiles[i][j] = new dominosa_solver.PositionList(potentialPositionsForTiles[i][j]);
                };
            };
        }
    }
    Board["__class"] = "dominosa_solver.Board";

}
namespace dominosa_solver {
    export class PositionList {
        /*private*/ list : Array<dominosa_solver.Position> = <any>(new Array<any>());

        public done : boolean = false;

        public constructor(orig? : any) {
            if(((orig != null && orig instanceof <any>dominosa_solver.PositionList) || orig === null)) {
                let __args = Array.prototype.slice.call(arguments);
                this.list = <any>(new Array<any>());
                this.done = false;
                (() => {
                    this.done = orig.done;
                    this.list = orig.list.slice();
                })();
            } else if(orig === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                this.list = <any>(new Array<any>());
                this.done = false;
            } else throw new Error('invalid overload');
        }

        public add(c : dominosa_solver.Position) {
            this.list.push(c);
        }

        public remove$def_js_Array$dominosa_solver_Position(l : Array<dominosa_solver.Position>, c : dominosa_solver.Position) {
            let index : number = this.indexOf(l, c);
            if(index > -1) {
                l.splice(index, 1);
            }
        }

        public remove(l? : any, c? : any) : any {
            if(((l != null && l instanceof <any>Array) || l === null) && ((c != null && c instanceof <any>dominosa_solver.Position) || c === null)) {
                return <any>this.remove$def_js_Array$dominosa_solver_Position(l, c);
            } else if(((l != null && l instanceof <any>dominosa_solver.Position) || l === null) && c === undefined) {
                return <any>this.remove$dominosa_solver_Position(l);
            } else throw new Error('invalid overload');
        }

        public remove$dominosa_solver_Position(c : dominosa_solver.Position) {
            this.remove$def_js_Array$dominosa_solver_Position(this.list, c);
            if(this.isEmpty()) throw new dominosa_solver.UnsolvableException();
        }

        /*private*/ indexOf(l : Array<dominosa_solver.Position>, c : dominosa_solver.Position) : number {
            let postionEquals : (p1: dominosa_solver.Position) => boolean = (p) => {
                return c.equals(p);
            };
            let index : number = (<number>l.findIndex(<any>(postionEquals))|0);
            return index;
        }

        public hasExactlyOne() : boolean {
            return this.list.length === 1;
        }

        public isEmpty() : boolean {
            return this.list.length === 0;
        }

        public size() : number {
            return (<number>this.list.length|0);
        }

        public getFirst() : dominosa_solver.Position {
            return this.list[0];
        }

        public setUniquePosition(position : dominosa_solver.Position) : Array<dominosa_solver.Position> {
            let oldList : Array<dominosa_solver.Position> = this.list;
            this.remove$def_js_Array$dominosa_solver_Position(this.list, position);
            this.list = <any>(new Array<any>());
            this.list.push(position);
            return oldList;
        }

        /**
         * 
         * @return {string}
         */
        public toString() : string {
            return (this.done?"done: ":"not done: ") + this.list.toString();
        }
    }
    PositionList["__class"] = "dominosa_solver.PositionList";

}
namespace dominosa_solver {
    export class PotentialDirections {
        left : boolean = true;

        right : boolean = true;

        up : boolean = true;

        down : boolean = true;

        done : boolean = false;

        public constructor(p? : any) {
            if(((p != null && p instanceof <any>dominosa_solver.PotentialDirections) || p === null)) {
                let __args = Array.prototype.slice.call(arguments);
                this.left = true;
                this.right = true;
                this.up = true;
                this.down = true;
                this.done = false;
                (() => {
                    this.left = p.left;
                    this.right = p.right;
                    this.up = p.up;
                    this.down = p.down;
                    this.done = p.done;
                })();
            } else if(p === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                this.left = true;
                this.right = true;
                this.up = true;
                this.down = true;
                this.done = false;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {string}
         */
        public toString() : string {
            let ret : string = "";
            ret += this.left?(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value()).charCodeAt(0):(' ').charCodeAt(0);
            ret += this.up?(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value()).charCodeAt(0):(' ').charCodeAt(0);
            ret += this.down?(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value()).charCodeAt(0):(' ').charCodeAt(0);
            ret += this.right?(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value()).charCodeAt(0):(' ').charCodeAt(0);
            return ret;
        }

        public static printPotentialDirections(potentialDirections : PotentialDirections[][]) {
            for(let i : number = 0; i < potentialDirections.length; i++) {
                console.log(PotentialDirections.printLine(potentialDirections[i]));
            };
        }

        public static printLine(line : PotentialDirections[]) : string {
            let ret : string = "";
            for(let i : number = 0; i < line.length; i++) {
                ret += line[i].toString();
                ret += "|";
            };
            return ret;
        }

        public static printFinalDirections(finalDirections : string[][]) {
            for(let i : number = 0; i < finalDirections.length; i++) {
                let line : string = "";
                if(i < 10) line += " ";
                line += (i + " ");
                for(let j : number = 0; j < finalDirections[i].length; j++) {
                    line += /* valueOf */new String(finalDirections[i][j]).toString();
                };
                line += (" " + i);
                console.log(line);
            };
        }

        public static initialize(matrix : PotentialDirections[][]) {
            for(let r : number = 0; r < matrix.length; r++) {
                let line : PotentialDirections[] = matrix[r];
                for(let c : number = 0; c < line.length; c++) {
                    line[c] = new PotentialDirections();
                };
            };
        }

        public clearAllExceptRight() {
            this.up = this.down = this.left = false;
            this.done = true;
        }

        public clearAllExceptLeft() {
            this.up = this.down = this.right = false;
            this.done = true;
        }

        public clearAllExceptDown() {
            this.up = this.left = this.right = false;
            this.done = true;
        }

        public clearAllExceptUp() {
            this.down = this.left = this.right = false;
            this.done = true;
        }

        public clearRight() {
            this.right = false;
            if(this.hasNoDirections()) throw new dominosa_solver.UnsolvableException();
        }

        public clearLeft() {
            this.left = false;
            if(this.hasNoDirections()) throw new dominosa_solver.UnsolvableException();
        }

        public clearDown() {
            this.down = false;
            if(this.hasNoDirections()) throw new dominosa_solver.UnsolvableException();
        }

        public clearUp() {
            this.up = false;
            if(this.hasNoDirections()) throw new dominosa_solver.UnsolvableException();
        }

        public hasOnlyOneDirection() : boolean {
            if(PotentialDirections.num(this.up) + PotentialDirections.num(this.down) + PotentialDirections.num(this.right) + PotentialDirections.num(this.left) === 1) {
                return true;
            }
            return false;
        }

        public oneDirection() : string {
            if(this.left) return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value();
            if(this.right) return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value();
            if(this.up) return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value();
            if(this.down) return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value();
            return ' ';
        }

        public hasNoDirections() : boolean {
            if(!this.left && !this.right && !this.up && !this.down) {
                return true;
            }
            return false;
        }

        /*private*/ static num(b : boolean) : number {
            if(b) return 1;
            return 0;
        }
    }
    PotentialDirections["__class"] = "dominosa_solver.PotentialDirections";

}
namespace dominosa_solver {
    export class Square {
        public constructor(row : number, column : number) {
            if(this.row===undefined) this.row = 0;
            if(this.column===undefined) this.column = 0;
            this.row = row;
            this.column = column;
        }

        row : number;

        column : number;
    }
    Square["__class"] = "dominosa_solver.Square";

}

