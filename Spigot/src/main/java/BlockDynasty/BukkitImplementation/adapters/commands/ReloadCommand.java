package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.platformAdapter.EntityConsoleAdapter;
import lib.commands.abstractions.AbstractCommand;
import util.colors.ChatColor;
import util.colors.Colors;

public class ReloadCommand extends AbstractCommand {
    private BlockDynastyEconomy plugin ;

    public ReloadCommand(BlockDynastyEconomy plugin) {
        super("reload", "BlockDynastyEconomy.economy.superUser");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(lib.commands.abstractions.IEntityCommands sender, String[] args) {
        if(!super.execute(sender, args)){
            return false;
        }
        if(sender instanceof EntityConsoleAdapter){
            plugin.reload();
        }else{
            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"This command can only be executed by the console.");
        }
        return true;
    }
}
