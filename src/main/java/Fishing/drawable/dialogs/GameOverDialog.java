
package Fishing.drawable.dialogs;

import com.jhlabs.image.SwimFilter;
import Fishing.FishingOptions;
import Fishing.drawable.controls.ButtonMenu;
import Fishing.drawable.controls.FishTable;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.TitleFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Dialog displayed after playing the game, if the player did not achieve a
 * new high score.
 * 
 * @author Brad
 */
public class GameOverDialog
    extends Dialog
{

    private SwimFilter titleFilter;

    private ButtonMenu buttons;

    private FishingOptions fishingOptions;

    private double padding = 20;

    public GameOverDialog(FishingOptions options) {
        TitleFont titleFont = new TitleFont("yellow");
        titleFont.setCharacterSpacing(15);
        titleFont.setLineSpacing(12);

        fishingOptions = options;

        double height = padding;

        // Filter for the title text
        titleFilter = new SwimFilter();
        titleFilter.setAmount(3);
        titleFilter.setScale(30f);

        // Create the title text
        BitmapText title = new BitmapText(titleFont, "Game Over");
        title.addFilter( titleFilter );
        title.setY(height);
        addDrawable(title);

        double interLineSpacing = titleFont.getFontSize() / 2;
        double maxWidth = title.getWidth();
        height += title.getHeight() + interLineSpacing;

        GeneratedFont textFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 28), Color.WHITE );

        // Create the score report text
        BitmapText scoreReportText = new BitmapText(textFont, "Your score was "+ fishingOptions.getFinalScore() +"!");
        scoreReportText.setY( height );
        addDrawable(scoreReportText);

        maxWidth = Math.max( maxWidth, scoreReportText.getWidth() );
        height += scoreReportText.getHeight() + interLineSpacing;

        // Create the fish stats text
        BitmapText fishStatsText = new BitmapText(textFont, "Fish Captured:");
        fishStatsText.setPosition( padding, height );
        addDrawable(fishStatsText);

        maxWidth = Math.max( maxWidth, fishStatsText.getWidth() );
        height += fishStatsText.getHeight() + interLineSpacing / 2;

        FishTable table = new FishTable(fishingOptions, textFont, false);
        table.setY(height);
        addDrawable(table);

        maxWidth = Math.max( maxWidth, table.getWidth() );
        height += table.getHeight() + interLineSpacing;

        // Add the action buttons
        buttons = new ButtonMenu(textFont);
        buttons.setButtonSpacing(interLineSpacing);
        buttons.addItem("PlayAgain", "Play Again");
        buttons.addItem("Quit", "Quit");
        buttons.setY(height);
        addDrawable(buttons);

        maxWidth = Math.max(buttons.getWidth(), maxWidth);
        height += buttons.getHeight();

        // Set the final size of the Dialog
        maxWidth += padding * 2;
        height += padding;
        setUnscaledSize( maxWidth, height );

        // Reposition elements as needed
        title.setX( (getUnscaledWidth() - title.getWidth()) / 2 );
        scoreReportText.setX( (getUnscaledWidth() - scoreReportText.getWidth()) / 2 );

        double xPos = (getUnscaledWidth() - buttons.getWidth()) / 2;
        buttons.setX( xPos );

        table.setX( (maxWidth - table.getWidth()) / 2 );

        // Add key listener to execute "Quit" action when escape key is pressed

        addKeyListener(new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        e.stopPropagation();
                        buttons.setActiveItem("Quit");
                        buttons.executeActiveItem();
                    }
                } // drawableKeyPressed(DrawableKeyEvent e)
            });
    } // GameOverDialog()


    public synchronized void addMenuListener( MenuListener listener ) {
        buttons.addMenuListener(listener);
    } // addMenuListener( MenuListener listener )


    public synchronized void removeMenuListener( MenuListener listener ) {
        buttons.addMenuListener(listener);
    } // removeMenuListener( MenuListener listener )


    @Override
    public void paint( Graphics2D g ) {
        titleFilter.setTime( titleFilter.getTime() + 0.05f );
        super.paint(g);
    } // paint( Graphics2D g )


} // class GameOverDialog
