package chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] THIRD_COLUMN = initColumn(2);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);
    public static final boolean[] FIFTH_COLUMN = initColumn(4);
    public static final boolean[] SIXTH_COLUMN = initColumn(5);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static boolean[] SECOND_RANK = initRow(48);
    public static boolean[] FIRST_RANK = initRow(56);

    public static final String[] ALGEBREIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    private BoardUtils(){
        throw new RuntimeException("BoardUtils cannot be instantiated");
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    private static String[] initializeAlgebraicNotation() {
        String[] finalArray = new String[64];
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int count = 0;
        for(int x : numbers) {
            for (String y : letters) {
                finalArray[count] = y + x;
                count++;
            }
        }
        return finalArray;
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for(int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBREIC_NOTATION[i],i );
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    private static boolean[] initColumn(int columnNumber) {

        final boolean[] column = new boolean[NUM_TILES];

        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }
        while(columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {

        final boolean[] row = new boolean[NUM_TILES];

        do{
            row[rowNumber] = true;
            rowNumber++;
        }
        while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    public static int getPositionAtCoordinate(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBREIC_NOTATION[coordinate];
    }
}
