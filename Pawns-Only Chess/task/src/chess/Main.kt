package chess
import kotlin.math.abs
import kotlin.system.exitProcess

val board = MutableList(17) { mutableListOf<String>() }
val regex = Regex("[a-h][1-8][a-h][1-8]")
val mapX = mapOf('8' to 1, '7' to 3, '6' to 5, '5' to 7, '4' to 9, '3' to 11, '2' to 13, '1' to 15)
val mapY = mapOf('a' to 5, 'b' to 9, 'c' to 13, 'd' to 17, 'e' to 21, 'f' to 25, 'g' to 29, 'h' to 33)
var p1turn = true
var firstMoveP1 = false
var firstMoveP2 = false
var move1_P1_x2 = 0
var move1_P1_y2 = 0
var move1_P2_x2 = 0
var move1_P2_y2 = 0
var enPasP1_x = 0
var enPasP1_y = 0
var enPasP2_x = 0
var enPasP2_y = 0

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val pl1 = readln()
    println("Second Player's name:")
    val pl2 = readln()
    createBoard()
    fillBoard()
    printBoard()
    play(pl1, pl2)
}

fun createBoard() {
    var countRow = 8
    val evenLine = "  +---+---+---+---+---+---+---+---+".split("").toMutableList()
    val lastLine = "    a   b   c   d   e   f   g   h  ".split("").toMutableList()
    for (i in 0..16) {
        val oddLine = "$countRow |   |   |   |   |   |   |   |   |".split("").toMutableList()
        if (i % 2 == 0) board[i] = evenLine else { board[i] = oddLine; countRow-- }
    }
    board.add(lastLine)
}

fun fillBoard() {
    for (i in 5..33 step 4) { board[3][i] = "B"; board[13][i] = "W" }
}

fun printBoard() {
    for (i in board) { println(i.joinToString("")); println() }
}

fun play(pl1: String, pl2: String) { while(true) { checkMoveP1(pl1, pl2); checkMoveP2(pl1, pl2) } }

fun checkMoveP1(pl1: String, pl2: String) {
    println("$pl1's turn:")
    val p1 = readln()
    if (p1 == "exit") { println("Bye!"); exitProcess(0) }
    var blackLeft = 0
    var (x1, y1, x2, y2) = MutableList(4) { 0 }
    for ((k, v) in mapX) { if (k == p1[1]) x1 = v; if (k == p1[3]) x2 = v }
    for ((k, v) in mapY) { if (k == p1[0]) y1 = v; if (k == p1[2]) y2 = v }

    if (board[x1][y1] != "W") { println("No white pawn at ${p1[0]}${p1[1]}"); play(pl1, pl2) }
    else if (p1.length > 4 || !regex.matches(p1) || p1[1] == '2' && p1[3] - p1[1] > 2 || p1[1] > p1[3] ||
        p1[0] == p1[2] && board[x2][y2] != " " || abs(y2 - y1) > 4 || p1[0] == p1[2] && p1[1] == p1[3] ||
        p1[0] != p1[2] && x2 != enPasP2_x && y2 != enPasP2_y && !firstMoveP2 && board[x2][y2] != "B" ||
        p1[0] != p1[2] && !firstMoveP2 && board[x2][y2] != "B" || p1[1] > '2' && p1[3] - p1[1] > 1 ) {
        println("Invalid Input"); play(pl1, pl2) }
    else if (p1[0] == p1[2] && p1[1] == '2' && p1[3] == '4') { board[x1][y1] = " ";  board[x2][y2] = "W"
        firstMoveP1 = true; move1_P1_x2 = x2; move1_P1_y2 = y2; enPasP1_x = move1_P1_x2 + 4
        enPasP1_y = move1_P1_y2; firstMoveP2 = false; printBoard() }
    else if (p1[0] != p1[2] && firstMoveP2 && x1 == move1_P2_x2 && (y1 - 4) == move1_P2_y2 ||
        y1 <= 29 && x1 == move1_P2_x2 && (y1 + 4) == move1_P2_y2) {
        board[x1][y1] = " "; board[x2][y2] = "W"; board[move1_P2_x2][move1_P2_y2] = " "
        for (i in 0..17) { for (j in 0..33) { if (board[i][j] == "B") blackLeft++ } }
        if (blackLeft == 0) { println("White Wins!\nBye!"); exitProcess(0) } else {
            firstMoveP2 = false; printBoard() } }
    else { if (p1[0] != p1[2] && board[x2][y2] == "B" || p1[0] == p1[2] && board[x2][y2] == " ") {
            board [x1][y1] = " ";  board[x2][y2] = "W"
            if (p1[3] == '8') { printBoard(); println("White Wins!\nBye!"); exitProcess(0) }
            for (i in 0..17) { for (j in 0..33) { if (board[i][j] == "B") blackLeft++ } }
            if (blackLeft == 0) { printBoard(); println("White Wins!\nBye!"); exitProcess(0) }
            else if (p1[0] == p1[2] && board[x2 - 2][y2] == "B" && blackLeft == 1 ) {
                printBoard(); println("Stalemate!\nBye!"); exitProcess(0) }
            else { firstMoveP2 = false; printBoard() } }
        else { println("Invalid Input"); play(pl1, pl2) } }
}

