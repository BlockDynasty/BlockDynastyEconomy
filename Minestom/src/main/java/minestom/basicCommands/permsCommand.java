package minestom.basicCommands;

import minestom.PermsServiceDefault;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class permsCommand extends Command {

    public permsCommand() {
        super("perms");

        //setArgs ("<permission>"); and playerName if you want to add perms to other players
        setDefaultExecutor((sender, commandContext) -> {
            Player player = (Player) sender;
            //commandContext.getInput();
            PermsServiceDefault.addPermission(player, "BlockDynastyEconomy.players.bank");
            PermsServiceDefault.addPermission(player, "BlockDynastyEconomy.players.offer");
            PermsServiceDefault.addPermission(player, "BlockDynastyEconomy.players.balance");
            PermsServiceDefault.addPermission(player, "BlockDynastyEconomy.players.exchange");
            PermsServiceDefault.addPermission(player, "BlockDynastyEconomy.players.pay");
        });
    }
}
