
package Fishing.screens;

import com.jhlabs.image.SwimFilter;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.controls.ListMenu;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.SoundManager;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.TitleFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 *
 * @author Brad
 */
public class MenuScreen
    extends Screen
    implements MenuListener
{

    private TitleFont font;
    private BitmapFont menuFont;
    private BitmapText titleText;
    private SwimFilter filter;
    private ListMenu menu;

    private int menuY;
    private double titleTextProportion;

    public MenuScreen( String backgroundImage, String backgroundMusic,
                    String titleFontStyle, String title,
                    int menuFontSize, int menuY,
                    double titleTextProportion, int titleTextY
                )
    {
        super( backgroundImage, backgroundMusic );

        this.menuY = menuY;
        this.titleTextProportion = titleTextProportion;

        // Load title font
        font = new TitleFont(titleFontStyle);
        font.setCharacterSpacing(15);
        font.setLineSpacing(12);

        // Create the title text
        titleText = new BitmapText(font, title);
        titleText.setY(titleTextY);
        titleText.setAlignment(BitmapFont.ALIGN_CENTER);

        // Filter for the title text
        filter = new SwimFilter();
        filter.setAmount(3);
        filter.setScale(30f);

        titleText.addFilter( filter );
        addDrawable(titleText);

        // Load the sound effect for the mouse-overs
        try {
            SoundManager.loadSoundEffect("hover", "assets/sfx/hover.wav" );
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        // The menu fonts
        menuFont = new GeneratedFont( new Font( "Arial Bold", Font.PLAIN, menuFontSize), Color.RED );

        // The menu
        menu = new ListMenu( menuFont );
        menu.addMenuListener(this);
        addDrawable(menu);
        setFocusObject(menu);

        // The screen is initially hidden
        // This is primarily so the background music is started when it's displayed.
        setVisible(false);
    } // TitleScreen()


    public BitmapText getTitleText() {
        return titleText;
    } // getTitleText()


    public ListMenu getMenu() {
        return menu;
    } // getMenu()


/*
public void addMenuListener( MenuListener listener ) {
        menu.addMenuListener(listener);
    } // addMenuListener( MenuListener listener )


    public void removeMenuListener( MenuListener listener ) {
        menu.removeMenuListener(listener);
    } // removeMenuListener( MenuListener listener )
*/

    @Override
    public void validate() {
        int w = (int) getUnscaledWidth();

        // Position and scale the title text
        double titleScale = (w * titleTextProportion) / titleText.getUnscaledWidth();
        titleText.setScale(titleScale, titleScale);
        titleText.setX( (int)((w - titleText.getWidth()) / 2) );

        // Position the menu
        menu.setX( (int)((w - menu.getWidth()) / 2) );
        menu.setY( menuY );
    } // validate()


    @Override
    public void paint( Graphics2D g ) {
        filter.setTime( filter.getTime() + 0.05f );

        super.paint(g);
    } // paint( Graphics2D g ) {


    @Override
    public void menuSelected(MenuEvent e) { }


    @Override
    public void activeMenuChanged(MenuEvent e) {
        if (e.getMenuIndex() != -1)
            SoundManager.playSoundEffect("hover");
    } // activeMenuChanged(MenuEvent e)


} // class MenuScreen
