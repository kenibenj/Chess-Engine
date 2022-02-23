package chess.engine.pieces;

import chess.engine.Team;
import chess.engine.board.Board;
import chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition ;
    protected final Team pieceTeam;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, int piecePosition, final Team pieceTeam, final Boolean isFirstMove){
        this.piecePosition = piecePosition;
        this.pieceTeam = pieceTeam;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceTeam.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result +(isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && this.pieceType == otherPiece.getPieceType() &&
                pieceTeam == otherPiece.getPieceTeam() && this.isFirstMove == otherPiece.isFirstMove();

    }

    @Override
    public int hashCode(){
       return this.cachedHashCode;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Team getPieceTeam(){
        return this.pieceTeam;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }

    public abstract Collection<Move> findLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);



    public enum PieceType{

        PAWN(100, "P"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(350, "B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900,"Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(100000,"K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final int pieceValue, final String pieceName){
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public int getPieceValue(){
            return this.pieceValue;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}


