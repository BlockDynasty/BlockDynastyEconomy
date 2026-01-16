package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialProvider;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

public class ItemStackCurrencyAdapter implements ItemStackCurrency {
    private final ItemStack itemStack;

    public ItemStackCurrencyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public NbtData getNbtData() {
        if (itemStack == null) {
            return new NbtData();
        }
        return MaterialProvider.getNBTData(itemStack);
    }

    @Override
    public  int getCantity() {
        return itemStack.getAmount();
    }

    @Override
    public Object getRoot() {
        return itemStack;
    }
}
