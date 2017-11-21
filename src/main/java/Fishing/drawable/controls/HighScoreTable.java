
package Fishing.drawable.controls;

import com.jhlabs.image.*;
import Fishing.HighScore;
import Fishing.HighScoreManager;
import Fishing.drawable.Drawable;
import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.TimerListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Brad
 */
public class HighScoreTable
    extends Drawable
    implements TimerListener
{

    private static final int rowSpacing = 10;
    private static final int columnSpacing = 20;
    private static final int horizontalPadding = 15;
    private static final int verticalPadding = 10;

    private GeneratedFont titleFont;
    private GeneratedFont rowFont;

    private BitmapText title;
    private Drawable headerRow;
    private Drawable scoreRows[];

    private String type;
    private float fadeInSpeed = 2.0f / 255;

    private boolean needsLayout = true;


    public HighScoreTable( String type, int fontSize ) {
        titleFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, fontSize * 2 ) );
        titleFont.setColor( Color.WHITE );
        titleFont.setOutlineColor( Color.BLACK );
        titleFont.setOutlineWidth( fontSize * 0.15f );

        rowFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, fontSize ) );
        rowFont.setColor( Color.WHITE );

        super.setVisible(false);
        
        this.type = type;

        layout();
    } // HighScoreTable()


    public String getType() {
        return type;
    } // getType()


    public void setType( String type ) {
        this.type = type;
        needsLayout = true;
    } // setType( String type )


    public String getTitle() {
        if (title != null)
            return title.getText();
        return null;
    } // getTitle()


    public void setTitle(String value) {
        needsLayout = true;

        if (value == null) {
            removeDrawable(title);
            title = null;
            return;
        }

        if (title == null) {
            title = new BitmapText(titleFont);
            addDrawable(title);
        }

        title.setText(value);
    } // setTitle(String value)


    @Override
    public void setVisible( boolean value ) {
        if (value == isVisible())
            return;

        super.setVisible(value);

        // Remove all drawable children
        if (!value)
            this.removeAllDrawables();

        needsLayout = true;
    } // setVisible( boolean value )


    private synchronized void layout() {
        if (!needsLayout)
            return;

        needsLayout = false;

        List<HighScore> scores = HighScoreManager.getHighScores(type);

        setOpacity(0);

        // Determine the width
        double rankWidth = rowFont.getTextSize("RANK").getWidth();
        double initialsWidth = rowFont.getTextSize("INITIALS").getWidth();
        double scoreWidth = rowFont.getTextSize("SCORE").getWidth();

        for (HighScore s : scores) {
            if (s == null)
                continue;

            double w;

            w = rowFont.getTextSize(""+s.getInitials()).getWidth();
            if (w > initialsWidth)
                initialsWidth = w;

            w = rowFont.getTextSize(""+s.getScore()).getWidth();
            if (w > scoreWidth)
                scoreWidth = w;
        } // for

        rankWidth += columnSpacing;
        initialsWidth += columnSpacing;
        int rowWidth = (int)(rankWidth + initialsWidth + scoreWidth);

        int y = verticalPadding / 2;

        if (title != null) {
            title.setX( (getUnscaledWidth() - title.getWidth()) / 2 );
            addDrawable(title);
            y += title.getHeight() + 5;
        }

        // Create header row
        headerRow = createRow(horizontalPadding / 2, y, (int)rankWidth, rowWidth, "RANK", "INITIALS", "SCORE");
        MaskFilter f = new MaskFilter();
        f.setMask(0xffffff00);
        headerRow.addFilter( f );
        addDrawable(headerRow);

        scoreRows = new Drawable[scores.size()];

        int i = 0;
        for (HighScore s : scores) {
            y += rowFont.getFontSize() + rowSpacing;

            scoreRows[i] = createRow(
                                    horizontalPadding / 2,
                                    y,
                                    (int)rankWidth,
                                    rowWidth,
                                    ""+ (i + 1),
                                    ""+ (s != null ? s.getInitials() : "???"),
                                    ""+ (s != null ? s.getScore() : 0)
                                );

            addDrawable(scoreRows[i]);

            ++i;
        } // for

        setUnscaledSize(
                    rowWidth + horizontalPadding,
                    y + rowFont.getFontSize() + verticalPadding
                );
    } // layout()


    private Drawable createRow( int x, int y, int rankWidth, int rowWidth, String rank, String initials, String score ) {
        Drawable rv = new Drawable();

        rv.setPosition( x, y );
        rv.setUnscaledSize( rowWidth, rowFont.getFontSize() );

        rv.setOpacity(0);

        // Create the Rank text item
        BitmapText txt = new BitmapText(rowFont);
        txt.setText(rank);
        rv.addDrawable(txt);

        // Create the Initials text item
        txt = new BitmapText(rowFont);
        txt.setText(initials);
        txt.setX(rankWidth);
        rv.addDrawable(txt);

        // Create the Score text item
        txt = new BitmapText(rowFont);
        txt.setText(score);
        txt.setX( rowWidth - txt.getWidth() );
        rv.addDrawable(txt);

        rv.setVisible(false);

        return rv;
    } // createRow( int x, int y, int width, String rank, String initials, String score )


    public float getFadeInSpeed() {
        return fadeInSpeed;
    } // getFadeInSpeed()


    public void setFadeInSpeed(float value) {
        fadeInSpeed = value;
    } // setFadeInSpeed()


    private boolean updateOpacity(Drawable obj) {
        if (obj == null)
            return false;

        if (!obj.isVisible())
            obj.setVisible(true);

        if (obj.getOpacity() == 1)
            return false;

        obj.setOpacity( Math.min(obj.getOpacity() + fadeInSpeed, 1.0f) );

        return true;
    } // updateOpacity(Drawable obj)


    @Override
    public synchronized void timerFired(TimerEvent e) {
        if (scoreRows == null)
            return;

        if (needsLayout)
            layout();

        // Update the header row opacity
        if (!updateOpacity(this) && !updateOpacity(headerRow)) {
            for (int i = 0; i < scoreRows.length; ++i) {
                if (updateOpacity(scoreRows[i]))
                    break;
            } // for
        }
    } // timerFired(TimerEvent e)


    @Override
    public void paint( Graphics2D g ) {
        if (needsLayout) layout();

        int w = (int) getUnscaledWidth();
        int h = (int) getUnscaledHeight();

        // Draw the table background
        int y = 0;

        if (title != null)
            y += title.getHeight() + 5;

        g.setColor( new Color(0xA0606060, true) );

        g.fillRoundRect(2, y, w - 4, h - 2 - y, 20, 20);

        g.setColor( new Color(0xA0FFFFFF, true) );
        g.setStroke( new BasicStroke( 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL ) );
        g.drawRoundRect(2, y, w - 4, h - y - 2, 20, 20);

        y += rowFont.getFontSize() + (verticalPadding + rowSpacing) / 2;
        g.drawLine(0, y, w, y);
    } // paint( Graphics2D g )


} // class HighScoreTable
