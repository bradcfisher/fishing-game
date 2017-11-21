
package Fishing.drawable.dialogs;

import com.jhlabs.image.SwimFilter;
import Fishing.SoundManager;
import Fishing.drawable.DrawablePanel;
import Fishing.drawable.controls.Button;
import Fishing.drawable.controls.Checkbox;
import Fishing.drawable.controls.Slider;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.DrawableTreeAdapter;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.TitleFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Dialog for configuring game options, such as background music and sound
 * effect volume.
 *
 * @author Brad
 */
public class OptionsDialog
    extends Dialog
{

    private SwimFilter titleFilter;
    private double padding = 20;

    public OptionsDialog() {
        TitleFont titleFont = new TitleFont("green");
        titleFont.setCharacterSpacing(15);
        titleFont.setLineSpacing(12);

        double sliderWidth = 200;
        double height = padding;

        // Filter for the title text
        titleFilter = new SwimFilter();
        titleFilter.setAmount(3);
        titleFilter.setScale(30f);

        // Create the title text
        BitmapText title = new BitmapText(titleFont, "Options");
        title.addFilter( titleFilter );
        title.setY(height);
        addDrawable(title);

        double interLineSpacing = titleFont.getFontSize() / 2;
        double maxWidth = title.getWidth();
        height += title.getHeight() + interLineSpacing;

        GeneratedFont textFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 24), Color.WHITE );

        // Create background music controls
        Checkbox bgMusicCheck = new Checkbox(textFont);
        bgMusicCheck.setLabel("Play Background Music");
        bgMusicCheck.setSelected( !SoundManager.getBackgroundMusicMuted() );
        bgMusicCheck.setPosition( padding, height );
        addDrawable(bgMusicCheck);
        bgMusicCheck.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged( ValueChangedEvent e ) {
                    boolean value = !(Boolean)e.getNewValue();
                    SoundManager.setBackgroundMusicMuted( value );
                } // valueChanged( ValueChangedEvent e )
            });

        maxWidth = Math.max(maxWidth, bgMusicCheck.getWidth());
        height += bgMusicCheck.getHeight() + interLineSpacing / 2;

        Slider bgMusicVolume = new Slider( -20, 6 );
        bgMusicVolume.setPosition( padding * 3, height );
        bgMusicVolume.setValue( SoundManager.getBackgroundMusicVolume() );
        bgMusicVolume.setWidth(sliderWidth);
        addDrawable(bgMusicVolume);
        bgMusicVolume.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    float value = ((Double)e.getNewValue()).floatValue();
                    //value = value * 20 - 10;
System.out.println("Music volume="+ value);
                    SoundManager.setBackgroundMusicVolume( value );
                } // valueChanged(ValueChangedEvent e)
            });

        maxWidth = Math.max(maxWidth, bgMusicVolume.getWidth() + padding * 2);
        height += bgMusicVolume.getHeight() + interLineSpacing;

        // Create sound effect controls
        Checkbox sfxCheck = new Checkbox(textFont);
        sfxCheck.setLabel("Play SoundEffects");
        sfxCheck.setSelected( !SoundManager.getSoundEffectsMuted() );
        sfxCheck.setPosition( padding, height );
        addDrawable(sfxCheck);
        sfxCheck.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged( ValueChangedEvent e ) {
                    boolean value = !(Boolean)e.getNewValue();
System.out.println("SFX muted="+ value);
                    SoundManager.setSoundEffectsMuted( value );
                    if (!value)
                        SoundManager.playSoundEffect("hover");
                } // valueChanged( ValueChangedEvent e )
            });

        maxWidth = Math.max(maxWidth, sfxCheck.getWidth());
        height += sfxCheck.getHeight() + interLineSpacing / 2;

        Slider sfxVolume = new Slider( -20, 6 );
        sfxVolume.setPosition( padding * 3, height );
        sfxVolume.setValue( SoundManager.getSoundEffectVolume() );
        sfxVolume.setWidth(sliderWidth);
        addDrawable(sfxVolume);
        sfxVolume.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    float value = ((Double)e.getNewValue()).floatValue();
System.out.println("SFX volume="+ value);
                    SoundManager.setSoundEffectVolume( value );
                    SoundManager.playSoundEffect("hover");
                } // valueChanged(ValueChangedEvent e)
            });

        maxWidth = Math.max(maxWidth, sfxVolume.getWidth() + padding * 2);
        height += sfxVolume.getHeight() + interLineSpacing;

        // Add frame rate checkbox
        Checkbox frameRate = new Checkbox(textFont);
        frameRate.setLabel("Show Framerate");
        frameRate.setPosition( padding, height );
        addDrawable(frameRate);

        // Can't determine the value assigned to the panel until this object
        // is added to the root stage.
        this.addTreeListener(new DrawableTreeAdapter() {
            @Override
            public void drawableAddedToRootStage(DrawableEvent e) {
                frameRate.setSelected( ((DrawablePanel)getRoot().getPanel()).isShowingFrameRate() );
            }
        });

        frameRate.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged( ValueChangedEvent e ) {
                    boolean value = (Boolean)e.getNewValue();
System.out.println("Show framerate="+ !value);
                    ((DrawablePanel)getRoot().getPanel()).setShowFrameRate( value );
                } // valueChanged( ValueChangedEvent e )
            });

        maxWidth = Math.max(maxWidth, frameRate.getWidth());
        height += frameRate.getHeight() + interLineSpacing;

        // Add the Close button
        Button close = new Button(textFont);
        close.setCaption("Close");
        close.setY(height);
        addDrawable(close);
        close.addMouseListener(new DrawableMouseAdapter() {
                @Override
                public void drawableMouseClicked( DrawableMouseEvent e) {
                    close();
                } // drawableMouseClicked( DrawableMouseEvent e)
            });

        maxWidth = Math.max(maxWidth, close.getWidth());
        height += close.getHeight();

        // Set the final size of the Dialog
        maxWidth += padding * 2;
        height += padding;

        setUnscaledSize( maxWidth, height );

        title.setX( (maxWidth - title.getWidth()) / 2 );
        close.setX( (maxWidth - close.getWidth()) / 2 );
    } // OptionsDialog()


    private void close() {
        getParent().removeDrawable(this);
    } // close()


    @Override
    public void paint( Graphics2D g ) {
        titleFilter.setTime( titleFilter.getTime() + 0.05f );
        super.paint(g);
    } // paint( Graphics2D g )


} // class OptionsDialog
