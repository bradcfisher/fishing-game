
package Fishing.drawable.dialogs;

import com.jhlabs.image.SwimFilter;
import Fishing.drawable.controls.ButtonMenu;
import Fishing.drawable.controls.TextInput;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.TitleFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Brad
 */
public class HighScoreDialog
    extends Dialog
{

    private SwimFilter titleFilter;
    private ButtonMenu buttons;

    private double padding = 20;

    public HighScoreDialog( int rank, long score ) {
        TitleFont titleFont = new TitleFont("yellow");
        titleFont.setCharacterSpacing(15);
        titleFont.setLineSpacing(12);

        double height = padding;

        // Filter for the title text
        titleFilter = new SwimFilter();
        titleFilter.setAmount(3);
        titleFilter.setScale(30f);

        // Create the title text
        BitmapText title = new BitmapText(titleFont, "New High Score");
        title.addFilter( titleFilter );
        title.setY(height);
        addDrawable(title);

        double interLineSpacing = titleFont.getFontSize() / 2;
        double maxWidth = title.getWidth();
        height += title.getHeight() + interLineSpacing;

        GeneratedFont textFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 28), Color.WHITE );
        GeneratedFont inputFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 28), Color.BLACK );

        // Create the score report text
        BitmapText scoreReportText = new BitmapText(textFont, "Your score was "+ score +"!");
        scoreReportText.setY( height );
        addDrawable(scoreReportText);

        maxWidth = Math.max( maxWidth, scoreReportText.getWidth() );
        height += scoreReportText.getHeight() + interLineSpacing;

        BitmapText textLine1 = new BitmapText(textFont, "Congratulations!  Your score qualifies you");
        textLine1.setY( height );
        addDrawable(textLine1);

        maxWidth = Math.max( maxWidth, textLine1.getWidth() );
        height += textLine1.getHeight() + interLineSpacing;

        String place = ""+ rank;
        switch ((int)((double)rank % 10)) {
            case 1:
                place += "st";
                break;
            case 2:
                place += "nd";
                break;
            case 3:
                place += "rd";
                break;
            default:
                place += "th";
        } // switch

        BitmapText textLine2 = new BitmapText(textFont, "for "+ place +" place in the top score standings!");
        textLine2.setY( height );
        addDrawable(textLine2);

        maxWidth = Math.max( maxWidth, textLine2.getWidth() );
        height += textLine2.getHeight() + interLineSpacing;

        // Add the text input asking for initials
        double xPos = padding;

        BitmapText textLine3 = new BitmapText(textFont, "Enter your initials: ");
        textLine3.setX(xPos);
        addDrawable(textLine3);

        xPos += textLine3.getWidth();

        TextInput initialsInput = new TextInput( inputFont );
        initialsInput.setName("Initials");
        initialsInput.setPosition(xPos, height);
        initialsInput.setMaxChars( 5 );
        addDrawable(initialsInput);

        initialsInput.addKeyListener(new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // Dispatch the event onto the menu
                        buttons.setActiveItem("Record");
                        buttons.executeActiveItem();
                    }
                } // drawableKeyPressed( DrawableKeyEvent e )
            });

        textLine3.setY( height + (initialsInput.getHeight() - textLine3.getHeight()) / 2 );

        maxWidth = Math.max( maxWidth, xPos + initialsInput.getWidth() + padding );
        height += textLine2.getHeight() + interLineSpacing;

        // Add the action buttons
        buttons = new ButtonMenu(textFont);
        buttons.setButtonSpacing(interLineSpacing);
        buttons.addItem("Record", "Record");
        buttons.addItem("Skip", "Skip");
        buttons.setY(height);
        addDrawable(buttons);

        maxWidth = Math.max(buttons.getWidth(), maxWidth);
        height += buttons.getHeight();

        // Set the final size of the Dialog
        setUnscaledSize(
                    padding * 2 + maxWidth,
                    padding + height
                );

        // Reposition elements as needed
        title.setX( (getUnscaledWidth() - title.getWidth()) / 2 );
        scoreReportText.setX( (getUnscaledWidth() - scoreReportText.getWidth()) / 2 );
        textLine1.setX( (getUnscaledWidth() - textLine1.getWidth()) / 2 );
        textLine2.setX( (getUnscaledWidth() - textLine2.getWidth()) / 2 );

        buttons.setX( (getUnscaledWidth() - buttons.getWidth()) / 2 );

        setMouseChildren(true);

        buttons.addMenuListener(new MenuAdapter() {
                @Override
                public void menuSelected( MenuEvent e ) {
                    if (e.getMenuName().equals("Record")) {
                        // Don't allow an empty name to be recorded.
                        if (getInitials().length() == 0) {
                            e.stopImmediatePropagation();

                            MsgDialog.show(getParent(),
                                        "Missing Initials",
                                          "Please enter you initials, or press 'Skip' to\n"
                                        + "continue without recording your score.");
                        }
                    }
                } // menuSelected( MenuEvent e )
            });

        addKeyListener(new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        e.stopPropagation();
                        buttons.setActiveItem("Skip");
                        buttons.executeActiveItem();
                    }
                } // drawableKeyPressed(DrawableKeyEvent e)
            });
    } // HighScoreDialog( int rank, long score )


    public String getInitials() {
        return ((TextInput)getDrawable("Initials")).getText();
    } // getInitials()


    public synchronized void addMenuListener( MenuListener listener ) {
        buttons.addMenuListener(listener);
    } // addMenuListener( MenuListener listener )


    public synchronized void removeMenuListener( MenuListener listener ) {
        buttons.removeMenuListener(listener);
    } // removeMenuListener( MenuListener listener )


    @Override
    public void paint( Graphics2D g ) {
        titleFilter.setTime( titleFilter.getTime() + 0.05f );
        super.paint(g);
    } // paint( Graphics2D g )


} // class HighScoreDialog
