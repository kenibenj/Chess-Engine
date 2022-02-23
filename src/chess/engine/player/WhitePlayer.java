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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board, final Collection<Move> whiteStarterMoves, final Collection<Move> blackStarterMoves) {
        super(board, whiteStarterMoves, blackStarterMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Team getTeam() {
        return Team.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {

            //WHITE KING SIDE CASTLE
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getOccupyingPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                    rookTile.getOccupyingPiece().getPieceType().isRook())
                    kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook)rookTile.getOccupyingPiece(), rookTile.getTileCoordinate(), 61));
                }
            }

            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getOccupyingPiece().isFirstMove() && Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()
                && rookTile.getOccupyingPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getOccupyingPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}