package chess.engine.player;

import chess.engine.Team;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board, final Collection<Move> whiteStarterMoves, final Collection<Move> blackStarterMoves) {
        super(board, blackStarterMoves, whiteStarterMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Team getTeam() {
        return Team.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {

            //BLACK KING SIDE CASTLE
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getOccupyingPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
                            rookTile.getOccupyingPiece().getPieceType().isRook())
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getOccupyingPiece(), rookTile.getTileCoordinate(), 5));
                }
            }

            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getOccupyingPiece().isFirstMove() && Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()
                && rookTile.getOccupyingPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 2, (Rook)rookTile.getOccupyingPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
