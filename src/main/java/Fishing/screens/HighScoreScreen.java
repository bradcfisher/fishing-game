
package Fishing.screens;

import Fishing.drawable.controls.ControlTimer;
import Fishing.drawable.controls.HighScoreTable;
import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.TimerListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Brad
 */
public class HighScoreScreen
    extends Screen
    implements TimerListener
{

    private BitmapText title;

    private HighScoreTable[] highScoreTable;

    public HighScoreScreen() {
        super( "assets/backgrounds/highScores.jpg", "assets/music/highScores.wav" );
        setVisible(false);

        // Title
        GeneratedFont titleFont = new GeneratedFont( new Font(/*"Arial Bold"*/ "Segoe Print", Font.BOLD, 100) );
        titleFont.setColor( new Color(0x00ffff) );
        titleFont.setOutlineColor( new Color(0xffffff) );
        titleFont.setOutlineWidth( 20 );

        title = new BitmapText(titleFont, "High Scores");
        title.setPosition( (getUnscaledWidth() - title.getWidth()) / 2, 75 );
        addDrawable(title);

        // High score table titles
        titleFont = new GeneratedFont( new Font(/*"Arial Bold"*/ "Segoe Print", Font.BOLD, 45) );
        titleFont.setColor( new Color(0x00ffff) );
        
        // High score tables
        highScoreTable = new HighScoreTable[3];

        int fontSize = 28;
        int y = 260;

        // Center table
        HighScoreTable hst = new HighScoreTable("Intermediate", fontSize);
        hst.setTitle("Intermediate");
        hst.setVisible(false);
        hst.setPosition( (getUnscaledWidth() - hst.getWidth()) / 2, y );
        addDrawable(hst);
        highScoreTable[0] = hst;

        // Left table
        hst = new HighScoreTable("Expert", fontSize);
        hst.setTitle("Expert");
        hst.setVisible(false);
        hst.setPosition( (highScoreTable[0].getX() - hst.getWidth()) / 2, y );
        addDrawable(hst);
        highScoreTable[1] = hst;

        // Right table
        hst = new HighScoreTable("Beginner", fontSize);
        hst.setTitle("Beginner");
        hst.setVisible(false);
        double x = highScoreTable[0].getX() + highScoreTable[0].getWidth();
        hst.setPosition( x + (getUnscaledWidth() - x - hst.getWidth()) / 2, y );
        addDrawable(hst);
        highScoreTable[2] = hst;

        setMouseEnabled(true);
        setFocusable(true);
        setFocusObject(this);

        ControlTimer.scheduleAtFixedRate(this, 1000 / 30);
    } // HighScoreScreen()


    private synchronized void resetOpacity() {
        title.setOpacity(0);

        for (int i = 0; i < highScoreTable.length; ++i)
            highScoreTable[i].setVisible(false);
    } // resetOpacity()


    @Override
    public synchronized void setVisible( boolean value ) {
        if (value == isVisible())
            return;

        // Reset opacity if needed
        if (value)
            resetOpacity();

        super.setVisible(value);
    } // setVisible( boolean value )


    public synchronized void timerFired(TimerEvent e) {
        if ((highScoreTable == null) || (highScoreTable[0] == null))
            return;
            
        if (!isVisible())
            return;

        // Fade in the title
        if (title.getOpacity() < 1) {
            float speed = 2.0f / 255;
            title.setOpacity(Math.min(title.getOpacity() + speed, 1.0f));
        }

        // Fading in the highScoreTable
        for (int i = 0; i < highScoreTable.length; ++i) {
            if (highScoreTable[i].getOpacity() == 1.0f)
                highScoreTable[i].timerFired(e);
            highScoreTable[i].timerFired(e);
        }
    } // timerFired(TimerEvent e)


} // class HighScoreScreen
