package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;

import java.util.function.Function;

public class TextInput implements ITextInput {
    private static String input = "";

    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        title= Color.parse(title);
        initialText= Color.parse(initialText);
        System.out.println("|-------------Input--------------|");
        System.out.println("     === " + title + " ===");
        System.out.print("         User input: " + input + "\n");
        System.out.println("|--------------------------------|");
        String result = function.apply(input);
        owner.sendMessage("Result: " + result);
    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        title= Color.parse(title);
        initialText= Color.parse(initialText);
        System.out.println("|-------------Input--------------|");
        System.out.println("     === " + title + " ===");
        System.out.print("         User input: " + input + "\n");
        System.out.println("|--------------------------------|");

        String result = function.apply(input);
        owner.sendMessage("Result: " + result);
    }

    public static void setInput(String userInput) {
        input = userInput;
    }

    @Override
    public ITextInput asInputChat() {
        return this;
    }
}
