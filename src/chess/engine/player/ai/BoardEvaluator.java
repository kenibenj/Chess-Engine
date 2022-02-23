package chess.engine.player.ai;

import chess.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);
}
