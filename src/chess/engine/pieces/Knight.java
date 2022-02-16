package chess.engine.pieces;

import chess.engine.Team;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import static chess.engine.board.Move.*;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private final static int[] POTENTIAL_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Team pieceTeam) {
        super(PieceType.KNIGHT,piecePosition, pieceTeam, true);
    }
    public Knight(final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        super(PieceType.KNIGHT,piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> findLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentPotentialMoveOffset : POTENTIAL_MOVE_COORDINATES) {

            final int potentialMoveCoordinate;
            potentialMoveCoordinate = this.piecePosition + currentPotentialMoveOffset;

            if (BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentPotentialMoveOffset) || isSecondColumnExclusion(this.piecePosition, currentPotentialMoveOffset)
                || isSeventhColumnExclusion(this.piecePosition, currentPotentialMoveOffset)|| isEighthColumnExclusion(this.piecePosition, currentPotentialMoveOffset)) {
                    continue;
                }

                final Tile potentialMoveTile = board.getTile(potentialMoveCoordinate);

                if (!potentialMoveTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, potentialMoveCoordinate));
                } else {
                    final Piece pieceAtPotentialMoveTile = potentialMoveTile.getOccupyingPiece();

                    if (this.pieceTeam != pieceAtPotentialMoveTile.getPieceTeam()) {
                        legalMoves.add(new MajorAttackMove(board, this, potentialMoveCoordinate, pieceAtPotentialMoveTile));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMoveCoordinate(), move.getPieceMoved().getPieceTeam());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] && (potentialMoveOffset == -17 || potentialMoveOffset == -10 ||
                potentialMoveOffset == 6 || potentialMoveOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.SECOND_COLUMN[currentPosition] && (potentialMoveOffset == -10 || potentialMoveOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (potentialMoveOffset == -6 || potentialMoveOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (potentialMoveOffset == -15 || potentialMoveOffset == 6 ||
                potentialMoveOffset == 10 || potentialMoveOffset == 17);
    }
}