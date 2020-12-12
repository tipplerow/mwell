
package mwell.rect;

import java.util.Collection;
import java.util.List;

import jam.bravais.UnitIndex;

import mwell.site.AbstractSite;
import mwell.site.SiteType;

/**
 * Represents a lattice site in the rectangular microwell device.
 */
public final class RectSite extends AbstractSite {
    private static final int DIMENSIONALITY = 2;

    private RectSite(SiteType type, UnitIndex index, Collection<UnitIndex> neighbors) {
        super(DIMENSIONALITY, type, index, neighbors);
    }

    /**
     * Creates a new rectangular lattice site.
     *
     * @param type the local environment of the site.
     *
     * @param index the lattice coordinates for the site.
     *
     * @param neighbors the coordinates of all neighboring sites.
     *
     * @return the rectangular lattice site with the specified
     * parameters.
     */
    public static RectSite create(SiteType type, UnitIndex index, Collection<UnitIndex> neighbors) {
        return new RectSite(type, index, neighbors);
    }

    /**
     * Returns the {@code x}-coordinate of this lattice site.
     *
     * @return the {@code x}-coordinate of this lattice site.
     */
    public int x() {
        return getIndex().coord(0);
    }

    /**
     * Returns the {@code y}-coordinate of this lattice site.
     *
     * @return the {@code y}-coordinate of this lattice site.
     */
    public int y() {
        return getIndex().coord(1);
    }

    @Override public String toString() {
        return String.format("RectSite(%s, %d, %d)", getType(), x(), y());
    }
}
