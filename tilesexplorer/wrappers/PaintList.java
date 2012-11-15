/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tilesexplorer.wrappers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.geom.RoundRectangle2D;
import tilesexplorer.impls.Executable;

/**
 *
 * @author VOLT
 */
public class PaintList {

    private final InfoText[] texts;
    private boolean visible = true;
    private Point p;
    private Graphics2D lastGraphics = null;

    public PaintList(Point p, InfoText... texts) {
        if (texts.length == 0) {
            throw new IllegalArgumentException("strings cannot be empty");
        }
        this.p = p;
        this.texts = texts;
    }

    public void setLocation(Point p) {
        this.p = p;
    }

    public synchronized Point getLocation() {
        return this.p;
    }

    public Rectangle2D.Double getXButtonArea(Graphics2D g) {
        if (g != null) {
            final int height = g.getFontMetrics().getHeight();
            return new Rectangle2D.Double(p.x - 10, p.y-2, height, height);
        }
        return null;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void drawX(Graphics2D g) {
        if (lastGraphics != null) {
            Double area = getXButtonArea(lastGraphics);
            g.drawString("X", (int)area.x, (int) (area.y + area.height));
        }
    }

    public RoundRectangle2D.Double getArea(FontMetrics fontMetrics) {
        int maxWidth = 0;
        for (InfoText text : texts) {
            int currWidth = fontMetrics.stringWidth(text.getString());
            if (currWidth > maxWidth) {
                maxWidth = currWidth;
            }
        }
        return new RoundRectangle2D.Double(p.x - 15, p.y - 5, maxWidth + 20,
                texts.length * fontMetrics.getHeight() + 15, 10, 10);
    }

    public Font getFont() {
        return new Font("Segoe UI", Font.PLAIN, 13);
    }

    public void render(Graphics2D g) {
        if (visible) {
            final Font font = getFont();
            final Color textColor = new Color(255, 250, 240);
            final Color hlColor = new Color(0, 50, 220);
            final Color bg = new Color(139, 71, 93, 80).darker();
            g.setFont(font);
            final FontMetrics fontMetrics = g.getFontMetrics();
            drawX(g);
            g.setColor(bg);
            g.fill(getArea(fontMetrics));
            g.setColor(textColor);
            int y = p.y;
            for (InfoText text : texts) {
                if (text.getExecutable() != null) {
                    g.setColor(hlColor);
                } else {
                    g.setColor(textColor);
                }
                g.drawString(text.getString(), p.x, y += fontMetrics.getHeight());
            }
        }
        lastGraphics = g;
    }

    public void onClick(MouseEvent e) {
        if (lastGraphics == null) {
            return;
        }
        if (getXButtonArea(lastGraphics).contains(e.getPoint())) {
            visible = false;
            return;
        }
        int y = p.y;
        FontMetrics fontMetrics = lastGraphics.getFontMetrics();
        int h = fontMetrics.getHeight();
        for (InfoText text : texts) {
            int width = fontMetrics.stringWidth(text.getString());
            Rectangle r = new Rectangle(p.x, y += h, width, h);
            if (r.contains(e.getPoint())) {
                System.out.println(text.toString() + ", " + r);
                Executable executable = text.getExecutable();
                if (executable != null) {
                    executable.execute();
                }
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public Graphics2D getLastGraphics() {
        return lastGraphics;
    }
}
