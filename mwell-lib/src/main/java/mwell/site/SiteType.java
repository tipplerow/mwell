
package mwell.site;

/**
 * Enumerates the distinct local environments of sites within a
 * microwell array.
 */
public enum SiteType {
    /**
     * Sites occupied by the solid body of the microwell device
     * (not accessible to cells or growth media).
     */
    BODY(true, false),

    /**
     * Sites above the microwell (in the bulk media) with no
     * neighboring surface sites.
     */
    BULK(true, true),

    /**
     * Sites within the microwell with no neighboring surface
     * sites.
     */
    WELL(true, true),

    /**
     * Sites within the microwell or immediately above the
     * microwell with at least one neighboring surface site.
     */
    SURFACE(true, true),

    /**
     * Denotes lattice coordinates outside of the simulation box.
     */
    NONE(false, false);

    private final boolean inBox;
    private final boolean isAccessible;

    private SiteType(boolean inBox, boolean isAccessible) {
        this.inBox = inBox;
        this.isAccessible = isAccessible;
    }

    /**
     * Identifies sites that lie within the simulation box.
     *
     * @return {@code true} iff sites of this type lie within the
     * simulation box.
     */
    public boolean inBox() {
        return inBox;
    }

    /**
     * Identifies sites that are accessible to cells and growth media.
     *
     * @return {@code true} iff sites of this type are accessible to
     * cells and growth media.
     */
    public boolean isAccessible() {
        return isAccessible;
    }
}
