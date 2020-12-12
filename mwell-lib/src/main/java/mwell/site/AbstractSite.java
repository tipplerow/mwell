
package mwell.site;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jam.bravais.UnitIndex;
import jam.lang.JamException;
import jam.util.CollectionUtil;

/**
 * Provides a base class for lattice site implementations.
 */
public abstract class AbstractSite implements Site {
    private final SiteType type;
    private final UnitIndex index;
    private final Collection<UnitIndex> neighbors;

    /**
     * Creates a new site with fixed attributes.
     *
     * @param dim the dimensionality of the site.
     *
     * @param type the local environment of the site.
     *
     * @param index the coordinates of the lattice site.
     *
     * @param neighbors the coordinates of the neighbors to the site.
     *
     * @throws RuntimeException unless the coordinates of the site and
     * its neighbors have dimensions equal to the {@code dim} argument
     * and all neighbors are unique.
     */
    protected AbstractSite(int dim, SiteType type, UnitIndex index, Collection<UnitIndex> neighbors) {
        this.type = type;
        this.index = index;
        this.neighbors = Collections.unmodifiableCollection(neighbors);

        validateIndex(dim, index);
        validateNeighbors();
    }

    private static void validateIndex(int dim, UnitIndex index) {
        if (index.dimensionality() != dim)
            throw JamException.runtime("Invalid index dimensionality.");
    }

    private void validateNeighbors() {
        if (!CollectionUtil.allUnique(neighbors))
            throw JamException.runtime("Neighbor sites are not unique.");

        for (UnitIndex neighbor : neighbors)
            validateNeighbor(neighbor);
    }

    private void validateNeighbor(UnitIndex neighbor) {
        if (neighbor.dimensionality() != index.dimensionality())
            throw JamException.runtime("Inconsistent neighbor dimensionality.");

        if (neighbor.equals(index))
            throw JamException.runtime("Neighbor and site must have different coordinates.");
    }

    @Override public SiteType getType() {
        return type;
    }

    @Override public UnitIndex getIndex() {
        return index;
    }

    @Override public Collection<UnitIndex> getNeighbors() {
        return neighbors;
    }

    @Override public int countNeighbors() {
        return neighbors.size();
    }
}
