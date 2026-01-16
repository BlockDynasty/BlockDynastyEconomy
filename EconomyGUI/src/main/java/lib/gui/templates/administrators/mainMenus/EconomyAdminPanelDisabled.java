package lib.gui.templates.administrators.mainMenus;

import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.generics.AbstractPanel;

public class EconomyAdminPanelDisabled implements IGUI {
    private IEntityGUI owner;
    public EconomyAdminPanelDisabled(IEntityGUI owner) {
        this.owner = owner;
    }

    @Override
    public void open() {
        owner.sendMessage("GUI is disabled.");
    }

    @Override
    public void close() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void handleLeftClick(int slot) {

    }

    @Override
    public void handleRightClick(int slot) {

    }

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public IGUI getParent() {
        return null;
    }

    @Override
    public void openParent() {

    }

    @Override
    public void fill() {

    }
}
