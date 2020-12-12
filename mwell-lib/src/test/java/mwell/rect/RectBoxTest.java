
package mwell.rect;

import java.util.List;

import jam.bravais.UnitIndex;

import mwell.site.SiteType;

import org.junit.*;
import static org.junit.Assert.*;

public class RectBoxTest {
    private static final int WELL_DEPTH = 5;
    private static final int WELL_WIDTH = 6;
    private static final int WELL_SPACING = 8;
    private static final int MEDIA_HEIGHT = 3;

    private static final RectBox box =
        RectBox.create(WELL_DEPTH, WELL_WIDTH, WELL_SPACING, MEDIA_HEIGHT);

    @Test public void testSites() {
        assertSite(0, 0, null);
        assertSite(3, 0, null);
        assertSite(4, 0, SiteType.SURFACE, UnitIndex.at(5, 0), UnitIndex.at(4, 1));
        assertSite(5, 0, SiteType.SURFACE, UnitIndex.at(4, 0), UnitIndex.at(6, 0), UnitIndex.at(5, 1));
        assertSite(9, 0, SiteType.SURFACE, UnitIndex.at(8, 0), UnitIndex.at(9, 1));
        assertSite(4, 1, SiteType.SURFACE, UnitIndex.at(4, 0), UnitIndex.at(5, 1), UnitIndex.at(4, 2));
        assertSite(5, 1, SiteType.WELL, UnitIndex.at(5, 0), UnitIndex.at(4, 1), UnitIndex.at(6, 1), UnitIndex.at(5, 2));
    }

    private void assertSite(int x, int y, SiteType type, UnitIndex... neighbors) {
        RectSite site = box.siteAt(x, y);

        if (type != null)
            assertSite(site, x, y, type, neighbors);
        else
            assertNull(site);
    }

    private void assertSite(RectSite site, int x, int y, SiteType type, UnitIndex... neighbors) {
        assertEquals(x, site.x());
        assertEquals(y, site.y());
        assertEquals(type, site.getType());
        assertEquals(neighbors.length, site.countNeighbors());

        for (UnitIndex neighbor : neighbors)
            assertTrue(site.isNeighbor(neighbor));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("mwell.rect.RectBoxTest");
    }
}
