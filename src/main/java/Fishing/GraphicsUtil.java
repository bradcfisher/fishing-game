
package Fishing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility class containing methods for simplifying certain graphical rendering
 * tasks.
 * 
 * @author Brad
 */
public final class GraphicsUtil {

    /**
     * The stroke used for drawing a focus indicator.
     */
    public static final Stroke focusStroke = new BasicStroke(
        2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 5, 5 }, 0
    );
    
    /**
     * Prevent instantiation of this utility class.
     */
    private GraphicsUtil() {
    } // GraphicsUtil()

    /**
     * Draws a rounded rectangle with a two color shaded border to simulate a
     * 3D raised/lowered effect.
     * 
     * @param g         The graphics context to draw the rectangle into.
     * @param clr1      The color to use for the top-left border.
     * @param clr2      The color to use for the bottom-right border.
     * @param clrFill   The color to use for filling the interior of the
     *                  rectangle.  If {@code null}, the interior will not be
     *                  filled or drawn into.
     * @param rect      Rectangle to draw the rounded rectangle inside of.
     * @param cornerRadius  The corner radius to apply to the drawn rectangle.
     */
    public static void draw3DRoundRect(
                        Graphics2D g,
                        Color clr1, Color clr2,
                        Color clrFill,  // May be null
                        Rectangle2D rect,
                        double cornerRadius
                    )
    {
        Path2D.Double path = new Path2D.Double();

        double w = rect.getWidth();
        double h = rect.getHeight();

        AffineTransform m = g.getTransform();
        g.translate(rect.getX(), rect.getY());

        // Fill the background

        // The top-left portion
        path.moveTo(0, h - cornerRadius);
        path.lineTo(0, cornerRadius);
        path.quadTo(0, 0, cornerRadius, 0);
        path.lineTo(w - cornerRadius, 0);
        path.quadTo(w, 0, w, cornerRadius);

        // The bottom-right portion
        path.lineTo(w, h - cornerRadius);
        path.quadTo(w, h, w - cornerRadius, h);
        path.lineTo(cornerRadius, h);
        path.quadTo(0, h, 0, h - cornerRadius);

        path.closePath();

        if (clrFill != null) {
            g.setColor(clrFill);
            g.fill(path);
        }

        // Draw the top highlight
        path.reset();
        path.moveTo(0, h - cornerRadius);
        path.lineTo(0, cornerRadius);
        path.quadTo(0, 0, cornerRadius, 0);
        path.lineTo(w - cornerRadius, 0);
        path.quadTo(w, 0, w, cornerRadius);

        g.setColor(clr1);
        g.draw(path);

        // Draw the bottom shadow
        path.reset();
        path.moveTo(w, cornerRadius);
        path.lineTo(w, h - cornerRadius);
        path.quadTo(w, h, w - cornerRadius, h);
        path.lineTo(cornerRadius, h);
        path.quadTo(0, h, 0, h - cornerRadius);

        g.setColor(clr2);
        g.draw(path);

        g.setTransform(m);
    } // draw3DRoundRect(...)

    /**
     * Draws a rectangle for indicating an object is focused using a dashed
     * stroke.
     * 
     * @param   g       The graphics context to draw into.
     * @param   rect    The bounds of the rectangle to draw.
     */
    public static void drawFocusRect( Graphics2D g, Rectangle2D rect ) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(rect.getMinX(), rect.getMinY());
        path.lineTo(rect.getMaxX(), rect.getMinY());
        path.lineTo(rect.getMaxX(), rect.getMaxY());
        path.lineTo(rect.getMinX(), rect.getMaxY());
        path.closePath();

        g = (Graphics2D) g.create();
        g.setXORMode(Color.WHITE);
        g.setColor(Color.BLACK);
        g.setStroke(focusStroke);
        g.draw(path);
    } // drawFocusRect( Graphics2D g, Rect2D bounds )

} // class GraphicsUtil
