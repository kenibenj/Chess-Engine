package chess;

import chess.engine.board.Board;
import chess.gui.Table;

public class ChessAPP {

    public static void main(String[] args){

        Board board = Board.createStarterBoard();

        System.out.println(board);

        Table.get().show();
    }
}
