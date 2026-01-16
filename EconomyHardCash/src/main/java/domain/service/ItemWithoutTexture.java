package domain.service;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import abstractions.platform.materials.Materials;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import util.colors.ChatColor;
import util.colors.Colors;

import java.math.BigDecimal;

public class ItemWithoutTexture implements ItemCreator {
    private HardCashCreator platform;
    public ItemWithoutTexture(HardCashCreator platform) {
        this.platform = platform;
    }

    @Override
    public ItemStackCurrency create(ICurrency currency, BigDecimal amount) {
        NbtData nbtData = new NbtData(currency.getSingular(),currency.getUuid().toString(),amount.toString());
        String color = ChatColor.stringValueOf(currency.getColor());
        RecipeItemCurrency recipe = RecipeItemCurrency.builder()
                .setNbtData(nbtData)
                .setLore("","Value: " +color+ currency.format(amount),"",
                        ChatColor.stringValueOf(Colors.GOLD)+"Consumable item.",
                        ChatColor.stringValueOf(Colors.GOLD)+"Trade it or store it safely!","",
                        ChatColor.stringValueOf(Colors.GRAY)+"[Valid for Dynasty Economy System]")
                .setName(color + currency.getSingular())
                .setMaterial(Materials.GOLD_INGOT)
                .build();
        return platform.createItemStackCurrency(recipe);
    }
}
