
package mwell.rect;

import java.util.ArrayList;
import java.util.List;

import jam.bravais.UnitIndex;
import jam.lang.JamException;

/**
 * Enumerates the orthogonal directions of translations within the
 * rectangular simulation box.
 */
public enum RectDirection {
    DOWN {
        @Override public UnitIndex neighbor(int x, int y) {
            return UnitIndex.at(x, y - 1);
        }
    },

    LEFT {
        @Override public UnitIndex neighbor(int x, int y) {
            return UnitIndex.at(x - 1, y);
        }
    },

    RIGHT {
        @Override public UnitIndex neighbor(int x, int y) {
            return UnitIndex.at(x + 1, y);
        }
    },

    UP {
        @Override public UnitIndex neighbor(int x, int y) {
            return UnitIndex.at(x, y + 1);
        }
    };

    /**
     * Returns the coordinates of the neighbors to a reference site
     * along a sequence of directions.
     *
     * @param x the {@code x}-coordinate of the reference site.
     *
     * @param y the {@code y}-coordinate of the reference site.
     *
     * @return the coordinates of the sites nearest to {@code (x, y)}
     * along the specified directions.
     */
    public static List<UnitIndex> neighbors(int x, int y, RectDirection... direcs) {
        List<UnitIndex> neighbors =
            new ArrayList<UnitIndex>(direcs.length);

        for (RectDirection direc : direcs)
            neighbors.add(direc.neighbor(x, y));

        return neighbors;
    }

    /**
     * Returns the coordinates of the first neighboring site along
     * this direction.
     *
     * @param x the {@code x}-coordinate of the reference site.
     *
     * @param y the {@code y}-coordinate of the reference site.
     *
     * @return the coordinates of the site nearest to {@code (x, y)}
     * along this direction.
     */
    public abstract UnitIndex neighbor(int x, int y);
}
