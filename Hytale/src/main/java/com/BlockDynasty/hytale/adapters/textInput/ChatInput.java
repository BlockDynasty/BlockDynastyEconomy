package com.BlockDynasty.hytale.adapters.textInput;

import au.ellie.hyui.builders.*;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ChatInput implements ITextInput {

    @Override
    public void open(IEntityGUI iEntityGUI, String s, String s1, Function<String, String> function) {
        PlayerRef playerRef = (PlayerRef) iEntityGUI.getRoot();
        PageBuilder pageBuilder = new PageBuilder(playerRef);
        GroupBuilder groupBuilder = new GroupBuilder();
        groupBuilder.withId("ParentGroup")
                .withAnchor(new HyUIAnchor().setWidth(800).setHeight(500))
                //.withStyle(new HyUIStyle()
                .withLayoutMode("Top")
                .inside("#Content");
        AtomicReference<String> input = new  AtomicReference<>("");
        groupBuilder.addChild(
                TextFieldBuilder.textInput()
                        .withId("MyTextField")
                        .withValue(s1)
                        .addEventListener(CustomUIEventBindingType.ValueChanged, (val, ctx) -> {
                            //playerRef.sendMessage(Message.raw("Text Field changed to: " + val));
                            input.set(val);
                        }));
        groupBuilder.addChild(
                ButtonBuilder.textButton()
                        .withId("Submit")
                        .withText("Submit")
                        .addEventListener(CustomUIEventBindingType.Activating, (val, ctx) -> {
                            function.apply(input.get());
                        })
        );

        pageBuilder.addElement(groupBuilder)
                .open(playerRef.getReference().getStore());
    }

    @Override
    public void open(IGUI igui, IEntityGUI iEntityGUI, String s, String s1, Function<String, String> function) {
        PlayerRef playerRef = (PlayerRef) iEntityGUI.getRoot();
        PageBuilder pageBuilder = new PageBuilder(playerRef);
        GroupBuilder groupBuilder = new GroupBuilder();
        groupBuilder.withId("ParentGroup")
                .withAnchor(new HyUIAnchor().setWidth(800).setHeight(500))
                //.withStyle(new HyUIStyle()
                .withLayoutMode("Top")
                .inside("#Content");
        AtomicReference<String> input = new  AtomicReference<>("");
        groupBuilder.addChild(
                TextFieldBuilder.textInput()
                        .withId("MyTextField")
                        .withValue(s1)
                        .addEventListener(CustomUIEventBindingType.ValueChanged, (val, ctx) -> {
                            //playerRef.sendMessage(Message.raw("Text Field changed to: " + val));
                            input.set(val);
                        }));
        groupBuilder.addChild(
                ButtonBuilder.textButton()
                        .withId("Submit")
                        .withText("Submit")
                        .addEventListener(CustomUIEventBindingType.Activating, (val, ctx) -> {
                            function.apply(input.get());
                        })
        );
        groupBuilder.addChild(ButtonBuilder.textButton()
                .withId("Cancel")
                .withText("Cancel")
                .addEventListener(CustomUIEventBindingType.Activating, (val, ctx) -> {
                    igui.open();
                })
        );

        pageBuilder.addElement(groupBuilder)
                .open(playerRef.getReference().getStore());
    }

    @Override
    public ITextInput asInputChat() {
        return this;
    }
}
