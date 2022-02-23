package chess.engine.pieces;

import chess.engine.Team;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{

    private static final int[] POTENTIAL_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(final int piecePosition, final Team pieceTeam) {
        super(PieceType.PAWN,piecePosition, pieceTeam, true);
    }
    public Pawn(final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        super(PieceType.PAWN,piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> findLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentPotentialMoveOffset : POTENTIAL_MOVE_COORDINATES){

            final int potentialMoveCoordinate;
            potentialMoveCoordinate = this.piecePosition + (currentPotentialMoveOffset * this.getPieceTeam().getDirection());

            if(!BoardUtils.isValidTileCoordinate(potentialMoveCoordinate)){
                continue;
            }

            //normal Pawn move
            if(currentPotentialMoveOffset == 8 && !board.getTile(potentialMoveCoordinate).isTileOccupied()){

                if(this.pieceTeam.isPawnPromotionSquare(potentialMoveCoordinate)){
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, potentialMoveCoordinate)));
                }
                else{
                    legalMoves.add(new Move.PawnMove(board, this, potentialMoveCoordinate));
                }
            }

            //Pawn Jump
            else if(currentPotentialMoveOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.pieceTeam.isBlack()) ||
                            (BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceTeam.isWhite()))){

                final int behindPotentialMoveCoordinate = this.piecePosition + (this.pieceTeam.getDirection() * 8);

                if (!board.getTile(behindPotentialMoveCoordinate).isTileOccupied() &&
                        !board.getTile(potentialMoveCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.PawnJump(board, this, potentialMoveCoordinate));
                }
            }

            //Pawn Attack
            else if(currentPotentialMoveOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceTeam.isWhite() ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack())))){

                if(board.getTile(potentialMoveCoordinate).isTileOccupied()){
                    final Piece pieceOnPotentialMove = board.getTile(potentialMoveCoordinate).getOccupyingPiece();
                    if(this.pieceTeam != pieceOnPotentialMove.getPieceTeam()){

                        if(this.pieceTeam.isPawnPromotionSquare(potentialMoveCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, potentialMoveCoordinate, pieceOnPotentialMove)));
                        }
                        else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, potentialMoveCoordinate, pieceOnPotentialMove));
                        }
                    }
                }
                else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceTeam.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceTeam != pieceOnCandidate.getPieceTeam()){
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, potentialMoveCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
            else if (currentPotentialMoveOffset == 9 && !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite() ||
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceTeam.isBlack())))){

                if(board.getTile(potentialMoveCoordinate).isTileOccupied()){
                    final Piece pieceOnPotentialMove = board.getTile(potentialMoveCoordinate).getOccupyingPiece();
                    if(this.pieceTeam != pieceOnPotentialMove.getPieceTeam()){
                        //Attack Move
                        if(this.pieceTeam.isPawnPromotionSquare(potentialMoveCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, potentialMoveCoordinate, pieceOnPotentialMove)));
                        }
                        else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, potentialMoveCoordinate, pieceOnPotentialMove));
                        }
                    }
                }
                else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceTeam.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceTeam != pieceOnCandidate.getPieceTeam()){
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, potentialMoveCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Pawn(move.getMoveCoordinate(), move.getPieceMoved().getPieceTeam());
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(){
        return new Queen( this.piecePosition, this.pieceTeam, false);
    }
}
