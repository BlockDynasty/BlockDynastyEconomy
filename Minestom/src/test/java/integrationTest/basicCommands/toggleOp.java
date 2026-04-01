package integrationTest.basicCommands;

import net.blockdynasty.economy.minestom.services.PermsServiceDefault;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class toggleOp extends Command {

    public toggleOp() {
        super("op");
        setDefaultExecutor((sender, commandContext) -> {
            Player player = (Player) sender;
            PermsServiceDefault.setOp(player, !PermsServiceDefault.isOp(player));
        });
    }
}
