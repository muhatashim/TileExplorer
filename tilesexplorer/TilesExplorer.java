/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tilesexplorer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import tilesexplorer.executables.FlagExplorer;
import tilesexplorer.wrappers.InfoText;
import tilesexplorer.wrappers.PaintList;
import tilesexplorer.wrappers.TileAnalyzer;

/**
 *
 * @author VOLT
 */
@Manifest(name = "Tile Explorer", authors = "VIRUS",version=0.01)
public class TilesExplorer extends ActiveScript implements PaintListener, MouseListener, MouseMotionListener {

    private TileAnalyzer analyzer = null;
    private PaintList list = null;
    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    @Override
    public int loop() {
        return 1;
    }

    @Override
    public void onRepaint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(antialiasing);
        if (analyzer != null) {
            analyzer.draw(g2d);
        }
        if (list != null) {
            list.render(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (list != null && list.isVisible()
                    && list.getArea(list.getLastGraphics().getFontMetrics())
                    .contains(e.getPoint())) {
                list.onClick(e);
                return;
            }
            for (int i = -20; i <= 20; i++) {
                for (int i2 = -20; i2 <= 20; i2++) {
                    Tile tile = Players.getLocal().getLocation().derive(i, i2);
                    if (tile.contains(e.getPoint())) {
                        analyzer = new TileAnalyzer(tile);

                        final int flag = analyzer.getFlag();
                        final SceneObject at = SceneEntities.getAt(analyzer);
                        InfoText[] info = new InfoText[]{
                            new InfoText("Location: " + analyzer.getLocation(), null),
                            new InfoText("Flag: " + flag, null),
                            new InfoText("    -Binary: 0b" + Integer.toBinaryString(flag), null),
                            new InfoText("    -Hex: 0x" + Integer.toHexString(flag), null),
                            new InfoText("    -Octal: 0" + Integer.toOctalString(i), null),
                            new InfoText("    +More info", new FlagExplorer(flag)),
                            new InfoText("Distance: " + Calculations.distanceTo(analyzer), null),
                            new InfoText("Object located on top: "
                            + ((at != null && at.getDefinition() != null)
                            ? at.getDefinition().getName() : at), null),
                            new InfoText("    -ID: " + ((at != null) ? at.getId() : "unknown"), null),
                            new InfoText("Can reach: " + analyzer.canReach(), null)};
                        list = new PaintList(e.getPoint(), info);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    private Point lastPoint = null;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (list != null && list.isVisible() && lastPoint != null) {
            Point p = e.getPoint();
            Point loc = list.getLocation();
            loc.translate(p.x - lastPoint.x, p.y - lastPoint.y);
            list.setLocation(loc);
            lastPoint = e.getPoint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastPoint = e.getPoint();
    }
}
