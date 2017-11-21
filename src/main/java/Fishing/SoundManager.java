
package Fishing;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Utility class for managing background music and sound effects.
 *
 * @author Brad
 */
public class SoundManager {

    /**
     * Whether the background music is currently muted ({@code true}) or not
     * ({@code false}).
     */
    private static boolean backgroundMusicMuted = false;

    /**
     * Whether all sound effects are currently muted ({@code true}) or not
     * ({@code false}).
     */
    private static boolean soundEffectsMuted = false;

    /**
     * The gain to apply to the background music, in decibels.
     * Positive values increase the volume, while negative values decrease it.
     */
    private static float backgroundMusicVolume = 0.0f;

    /**
     * The gain to apply to sound effects, in decibels.
     * Positive values increase the volume, while negative values decrease it.
     */
    private static float soundEffectVolume = 0.0f;

    /**
     * The current background music clip, or {@code null} if none.
     */
    private static Clip backgroundMusic;

    /**
     * Map of loaded sound effect clips, keyed on the name assigned when the
     * clip was loaded.
     */
    private static final HashMap<String,Clip> soundEffects = new HashMap<>();

    /**
     * Retrieves whether the background music is currently muted.
     * 
     * @return  Whether the background music is currently muted ({@code true})
     *          or not ({@code false}).
     */
    public static boolean getBackgroundMusicMuted() {
        return backgroundMusicMuted;
    } // getBackgroundMusicMuted()

    /**
     * Mutes or unmutes the background music.
     * @param   value   Whether the background music should be muted
     *                  ({@code true}) or not ({@code false}).
     */
    public static void setBackgroundMusicMuted( boolean value ) {
        backgroundMusicMuted = value;

        if (backgroundMusic != null) {
            try {
                BooleanControl muteControl = (BooleanControl)backgroundMusic.getControl(BooleanControl.Type.MUTE);
                muteControl.setValue(backgroundMusicMuted);
            } catch (Exception ex) {
                Logger.getLogger(SoundManager.class.getName()).log(
                    Level.SEVERE,
                    "Unable to mute background music",
                    ex
                );
            }
        }
    } // setBackgroundMusicMuted( boolean value )

    /**
     * Retrieves whether all sound effects are currently muted.
     * 
     * @return  Whether all sound effects are currently muted ({@code true})
     *          or not ({@code false}).
     */
    public static boolean getSoundEffectsMuted() {
        return soundEffectsMuted;
    } // getSoundEffectsMuted()

    /**
     * Mutes or unmutes all sound effects.
     * @param   value   Whether all sound effects should be muted
     *                  ({@code true}) or not ({@code false}).
     */
    public static void setSoundEffectsMuted( boolean value ) {
        soundEffectsMuted = value;
    } // setSoundEffectsMuted( boolean value )

    /**
     * Sets the gain to apply to the background music, in decibels.
     * 
     * @param   value   The new gain value.  Positive values increase the
     *                  volume, while negative values decrease it.
     */
    public static void setBackgroundMusicVolume( float value ) {
        backgroundMusicVolume = value;

        if (backgroundMusic != null) {
            try {
                FloatControl volumeControl = (FloatControl)backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue( value );
            } catch (Exception ex) {
                Logger.getLogger(SoundManager.class.getName()).log(
                    Level.SEVERE,
                    "Unable to update background music gain",
                    ex
                );
            }
        }
    } // setBackgroundMusicVolume( float value )

    /**
     * Retrieves the gain applied to the background music, in decibels.
     * 
     * @return  The gain applied to the background music, in decibels.
     *          Positive values increase the volume, while negative values
     *          decrease it.
     */
    public static float getBackgroundMusicVolume() {
        return backgroundMusicVolume;
    } // setBackgroundMusicVolume( float value )


    /**
     * Sets the gain to apply to all sound effects, in decibels.
     * 
     * @param   value   The new gain value.  Positive values increase the
     *                  volume, while negative values decrease it.
     */
    public static void setSoundEffectVolume( float value ) {
        soundEffectVolume = value;
    } // setSoundEffectVolume( float value )


    /**
     * Retrieves the gain applied to all sound effects, in decibels.
     * 
     * @return  The gain applied to all sound effects, in decibels.
     *          Positive values increase the volume, while negative values
     *          decrease it.
     */
    public static float getSoundEffectVolume() {
        return soundEffectVolume;
    } // getSoundEffectVolume()

// TODO: Document
    /**
     * Loads a background music resource and starts playing it.
     * 
     * Any previously loaded background music is stopped and unloaded before
     * the new background music is started.
     * 
     * @param   musicResource   Path to load the background music resource from.
     *                          This is interpreted as a resource path relative
     *                          to the {@link ClassLoader} associated with the
     *                          {@link Resources} utility class.
     * 
     * @throws  UnsupportedAudioFileException if the format of the specified
     *          resource is not supported.
     * @throws  IOException if an exception occurs accessing the specified
     *          resource file.
     */
    public static void setBackgroundMusic( String musicResource )
        throws UnsupportedAudioFileException, IOException
    {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.flush();
            backgroundMusic.close();
        }

