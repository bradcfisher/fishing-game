package Fishing.drawable.controls;

import Fishing.FishingOptions;
import Fishing.drawable.Drawable;
import Fishing.drawable.events.FishingOptionsAdapter;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.text.BitmapFont;
import Fishing.screens.Screen;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides "Expert" level controls
 * @author Brad
 */
public class FishTankControls
    extends Drawable
{

    private FishingOptions fishingOptions;

    private ImageSelector backgroundSelector;

    private ImageSelector musicSelector;

    private double padding = 5;

    private BitmapFont font;

    public FishTankControls( FishingOptions options, BitmapFont font ) {
        setMouseEnabled(true);
        setMouseChildren(true);

        fishingOptions = options;

        fishingOptions.addListener(new FishingOptionsAdapter() {
                @Override
                public void titleChanged( ValueChangedEvent e ) {
                    showForExpert();
                } // titleChanged( ValueChangedEvent e )

                @Override
                public void targetFishSpeciesChanged( ValueChangedEvent e ) {
                    invalidate();
                } // titleChanged( ValueChangedEvent e )
            });

        this.font = font;
    } // FishTankControls( FishingOptions options, BitmapFont font )


    private void showForExpert() {
        boolean show = ("Expert".equals(fishingOptions.getTitle()));

        if (backgroundSelector != null)
            backgroundSelector.setVisible(show);

        if (musicSelector != null)
            musicSelector.setVisible(show);
    } // showForExpert()


    @Override
    public void validate() {
        removeAllDrawables();

        // Create the selector for picking the background in expert mode.
        try {
            backgroundSelector = new ImageSelector( "assets/controls/expert_background_options.jpg", 3 );
        } catch (IOException ex) {
            Logger.getLogger(FishTankControls.class.getName()).log(Level.SEVERE, null, ex);
        }
        backgroundSelector.setMouseEnabled(true);
        backgroundSelector.setVisible(false);
        backgroundSelector.setFrameSize( 93, 70 );
        backgroundSelector.setPosition(padding, padding);
        backgroundSelector.addMenuListener( new MenuAdapter() {
                        @Override
                        public void menuSelected(MenuEvent e) {
                            getScreen().setBackground( e.getMenuIndex() );
                        } // menuSelected(MenuEvent e)
                 });
        addDrawable( backgroundSelector );

        // Create the selector for picking the background music in expert mode.
        try {
            musicSelector = new ImageSelector( "assets/controls/expert_music_options.jpg", 2 );
        } catch (IOException ex) {
            Logger.getLogger(FishTankControls.class.getName()).log(Level.SEVERE, null, ex);
        }
        musicSelector.setVisible(false);
        musicSelector.setMouseEnabled(true);
        musicSelector.setFrameSize( 70, 70 );
        musicSelector.setPosition(backgroundSelector.getWidth() + padding * 2, padding);
        musicSelector.addMenuListener( new MenuAdapter() {
                        @Override
                        public void menuSelected(MenuEvent e) {
                            getScreen().setBackgroundMusic( e.getMenuIndex() );
                        } // menuSelected(MenuEvent e)
                 });
        addDrawable( musicSelector );    

        // Build the display of the fish we're capturing
        FishTable table = new FishTable( fishingOptions, font, true );
        table.setSpecies( fishingOptions.getTargetFishSpecies() );
        addDrawable(table);

        // For Expert, show the background and music selectors
        showForExpert();

        double height = (backgroundSelector.isVisible()
                            ? backgroundSelector.getHeight()
                            : musicSelector.isVisible()
                                ? musicSelector.getHeight()
                                : table.getHeight()) + padding * 2 ;

        // Size and position
        setUnscaledSize( getParent().getUnscaledWidth(), height );
        setPosition(0, getParent().getUnscaledHeight() - getHeight() + 1);

        table.setPosition( getUnscaledWidth() - table.getWidth(), (height - table.getHeight()) / 2 );
    } // validate()

    public Screen getScreen() {
        Drawable p = getParent();
        while ((p != null) && !(p instanceof Screen))
            p = p.getParent();
        return (Screen) p;
    } // getScreen()


    @Override
    public void paint(Graphics2D g) {
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();

        g.setColor(Color.DARK_GRAY);
        g.fill(new Rectangle2D.Double(0, 0, w, h));

        float stroke = 2;
        g.setStroke( new BasicStroke(stroke) );
        g.setColor(Color.WHITE);
        g.draw(new Line2D.Double(0, stroke / 2, w, stroke / 2));
    } // paint(Graphics2D g)

} // class FishTankControls
