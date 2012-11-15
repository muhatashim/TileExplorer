/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tilesexplorer.wrappers;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.wrappers.RegionOffset;
import org.powerbot.game.api.wrappers.Tile;

/**
 *
 * @author VOLT
 */
public class TileAnalyzer extends Tile {

    public TileAnalyzer(Tile tile) {
        super(tile.getX(), tile.getY(), tile.getPlane());
    }

    public int getFlag() {
        RegionOffset off = getRegionOffset();
        int[][] collisionFlags = Walking.getCollisionFlags(off.getPlane());
        if (off.getX() <= 1 || off.getY() <= 1
                || off.getX() >= collisionFlags.length || off.getY() >= collisionFlags[0].length) {
            return -1;
        }
        return collisionFlags[off.getX() - 2][off.getY() - 2];
    }
}
