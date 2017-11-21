
package Fishing.drawable.dialogs;

import com.jhlabs.image.SwimFilter;
import Fishing.drawable.Drawable;
import Fishing.drawable.controls.ButtonMenu;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.text.TitleFont;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Brad
 */
public class PauseDialog
    extends MsgDialog
{

    private SwimFilter titleFilter;


    public PauseDialog() {
        super("Paused", "Click 'Continue' or press the space bar to return to the game.");

        // Switch the button
        ButtonMenu buttons = getButtons();
        buttons.removeAllDrawables();
        buttons.addItem("Continue", "Continue");

        // Set the new title font
        TitleFont titleFont = new TitleFont("green");
        titleFont.setCharacterSpacing(15);
        titleFont.setLineSpacing(12);
        setTitleFont( titleFont );

        // Add the swim filter to the title
        titleFilter = new SwimFilter();
        titleFilter.setAmount(3);
        titleFilter.setScale(30f);
        getTitleControl().addFilter(titleFilter);

        addKeyListener(new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_SPACE:
                        case KeyEvent.VK_ESCAPE:
                            e.consume();
                            getButtons().setActiveItem("Continue");
                            getButtons().executeActiveItem();
                            break;
                    } // switch
                } // drawableKeyPressed(DrawableKeyEvent e)
            });
    } // PauseDialog()


    @Override
    public void paint(Graphics2D g) {
        titleFilter.setTime( titleFilter.getTime() + 0.05f );
        super.paint(g);
    } // paint(Graphics2D g)


    public static PauseDialog show( Drawable root ) {
        PauseDialog dlg = new PauseDialog();
        dlg.show(root, true);
        return dlg;
    } // show( Drawable root )


} // class PauseDialog
