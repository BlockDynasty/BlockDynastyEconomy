package lib.gui.components.generics;

import lib.gui.components.IButton;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IItemStack;

import java.util.function.Consumer;

public class Button implements IButton {
    private final IItemStack itemStack;
    private final Consumer<IEntityGUI> leftClickAction;
    private final Consumer<IEntityGUI> rightClickAction;

    private Button(IItemStack itemStack, Consumer<IEntityGUI> leftClickAction, Consumer<IEntityGUI> rightClickAction) {
        this.itemStack = itemStack;
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
    }

    @Override
    public Consumer<IEntityGUI> getLeftClickAction() {
        return leftClickAction;
    }

    @Override
    public Consumer<IEntityGUI> getRightClickAction() {
        return rightClickAction;
    }

    @Override
    public IItemStack getItemStack() {
        return itemStack;
    }

    public static ButtonBuilder builder() {
        return new ButtonBuilder();
    }

    //builder pattern.
    public static class ButtonBuilder {
        private IItemStack itemStack = null;
        private Consumer<IEntityGUI> leftClickAction = null;
        private Consumer<IEntityGUI> rightClickAction = null;

        public ButtonBuilder setItemStack(IItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public ButtonBuilder setLeftClickAction(Consumer<IEntityGUI> leftClickAction) {
            this.leftClickAction = leftClickAction;
            return this;
        }

        public ButtonBuilder setRightClickAction(Consumer<IEntityGUI> rightClickAction) {
            this.rightClickAction = rightClickAction;
            return this;
        }

        public Button build() {
            return new Button(itemStack, leftClickAction, rightClickAction);
        }
    }


}
