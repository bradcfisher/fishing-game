
package Fishing.screens;

import Fishing.drawable.controls.ListMenu;
import Fishing.drawable.dialogs.OptionsDialog;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import java.awt.event.KeyEvent;


/**
 *
 * @author Brad
 */
public class TitleScreen
    extends MenuScreen
{

    private static final int menuFontSize = 40;
    private static final int menuY = 450;

    private static final double titleTextProportion = 0.70;
    private static final int titleTextY = 80;


    public TitleScreen() {
        super( "assets/backgrounds/title.jpg", "assets/music/title.wav",
               "red", "Fishing\nGame",
               menuFontSize, menuY,
               titleTextProportion, titleTextY );

        setMouseChildren(true);
        setName("TitleScreen");

        // The menu
        final ListMenu menu = getMenu();
        menu.addItem("StartGame", "START GAME");
        menu.addItem("HighScores", "High Scores");
        menu.addItem("Options", "Options");
        menu.addItem("Exit", "Exit");

        menu.addMenuListener(
            new MenuAdapter() {
                @Override
                public void menuSelected(MenuEvent e) {
                    System.out.println("TitleScreen: menuSelected: "+ e);

                    if ("StartGame".equals(e.getMenuName())) {
                        getParent().getDrawable("selectDifficultyScreen").setVisible(true);
                    } else if ("HighScores".equals(e.getMenuName())) {
                        getParent().getDrawable("highScoreScreen").setVisible(true);
                    } else if ("Options".equals(e.getMenuName())) {
                        showOptionsDialog();
                    } else if ("Exit".equals(e.getMenuName())) {
                        System.exit(0);
                    }
                }
            }
        );

        getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (!"Exit".equals(menu.getActiveItemName()))
                            menu.setActiveItem("Exit");
                        else
                            menu.executeActiveItem();
                    }
                } // drawableKeyPressed(KeyEvent e)
            }
        );
    } // TitleScreen()

    public void showOptionsDialog() {
        OptionsDialog dlg = new OptionsDialog();
        dlg.show(this, true);
    } // showOptionsDialog()

} // class TitleScreen
