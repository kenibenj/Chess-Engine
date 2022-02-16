package chess.engine.pieces;

import chess.engine.Team;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece{

    private final static int[] POTENTIAL_MOVES = {-8, -1, 1, 8,};

    public Rook(final int piecePosition, final Team pieceTeam) {
        super(PieceType.ROOK,piecePosition, pieceTeam, true);
    }
    public Rook(final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        super(PieceType.ROOK,piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> findLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentPotentialMoveOffset : POTENTIAL_MOVES) {

            final int potentialMoveCoordinate;
            potentialMoveCoordinate = this.piecePosition + currentPotentialMoveOffset;

            if (BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentPotentialMoveOffset) || isEighthColumnExclusion(this.piecePosition, currentPotentialMoveOffset)) {
                    continue;
                }

                final Tile potentialMoveTile = board.getTile(potentialMoveCoordinate);

                if (!potentialMoveTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, potentialMoveCoordinate));
                } else {
                    final Piece pieceAtPotentialMoveTile = potentialMoveTile.getOccupyingPiece();

                    if (this.pieceTeam != pieceAtPotentialMoveTile.getPieceTeam()) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, potentialMoveCoordinate, pieceAtPotentialMoveTile));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getMoveCoordinate(), move.getPieceMoved().getPieceTeam());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] && (potentialMoveOffset == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (potentialMoveOffset == 1);
    }
}

