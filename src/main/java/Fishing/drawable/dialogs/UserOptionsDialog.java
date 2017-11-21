
package Fishing.drawable.dialogs;

import com.jhlabs.image.SwimFilter;
import Fishing.FishingOptions;
import Fishing.drawable.controls.ButtonMenu;
import Fishing.drawable.controls.FishSelectorGroup;
import Fishing.drawable.controls.FishTable;
import Fishing.drawable.controls.Slider;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.TitleFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author Brad
 */
public class UserOptionsDialog
    extends Dialog
{

    private SwimFilter titleFilter;
    private FishingOptions fishingOptions;
    private ButtonMenu buttons;
    private double padding = 20;

    public UserOptionsDialog( FishingOptions options ) {
        TitleFont titleFont = new TitleFont("yellow");
        titleFont.setCharacterSpacing(15);
        titleFont.setLineSpacing(12);

        fishingOptions = options;

        double height = padding;

        // Filter for the title text
        titleFilter = new SwimFilter();
        titleFilter.setAmount(3);
        titleFilter.setScale(30f);

        // Create the title text
        BitmapText title = new BitmapText(titleFont, fishingOptions.getTitle());
        title.addFilter( titleFilter );
        title.setY(height);
        addDrawable(title);

        double interLineSpacing = titleFont.getFontSize() / 2;
        double maxWidth = title.getWidth();
        height += title.getHeight() + interLineSpacing;

        GeneratedFont textFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 28), Color.WHITE );

        // Create the intro/instructions text
        String instructions = "You have "+ fishingOptions.getTimeLimit() +" seconds to capture as many fish as you can.\n";
        if ("Beginner".equals(fishingOptions.getTitle())) {
            instructions += "Click on a fish to \"catch\" it.  If you get a high score,\n"
                         + "it can be recorded for others to see and compete against.";
        } else if ("Intermediate".equals(fishingOptions.getTitle())) {
            instructions += "Click on the selected fish to \"catch\" it.  Click on the\n"
                         + "wrong type of fish, and its value will be subtracted\n"
                         + "from your score.";
        } else {
            instructions += "Click any of the selected types of fish to \"catch\" them.\n"
                         + "Click on the wrong type of fish, and its value will be\n"
                         + "subtracted from your score.";
        }

        BitmapText instructionsText = new BitmapText(textFont, instructions);
        instructionsText.setPosition(padding, height);
        addDrawable(instructionsText);

        maxWidth = Math.max(maxWidth, instructionsText.getWidth());
        height += instructionsText.getHeight() + interLineSpacing;

        // Create the fish score text
        BitmapText fishStatsText = new BitmapText(textFont, "Fish Scoring:");
        fishStatsText.setPosition( padding, height );
        addDrawable(fishStatsText);

        maxWidth = Math.max( maxWidth, fishStatsText.getWidth() );
        height += fishStatsText.getHeight() + interLineSpacing / 2;

        // Generate the fish table displaying the available fish species
        FishTable table = new FishTable(fishingOptions, textFont, true);
        table.setY(height);
        addDrawable(table);

        maxWidth = Math.max( maxWidth, table.getWidth() );
        height += table.getHeight() + interLineSpacing;

        FishSelectorGroup fishSelectors = null;
        BitmapText fishSelectText = null;
        Slider speedSlider = null;
        BitmapText speedText = null;

        // Generate non-Beginner controls
        if ("Intermediate".equals(fishingOptions.getTitle()) || "Expert".equals(fishingOptions.getTitle())) {
            fishSelectText = new BitmapText(textFont, "Select Fish to Capture");
            fishSelectText.setPosition( padding, height );
            addDrawable(fishSelectText);

            maxWidth = Math.max( maxWidth, fishSelectText.getWidth() );
            height += fishSelectText.getHeight();

            // Generate the fish selector(s)
            // For Intermediate, there will be one.
            // For Expert, there will be three
            fishSelectors = new FishSelectorGroup(
                                    fishingOptions,
                                    "Expert".equals(fishingOptions.getTitle()) ? 3 : 1
                                );
            final FishSelectorGroup fSels = fishSelectors;
            fishSelectors.addValueChangedListener(new ValueChangedListener() {
                    @Override
                    public void valueChanged( ValueChangedEvent e ) {
//System.out.println("setTargetFishSpecies: "+ fSels.getSelections());
                        fishingOptions.setTargetFishSpecies( fSels.getSelections() );
                    } // valueChanged( ValueChangedEvent e )
                });
            fishSelectors.setY(height);
            fishSelectors.setSelections( fishingOptions.getTargetFishSpecies() );
            addDrawable(fishSelectors);

            maxWidth = Math.max( maxWidth, fishSelectors.getWidth() );
            height += fishSelectors.getHeight() + interLineSpacing;

            speedText = new BitmapText(textFont, "Fish Movement Speed");
            speedText.setY( height );
            addDrawable(speedText);

            maxWidth = Math.max( maxWidth, fishSelectors.getWidth() );
            height += speedText.getHeight();

            // For Intermediate or Expert, generate the Fish Speed slider
            speedSlider = new Slider( fishingOptions.getMinFishSpeed(), fishingOptions.getMaxFishSpeed() );
            speedSlider.setValue( fishingOptions.getMaxFishSpeed() );
            speedSlider.setUnscaledWidth( 250 );
            speedSlider.setY(height);
            addDrawable(speedSlider);

            // On change, set the max fish speed to the selected value
            speedSlider.addValueChangedListener(new ValueChangedListener() {
                    public void valueChanged( ValueChangedEvent e ) {
                        fishingOptions.setMaxFishSpeed( (Double) e.getNewValue() );
                    } // valueChanged( ValueChangedevent e )
                });

            maxWidth = Math.max( maxWidth, speedSlider.getWidth() );
            height += speedSlider.getHeight() + interLineSpacing;
        }

        // Add the action buttons
        buttons = new ButtonMenu(textFont);
        buttons.setButtonSpacing(interLineSpacing);
        buttons.addItem("Play", "Let's Play!");
        buttons.addItem("Cancel", "Cancel");
        buttons.setY(height);
        addDrawable(buttons);

        maxWidth = Math.max(buttons.getWidth(), maxWidth);
        height += buttons.getHeight();

        // Set the final size of the Dialog
        maxWidth += padding * 2;
        height += padding * 2;
        setUnscaledSize(maxWidth, height);

        // Align the rest of the controls
        title.setX((maxWidth - title.getWidth()) / 2);
        table.setX((maxWidth - table.getWidth()) / 2);
        buttons.setX((maxWidth - buttons.getWidth()) / 2);

        if (fishSelectors != null)
            fishSelectors.setX((maxWidth - fishSelectors.getWidth()) / 2);

        if (speedSlider != null) {
            speedSlider.setX((maxWidth - speedSlider.getWidth()) / 2);
            fishSelectText.setX((maxWidth - fishSelectText.getWidth()) / 2);
            speedText.setX((maxWidth - speedText.getWidth()) / 2);
        }
    } // UserOptionsDialog(FishingOptions options)


    public ButtonMenu getButtons() {
        return buttons;
    } // getButtons()


    @Override
    public void paint( Graphics2D g ) {
        titleFilter.setTime( titleFilter.getTime() + 0.05f );
        super.paint(g);
    } // paint( Graphics2D g ) {


} // class UserOptionsDialog
