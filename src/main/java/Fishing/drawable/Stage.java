
package Fishing.drawable;

import java.util.Objects;
import javax.swing.JPanel;

/**
 * Drawable subclass representing a Stage container for rendering a scene
 * within.
 * 
 * <p>All display lists start with a Scene instance as the root.  Stages may be
 * nested, in which case the Stage element will act as a clipping container
 * within the parent scene.
 * 
 * @author Brad
 */
public class Stage
    extends Drawable
{

    /**
     * The Swing panel displaying this Stage.
     * 
     * <p>This property is only non-{@code null} for a root-level Stage.
     */
    private JPanel panel;

    /**
     * The FocusManager for this Stage.
     * 
     * <p>This property is only non-{@code null} for a root-level Stage.
     */
    private FocusManager focusManager;

    /**
     * Determines whether this is a root level stage or not.
     * 
     * @return  {@code true} if this is a root level stage, {@code false} if
     *          not.
     */
    public boolean isRootStage() {
        return (panel != null);
    } // isRootStage()

    @Override
    public Stage getRoot() {
        return isRootStage()
                ? this
                : super.getRoot();
    } // getRoot()

    @Override
    public Stage getStage() {
        return isRootStage()
                ? this
                : super.getStage();
    } // getStage()

    /**
     * Retrieves the Swing panel displaying this Stage.
     * @return  The Swing panel displaying this Stage, or {@code null) if there
     *          is no associated panel.
     */
    public JPanel getPanel() {
        if (isRootStage()) {
            return panel;
        }

        // Not a root stage, so return the panel for the containing stage (if any).
        Stage stage = getStage();

        return (stage != null)
                ? stage.getPanel()
                : null;
    } // getPanel()

    @Override
    public FocusManager getFocusManager() {
        return isRootStage()
                ? focusManager
                : super.getFocusManager();
    } // getFocusManager()

    /**
     * Constructs a new root Stage.
     *
     * <p>The new instance's {@code root} and {@code stage} properties will be
     * set to reference itself.
     * 
     * @param   panel   The Swing panel which will be displaying this Stage.
     * 
     * @throws  NullPointerException if {@code panel} is {@code null}.
     */
    public Stage( JPanel panel ) {
        this.panel = Objects.requireNonNull(panel);
        this.focusManager = new FocusManager(this);
        initCommonProperties();
    } // Stage

    /**
     * Constructs a new sub-Stage.
     * 
     * <p>A Stage of this type must be added as a descendant of a root stage
     * to be rendered.
     */
    public Stage() {
        initCommonProperties();
    } // Stage

    /**
     * Initializes common stage properties to appropriate values for a stage.
     */
    private void initCommonProperties() {
        setMouseChildren(true);
        setClipChildren(true);
        setFocusable(true);

        final Stage self = this;

        BehaviorUtil.addFocusableBehavior(this);

        BehaviorUtil.addTabbableContainerBehavior(this);
    } // initCommonProperties()

    /**
     * {@inheritDoc}
     * @throws  UnsupportedOperationException if called on a root stage.
     */
    @Override
    public void setVisible(boolean value) {
        if (panel != null)
            throw new UnsupportedOperationException("A root stage cannot be hidden");

        super.setVisible(value);
    } // setVisible

} // Stage
