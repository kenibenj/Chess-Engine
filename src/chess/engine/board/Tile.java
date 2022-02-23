package chess.engine.board;

import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_MAP = getStartingEmptyTiles();


    private static Map<Integer, EmptyTile> getStartingEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_MAP.get(tileCoordinate);
    }

    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getOccupyingPiece();

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }



    public static final class EmptyTile extends Tile{

        private EmptyTile(final int tileCoordinate){
            super(tileCoordinate);
        }

        @Override
        public String toString(){
            return "-";
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getOccupyingPiece(){
            return null;
        }
    }



    public static final class OccupiedTile extends Tile{

        private final Piece occupyingPiece;

        private OccupiedTile(final int tileCoordinate, final Piece occupyingPiece){
            super(tileCoordinate);
            this.occupyingPiece = occupyingPiece;
        }

        @Override
        public String toString(){
            return getOccupyingPiece().getPieceTeam().isBlack() ? getOccupyingPiece().toString().toLowerCase(Locale.ROOT) :
                    getOccupyingPiece().toString();
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Piece getOccupyingPiece(){
            return occupyingPiece;
        }
    }
}
