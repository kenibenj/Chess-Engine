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

public class King extends Piece{

    private static final int[] POTENTIAL_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final int piecePosition, final Team pieceTeam, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, pieceTeam, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }
    public King(final int piecePosition, final Team pieceTeam, final boolean isFirstMove, final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable){
        super(PieceType.KING,piecePosition, pieceTeam, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled(){
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable(){
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable(){
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for( final int currentPotentialMoveOffset : POTENTIAL_MOVE_COORDINATES) {

            int potentialMoveCoordinate;
            potentialMoveCoordinate = this.piecePosition + currentPotentialMoveOffset;

            if(isFirstColumnExclusion(this.piecePosition, currentPotentialMoveOffset) || isEighthColumnExclusion(this.piecePosition, currentPotentialMoveOffset)){
                continue;
            }

            if (BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)) {
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
    public King movePiece(final Move move) {
        return new King(move.getMoveCoordinate(), move.getPieceMoved().getPieceTeam(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] && (potentialMoveOffset == -9 || potentialMoveOffset == -1 ||
                potentialMoveOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int potentialMoveOffset) {

        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (potentialMoveOffset == -7 || potentialMoveOffset == 1 || potentialMoveOffset == 9);
    }
}
