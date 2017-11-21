
package Fishing.screens;

import com.jhlabs.image.MaskFilter;
import com.jhlabs.image.OpacityFilter;
import Fishing.FishingOptions;
import Fishing.HighScoreManager;
import Fishing.SoundManager;
import Fishing.drawable.dialogs.GameOverDialog;
import Fishing.drawable.Drawable;
import Fishing.drawable.controls.ControlTimer;
import Fishing.drawable.controls.Fish;
import Fishing.drawable.controls.FishTankControls;
import Fishing.drawable.controls.ScoreDisplay;
import Fishing.drawable.dialogs.HighScoreDialog;
import Fishing.drawable.dialogs.PauseDialog;
import Fishing.drawable.events.DrawableAdapter;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.DrawableTreeAdapter;
import Fishing.drawable.events.FishingOptionsAdapter;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.TimerListener;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Brad
 */
public class FishTankScreen
    extends Screen
    implements TimerListener
{

    private int animationTaskHandle = 0;

    private ScoreDisplay scoreDisplay;

    private BitmapText title;
    private BitmapText playTimer;
    private int playTime;
    private int playTimerHandle = 0;
    private boolean gameInProgress = false;     // Whether a game is currently running
    private PauseDialog pauseDialog = null;     // If the currently running game is paused, this is non-null

    private Drawable fishContainer;
    private Drawable uiContainer;
    private FishTankControls controls;

    private GeneratedFont uiFont;

    private FishingOptions fishingOptions;
    private ArrayList<Fish> fishies;
    private ArrayList<BitmapText> scoreTextItems;


    public FishTankScreen( FishingOptions options ) {
        super.setVisible(false);

        if (options == null)
            throw new IllegalArgumentException("The options parameter cannot be null.");

        setFocusable(true);
        setFocusObject(this);

        try {
            SoundManager.loadSoundEffect("GoodFish", "assets/sfx/good_fish.wav");
            SoundManager.loadSoundEffect("BadFish", "assets/sfx/bad_fish.wav");
            SoundManager.loadSoundEffect("TimeAlmostUp", "assets/sfx/time_almost_up.wav");
            SoundManager.loadSoundEffect("TimeUp", "assets/sfx/time_up.wav");
        } catch (UnsupportedAudioFileException ex) {
        } catch (IOException ex) {
        } catch (LineUnavailableException ex) {
        }

        fishingOptions = options;
        fishies = new ArrayList<Fish>();
        scoreTextItems = new ArrayList<BitmapText>();

        // Container for the fish
        // We use a separate container so we can control its position and layer ordering (keep fish under everything else)
        fishContainer = new Drawable();
        fishContainer.setMouseChildren(true);
        fishContainer.setUnscaledSize( getUnscaledWidth(), getUnscaledHeight() );
        addDrawable(fishContainer);

        uiContainer = new Drawable();
        uiContainer.setMouseChildren(true);
        uiContainer.setUnscaledSize( getUnscaledWidth(), getUnscaledHeight() );
        addDrawable(uiContainer);

        scoreDisplay = new ScoreDisplay();
        scoreDisplay.setY( 0 );
        uiContainer.addDrawable(scoreDisplay);

        uiFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 40) );
        uiFont.setColor( Color.WHITE );
        uiFont.setOutlineColor( Color.BLACK );
        uiFont.setOutlineWidth( 5 );

        title = new BitmapText(uiFont, ""+options.getTitle() );
        double y = (scoreDisplay.getHeight() - title.getHeight()) / 2;
        title.setPosition( (uiContainer.getWidth() - title.getWidth()) / 2, y );
        uiContainer.addDrawable(title);

        BitmapText scoreLabel = new BitmapText(uiFont, "Score ");
        scoreLabel.setPosition(uiContainer.getWidth() - scoreDisplay.getWidth() - scoreLabel.getWidth(), y);
        uiContainer.addDrawable(scoreLabel);

        playTime = options.getTimeLimit();
        playTimer = new BitmapText(uiFont);
        playTimer.setPosition(0, y);
        updatePlayTimerText();
        uiContainer.addDrawable(playTimer);

        controls = new FishTankControls( fishingOptions, uiFont );
        uiContainer.addDrawable(controls);

        addDrawableListener(
            new DrawableAdapter() {
                    @Override
                    public void drawableResized( DrawableEvent e ) {
                        double w = getUnscaledWidth();
                        double h = getUnscaledHeight();
                        fishContainer.setUnscaledSize( w, h );
                        uiContainer.setUnscaledSize( w, h );
                        scoreDisplay.setX( w - scoreDisplay.getWidth() );
                    } // drawableResized( DrawableEvent e )
            }
        );

        setMouseChildren(true);
        setMouseEnabled(true);

        addMouseListener(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMouseClicked(DrawableMouseEvent e) {
                    if (e.getSource() instanceof Fish) {
                        if (gameInProgress) {
                            Fish f = (Fish)e.getSource();
                            fishContainer.removeDrawable(f);

                            long score = f.getScore();
                            if (!fishingOptions.isTargetFishSpecies(f.getSpecies()))
                                score = -score; // Penalty

                            BitmapText scoreText = new BitmapText(uiFont, ""+score);


                            Point2D pt = globalToLocal(f.localToGlobal(e.getPosition()));
//System.out.println("local="+ e.getPosition() +"  global="+ f.localToGlobal(e.getPosition()) +"  local="+ pt);
                            scoreText.setPosition( pt.getX() - scoreText.getWidth()/2, pt.getY() - scoreText.getHeight()/2);

/*
                            scoreText.setPosition(
                                            f.getX() + (f.getWidth() - scoreText.getWidth())/2,
                                            f.getY() + (f.getHeight() - scoreText.getHeight())/2
                                        );
*/

                            OpacityFilter filter = new OpacityFilter();
                            filter.setOpacity(255);
                            scoreText.addFilter( filter );

                            if (score < 0) {
                                MaskFilter mFilter = new MaskFilter();
                                mFilter.setMask(0xffff0000);
                                scoreText.addFilter(mFilter);

                                SoundManager.playSoundEffect("BadFish");
                            } else {
                                SoundManager.playSoundEffect("GoodFish");
                            }

                            scoreTextItems.add(scoreText);
                            addDrawable(scoreText);

                            scoreDisplay.addToScore(score);

                            if (score >= 0)
                                fishingOptions.incrementFishSpeciesCaptureCount(f.getSpecies());
                        }
                    } else if (fishingOptions.getNewFishProbability() == 0)
                        addFish(0);
                } // drawableMouseClicked(DrawingMouseEvent e)
            }
        );

        final FishTankScreen self = this;
        options.addListener(new FishingOptionsAdapter() {
            @Override
            public void maxFishChanged(ValueChangedEvent e) {
                int max = (Integer) e.getNewValue();

                // Remove fish until we're below the limit
                synchronized (self) {
                    while (fishies.size() > max) {
                        fishies.remove( 0 );
                    } // while
                }
            } // maxFishChanged(ValueChangedEvent e)

            @Override
            public void maxFishSpeedChanged(ValueChangedEvent e) {
                double maxFishSpeed = (Double) e.getNewValue();

                // Cap the existing fish to the specified speed
                synchronized (self) {
                    for (Fish f : fishies) {
                        if (Math.abs(f.getDeltaX()) > maxFishSpeed)
                            f.setDeltaX( (f.getDeltaX() < 0 ? -maxFishSpeed : maxFishSpeed) );
                    } // for
                }
            } // maxFishSpeedChanged(ValueChangedEvent e)

            @Override
            public void minFishSpeedChanged(ValueChangedEvent e) {
                double minFishSpeed = (Double) e.getNewValue();

                // Cap the existing fish to the specified speed
                synchronized (self) {
                    for (Fish f : fishies) {
                        if (Math.abs(f.getDeltaX()) < minFishSpeed)
                            f.setDeltaX( (f.getDeltaX() < 0 ? -minFishSpeed : minFishSpeed) );
                    } // for
                }
            } // minFishSpeedChanged(ValueChangedEvent e)

            @Override
            public void timeLimitChanged(ValueChangedEvent e) {
                playTime = (Integer) e.getNewValue();
                if (playTimerHandle != 0) {
                    ControlTimer.cancelTimerListener(playTimerHandle);
                    playTimerHandle = 0;
                }
            } // timeLimitChanged(ValueChangedEvent e)

            @Override
            public void titleChanged(ValueChangedEvent e) {
                title.setText((String)e.getNewValue());
                title.setX( (uiContainer.getWidth() - title.getWidth()) / 2 );
            } // titleChanged(ValueChangedEvent e)
        });

        addKeyListener(new DrawableKeyAdapter() {
            @Override
            public void drawableKeyPressed(DrawableKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    setPaused(true);
            } // drawableKeyPressed(KeyEvent e)
        });
    } // FishTankScreen()


    public boolean isPaused() {
        return (pauseDialog != null);
    } // isPaused()


    public synchronized void setPaused( boolean value ) {
        if (!gameInProgress || (isPaused() == value))
            return;

        if (value) {
            if (pauseDialog == null) {
                pauseDialog = PauseDialog.show( this );
                pauseDialog.addMenuListener(new MenuAdapter() {
                        @Override
                        public void menuSelected( MenuEvent e ) {
                            setPaused(false);
                        } // menuSelected( MenuEvent e )
                    });
            }
        } else {
            if (pauseDialog != null) {
                pauseDialog.getParent().removeDrawable(pauseDialog);
                pauseDialog = null;
            }
        }
    } // setPaused( boolean value )


    @Override
    public synchronized void setVisible( boolean value ) {
        if (isVisible() == value)
            return;

        super.setVisible(value);

        if (value) {
            startGame();
        } else if (animationTaskHandle != 0) {
            ControlTimer.cancelTimerListener(animationTaskHandle);
            animationTaskHandle = 0;
            gameInProgress = false;
        }
    } // setVisible( boolean value )


    private void startGame() {
        playTimer.setFilters(null);

        playTime = fishingOptions.getTimeLimit();
        if (playTimerHandle != 0) {
            ControlTimer.cancelTimerListener(playTimerHandle);
            playTimerHandle = 0;
        }

        fishingOptions.resetStatistics();

        // Clear the score
        uiContainer.removeDrawable(scoreDisplay);
        scoreDisplay = new ScoreDisplay();
        scoreDisplay.setPosition( uiContainer.getUnscaledWidth() - scoreDisplay.getWidth(), 0 );
        uiContainer.addDrawable(scoreDisplay);

//        scoreDisplay.reset();
//        scoreDisplay.finishAnimation();

        // Remove existing fish
        fishContainer.removeAllDrawables();

        // Create initial population of fish
        for (int i = 0; i < fishingOptions.getMaxFish(); ++i) {
            addFish( 0 );
        } // for

        // Add timer task
        if (animationTaskHandle == 0)
            animationTaskHandle = ControlTimer.scheduleAtFixedRate(this, 1000 / 30);

        gameInProgress = true;
    } // startGame()


    public synchronized void addFish( int origin ) {
        if (fishies.size() >= fishingOptions.getMaxFish())
            return; // Cannot add more fish

        Fish f = fishingOptions.createFish();
        if (f == null)
            return;

        double x;
        double y = Math.random() * getUnscaledHeight();

        double speed = fishingOptions.getMinFishSpeed() +
                        Math.random() * (fishingOptions.getMaxFishSpeed() - fishingOptions.getMinFishSpeed());

        if (origin == 0) {
            x = Math.random() * getUnscaledWidth();

            // Random direction
            f.setDeltaX( Math.random() >= 0.5 ? speed : -speed );
        } else if (origin < 0) {
            x = 0;

            // Move to right
            f.setDeltaX( speed );
        } else {
            x = getUnscaledWidth();

            // Move to left
            f.setDeltaX( -speed );
        }

//System.out.println("addFish: origin="+ origin +"  "+ x +","+ y +"  speed="+ speed);
        f.setPosition(x , y);

        f.addTreeListener(new DrawableTreeAdapter() {
                        @Override
                        public void drawableRemoved( DrawableEvent e ) {
                            fishies.remove((Fish) e.getSource());
                        } // drawableRemoved( DrawableEvent e )
                    });

        fishies.add(f);
        fishContainer.addDrawable(f);
    } // addFish( int origin )


    @Override
    public synchronized void paint(Graphics2D g) {
        if (gameInProgress && playTimerHandle == 0)   // Start the timer if it hasn't already been (typically on the first frame drawn)
            playTimerHandle = ControlTimer.scheduleAtFixedRate(this, 1000);

        super.paint(g);
    } // paint(Graphics2D g)
 

    private void updatePlayTimerText() {
        if (playTime >= 0) {
            if (playTime == 10) {
                SoundManager.playSoundEffect("TimeAlmostUp");

                MaskFilter mFilter = new MaskFilter();
                mFilter.setMask(0xffff0000);
                playTimer.addFilter(mFilter);
            }

            int secs = playTime % 60;
            playTimer.setText( "Time "+ (playTime / 60) +":"+ (secs < 10 ? "0" : "") + secs );
            --playTime;
        }
        
        if (playTime < 0) {
            SoundManager.playSoundEffect("TimeUp");

            gameInProgress = false;
            fishingOptions.setFinalScore(scoreDisplay.getScore());

            // Stop the timer
            ControlTimer.cancelTimerListener(playTimerHandle);
            playTimerHandle = 0;

            // Show the new high score dialog
            long score = fishingOptions.getFinalScore();
            int rank = HighScoreManager.getRankForScore(fishingOptions.getTitle(), score);
            if ((score > 0) && (rank > 0)) {
                showHighScoreDialog( rank, score );
            } else
                showGameOverDialog();
        }
    } //updatePlayTimerText()

    /**
     * Shows a HighScoreDialog centered within this screen.
     */
    private void showHighScoreDialog( int rank, final long score ) {
        final HighScoreDialog dlg = new HighScoreDialog( rank, score );
        dlg.setPosition( (getUnscaledWidth() - dlg.getWidth()) / 2, (getUnscaledHeight() - dlg.getHeight()) / 2 );
        dlg.setModal(true);
        addDrawable(dlg);

        dlg.addMenuListener(new MenuAdapter() {
                @Override
                public void menuSelected(MenuEvent e) {
                    removeDrawable(dlg);

                    if ("Record".equals(e.getMenuName())) {
                        // Record the high score
                        HighScoreManager.setHighScore(fishingOptions.getTitle(), score, dlg.getInitials());
                    }

                    showGameOverDialog();
                } // menuSelected(MenuEvent e)
            });
    } // showHighScoreDialog()


    /**
     * Shows a GameOverDialog centered within this screen.
     */
    private void showGameOverDialog() {
        // Show the game over dialog
        final GameOverDialog dlg = new GameOverDialog(fishingOptions);
        dlg.setPosition( (getUnscaledWidth() - dlg.getWidth()) / 2, (getUnscaledHeight() - dlg.getHeight()) / 2 );
        dlg.setModal(true);
        addDrawable(dlg);

        // Listen for menu events based on user selections
        final FishTankScreen self = this;
        dlg.addMenuListener(new MenuAdapter() {
                @Override
                public void menuSelected(MenuEvent e) {
                    removeDrawable(dlg);

                    if ("PlayAgain".equals(e.getMenuName())) {
                        startGame();
                        self.setFocus();
                    } else if ("Quit".equals(e.getMenuName())) {
                        getParent().getDrawable("titleScreen").setVisible(true);
                    }
                } // menuSelected(MenuEvent e)
            });
    } // showGameOverDialog()


    public synchronized void timerFired(TimerEvent e) {
        if (!isVisible() || isPaused())
            return;

        if (e.getSource() == animationTaskHandle) {
            // Compute the bounds of the area the fish should swim within
            // This is the bounds of the screen minus the score display and
            // other UI controls.
            Rectangle2D bounds = getUnscaledBounds();
            double y = scoreDisplay.getHeight();
            bounds.setRect( bounds.getX(), y, bounds.getWidth(), bounds.getHeight() - y - controls.getHeight() );

            // 
            if (Math.random() <= fishingOptions.getNewFishProbability())
                addFish( Math.random() >= 0.5 ? 1 : -1 );

            // Update positions for fish
            ArrayList<Fish> list = (ArrayList<Fish>)fishies.clone();
            for (Fish f : list) {
                try {
                    f.animate( bounds );
                } catch (Exception ex) { }
            } // for

            // Fade out score text items
            ArrayList<BitmapText> removeItems = new ArrayList<BitmapText>();
            for (BitmapText t : scoreTextItems) {
                OpacityFilter filter = (OpacityFilter) t.getFilters().get(0);

                int o = filter.getOpacity() - 10;
                if (o < 0)
                    removeItems.add(t);
                else {
                    t.setY( t.getY() - 3 );
                    filter.setOpacity(o);
                }
            } // for

            // Remove items that are done fading
            for (BitmapText t : removeItems) {
                t.getParent().removeDrawable(t);
                scoreTextItems.remove(t);
            } // for
        } else if (e.getSource() == playTimerHandle) {
            updatePlayTimerText();
        }
    } // timerFired(TimerEvent e)


} // class FishTankScreen
