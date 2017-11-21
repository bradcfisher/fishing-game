
package Fishing.screens;

import Fishing.FishingOptions;
import Fishing.drawable.controls.ListMenu;
import Fishing.drawable.dialogs.UserOptionsDialog;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author Brad
 */
public class SelectDifficultyScreen
    extends MenuScreen
{

    private static final int menuFontSize = 60;
    private static final int menuY = 350;

    private static final double titleTextProportion = 0.35;  //0.50;
    private static final int titleTextY = 100;

    private FishingOptions fishingOptions;
    private ListMenu menu;

    public SelectDifficultyScreen( FishingOptions options ) {
        super( "assets/backgrounds/selectLevel.jpg", "assets/music/selectLevel.wav",
               "red",
               "Select\nLevel",
               //"Select\nDifficulty",
               menuFontSize, menuY,
               titleTextProportion, titleTextY );

        fishingOptions = options;

        // The menu
        menu = getMenu();
        menu.addItem("Beginner", "Beginner");
        menu.addItem("Intermediate", "Intermediate");
        menu.addItem("Expert", "Expert");

        // Handler to display the user options dialog after an item is selected
        menu.addMenuListener(
            new MenuAdapter() {
                @Override
                public void menuSelected( MenuEvent e ) {
                    showUserOptions(e.getMenuName());
                } // menuSelected( MenuEvent e )
            }
        );

        getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        getParent().getDrawable("titleScreen").setVisible(true);
                    }
                } // drawableKeyPressed(DrawableKeyEvent e)
            }
        );

        setMouseChildren(true);
    } // SelectDifficultyScreen()


    private void showUserOptions( String type ) {
        getTitleText().setVisible(false);
        getMenu().setVisible(false);

        final Screen fishTankScreen = (Screen)getParent().getDrawable("fishTankScreen");
        initGame(type, fishTankScreen);

        final UserOptionsDialog dlg = new UserOptionsDialog(fishingOptions);

        // Add event handlers
        final SelectDifficultyScreen self = this;
        dlg.getButtons().addMenuListener(new MenuAdapter() {
                @Override
                public void menuSelected( MenuEvent e ) {
                    // Reset screen back to original layout
                    dlg.getParent().removeDrawable(dlg);
                    getTitleText().setVisible(true);
                    getMenu().setVisible(true);

                    // Handle the button
                    if ("Play".equals(e.getMenuName())) {
                        fishTankScreen.setVisible(true);
                    } else
                        self.setFocus();
                } // menuSelected( MenuEvent e )
            });
        dlg.show(this, true);
    } // showUserOptions()


    private void initGame( String mode, Screen fishTankScreen ) {
        fishTankScreen.clearRegisteredBackgrounds();
        fishTankScreen.clearRegisteredBackgroundMusic();

        fishingOptions.setTitle(mode);
        fishingOptions.disableAllFishSpecies();
        fishingOptions.removeAllTargetFishSpecies();

        fishingOptions.setMinFishSpeed(0.1);
        fishingOptions.setMaxFishSpeed(1000);

        if ("Beginner".equals(mode)) {
            fishingOptions.setTimeLimit(30);
            fishingOptions.setMaxFish( 5 );
            fishingOptions.setNewFishProbability( 0.05 );
            fishingOptions.setMinFishSpeed(2);
            fishingOptions.setMaxFishSpeed(3);

            fishingOptions.enableFishSpecies("discus",      1,  100);
            fishingOptions.addTargetFishSpecies("discus");

            fishTankScreen.registerBackground( "assets/backgrounds/beginner.jpg" );
            fishTankScreen.registerBackgroundMusic( "assets/music/beginner.wav" );
        } else if ("Intermediate".equals(mode)) {
            fishingOptions.setTimeLimit(45);
            fishingOptions.setMaxFish( 10 );
            fishingOptions.setNewFishProbability( 0.05 );
            fishingOptions.setMinFishSpeed(4);
            fishingOptions.setMaxFishSpeed(7);

            fishingOptions.enableFishSpecies("angel",       5,  70);
            fishingOptions.enableFishSpecies("cod",        15,  30);
            fishingOptions.enableFishSpecies("tigerbarb", 100,   3);
            fishingOptions.addTargetFishSpecies("cod");

            fishTankScreen.registerBackground( "assets/backgrounds/intermediate.jpg" );
            fishTankScreen.registerBackgroundMusic( "assets/music/intermediate.wav" );
        } else if ("Expert".equals(mode)) {
            fishingOptions.setTimeLimit(60);
            fishingOptions.setMaxFish( 20 );
            fishingOptions.setMinFishSpeed(6);
            fishingOptions.setMaxFishSpeed(10);
            fishingOptions.setNewFishProbability( 0.5 );

            fishingOptions.disableAllFishSpecies();
            fishingOptions.enableFishSpecies("discus",      1,  50);
            fishingOptions.enableFishSpecies("clown",       5,  80);
            fishingOptions.enableFishSpecies("angel",      10, 100);
            fishingOptions.enableFishSpecies("guppy",      40, 100);
            fishingOptions.enableFishSpecies("tetra",      75,  80);
            fishingOptions.enableFishSpecies("tigerbarb", 100,  80);
            fishingOptions.enableFishSpecies("zebra",     200,  50);
            fishingOptions.enableFishSpecies("cod",        50,  10);
            fishingOptions.enableFishSpecies("shark",    1000,   1);

            fishingOptions.addTargetFishSpecies("guppy");
            fishingOptions.addTargetFishSpecies("clown");
            fishingOptions.addTargetFishSpecies("angel");

            fishTankScreen.registerBackground( "assets/backgrounds/expert.jpg" );
            fishTankScreen.registerBackground( "assets/backgrounds/expert2.jpg" );
            fishTankScreen.registerBackground( "assets/backgrounds/intermediate.jpg" );

            fishTankScreen.registerBackgroundMusic( "assets/music/expert.wav" );
            fishTankScreen.registerBackgroundMusic( "assets/music/expert2.wav" );
        }

        fishingOptions.resetStatistics();
        fishTankScreen.setBackground( 0 );
        fishTankScreen.setBackgroundMusic( 0 );
    } // initGame( String mode )


} // class SelectDifficultyScreen
