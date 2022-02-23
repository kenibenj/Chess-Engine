package chess.engine.board;

import chess.engine.Team;
import chess.engine.pieces.*;
import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Map<Integer, Piece> boardConfig;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;


    private Board(final Builder builder){
        this.boardConfig = Collections.unmodifiableMap(builder.boardConfig);
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = findActivePieces(this.gameBoard, Team.WHITE);
        this.blackPieces = findActivePieces(this.gameBoard, Team.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStarterMoves = findLegalMoves(this.whitePieces);
        final Collection<Move> blackStarterMoves = findLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStarterMoves, blackStarterMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStarterMoves, blackStarterMoves);
        this.currentPlayer = builder.nextPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString(){
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i <BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            stringBuilder.append(String.format("%3s", tileText));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn(){
        return enPassantPawn;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Piece getPiece(final int coordinate) {
        return this.boardConfig.get(coordinate);
    }

    private Collection<Move> findLegalMoves(Collection<Piece> pieces) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece : pieces){

            legalMoves.addAll(piece.findLegalMoves(this));
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> findActivePieces(final List<Tile> gameBoard, final Team team) {

        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getOccupyingPiece();
                if(piece.getPieceTeam() == team){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i =0; i < BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStarterBoard(){
        final Builder builder = new Builder();

        builder.setPiece(new Rook(0, Team.BLACK));
        builder.setPiece(new Knight(1, Team.BLACK));
        builder.setPiece(new Bishop(2, Team.BLACK));
        builder.setPiece(new Queen(3, Team.BLACK));
        builder.setPiece(new King(4, Team.BLACK, true, true));
        builder.setPiece(new Bishop(5, Team.BLACK));
        builder.setPiece(new Knight(6, Team.BLACK));
        builder.setPiece(new Rook(7, Team.BLACK));
        builder.setPiece(new Pawn(8, Team.BLACK));
        builder.setPiece(new Pawn(9, Team.BLACK));
        builder.setPiece(new Pawn(10, Team.BLACK));
        builder.setPiece(new Pawn(11, Team.BLACK));
        builder.setPiece(new Pawn(12, Team.BLACK));
        builder.setPiece(new Pawn(13, Team.BLACK));
        builder.setPiece(new Pawn(14, Team.BLACK));
        builder.setPiece(new Pawn(15, Team.BLACK));

        builder.setPiece(new Rook(63, Team.WHITE));
        builder.setPiece(new Knight(62, Team.WHITE));
        builder.setPiece(new Bishop(61, Team.WHITE));
        builder.setPiece(new Queen(59, Team.WHITE));
        builder.setPiece(new King(60, Team.WHITE, true, true));
        builder.setPiece(new Bishop(58, Team.WHITE));
        builder.setPiece(new Knight(57, Team.WHITE));
        builder.setPiece(new Rook(56, Team.WHITE));
        builder.setPiece(new Pawn(55, Team.WHITE));
        builder.setPiece(new Pawn(54, Team.WHITE));
        builder.setPiece(new Pawn(53, Team.WHITE));
        builder.setPiece(new Pawn(52, Team.WHITE));
        builder.setPiece(new Pawn(51, Team.WHITE));
        builder.setPiece(new Pawn(50, Team.WHITE));
        builder.setPiece(new Pawn(49, Team.WHITE));
        builder.setPiece(new Pawn(48, Team.WHITE));

        builder.setNextPlayer(Team.WHITE);
        return builder.build();
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }


    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Team nextPlayer = Team.WHITE;
        Pawn enPassantPawn;
        Move transitionMove;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setNextPlayer(final Team nextPlayer){
            this.nextPlayer = nextPlayer;
            return this;
        }


        public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }

}
