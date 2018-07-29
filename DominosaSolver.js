/* Generated from Java with JSweet 2.2.0-SNAPSHOT - http://www.jsweet.org */
var dominosa_solver;
(function (dominosa_solver) {
    var Orientation;
    (function (Orientation) {
        Orientation[Orientation["HORIZONTAL"] = 0] = "HORIZONTAL";
        Orientation[Orientation["VERTICAL"] = 1] = "VERTICAL";
    })(Orientation = dominosa_solver.Orientation || (dominosa_solver.Orientation = {}));
    /** @ignore */
    class Orientation_$WRAPPER {
        constructor(_$ordinal, _$name, c, adverb) {
            this._$ordinal = _$ordinal;
            this._$name = _$name;
            if (this.c === undefined)
                this.c = null;
            if (this.adverb === undefined)
                this.adverb = null;
            this.c = c;
            this.adverb = adverb;
        }
        getSymbol() {
            return "" + this.c;
        }
        getAdverb() {
            return this.adverb;
        }
        name() { return this._$name; }
        ordinal() { return this._$ordinal; }
    }
    dominosa_solver.Orientation_$WRAPPER = Orientation_$WRAPPER;
    Orientation["__class"] = "dominosa_solver.Orientation";
    Orientation["__interfaces"] = ["java.lang.Comparable", "java.io.Serializable"];
    Orientation["_$wrappers"] = [new Orientation_$WRAPPER(0, "HORIZONTAL", 'H', "horizontally"), new Orientation_$WRAPPER(1, "VERTICAL", 'V', "vertically")];
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    var SolveStatus;
    (function (SolveStatus) {
        SolveStatus[SolveStatus["SOLVED"] = 0] = "SOLVED";
        SolveStatus[SolveStatus["INCOMPLETE"] = 1] = "INCOMPLETE";
        SolveStatus[SolveStatus["CONFLICT"] = 2] = "CONFLICT";
    })(SolveStatus = dominosa_solver.SolveStatus || (dominosa_solver.SolveStatus = {}));
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class UnsolvableException extends Error {
        constructor(message) {
            if (((typeof message === 'string') || message === null)) {
                let __args = Array.prototype.slice.call(arguments);
                super(message);
                this.message = message;
                Object.setPrototypeOf(this, UnsolvableException.prototype);
            }
            else if (message === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                super();
                Object.setPrototypeOf(this, UnsolvableException.prototype);
            }
            else
                throw new Error('invalid overload');
        }
    }
    dominosa_solver.UnsolvableException = UnsolvableException;
    UnsolvableException["__class"] = "dominosa_solver.UnsolvableException";
    UnsolvableException["__interfaces"] = ["java.io.Serializable"];
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class DominosaSolver {
        static getSolution(numRows, numColumns, highestNumber, numbersOnBoard) {
            dominosa_solver.Board.printSteps = true;
            let board = dominosa_solver.Board.createInitialBoard(highestNumber, numRows, numColumns, numbersOnBoard);
            let solveStatus = board.seek();
            if (solveStatus !== dominosa_solver.SolveStatus.SOLVED) {
                return null;
            }
            else {
                console.log("End - final directions:");
                dominosa_solver.PotentialDirections.printFinalDirections(board.finalDirections);
                return DominosaSolver.getPositions(board.potentialPositionsForTiles, highestNumber);
            }
        }
        /*private*/ static getPositions(solution, highestNumber) {
            let positions = (new Array());
            for (let i = 0; i <= highestNumber; i++) {
                for (let j = i; j <= highestNumber; j++) {
                    let pos = solution[i][j].getFirst();
                    positions.push(pos);
                }
                ;
            }
            ;
            return positions;
        }
        /*private*/ static leadingZeroes(number, leadingZeroes) {
            if (leadingZeroes === false)
                return "" + number;
            if (number <= 9)
                return "0" + number;
            else
                return "" + number;
        }
    }
    dominosa_solver.DominosaSolver = DominosaSolver;
    DominosaSolver["__class"] = "dominosa_solver.DominosaSolver";
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class Position {
        constructor(row, column, orientation) {
            if (this.row === undefined)
                this.row = 0;
            if (this.column === undefined)
                this.column = 0;
            if (this.orientation === undefined)
                this.orientation = null;
            this.row = row;
            this.column = column;
            this.orientation = orientation;
        }
        /**
         *
         * @return {string}
         */
        toString() {
            return this.row + "," + this.column + " " + this.orientation;
        }
        getOverlappingPositions() {
            let list = (new Array());
            {
                let array122 = this.getSquaresAbove();
                for (let index121 = 0; index121 < array122.length; index121++) {
                    let a = array122[index121];
                    {
                        list.push(new Position(a.row, a.column, dominosa_solver.Orientation.VERTICAL));
                    }
                }
            }
            {
                let array124 = this.getSquaresLeft();
                for (let index123 = 0; index123 < array124.length; index123++) {
                    let l = array124[index123];
                    {
                        list.push(new Position(l.row, l.column, dominosa_solver.Orientation.HORIZONTAL));
                    }
                }
            }
            {
                let array126 = this.getSquaresBelow();
                for (let index125 = 0; index125 < array126.length; index125++) {
                    let b = array126[index125];
                    {
                        list.push(new Position(b.row - 1, b.column, dominosa_solver.Orientation.VERTICAL));
                    }
                }
            }
            {
                let array128 = this.getSquaresRight();
                for (let index127 = 0; index127 < array128.length; index127++) {
                    let r = array128[index127];
                    {
                        list.push(new Position(r.row, r.column - 1, dominosa_solver.Orientation.HORIZONTAL));
                    }
                }
            }
            return list;
        }
        getSmallerNumber() {
            if (this.getFirstNumber() <= this.getSecondNumber())
                return this.getFirstNumber();
            return this.getSecondNumber();
        }
        getBiggerNumber() {
            if (this.getFirstNumber() <= this.getSecondNumber())
                return this.getSecondNumber();
            return this.getFirstNumber();
        }
        /*private*/ getFirstNumber() {
            return dominosa_solver.Board.numbersOnBoard[this.row][this.column];
        }
        /*private*/ getSecondNumber() {
            return dominosa_solver.Board.numbersOnBoard[this.secondRow()][this.secondColumn()];
        }
        getSquares() {
            let squares = [null, null];
            squares[0] = new dominosa_solver.Square(this.row, this.column);
            if (this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares[1] = new dominosa_solver.Square(this.row, this.column + 1);
            }
            else if (this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column);
            }
            return squares;
        }
        secondRow() {
            return this.getSquares()[1].row;
        }
        secondColumn() {
            return this.getSquares()[1].column;
        }
        /**
         *
         * @return {Array} the square or squares above, if any
         */
        getSquaresAbove() {
            let squares = [];
            if (this.row === 0)
                return squares;
            if (this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row - 1, this.column);
                squares[1] = new dominosa_solver.Square(this.row - 1, this.column + 1);
            }
            else if (this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row - 1, this.column);
            }
            return squares;
        }
        /**
         *
         * @return {Array} the square or squares below, if any
         */
        getSquaresBelow() {
            let squares = [];
            if (this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                if (this.row === dominosa_solver.Board.numRows - 1)
                    return squares;
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row + 1, this.column);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column + 1);
            }
            else if (this.orientation === dominosa_solver.Orientation.VERTICAL) {
                if (this.row === dominosa_solver.Board.numRows - 2)
                    return squares;
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row + 2, this.column);
            }
            return squares;
        }
        /**
         *
         * @return {Array} the square or squares to the left, if any
         */
        getSquaresLeft() {
            let squares = [];
            if (this.column === 0)
                return squares;
            if (this.orientation === dominosa_solver.Orientation.VERTICAL) {
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row, this.column - 1);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column - 1);
            }
            else if (this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row, this.column - 1);
            }
            return squares;
        }
        /**
         *
         * @return {Array} the square or squares to the right, if any
         */
        getSquaresRight() {
            let squares = [];
            if (this.orientation === dominosa_solver.Orientation.VERTICAL) {
                if (this.column === dominosa_solver.Board.numColumns - 1)
                    return squares;
                squares = [null, null];
                squares[0] = new dominosa_solver.Square(this.row, this.column + 1);
                squares[1] = new dominosa_solver.Square(this.row + 1, this.column + 1);
            }
            else if (this.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                if (this.column === dominosa_solver.Board.numColumns - 2)
                    return squares;
                squares = [null];
                squares[0] = new dominosa_solver.Square(this.row, this.column + 2);
            }
            return squares;
        }
        /**
         *
         * @return {number}
         */
        hashCode() {
            let prime = 31;
            let result = 1;
            result = prime * result + ((this.orientation == null) ? 0 : dominosa_solver.Orientation["_$wrappers"][this.orientation].hashCode());
            result = prime * result + this.row;
            result = prime * result + this.column;
            return result;
        }
        /**
         *
         * @param {*} obj
         * @return {boolean}
         */
        equals(obj) {
            if (this === obj)
                return true;
            if (obj == null)
                return false;
            if (this.constructor !== obj.constructor)
                return false;
            let other = obj;
            if (this.orientation !== other.orientation)
                return false;
            if (this.row !== other.row)
                return false;
            if (this.column !== other.column)
                return false;
            return true;
        }
    }
    dominosa_solver.Position = Position;
    Position["__class"] = "dominosa_solver.Position";
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    var InputModeEnum;
    (function (InputModeEnum) {
        InputModeEnum[InputModeEnum["DEFAULT"] = 0] = "DEFAULT";
        InputModeEnum[InputModeEnum["SPACES"] = 1] = "SPACES";
        InputModeEnum[InputModeEnum["LETTERS"] = 2] = "LETTERS";
        InputModeEnum[InputModeEnum["ZEROES"] = 3] = "ZEROES";
    })(InputModeEnum = dominosa_solver.InputModeEnum || (dominosa_solver.InputModeEnum = {}));
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    var DirectionEnum;
    (function (DirectionEnum) {
        DirectionEnum[DirectionEnum["LEFT"] = 0] = "LEFT";
        DirectionEnum[DirectionEnum["RIGHT"] = 1] = "RIGHT";
        DirectionEnum[DirectionEnum["UP"] = 2] = "UP";
        DirectionEnum[DirectionEnum["DOWN"] = 3] = "DOWN";
        DirectionEnum[DirectionEnum["NOT_SET"] = 4] = "NOT_SET";
    })(DirectionEnum = dominosa_solver.DirectionEnum || (dominosa_solver.DirectionEnum = {}));
    /** @ignore */
    class DirectionEnum_$WRAPPER {
        constructor(_$ordinal, _$name, value) {
            this._$ordinal = _$ordinal;
            this._$name = _$name;
            if (this.depiction === undefined)
                this.depiction = null;
            this.depiction = value;
        }
        /**
         *
         * @return {string}
         */
        toString() {
            return "" + this.depiction;
        }
        value() {
            return this.depiction;
        }
        name() { return this._$name; }
        ordinal() { return this._$ordinal; }
    }
    dominosa_solver.DirectionEnum_$WRAPPER = DirectionEnum_$WRAPPER;
    DirectionEnum["__class"] = "dominosa_solver.DirectionEnum";
    DirectionEnum["__interfaces"] = ["java.lang.Comparable", "java.io.Serializable"];
    DirectionEnum["_$wrappers"] = [new DirectionEnum_$WRAPPER(0, "LEFT", '<'), new DirectionEnum_$WRAPPER(1, "RIGHT", '>'), new DirectionEnum_$WRAPPER(2, "UP", '^'), new DirectionEnum_$WRAPPER(3, "DOWN", 'v'), new DirectionEnum_$WRAPPER(4, "NOT_SET", '?')];
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class Board {
        constructor(b) {
            if (((b != null && b instanceof dominosa_solver.Board) || b === null)) {
                let __args = Array.prototype.slice.call(arguments);
                if (this.potentialDirections === undefined)
                    this.potentialDirections = null;
                if (this.finalDirections === undefined)
                    this.finalDirections = null;
                if (this.potentialPositionsForTiles === undefined)
                    this.potentialPositionsForTiles = null;
                if (this.potentialDirections === undefined)
                    this.potentialDirections = null;
                if (this.finalDirections === undefined)
                    this.finalDirections = null;
                if (this.potentialPositionsForTiles === undefined)
                    this.potentialPositionsForTiles = null;
                (() => {
                    this.potentialDirections = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                        return null;
                    }
                    else {
                        let array = [];
                        for (let i = 0; i < dims[0]; i++) {
                            array.push(allocate(dims.slice(1)));
                        }
                        return array;
                    } }; return allocate(dims); })([Board.numRows, Board.numColumns]);
                    this.finalDirections = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                        return null;
                    }
                    else {
                        let array = [];
                        for (let i = 0; i < dims[0]; i++) {
                            array.push(allocate(dims.slice(1)));
                        }
                        return array;
                    } }; return allocate(dims); })([Board.numRows, Board.numColumns]);
                    this.potentialPositionsForTiles = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                        return null;
                    }
                    else {
                        let array = [];
                        for (let i = 0; i < dims[0]; i++) {
                            array.push(allocate(dims.slice(1)));
                        }
                        return array;
                    } }; return allocate(dims); })([Board.highestNumber + 1, Board.highestNumber + 1]);
                    Board.copy(b.potentialDirections, this.potentialDirections, b.finalDirections, this.finalDirections, b.potentialPositionsForTiles, this.potentialPositionsForTiles);
                })();
            }
            else if (b === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                if (this.potentialDirections === undefined)
                    this.potentialDirections = null;
                if (this.finalDirections === undefined)
                    this.finalDirections = null;
                if (this.potentialPositionsForTiles === undefined)
                    this.potentialPositionsForTiles = null;
                if (this.potentialDirections === undefined)
                    this.potentialDirections = null;
                if (this.finalDirections === undefined)
                    this.finalDirections = null;
                if (this.potentialPositionsForTiles === undefined)
                    this.potentialPositionsForTiles = null;
            }
            else
                throw new Error('invalid overload');
        }
        /**
         *
         * @return {dominosa_solver.SolveStatus} solved / incomplete / conflict
         * @private
         */
        /*private*/ seekUnique() {
            let solveStatus = dominosa_solver.SolveStatus.INCOMPLETE;
            try {
                let changed = true;
                while ((changed === true)) {
                    changed = false;
                    for (let i = 0; i <= Board.highestNumber; i++) {
                        for (let j = i; j <= Board.highestNumber; j++) {
                            if (this.potentialPositionsForTiles[i][j].hasExactlyOne() && this.potentialPositionsForTiles[i][j].done === false) {
                                if (Board.printSteps)
                                    console.log("Only one potential position: ");
                                this.potentialPositionsForTiles[i][j].done = true;
                                changed = true;
                                let position = this.potentialPositionsForTiles[i][j].getFirst();
                                this.setUniquePosition$int$int$dominosa_solver_Position(i, j, position);
                            }
                        }
                        ;
                    }
                    ;
                    for (let r = 0; r < Board.numRows; r++) {
                        for (let c = 0; c < Board.numColumns; c++) {
                            let potDir = this.potentialDirections[r][c];
                            if (potDir.done === false && potDir.hasOnlyOneDirection() === true) {
                                if (Board.printSteps)
                                    console.log("Only one potential direction: ");
                                potDir.done = true;
                                changed = true;
                                let top = -1;
                                let left = -1;
                                let dir = null;
                                if (potDir.right) {
                                    top = r;
                                    left = c;
                                    dir = dominosa_solver.Orientation.HORIZONTAL;
                                }
                                if (potDir.left) {
                                    top = r;
                                    left = c - 1;
                                    dir = dominosa_solver.Orientation.HORIZONTAL;
                                }
                                if (potDir.up) {
                                    top = r - 1;
                                    left = c;
                                    dir = dominosa_solver.Orientation.VERTICAL;
                                }
                                if (potDir.down) {
                                    top = r;
                                    left = c;
                                    dir = dominosa_solver.Orientation.VERTICAL;
                                }
                                this.setUniquePosition$dominosa_solver_Position(new dominosa_solver.Position(top, left, dir));
                            }
                        }
                        ;
                    }
                    ;
                }
                ;
                solveStatus = this.isDone();
            }
            catch (ex) {
                solveStatus = dominosa_solver.SolveStatus.CONFLICT;
            }
            ;
            return solveStatus;
        }
        /**
         *
         * @return {dominosa_solver.SolveStatus} solved / incomplete / conflict
         */
        seek() {
            let seekUniqueSolveStatus = this.seekUnique();
            if (seekUniqueSolveStatus === dominosa_solver.SolveStatus.SOLVED) {
                return dominosa_solver.SolveStatus.SOLVED;
            }
            if (seekUniqueSolveStatus === dominosa_solver.SolveStatus.CONFLICT) {
                return dominosa_solver.SolveStatus.CONFLICT;
            }
            if (seekUniqueSolveStatus === dominosa_solver.SolveStatus.INCOMPLETE) {
                let newBoard = null;
                let clonedBoardSolveStatus = null;
                let seekUniqueSolveStatus2 = null;
                while ((true)) {
                    let guess = this.guess();
                    if (guess == null) {
                        return dominosa_solver.SolveStatus.CONFLICT;
                    }
                    newBoard = new Board(this);
                    if (Board.printSteps)
                        console.log("--> Making a guess: ");
                    newBoard.applyGuess(guess);
                    clonedBoardSolveStatus = newBoard.seek();
                    if (clonedBoardSolveStatus === dominosa_solver.SolveStatus.SOLVED) {
                        if (Board.printSteps)
                            console.info("This guess worked.");
                        this.applyState(newBoard);
                        return dominosa_solver.SolveStatus.SOLVED;
                    }
                    if (clonedBoardSolveStatus === dominosa_solver.SolveStatus.CONFLICT) {
                        if (Board.printSteps)
                            console.info("<-- This guess didn\'t work.");
                        this.markGuessAsImpossible(guess);
                        seekUniqueSolveStatus2 = this.seekUnique();
                        switch ((seekUniqueSolveStatus2)) {
                            case dominosa_solver.SolveStatus.SOLVED:
                                return dominosa_solver.SolveStatus.SOLVED;
                            case dominosa_solver.SolveStatus.CONFLICT:
                                if (Board.printSteps)
                                    console.info("<-- This guess didn\'t work. The alternative didn\'t work either.");
                                return dominosa_solver.SolveStatus.CONFLICT;
                            case dominosa_solver.SolveStatus.INCOMPLETE:
                                if (Board.printSteps)
                                    console.info("<-- This guess didn\'t work, but helped us narrow down the possibilities.");
                                break;
                        }
                    }
                }
                ;
            }
            return seekUniqueSolveStatus;
        }
        /**
         * doesn't change the board
         * @return {dominosa_solver.Position} null if it was not possible to make a guess
         * @private
         */
        /*private*/ guess() {
            let positionListLength2 = null;
            let lastPositionListLength3orMore = null;
            out: for (let row = 0; row < this.potentialPositionsForTiles.length; row++) {
                for (let column = 0; column < this.potentialPositionsForTiles[row].length; column++) {
                    let positionList = this.potentialPositionsForTiles[row][column];
                    let size = positionList.size();
                    if (size === 0 || size === 1) {
                        continue;
                    }
                    if (size === 2) {
                        positionListLength2 = positionList;
                        break out;
                    }
                    if (size >= 3) {
                        lastPositionListLength3orMore = positionList;
                    }
                }
                ;
            }
            ;
            let position = null;
            if (positionListLength2 != null) {
                position = positionListLength2.getFirst();
            }
            else if (lastPositionListLength3orMore != null) {
                position = lastPositionListLength3orMore.getFirst();
            }
            else {
                return null;
            }
            return position;
        }
        static createInitialBoard(highestNumber, numRows, numColumns, numbersOnBoard) {
            Board.highestNumber = highestNumber;
            Board.numRows = numRows;
            Board.numColumns = numColumns;
            Board.numbersOnBoard = numbersOnBoard;
            let board = new Board();
            board.initializeFirstBoard();
            return board;
        }
        /*private*/ initializeFirstBoard() {
            this.finalDirections = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                return null;
            }
            else {
                let array = [];
                for (let i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([Board.numRows, Board.numColumns]);
            for (let i = 0; i < this.finalDirections.length; i++) {
                for (let j = 0; j < this.finalDirections[i].length; j++) {
                    this.finalDirections[i][j] = '?';
                }
                ;
            }
            ;
            this.potentialDirections = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                return null;
            }
            else {
                let array = [];
                for (let i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([Board.numRows, Board.numColumns]);
            dominosa_solver.PotentialDirections.initialize(this.potentialDirections);
            for (let i = 0; i < Board.numColumns; i++) {
                this.potentialDirections[0][i].up = false;
                this.potentialDirections[Board.numRows - 1][i].down = false;
            }
            ;
            for (let i = 0; i < Board.numRows; i++) {
                this.potentialDirections[i][0].left = false;
                this.potentialDirections[i][Board.numColumns - 1].right = false;
            }
            ;
            this.potentialPositionsForTiles = (function (dims) { let allocate = function (dims) { if (dims.length == 0) {
                return null;
            }
            else {
                let array = [];
                for (let i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([Board.highestNumber + 1, Board.highestNumber + 1]);
            for (let i = 0; i <= Board.highestNumber; i++) {
                for (let j = i; j <= Board.highestNumber; j++) {
                    this.potentialPositionsForTiles[i][j] = new dominosa_solver.PositionList();
                }
                ;
            }
            ;
            for (let i = 0; i < Board.numbersOnBoard.length; i++) {
                for (let j = 0; j < Board.numbersOnBoard[i].length - 1; j++) {
                    let numberOnLeftTile = Board.numbersOnBoard[i][j];
                    let numberOnRightTile = Board.numbersOnBoard[i][j + 1];
                    if (numberOnLeftTile <= numberOnRightTile)
                        this.potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.HORIZONTAL));
                    else
                        this.potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.HORIZONTAL));
                }
                ;
            }
            ;
            for (let i = 0; i < Board.numbersOnBoard.length - 1; i++) {
                for (let j = 0; j < Board.numbersOnBoard[i].length; j++) {
                    let numberOnTopTile = Board.numbersOnBoard[i][j];
                    let numberOnBottomTile = Board.numbersOnBoard[i + 1][j];
                    if (numberOnTopTile <= numberOnBottomTile)
                        this.potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.VERTICAL));
                    else
                        this.potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile].add(new dominosa_solver.Position(i, j, dominosa_solver.Orientation.VERTICAL));
                }
                ;
            }
            ;
        }
        /**
         * like removePotentialTile() without the exception
         * @param {dominosa_solver.Position} guess
         * @private
         */
        /*private*/ markGuessAsImpossible(guess) {
            try {
                this.removePotentialTile(guess);
            }
            catch (ex) {
            }
            ;
        }
        /*private*/ removePotentialTile(position) {
            this.potentialPositionsForTiles[position.getSmallerNumber()][position.getBiggerNumber()].remove$dominosa_solver_Position(position);
            let potDir1 = this.potentialDirections[position.row][position.column];
            let potDir2 = this.potentialDirections[position.secondRow()][position.secondColumn()];
            if (position.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                potDir1.clearRight();
                potDir2.clearLeft();
            }
            else if (position.orientation === dominosa_solver.Orientation.VERTICAL) {
                potDir1.clearDown();
                potDir2.clearUp();
            }
        }
        /**
         * like setUniquePosition() but no exception possible
         * @param {dominosa_solver.Position} guess
         * @private
         */
        /*private*/ applyGuess(guess) {
            try {
                this.setUniquePosition$dominosa_solver_Position(guess);
            }
            catch (ex) {
            }
            ;
        }
        /*private*/ setUniquePosition$dominosa_solver_Position(uniquePosition) {
            let smallerNumber = uniquePosition.getSmallerNumber();
            let biggerNumber = uniquePosition.getBiggerNumber();
            this.setUniquePosition$int$int$dominosa_solver_Position(smallerNumber, biggerNumber, uniquePosition);
        }
        setUniquePosition$int$int$dominosa_solver_Position(smallerNumber, biggerNumber, uniquePosition) {
            if (Board.printSteps)
                console.info("Placing tile [" + smallerNumber + " " + biggerNumber + "] at (" + uniquePosition.row + "/" + uniquePosition.column + ") " + dominosa_solver.Orientation["_$wrappers"][uniquePosition.orientation].getAdverb() + " (row/column) (counting from 0).");
            let removedPositions = this.potentialPositionsForTiles[smallerNumber][biggerNumber].setUniquePosition(uniquePosition);
            this.potentialPositionsForTiles[smallerNumber][biggerNumber].done = true;
            for (let index129 = 0; index129 < removedPositions.length; index129++) {
                let removedPosition = removedPositions[index129];
                {
                    this.removePotentialTile(removedPosition);
                }
            }
            let row = uniquePosition.row;
            let column = uniquePosition.column;
            if (uniquePosition.orientation === dominosa_solver.Orientation.HORIZONTAL) {
                this.finalDirections[row][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value();
                this.finalDirections[row][column + 1] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value();
                this.potentialDirections[row][column].clearAllExceptRight();
                this.potentialDirections[row][column + 1].clearAllExceptLeft();
            }
            else if (uniquePosition.orientation === dominosa_solver.Orientation.VERTICAL) {
                this.finalDirections[row][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value();
                this.finalDirections[row + 1][column] = dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value();
                this.potentialDirections[row][column].clearAllExceptDown();
                this.potentialDirections[row + 1][column].clearAllExceptUp();
            }
            let overlappingPositions = uniquePosition.getOverlappingPositions();
            for (let index130 = 0; index130 < overlappingPositions.length; index130++) {
                let overlapPos = overlappingPositions[index130];
                {
                    this.removePotentialTile(overlapPos);
                }
            }
        }
        setUniquePosition(smallerNumber, biggerNumber, uniquePosition) {
            if (((typeof smallerNumber === 'number') || smallerNumber === null) && ((typeof biggerNumber === 'number') || biggerNumber === null) && ((uniquePosition != null && uniquePosition instanceof dominosa_solver.Position) || uniquePosition === null)) {
                return this.setUniquePosition$int$int$dominosa_solver_Position(smallerNumber, biggerNumber, uniquePosition);
            }
            else if (((smallerNumber != null && smallerNumber instanceof dominosa_solver.Position) || smallerNumber === null) && biggerNumber === undefined && uniquePosition === undefined) {
                return this.setUniquePosition$dominosa_solver_Position(smallerNumber);
            }
            else
                throw new Error('invalid overload');
        }
        isDone() {
            let potentialDirectionsUnique = true;
            let finalDirectionsSet = true;
            let potentialPositionsUnique = true;
            let zeroDirectionExists = false;
            let zeroPositionExists = false;
            out: for (let index131 = 0; index131 < this.potentialDirections.length; index131++) {
                let potentialDirectionsRow = this.potentialDirections[index131];
                {
                    for (let index132 = 0; index132 < potentialDirectionsRow.length; index132++) {
                        let potentialDirectionsSquare = potentialDirectionsRow[index132];
                        {
                            if (potentialDirectionsSquare.hasOnlyOneDirection() === false) {
                                potentialDirectionsUnique = false;
                            }
                            if (potentialDirectionsSquare.hasNoDirections()) {
                                zeroDirectionExists = true;
                                break out;
                            }
                        }
                    }
                }
            }
            for (let index133 = 0; index133 < this.finalDirections.length; index133++) {
                let cs = this.finalDirections[index133];
                {
                    for (let index134 = 0; index134 < cs.length; index134++) {
                        let c = cs[index134];
                        {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.NOT_SET].value())) {
                                finalDirectionsSet = false;
                            }
                        }
                    }
                }
            }
            out: for (let i = 0; i < this.potentialPositionsForTiles.length; i++) {
                for (let j = i; j < this.potentialPositionsForTiles[i].length; j++) {
                    if (this.potentialPositionsForTiles[i][j].hasExactlyOne() === false) {
                        potentialPositionsUnique = false;
                    }
                    if (this.potentialPositionsForTiles[i][j].isEmpty()) {
                        zeroPositionExists = true;
                        break out;
                    }
                }
                ;
            }
            ;
            let returnStatus = dominosa_solver.SolveStatus.INCOMPLETE;
            if (zeroDirectionExists || zeroPositionExists) {
                returnStatus = dominosa_solver.SolveStatus.CONFLICT;
            }
            else if (potentialDirectionsUnique || finalDirectionsSet || potentialPositionsUnique) {
                returnStatus = dominosa_solver.SolveStatus.SOLVED;
                if (!potentialDirectionsUnique || !finalDirectionsSet || !potentialPositionsUnique) {
                    console.info("Something is wrong, either all three criteria should say it\'s solved or none!");
                }
            }
            return returnStatus;
        }
        /*private*/ applyState(otherBoard) {
            this.potentialDirections = otherBoard.potentialDirections;
            this.finalDirections = otherBoard.finalDirections;
            this.potentialPositionsForTiles = otherBoard.potentialPositionsForTiles;
        }
        /*private*/ static copy(potentialDirections, newPotentialDirections, finalDirections, newFinalDirections, potentialPositionsForTiles, newPotentialPositionsForTiles) {
            for (let i = 0; i < potentialDirections.length; i++) {
                for (let j = 0; j < potentialDirections[i].length; j++) {
                    newPotentialDirections[i][j] = new dominosa_solver.PotentialDirections(potentialDirections[i][j]);
                }
                ;
            }
            ;
            for (let i = 0; i < finalDirections.length; i++) {
                for (let j = 0; j < finalDirections[i].length; j++) {
                    newFinalDirections[i][j] = finalDirections[i][j];
                }
                ;
            }
            ;
            for (let i = 0; i < potentialPositionsForTiles.length; i++) {
                for (let j = i; j < potentialPositionsForTiles[i].length; j++) {
                    newPotentialPositionsForTiles[i][j] = new dominosa_solver.PositionList(potentialPositionsForTiles[i][j]);
                }
                ;
            }
            ;
        }
    }
    Board.numColumns = 0;
    Board.numRows = 0;
    Board.highestNumber = 0;
    Board.numbersOnBoard = null;
    Board.printSteps = false;
    dominosa_solver.Board = Board;
    Board["__class"] = "dominosa_solver.Board";
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class PositionList {
        constructor(orig) {
            /*private*/ this.list = (new Array());
            this.done = false;
            if (((orig != null && orig instanceof dominosa_solver.PositionList) || orig === null)) {
                let __args = Array.prototype.slice.call(arguments);
                this.list = (new Array());
                this.done = false;
                (() => {
                    this.done = orig.done;
                    this.list = orig.list.slice();
                })();
            }
            else if (orig === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                this.list = (new Array());
                this.done = false;
            }
            else
                throw new Error('invalid overload');
        }
        add(c) {
            this.list.push(c);
        }
        remove$def_js_Array$dominosa_solver_Position(l, c) {
            let index = this.indexOf(l, c);
            if (index > -1) {
                l.splice(index, 1);
            }
        }
        remove(l, c) {
            if (((l != null && l instanceof Array) || l === null) && ((c != null && c instanceof dominosa_solver.Position) || c === null)) {
                return this.remove$def_js_Array$dominosa_solver_Position(l, c);
            }
            else if (((l != null && l instanceof dominosa_solver.Position) || l === null) && c === undefined) {
                return this.remove$dominosa_solver_Position(l);
            }
            else
                throw new Error('invalid overload');
        }
        remove$dominosa_solver_Position(c) {
            this.remove$def_js_Array$dominosa_solver_Position(this.list, c);
            if (this.isEmpty())
                throw new dominosa_solver.UnsolvableException();
        }
        /*private*/ indexOf(l, c) {
            let postionEquals = (p) => {
                return c.equals(p);
            };
            let index = (l.findIndex((postionEquals)) | 0);
            return index;
        }
        hasExactlyOne() {
            return this.list.length === 1;
        }
        isEmpty() {
            return this.list.length === 0;
        }
        size() {
            return (this.list.length | 0);
        }
        getFirst() {
            return this.list[0];
        }
        setUniquePosition(position) {
            let oldList = this.list;
            this.remove$def_js_Array$dominosa_solver_Position(this.list, position);
            this.list = (new Array());
            this.list.push(position);
            return oldList;
        }
        /**
         *
         * @return {string}
         */
        toString() {
            return (this.done ? "done: " : "not done: ") + this.list.toString();
        }
    }
    dominosa_solver.PositionList = PositionList;
    PositionList["__class"] = "dominosa_solver.PositionList";
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class PotentialDirections {
        constructor(p) {
            this.left = true;
            this.right = true;
            this.up = true;
            this.down = true;
            this.done = false;
            if (((p != null && p instanceof dominosa_solver.PotentialDirections) || p === null)) {
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
            }
            else if (p === undefined) {
                let __args = Array.prototype.slice.call(arguments);
                this.left = true;
                this.right = true;
                this.up = true;
                this.down = true;
                this.done = false;
            }
            else
                throw new Error('invalid overload');
        }
        /**
         *
         * @return {string}
         */
        toString() {
            let ret = "";
            ret += this.left ? (dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value()).charCodeAt(0) : (' ').charCodeAt(0);
            ret += this.up ? (dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value()).charCodeAt(0) : (' ').charCodeAt(0);
            ret += this.down ? (dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value()).charCodeAt(0) : (' ').charCodeAt(0);
            ret += this.right ? (dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value()).charCodeAt(0) : (' ').charCodeAt(0);
            return ret;
        }
        static printPotentialDirections(potentialDirections) {
            for (let i = 0; i < potentialDirections.length; i++) {
                console.log(PotentialDirections.printLine(potentialDirections[i]));
            }
            ;
        }
        static printLine(line) {
            let ret = "";
            for (let i = 0; i < line.length; i++) {
                ret += line[i].toString();
                ret += "|";
            }
            ;
            return ret;
        }
        static printFinalDirections(finalDirections) {
            for (let i = 0; i < finalDirections.length; i++) {
                let line = "";
                if (i < 10)
                    line += " ";
                line += (i + " ");
                for (let j = 0; j < finalDirections[i].length; j++) {
                    line += new String(finalDirections[i][j]).toString();
                }
                ;
                line += (" " + i);
                console.log(line);
            }
            ;
        }
        static initialize(matrix) {
            for (let r = 0; r < matrix.length; r++) {
                let line = matrix[r];
                for (let c = 0; c < line.length; c++) {
                    line[c] = new PotentialDirections();
                }
                ;
            }
            ;
        }
        clearAllExceptRight() {
            this.up = this.down = this.left = false;
            this.done = true;
        }
        clearAllExceptLeft() {
            this.up = this.down = this.right = false;
            this.done = true;
        }
        clearAllExceptDown() {
            this.up = this.left = this.right = false;
            this.done = true;
        }
        clearAllExceptUp() {
            this.down = this.left = this.right = false;
            this.done = true;
        }
        clearRight() {
            this.right = false;
            if (this.hasNoDirections())
                throw new dominosa_solver.UnsolvableException();
        }
        clearLeft() {
            this.left = false;
            if (this.hasNoDirections())
                throw new dominosa_solver.UnsolvableException();
        }
        clearDown() {
            this.down = false;
            if (this.hasNoDirections())
                throw new dominosa_solver.UnsolvableException();
        }
        clearUp() {
            this.up = false;
            if (this.hasNoDirections())
                throw new dominosa_solver.UnsolvableException();
        }
        hasOnlyOneDirection() {
            if (PotentialDirections.num(this.up) + PotentialDirections.num(this.down) + PotentialDirections.num(this.right) + PotentialDirections.num(this.left) === 1) {
                return true;
            }
            return false;
        }
        oneDirection() {
            if (this.left)
                return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.LEFT].value();
            if (this.right)
                return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.RIGHT].value();
            if (this.up)
                return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.UP].value();
            if (this.down)
                return dominosa_solver.DirectionEnum["_$wrappers"][dominosa_solver.DirectionEnum.DOWN].value();
            return ' ';
        }
        hasNoDirections() {
            if (!this.left && !this.right && !this.up && !this.down) {
                return true;
            }
            return false;
        }
        /*private*/ static num(b) {
            if (b)
                return 1;
            return 0;
        }
    }
    dominosa_solver.PotentialDirections = PotentialDirections;
    PotentialDirections["__class"] = "dominosa_solver.PotentialDirections";
})(dominosa_solver || (dominosa_solver = {}));
(function (dominosa_solver) {
    class Square {
        constructor(row, column) {
            if (this.row === undefined)
                this.row = 0;
            if (this.column === undefined)
                this.column = 0;
            this.row = row;
            this.column = column;
        }
    }
    dominosa_solver.Square = Square;
    Square["__class"] = "dominosa_solver.Square";
})(dominosa_solver || (dominosa_solver = {}));