fun checkMoveP2(pl1: String, pl2: String) {
    var (x1, y1, x2, y2) = MutableList(4) { 0 }
    p1turn = false
    var wrongInpP2 = false
    do {
        println("$pl2's turn:")
        val p2 = readln()
        if (p2 == "exit") { println("Bye!"); exitProcess(0) }
        var whiteLeft = 0
        for ((k, v) in mapX) { if (k == p2[1]) x1 = v; if (k == p2[3]) x2 = v }
        for ((k, v) in mapY) { if (k == p2[0]) y1 = v; if (k == p2[2]) y2 = v }

        if (board[x1][y1] != "B") { println("No black pawn at ${p2[0]}${p2[1]}"); wrongInpP2 = true }
        else if (p2.length > 4 || !regex.matches(p2) || p2[1] == '7' && p2[1] - p2[3] > 2 || p2[1] < p2[3] ||
            p2[0] == p2[2] && board[x2][y2] != " " || abs(y2 - y1) > 4 || p2[0] == p2[2] && p2[1] == p2[3] ||
            p2[0] != p2[2] && x2 != enPasP1_x && y2 != enPasP1_y && !firstMoveP1 && board[x2][y2] != "W" ||
            p2[0] != p2[2] && !firstMoveP1 && board[x2][y2] != "W" || p2[1] < '7' && p2[1] - p2[3] > 1) {
            println("Invalid Input"); wrongInpP2 = true }
        else if (p2[0] == p2[2] && p2[1] == '7' && p2[3] == '5') {
            board[x1][y1] = " ";  board[x2][y2] = "B"; firstMoveP2 = true; move1_P2_x2 = x2; move1_P2_y2 = y2
            enPasP2_x = move1_P2_x2 - 4; enPasP2_y = move1_P2_y2; firstMoveP1 = false; break }
        else if (p2[0] != p2[2] && firstMoveP1 && x1 == move1_P1_x2 && (y1 - 4) == move1_P1_y2 ||
            y1 <= 29 && x1 == move1_P1_x2 && (y1 + 4) == move1_P1_y2) { board[x1][y1] = " "
            board[x2][y2] = "B"; board[move1_P1_x2][move1_P1_y2] = " "
            for (i in 0..17) { for (j in 0..33) { if (board[i][j] == "W") whiteLeft++ } }
            if (whiteLeft == 0) { printBoard(); println("Black Wins!\nBye!"); exitProcess(0) }
            else { firstMoveP1 = false; break } }
        else { if (p2[0] != p2[2] && board[x2][y2] == "W" || p2[0] == p2[2] && board[x2][y2] == " ") {
                board [x1][y1] = " "; board[x2][y2] = "B"
                if (p2[3] == '1') { printBoard(); println("Black Wins!\nBye!"); exitProcess(0) }
                for (i in 0..17) { for (j in 0..33) { if (board[i][j] == "W") whiteLeft++ } }
                if (whiteLeft == 0) { printBoard(); println("Black Wins!\nBye!"); exitProcess(0) }
                else if (p2[0] == p2[2] && board[x2 + 2][y2] == "W" && whiteLeft == 1) {
                    printBoard(); println("Stalemate!\nBye!"); exitProcess(0) }
                else { firstMoveP1 = false } }
            else { println("Invalid Input"); wrongInpP2 = true } }
    } while(wrongInpP2)
    printBoard()
    play(pl1, pl2)
}