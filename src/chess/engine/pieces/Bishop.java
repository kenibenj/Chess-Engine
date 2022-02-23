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

public class Bishop extends Piece{

    private static final int[] POTENTIAL_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(final int piecePosition, final Team pieceTeam) {
        super(PieceType.BISHOP,piecePosition, pieceTeam, true);
    }
    public Bishop(final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        super(PieceType.BISHOP,piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> findLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentPotentialMoveOffset: POTENTIAL_MOVE_VECTOR_COORDINATES){
            int potentialMoveCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)) {
                if(isFirstColumnExclusion(potentialMoveCoordinate, currentPotentialMoveOffset) || isEighthColumnExclusion(potentialMoveCoordinate, currentPotentialMoveOffset)){
                    break;
                }

                potentialMoveCoordinate += currentPotentialMoveOffset;
                if(BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)){
                    final Tile potentialMoveTile = board.getTile(potentialMoveCoordinate);
                    if (!potentialMoveTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, potentialMoveCoordinate));
                    } else {
                        final Piece pieceAtPotentialMoveTile = potentialMoveTile.getOccupyingPiece();
                        if (this.pieceTeam != pieceAtPotentialMoveTile.getPieceTeam()) {
                            legalMoves.add(new MajorAttackMove(board, this, potentialMoveCoordinate, pieceAtPotentialMoveTile));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMoveCoordinate(), move.getPieceMoved().getPieceTeam());
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int potentialMoveOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (potentialMoveOffset == -9 || potentialMoveOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int potentialMoveOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (potentialMoveOffset == -7 || potentialMoveOffset == 9);
    }
}
