package EngineTest.mocks;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.recipes.RecipeInventory;

public class Inventory implements IInventory {
    //matriz de items [][]
    private RecipeInventory recipeInventory;
    private final int columns=9;
    private IItemStack[][] inventory;


    public Inventory(RecipeInventory recipeInventory) {
        this.recipeInventory = recipeInventory;
        inventory = new IItemStack[columns][recipeInventory.getRows()];
    }

    @Override
    public void set(int slot, IItemStack item) {
        int column= slot % columns;
        int row= slot / columns;
        this.inventory[column][row] = item;
        //System.out.println(this.inventory[column][row].toString());
    }

    @Override
    public Object getHandle() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int slotWidth = 16;
        int slotHeight = 2; // Doble altura
        int titlePadding = (columns * (slotWidth + 1) + 1 - recipeInventory.getTitle().length() - 12) / 2;

        // Título centrado dinámicamente
        sb.append("+");
        sb.append("-".repeat(Math.max(0, titlePadding)));
        sb.append(" ").append(recipeInventory.getTitle()).append(" ");
        sb.append("-".repeat(Math.max(0, titlePadding)));
        sb.append("+\n");

        for (int r = 0; r < this.recipeInventory.getRows(); r++) {
            // Borde superior con números de slot
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                int slotNumber = r * columns + c;
                String slotText = String.format("[%02d]", slotNumber); // Formato: [00], [01], etc.

                // Calcular la posición para centrar el número en los guiones
                int totalDashes = slotWidth - slotText.length();
                int dashesBefore = totalDashes / 2;
                int dashesAfter = totalDashes - dashesBefore;

                sb.append("-".repeat(dashesBefore))
                        .append(slotText)
                        .append("-".repeat(dashesAfter))
                        .append("|");
            }
            sb.append("\n");

            // PRIMERA LÍNEA de la celda (doble altura)
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                IItemStack item = inventory[c][r];
                String cellContent;

                if (item != null) {
                    String materialName = item.toString();
                    int maxVisualLength = slotWidth - 2;

                    // Primera línea: truncar manteniendo ANSI
                    String firstLine = truncateWithAnsi(materialName, maxVisualLength);
                    int visualLength = getVisualLength(firstLine);

                    // Calcular padding basado en longitud visual
                    String padding = " ".repeat(maxVisualLength - visualLength);

                    // Asegurar que los colores se resetee al final de la celda
                    String resetIfNeeded = firstLine.contains("\u001B") ? "\u001B[0m" : "";
                    cellContent = String.format(" %s%s%s ", firstLine, resetIfNeeded, padding);
                } else {
                    cellContent = " ".repeat(slotWidth);
                }
                sb.append(cellContent).append("|");
            }
            sb.append("\n");

            // SEGUNDA LÍNEA de la celda (doble altura)
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                IItemStack item = inventory[c][r];
                String cellContent;

                if (item != null) {
                    String materialName = item.toString();
                    int maxVisualLength = slotWidth - 2;

                    // Calcular cuánto texto se mostró en la primera línea
                    String firstLine = truncateWithAnsi(materialName, maxVisualLength);
                    int firstLineVisualLength = getVisualLength(firstLine);

                    // Obtener el texto restante después de la primera línea
                    String remainingText = materialName;
                    if (firstLineVisualLength < getVisualLength(materialName)) {
                        // Remover el texto visual que ya se mostró (no los códigos ANSI)
                        StringBuilder temp = new StringBuilder();
                        int visualCharsRemoved = 0;
                        boolean inAnsiCode = false;

                        for (int i = 0; i < materialName.length(); i++) {
                            char ch = materialName.charAt(i);

                            if (ch == '\u001B') {
                                inAnsiCode = true;
                                temp.append(ch);
                            } else if (inAnsiCode) {
                                temp.append(ch);
                                if (ch == 'm') {
                                    inAnsiCode = false;
                                }
                            } else {
                                if (visualCharsRemoved < firstLineVisualLength) {
                                    visualCharsRemoved++;
                                } else {
                                    // Empezar a guardar desde aquí
                                    temp.append(materialName.substring(i));
                                    break;
                                }
                            }
                        }
                        remainingText = temp.toString();
                    } else {
                        // No hay más texto después de la primera línea
                        remainingText = "";
                    }

                    // Segunda línea: truncar el texto restante
                    String secondLine = truncateWithAnsi(remainingText, maxVisualLength);
                    int visualLength = getVisualLength(secondLine);

                    // Calcular padding basado en longitud visual
                    String padding = " ".repeat(maxVisualLength - visualLength);

                    // Asegurar que los colores se resetee al final de la celda
                    String resetIfNeeded = secondLine.contains("\u001B") ? "\u001B[0m" : "";
                    cellContent = String.format(" %s%s%s ", secondLine, resetIfNeeded, padding);
                } else {
                    cellContent = " ".repeat(slotWidth);
                }
                sb.append(cellContent).append("|");
            }
            sb.append("\n");
        }

        // Borde inferior con números de slot (opcional)
        sb.append("|");
        for (int c = 0; c < columns; c++) {
            sb.append("-".repeat(slotWidth)).append("|");
        }
        sb.append("\n");

        return sb.toString();
    }

    // Función auxiliar para eliminar códigos ANSI y obtener longitud visual
    private int getVisualLength(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "").length();
    }


    // Función auxiliar para truncar texto manteniendo códigos ANSI
    private String truncateWithAnsi(String text, int maxVisualLength) {
        if (getVisualLength(text) <= maxVisualLength) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        int visualLength = 0;
        boolean inAnsiCode = false;
        StringBuilder ansiBuffer = new StringBuilder();

        for (int i = 0; i < text.length() && visualLength < maxVisualLength; i++) {
            char c = text.charAt(i);

            if (c == '\u001B') {
                inAnsiCode = true;
                ansiBuffer.setLength(0);
                ansiBuffer.append(c);
            } else if (inAnsiCode) {
                ansiBuffer.append(c);
                if (c == 'm') {
                    inAnsiCode = false;
                    result.append(ansiBuffer);
                }
            } else {
                result.append(c);
                visualLength++;
            }
        }

        return result.toString();
    }
}
