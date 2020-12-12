
package mwell.rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jam.bravais.Period;
import jam.bravais.UnitIndex;
import jam.collect.ObjectMatrix;
import jam.lang.JamException;

import mwell.site.SiteType;

/**
 * Defines a two-dimensional rectangular geometry for a microwell
 * device.
 */
public final class RectBox {
    private final int wellDepth;
    private final int wellWidth;
    private final int wellSpacing;
    private final int mediaHeight;

    private final int totalWidth;
    private final int totalHeight;

    private final List<RectSite> siteList;
    private final ObjectMatrix<RectSite> siteMatrix;

    private RectBox(int wellDepth,
                    int wellWidth,
                    int wellSpacing,
                    int mediaHeight) {
        this.wellDepth = wellDepth;
        this.wellWidth = wellWidth;
        this.wellSpacing = wellSpacing;
        this.mediaHeight = mediaHeight;

        validateDim();

        this.totalWidth = wellWidth + wellSpacing;
        this.totalHeight = wellDepth + mediaHeight;

        this.siteList = new ArrayList<RectSite>();
        this.siteMatrix = ObjectMatrix.create(totalWidth, totalHeight);

        fillSites();
    }

    private void validateDim() {
        validateDim(wellDepth, "well depth");
        validateDim(wellWidth, "well width");
        validateDim(wellSpacing, "well spacing");
        validateDim(mediaHeight, "media height");
    }

    private static void validateDim(int dim, String desc) {
        if (dim < 1)
            throw JamException.runtime("Invalid dimension: %s must be positive.", desc);
    }

    private int imageOf(int x) {
        return Period.imageOf(x, totalWidth);
    }

    private void fillSites() {
        int nx = totalWidth;
        int ny = totalHeight;

        for (int y = 0; y < ny; ++y)
            for (int x = 0; x < nx; ++x)
                addSite(x, y);
    }

    private void addSite(int x, int y) {
        SiteType type = classifySite(x, y);

        if (type.isAccessible())
            addSite(type, x, y);
    }

    private void addSite(SiteType type, int x, int y) {
        UnitIndex index = UnitIndex.at(x, y);
        List<UnitIndex> neighbors = findNeighbors(x, y);

        addSite(RectSite.create(type, index, neighbors));
    }

    private void addSite(RectSite site) {
        siteList.add(site);
        siteMatrix.set(site.x(), site.y(), site);
    }

    private List<UnitIndex> findNeighbors(int x, int y) {
        List<UnitIndex> neighbors = new ArrayList<UnitIndex>(4);

        for (RectDirection direc : RectDirection.values()) {
            UnitIndex neighbor = direc.neighbor(x, y);

            if (classifySite(neighbor.coord(0), neighbor.coord(1)).isAccessible())
                neighbors.add(neighbor);
        }

        return neighbors;
    }

    /**
     * Creates a new rectangular box with fixed dimensions.
     *
     * @param wellDepth the vertical depth of the microwell
     * (expressed as a number of lattice sites).
     *
     * @param wellWidth the horizontal width of the microwell
     * (expressed as a number of lattice sites).
     *
     * @param wellSpacing the number of lattice sites between
     * microwells.
     *
     * @param mediaHeight the number of lattice sites above the
     * microwells that contain free growth media.
     *
     * @return a new rectangular box with the specified dimensions.
     *
     * @throws RuntimeException unless all parameters are positive.
     */
    public static RectBox create(int wellDepth,
                                 int wellWidth,
                                 int wellSpacing,
                                 int mediaHeight) {
        return new RectBox(wellDepth, wellWidth, wellSpacing, mediaHeight);
    }

    /**
     * Determines the local environment of a latice site in this
     * rectangular box.
     *
     * @param x the {@code x}-coordinate of the site.
     *
     * @param y the {@code y}-coordinate of the site.
     *
     * @return the local environment of the specified latice site (or
     * {@code null} if the site lies outside of this box).
     */
    public SiteType classifySite(int x, int y) {
        //
        // Apply periodic boundary conditions in the x-direction...
        //
        x = imageOf(x);

        int topY = getTopSurfaceY();
        int bottomY = getBottomSurfaceY();
        int ceilingY = getMediaCeilingY();

        if (y < bottomY)
            return SiteType.NONE; // y-coordinate is below the box...

        if (y == bottomY)
            return classifyBottomRow(x);

        if (y < topY)
            return classifyWellRow(x);

        if (y == topY)
            return classifyTopSurface(x);

        if (y <= ceilingY)
            return SiteType.BULK; // All x-coordinates above the top surface are bulk sites...

        return SiteType.NONE; // y-coordinate is above the box...
    }