        Clip line = null;

        if (musicResource != null) {
            AudioInputStream stream = AudioSystem.getAudioInputStream(Resources.getStream(musicResource));

            DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // format is an AudioFormat object
            if (!AudioSystem.isLineSupported(info)) {
                Logger.getLogger(SoundManager.class.getName()).log(
                    Level.SEVERE,
                    "Unsupported audio line: {1}",
                    info
                );
                return;
            }

            // Obtain and open the line.
            try {
                line = (Clip) AudioSystem.getLine(info);
                line.open( stream );
            } catch (LineUnavailableException ex) {
                Logger.getLogger(SoundManager.class.getName()).log(
                    Level.SEVERE,
                    "Unable to open audio line",
                    ex
                );
                return;
            }
        }

        backgroundMusic = line;

        if (line != null) {
            setBackgroundMusicMuted(backgroundMusicMuted);
            setBackgroundMusicVolume(backgroundMusicVolume);

            line.loop(Clip.LOOP_CONTINUOUSLY);
            line.start();
        }
    } // setBackgroundMusic( String musicResource )

    /**
     * Loads a sound effect resource and registers it with the SoundManager.
     *
     * @param   name            Name to assign to the loaded sound effect.  This
     *                          name is used for referencing the sound effect in
     *                          other methods.
     * @param   resourcePath    Path to load the sound effect resource from.
     *                          This is interpreted as a resource path relative
     *                          to the {@link ClassLoader} associated with the
     *                          {@link Resources} utility class.
     * 
     * @throws  UnsupportedAudioFileException if the format of the specified
     *          resource is not supported.
     * @throws  IOException if an exception occurs accessing the specified
     *          resource file.
     * @throws  LineUnavailableException if a matching audio line is not
     *          available due to resource restrictions.
     */
    public static void loadSoundEffect( String name, String resourcePath )
        throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        synchronized (soundEffects) {
            if (soundEffects.containsKey(name))
                return;
                // TODO: Should possibly check resourcePath here and throw exception if the new path doesn't match the previous path.
                //throw new IllegalArgumentException("There is already a sound effect named "+ name);

            AudioInputStream stream = AudioSystem.getAudioInputStream(Resources.getStream(resourcePath));

            DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // format is an AudioFormat object
            if (!AudioSystem.isLineSupported(info))
                throw new IllegalArgumentException("Unsupported audio format");

            // Obtain and open the line.
            Clip line = (Clip) AudioSystem.getLine(info);
            line.open( stream );

            soundEffects.put(name, line);
        }
    } // loadSoundEffect( String name, String resourcePath )

    /**
     * Removes the named sound effect from the list of managed sound effects.
     * 
     * @param   name    The name of a previously loaded sound effect.
     * 
     * @throws  IllegalArgumentException if no loaded sound effect matches the
     *          specified name.
     */
    public static void unloadSoundEffect( String name ) {
        if (!soundEffects.containsKey(name))
            throw new IllegalArgumentException("There is no sound effect named "+ name);

        soundEffects.remove(name);
    } // unloadSoundEffect( String name )

    /**
     * Play the sound effect with the specified name.
     * 
     * If sound effects are muted, this method has no effect.
     * 
     * @param   name    The name of the sound effect to play.
     * 
     * @throws  IllegalArgumentException if there is no registered sound effect
     *          with the specified name.
     * 
     * @see     #loadSoundEffect(String, String) 
     */
    public static void playSoundEffect( String name ) {
        if (getSoundEffectsMuted())
            return;

        Clip line = soundEffects.get(name);
        if (line == null)
            throw new IllegalArgumentException("There is no sound effect named "+ name);

        line.stop();
        line.flush();
        line.setFramePosition(0);

        try {
            FloatControl volumeControl = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue( soundEffectVolume );
        } catch (Exception ex) {
            Logger.getLogger(SoundManager.class.getName()).log(
                Level.SEVERE,
                "Unable to update sound effect gain",
                ex
            );
        }

        line.start();
    } // playSoundEffect( String name )

    /**
     * Don't allow this utility class to be instantiated.
     */
    private SoundManager() {
    } // SoundManager()

} // class SoundManager
