/*
 * FishingView.java
 */

package Fishing;

import Fishing.drawable.DrawablePanel;
import Fishing.drawable.Stage;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.screens.FishTankScreen;
import Fishing.screens.TitleScreen;
import Fishing.screens.HighScoreScreen;
import Fishing.screens.SelectDifficultyScreen;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class FishingView extends FrameView {

    /**
     * Constructs a new instance of this view.
     * 
     * @param app
     * 
     * @throws IOException if a require resource could not be loaded.
     */
    public FishingView(SingleFrameApplication app)
        throws IOException
    {
        super(app);

        initComponents();

        displayPanel = new DrawablePanel(30);
        //displayPanel.setShowFrameRate(true);
        displayPanelContainer.add(displayPanel);

        displayPanel.requestFocusInWindow();

        //Make textField get the focus whenever frame is activated.
        getFrame().addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                displayPanel.requestFocusInWindow();
            }
        });

        // The object containing options for the game
        fishingOptions = new FishingOptions();

        Stage stage = displayPanel.getStage();
                
        // Create the title screen
        titleScreen = new TitleScreen();
        titleScreen.setName("titleScreen");
        stage.addDrawable(titleScreen);

        // Create the select difficulty screen
        selectDifficultyScreen = new SelectDifficultyScreen( fishingOptions );
        selectDifficultyScreen.setName("selectDifficultyScreen");
        stage.addDrawable(selectDifficultyScreen);

        // Create the High Scores screen
        highScoreScreen = new HighScoreScreen();
        highScoreScreen.setName("highScoreScreen");
        stage.addDrawable(highScoreScreen);

        highScoreScreen.addMouseListener(new DrawableMouseAdapter() {
                        @Override
                        public void drawableMouseClicked( DrawableMouseEvent e ) {
                            titleScreen.setVisible(true);
                        } // drawableMouseClicked( DrawableMouseEvent e )
                    });
        highScoreScreen.addKeyListener(new DrawableKeyAdapter() {
            @Override
            public void drawableKeyPressed(DrawableKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    titleScreen.setVisible(true);
            }
        });

        // Create the fish tank screen
        fishTankScreen = new FishTankScreen( fishingOptions );
        fishTankScreen.setClipChildren(true);
        fishTankScreen.setName("fishTankScreen");
        stage.addDrawable(fishTankScreen);
        
        // Handle the Escape key pressed on various screens
        fishTankScreen.addKeyListener(new DrawableKeyAdapter() {
            @Override
            public void drawableKeyPressed(DrawableKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    titleScreen.setVisible(true);
            } // drawableKeyPressed(KeyEvent e)
        });


        resetSize();

        // Start out with the title screen
        titleScreen.setVisible(true);
    }


    private void resetSize() {
        getFrame().setSize((int)titleScreen.getUnscaledWidth(), (int)titleScreen.getUnscaledHeight() + menuBar.getHeight());
    } // resetSize()

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = FishingApp.getApplication().getMainFrame();
            aboutBox = new FishingAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        FishingApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        displayPanelContainer = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        displayPanelContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        displayPanelContainer.setName("displayPanelContainer"); // NOI18N
        displayPanelContainer.setLayout(new javax.swing.BoxLayout(displayPanelContainer, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(Fishing.FishingApp.class).getContext().getResourceMap(FishingView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(Fishing.FishingApp.class).getContext().getActionMap(FishingView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        resetSize();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel displayPanelContainer;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;

    private DrawablePanel displayPanel;

    private TitleScreen titleScreen;
    private SelectDifficultyScreen selectDifficultyScreen;
    private HighScoreScreen highScoreScreen;
    private FishingOptions fishingOptions;
    private FishTankScreen fishTankScreen;

}