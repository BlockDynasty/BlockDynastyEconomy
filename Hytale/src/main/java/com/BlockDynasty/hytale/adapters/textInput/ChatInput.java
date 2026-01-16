package com.BlockDynasty.hytale.adapters.textInput;

import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;

import java.util.function.Function;

public class ChatInput implements ITextInput {

    @Override
    public void open(IEntityGUI iEntityGUI, String s, String s1, Function<String, String> function) {
    }

    @Override
    public void open(IGUI igui, IEntityGUI iEntityGUI, String s, String s1, Function<String, String> function) {

    }

    @Override
    public ITextInput asInputChat() {
        return this;
    }
}
