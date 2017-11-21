
package Fishing.drawable.controls;

import Fishing.FishingOptions;
import Fishing.drawable.Drawable;
import Fishing.drawable.Animation;
import Fishing.drawable.SpriteFrameSet;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.text.BitmapText;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brad
 */
public class FishTable
    extends Drawable
{

    private BitmapFont font;

    private int fishPerRow = 5;

    private double spacing = 10;

    private double rowHeight = 60;

    private double maxFishWidthRatio = 2.0;

    private FishingOptions fishingOptions;

    private boolean showScores;

    private List<String> species;


    /**
     * Constructs a new instance.
     * 
     * @param options       The fishing options object containing the sprites,
     *                      statistics, etc.
     * @param font          The font to use for the text in the table
     * @param showScores    Whether to show scores ({@code true}) or capture
     *                      statistics ({@code false}).
     */
    public FishTable( FishingOptions options, BitmapFont font, boolean showScores ) {
        fishingOptions = options;
        this.showScores = showScores;
        setFont(font);
    } // FishTable( FishingOptions options, BitmapFont font, boolean showScores )


    public void setSpecies(List<String> value) {
        if (value == null)
            species = null;
        else
            species = new ArrayList<>(value);
    } // setSpecies(ArrayList<String> value)


    public List<String> getSpecies() {
        if (species == null)
            return null;
        return new ArrayList<>(species);
    } // getSpecies()


    public void setFont( BitmapFont value ) {
        if (value == null)
            throw new IllegalArgumentException("The font cannot be null.");
        font = value;
        invalidate();
    } // setFont( BitmapFont value )


    public BitmapFont getFont() {
        return font;
    } // getFont()


    public void setFishPerRow( int value ) {
        if (value < 1)
            throw new IllegalArgumentException("The fishPerRow cannot be less than 1.");

        fishPerRow = value;
        invalidate();
    } // setFishPerRow( int value )


    public int getFishPerRow() {
        return fishPerRow;
    } // getFishPerRow()


    public void setSpacing( double value ) {
        spacing = value;
        invalidate();
    } // setSpacing( double value )


    public double getSpacing() {
        return spacing;
    } // getSpacing()


    public void setRowHeight( double value ) {
        if (value < 1)
            throw new IllegalArgumentException("The rowHeight cannot be less than 1.");
        rowHeight = value;
        invalidate();
    } // setRowHeight( double value )


    public void setFishingOptions( FishingOptions value ) {
        if (value == null)
            throw new IllegalArgumentException("The fishingOptions cannot be null.");
        fishingOptions = value;
    } // setFishingOptions( FishingOptions value )


    public FishingOptions getFishingOptions() {
        return fishingOptions;
    } // getFishingOptions()


    public double getMaxFishWidthRatio() {
        return maxFishWidthRatio;
    } // getMaxFishWidthRatio()


    public void setMaxFishWidthRatio(double value) {
        if (value < 1)
            throw new IllegalArgumentException("The maxFishWidth ratio cannot be less than 1.");
        maxFishWidthRatio = value;
    } // setMaxFishWidthRatio(double value)


    public boolean getShowScores() {
        return showScores;
    } // getShowScores()


    public void setShowScores( boolean value ) {
        if (showScores == value)
            return;

        showScores = value;

        invalidate();
    } // setShowScores( boolean value )

    @Override
    public void validate() {
        removeAllDrawables();

        double y = 0;

        // Create the per-fish stats
        List<String> fishSpecies = (species == null
                                        ? fishingOptions.getEnabledFishSpecies()
                                        : species);

        int n = 0;
        int s = fishSpecies.size() - 1;
        double[] colSizes = new double[fishPerRow * 2];
        List<Drawable> rows = new ArrayList<>();
        Drawable row = new Drawable();
        row.setY(y);
        addDrawable(row);
        rows.add(row);
        int col = 0;

        while (n <= s) {
            String name = fishSpecies.get(n);
            SpriteFrameSet ff = fishingOptions.getFishFrameset(name);

            long num;
            if (showScores)
                num = fishingOptions.getFishSpeciesScore(name);
            else
                num = fishingOptions.getFishSpeciesCaptureCount(name);

            // Create fish count text
            BitmapText countText = new BitmapText(font);
            countText.setText(""+ num +" ");
            countText.setY((rowHeight - countText.getHeight()) / 2);
            row.addDrawable(countText);

            colSizes[col] = Math.max(colSizes[col], countText.getWidth());
            ++col;

            // Create fish animation
            Animation fish = new Animation( ff );
            double scale = Math.min( rowHeight / fish.getHeight(), (rowHeight * maxFishWidthRatio) / fish.getWidth() );
            fish.setScale( scale, scale );
            fish.setCenterX(0);
            fish.setCenterY(0);
            fish.setRepeat(true);
            fish.start();
            row.addDrawable(fish);

            colSizes[col] = Math.max(colSizes[col], fish.getWidth());
            ++col;

            ++n;
            if (n % fishPerRow == 0) {
                y += spacing + rowHeight;

                col = 0;
                row = new Drawable();
                row.setY(y);
                addDrawable(row);
                rows.add(row);
            }
        } // while

        double xPos;
        double maxWidth = 0;
        for (Drawable r : rows) {
            xPos = 0;

            for (n = 0; n < r.getNumDrawables(); ++n) {
                Drawable c = r.getDrawableAt(n);

                c.setPosition( xPos, (rowHeight - c.getHeight()) / 2 );
                xPos += colSizes[n] + spacing * (n % 2);
            } // for

            r.setUnscaledSize(xPos - spacing, rowHeight);
            maxWidth = Math.max(maxWidth, r.getWidth());
        } // for

        setUnscaledSize(maxWidth, y + rowHeight);
    } // validate()

} // class FishTable
