
package Fishing.drawable.controls;

import Fishing.FishingOptions;
import Fishing.drawable.Drawable;
import Fishing.drawable.SpriteFrameSet;
import Fishing.drawable.events.EventBase;
import Fishing.drawable.events.FishingOptionsAdapter;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A grouping of multiple ImageSelector controls which retrieve their
 * selectable options from the enabled fish species of a FishingOptions
 * instance.
 * 
 * @author Brad
 */
public class FishSelectorGroup
    extends Drawable
{

    /**
     * The FishingOptions that this selector group is associated with.
     */
    private FishingOptions fishingOptions;

    /**
     * The ImageSelector children.
     */
    private List<ImageSelector> selectors = new ArrayList<>();

    /**
     * Spacing to apply between the selectors.
     */
    private double spacing = 30;

    /**
     * Internal flag indicating that the selectors are currently being
     * populated by {@link #setSelections(List<String>)}.
     * 
     * When this flag is {@code true}, no ValueChangeEvents will be sent.
     */
    private boolean settingSelectors = false;

    /**
     * The registered ValueChangedListeners.
     */
    private List<ValueChangedListener> valueChangedListeners;

    /**
     * Creates a new instance.
     * 
     * @param options   The FishingOptions instance to associate this selector
     *                  group with.
     * @param num       The number of selector children to create.
     */
    public FishSelectorGroup( FishingOptions options, int num ) {
        setMouseChildren(true);

        fishingOptions = options;
        final FishSelectorGroup self = this;

        while (num > 0) {
            --num;

            final ImageSelector sel = new ImageSelector();
            sel.setMouseEnabled(true);
            sel.setFrameSize( 100, 75 );
            addDrawable( sel );
            selectors.add(sel);

            // Add a menuSelect handler to prevent any of the controls from having the same selected item as any other...
            sel.addMenuListener(
                new MenuAdapter() {
                    int oldValue = 0;

                    @Override
                    public void menuSelected(MenuEvent e) {
                        if (settingSelectors)
                            return;

                        int newValue = e.getMenuIndex();

                        // Are any of the other items in the same state?
                        boolean updated = false;
                        boolean conflict;
                        do {
                            conflict = false;
                            for (ImageSelector s : selectors) {
                                if ((s != sel) && (newValue == s.getSelectedItem())) {
                                    // Found a conflict
                                    updated = true;
                                    conflict = true;

                                    if (oldValue > newValue) {
                                        // Keep moving to left
                                        --newValue;
                                    } else {
                                        // Keep moving to right
                                        ++newValue;
                                    }

                                    if ((newValue < 0) || (newValue >= sel.getNumFrames())) {
                                        // Can't do anything...
                                        newValue = oldValue;
                                        conflict = false;
                                        break;
                                    }
                                }
                            } // for
//if (conflict)
//    System.out.println("CONFLICT: "+ newValue +" (requested "+ e.getMenuIndex() +", oldValue="+ oldValue +")");
                        } while (conflict);

                        if (updated) {
//System.out.println("Reposition menu to "+ newValue +" (requested "+ e.getMenuIndex() +", oldValue="+ oldValue +")");
                            sel.setSelectedItem(newValue);
                        }

                        EventBase.notifyListeners(
                            valueChangedListeners,
                            new ValueChangedEvent(self, null, null),
                            ValueChangedListener::valueChanged
                        );

                        oldValue = newValue;
                    } // menuSelected(MenuEvent e)

                }
            );
        } // while

        // If the selected species are updated, refresh the options available.
        options.addListener(
            new FishingOptionsAdapter() {
                @Override
                public void enabledFishSpeciesChanged(ValueChangedEvent e) {
                    invalidate();
                } // enabledFishSpeciesChanged(ValueChangedEvent e)
            }
        );
    } // FishSelectorGroup( FishingOptions options, int num )

    /**
     * Sets the selected fish species.
     * 
     * @param value List of fish species selections, one per child selector
     *              control.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     * @throws  IllegalArgumentException if the number of items in
     *          {@code value} does not match the number of child selectors
     *          or if an entry in {@code value} is not the name of an enabled
     *          fish species from the associated {@code options}.
     */
    public void setSelections( List<String> value ) {
        Objects.requireNonNull(value, "The list of selections cannot be null.");

        if (value.size() != selectors.size())
            throw new IllegalArgumentException("The number of items in the list provided must match the number of selectors.");

        if (isInvalidated())
            validateDrawable();

        List<String> species = fishingOptions.getEnabledFishSpecies();

        for (String name : value) {
            if (!species.contains(name) ) {
                throw new IllegalArgumentException("Unknown fish type "+ name);
            }
        }

        settingSelectors = true;
        for (int i = 0; i < value.size(); ++i) {
            selectors.get(i).setSelectedItem( species.indexOf( value.get(i) ) );
        } // for
        settingSelectors = false;
    } // setSelections( List<String> value )

    /**
     * Retrieves the currently selected fish species.
     * 
     * @return  The currently selected fish species.
     */
    public List<String> getSelections() {
        if (isInvalidated())
            validateDrawable();

        List<String> rv = new ArrayList<>();
        List<String> species = fishingOptions.getEnabledFishSpecies();

        for (ImageSelector sel : selectors)
            rv.add( species.get( sel.getSelectedItem() ) );

        return rv;
    } // getSelections()

    @Override
    public void validate() {
        BufferedImage fishSelectorImage = getFishSelectorImage();

        double width = 0;
        double height = 0;

        int n = fishingOptions.getEnabledFishSpecies().size();
        for (ImageSelector sel : selectors) {
            sel.setImage(fishSelectorImage, n);

            sel.setX(width);
            width += sel.getWidth() + spacing;
            height = Math.max(height, sel.getHeight());
        }

        width -= spacing;

        setUnscaledSize( width, height );
    } // validate()

    /**
     * Constructs an image suitable for use by an ImageSelector control with
     * an image for each enabled fish species from the associated
     * {@code options}.
     * 
     * @return  An image suitable for use by an ImageSelector control with
     *          an image for each enabled fish species from the associated
     *          {@code options}.
     */
    private BufferedImage getFishSelectorImage() {
        int w = 125;
        int h = 94;

        List<String> fishSpecies = fishingOptions.getEnabledFishSpecies();
        int n = fishSpecies.size();
        int imgW = (n > 0) ? w * n : w;

        BufferedImage img = new BufferedImage( imgW, h, BufferedImage.TYPE_INT_ARGB );

        Graphics2D g = img.createGraphics();

        g.setColor( new Color( 0xCFffffff, true ) );
        g.fillRect(0, 0, imgW, h);

        AffineTransform m = g.getTransform();

        int xOfs = 0;
        for (String name : fishSpecies) {
            SpriteFrameSet ff = fishingOptions.getFishFrameset(name);

            double scaleW = (double)w / ff.getFrameWidth();
            double scaleH = (double)h / ff.getFrameHeight();
            int adjX = 0;
            int adjY = 0;

            if (scaleW < scaleH) {
                adjY = (int)((h - scaleW * ff.getFrameHeight()) / 2);
            } else {
                adjX = (int)((w - scaleH * ff.getFrameWidth()) / 2);
                scaleW = scaleH;
            }

            g.setTransform(m);
            g.translate(xOfs + adjX, adjY);
            g.scale(scaleW, scaleW);

            ff.drawFrame(0, g);

            xOfs += w;
        } // for

        g.setTransform(m);

        g.dispose();
        return img;
    } // getFishSelectorImage()

    /**
     * Adds a new ValueChangedListener to the set of registered listeners.
     * 
     * <p>If the specified listener already exists in the set of registered
     * listeners, it will not be added again.
     * 
     * @param listener The new listener instance to add.
     */
    public void addValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners = EventBase.addListener(valueChangedListeners, listener);
    } // addValueChangedListener( ValueChangedListener listener )

    /**
     * Removes a previously registered ValueChangedListener from the set of
     * registered listeners.
     *
     * <p>If the specified listener is not found in the set of registered
     * listeners, the set of registered listeners will remain unchanged.
     *
     * @param listener The listener instance to remove.
     */
    public void removeValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners = EventBase.removeListener(valueChangedListeners, listener);
    } // removeValueChangedListener( ValueChangedListener listener )

} // class FishSelectorGroup
