package chess.engine.board;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

public abstract class Move {

    protected Board board;
    protected Piece pieceMoved;
    protected int moveCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece pieceMoved, final int moveCoordinate){
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.moveCoordinate = moveCoordinate;
        this.isFirstMove = pieceMoved.isFirstMove();
    }

    private Move(final Board board, final int moveCoordinate){
        this.board = board;
        this.moveCoordinate = moveCoordinate;
        this.pieceMoved = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime*result+ this.moveCoordinate;
        result = prime*result + this.pieceMoved.hashCode();
        result = prime*result + this.pieceMoved.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getMoveCoordinate() == otherMove.getMoveCoordinate() &&
                getPieceMoved().equals(otherMove.getPieceMoved());
    }

    public Board getBoard(){
        return this.board;
    }

    public int getCurrentCoordinate(){
        return this.pieceMoved.getPiecePosition();
    }

    public int getMoveCoordinate(){
        return this.moveCoordinate;
    }

    public Piece getPieceMoved(){
        return this.pieceMoved;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()){

            if(!this.pieceMoved.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //move
        builder.setPiece(this.pieceMoved.movePiece(this));
        builder.setNextPlayer(this.board.currentPlayer().getOpponent().getTeam());

        return builder.build();
    }

    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(final Board board, final Piece pieceMoved, final int destinationCoordinate, final Piece pieceAttacked){
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return pieceMoved.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.moveCoordinate);
        }
    }

    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece pieceMoved, final int moveCoordinate) {
            super(board, pieceMoved, moveCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        public String toString(){
            return pieceMoved.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.moveCoordinate);
        }

    }


    public static class AttackMove extends Move{

        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Piece attackedPiece) {
            super(board, pieceMoved, moveCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }


    public static final class PawnMove extends Move{

        public PawnMove(final Board board, final Piece pieceMoved, final int moveCoordinate) {
            super(board, pieceMoved, moveCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.moveCoordinate);
        }
    }


    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Piece attackedPiece) {
            super(board, pieceMoved, moveCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.pieceMoved.getPiecePosition()).substring(0, 1) + "x" + BoardUtils.getPositionAtCoordinate(this.moveCoordinate);
        }
    }


    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Piece attackedPiece) {
            super(board, pieceMoved, moveCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.pieceMoved.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.attackedPiece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.pieceMoved.movePiece(this));
            builder.setNextPlayer(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }
    }

public static class PawnPromotion extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;

    @Override
    public Board execute(){
        final Board pawnMovedBoard = this.decoratedMove.execute();
        final Board.Builder builder = new Board.Builder();
        for(final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
            if(!this.promotedPawn.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
        builder.setNextPlayer(pawnMovedBoard.currentPlayer().getTeam());
        return builder.build();
    }

    @Override
    public int hashCode(){
        return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
    }

    @Override
    public boolean equals(final Object other){
        return this == other || other instanceof PawnPromotion && (super.equals(other));
    }

    @Override
    public boolean isAttack(){
        return this.decoratedMove.isAttack();
    }

    @Override
    public Piece getAttackedPiece(){
        return this.decoratedMove.getAttackedPiece();
    }

    @Override
    public String toString(){
        return "";
    }

    public PawnPromotion(final Move decoratedMove) {
        super(decoratedMove.getBoard(), decoratedMove.getPieceMoved(), decoratedMove.getMoveCoordinate());
        this.decoratedMove = decoratedMove;
        this.promotedPawn = (Pawn) decoratedMove.getPieceMoved();
    }
}


    public static final class PawnJump extends Move{

        public PawnJump(final Board board, final Piece pieceMoved, final int moveCoordinate) {
            super(board, pieceMoved, moveCoordinate);
        }

        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.pieceMoved.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.pieceMoved.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextPlayer(this.board.currentPlayer().getOpponent().getTeam());
            builder.setMoveTransition(this);
            return builder.build();
        }

        @Override
        public String toString(){
            return String.valueOf(BoardUtils.getPositionAtCoordinate(this.moveCoordinate));
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }
    }

    static abstract class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;


        public CastleMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination){
            super(board, pieceMoved, moveCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.pieceMoved.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.pieceMoved.movePiece(this));

            //TODO FIRSTMOVE
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceTeam()));
            builder.setNextPlayer(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove)other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, pieceMoved, moveCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "0-0";
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board, final Piece pieceMoved, final int moveCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, pieceMoved, moveCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "0-0-0";
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }

    public static final class NullMove extends Move{

        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute the null move");
        }

        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("MoveFactory is not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){

            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate &&
                move.getMoveCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
