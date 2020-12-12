
package mwell.site;

import java.util.Collection;

import jam.bravais.UnitIndex;

/**
 * Represents a lattice site in a microwell device.
 */
public interface Site {
    /**
     * Returns the enumerated type of this site.
     *
     * @return the enumerated type of this site.
     */
    public abstract SiteType getType();

    /**
     * Returns the location of this site.
     *
     * @return the location of this site.
     */
    public abstract UnitIndex getIndex();

    /**
     * Returns the locations of all accessible neighbors to this site.
     *
     * @return the locations of all accessible neighbors to this site.
     */
    public abstract Collection<UnitIndex> getNeighbors();

    /**
     * Returns the number of accessible nearest neighbors to this site.
     *
     * @return the number of accessible nearest neighbors to this site.
     */
    public default int countNeighbors() {
        return getNeighbors().size();
    }

    /**
     * Identifies accessible nearest neighbors to this site.
     *
     * @param index the coordinates of a neighbor site to test.
     *
     * @return {@code true} iff the site at the specified coordinates
     * is an accessible nearest neighbor to this site.
     */
    public default boolean isNeighbor(UnitIndex index) {
        for (UnitIndex neighbor : getNeighbors())
            if (neighbor.equals(index))
                return true;

        return false;
    }
}
