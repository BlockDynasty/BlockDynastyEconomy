package lib.gui.components;

import java.util.function.Consumer;

public interface IButton {
    Consumer<IEntityGUI> getLeftClickAction();
    Consumer<IEntityGUI> getRightClickAction();
    IItemStack getItemStack();
}