    private SiteType classifyBottomRow(int x) {
        int left = getLeftSurfaceX();
        int right = getRightSurfaceX();

        if (x < left || x > right)
            return SiteType.BODY;
        else
            return SiteType.SURFACE;
    }

    private SiteType classifyWellRow(int x) {
        int left = getLeftSurfaceX();
        int right = getRightSurfaceX();

        if (x < left || x > right)
            return SiteType.BODY;

        if (x > left && x < right)
            return SiteType.WELL;

        return SiteType.SURFACE;
    }

    private SiteType classifyTopSurface(int x) {
        int left = getLeftSurfaceX();
        int right = getRightSurfaceX();

        if (x < left || x > right)
            return SiteType.SURFACE;
        else
            return SiteType.BULK;
    }

    /**
     * Returns the {@code y}-coordinate of the sites on the bottom
     * wall of the microwell.
     *
     * @return the {@code y}-coordinate of the sites on the bottom
     * wall of the microwell.
     */
    public int getBottomSurfaceY() {
        return 0;
    }

    /**
     * Returns the {@code x}-coordinate of the sites on the surface of
     * the left-side wall of the microwell.
     *
     * @return the {@code x}-coordinate of the sites on the surface of
     * the left-side wall of the microwell.
     */
    public int getLeftSurfaceX() {
        return wellSpacing / 2;
    }

    /**
     * Returns the {@code y}-coordinate of the uppermost free media.
     *
     * @return the {@code y}-coordinate of the uppermost free media.
     */
    private int getMediaCeilingY() {
        return getTopSurfaceY() + mediaHeight - 1;
    }

    /**
     * Returns the height of the growth medium above the microwell in
     * this rectangular box.
     *
     * @return the height of the growth medium above the microwell in
     * this rectangular box.
     */
    public int getMediaHeight() {
        return mediaHeight;
    }

    /**
     * Returns the {@code x}-coordinate of the sites on the surface of
     * the right-side wall of the microwell.
     *
     * @return the {@code x}-coordinate of the sites on the surface of
     * the right-side wall of the microwell.
     */
    public int getRightSurfaceX() {
        return getLeftSurfaceX() + wellWidth - 1;
    }

    /**
     * Returns the depth of the microwell in this rectangular box.
     *
     * @return the depth of the microwell in this rectangular box.
     */
    public int getWellDepth() {
        return wellDepth;
    }

    /**
     * Returns the number of sites between microwells in the
     * horizontal direction.
     *
     * @return the number of sites between microwells in the
     * horizontal direction.
     */
    public int getWellSpacing() {
        return wellSpacing;
    }

    /**
     * Returns the width of the microwell in this rectangular box.
     *
     * @return the width of the microwell in this rectangular box.
     */
    public int getWellWidth() {
        return wellWidth;
    }

    /**
     * Returns the {@code y}-coordinate of the sites on the top
     * wall of the microwell.
     *
     * @return the {@code y}-coordinate of the sites on the top
     * wall of the microwell.
     */
    public int getTopSurfaceY() {
        return getBottomSurfaceY() + wellDepth;
    }

    /**
     * Returns the total width of this rectangular box (the number of
     * unique {@code x}-coordinates).
     *
     * @return the total width of this rectangular box.
     */
    public int getTotalWidth() {
        return totalWidth;
    }

    /**
     * Returns the total height of this rectangular box (the number of
     * unique {@code y}-coordinates).
     *
     * @return the total height of this rectangular box.
     */
    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * Returns an unmodifiable list containing all available sites in
     * this rectangular box.
     *
     * @return an unmodifiable list containing all available sites in
     * this rectangular box.
     */
    public List<RectSite> listSites() {
        return Collections.unmodifiableList(siteList);
    }

    /**
     * Returns the lattice site at a particular location.
     *
     * @param x the {@code x}-coordinate of the site.
     *
     * @param y the {@code y}-coordinate of the site.
     *
     * @return the lattice site at the specified location (or
     * {@code null} if the coordinates identify an inaccessible
     * location).
     *
     * @throws IndexOutOfBoundsException unless the coordinates
     * lie within this box.
     */
    public RectSite siteAt(int x, int y) {
        return siteMatrix.get(x, y);
    }
}
