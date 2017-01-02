package net.commstationfive.redapple;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

public class LemonPledgeCommand implements CommandExecutor {
	
	private static final Logger logger = RedApple.getLogger();

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		MessageChannel broadcast = MessageChannel.TO_ALL;
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			broadcast.send(Text.of(player.getName() + " demands more lemon pledge!"));		
			
		} else {
			
			broadcast.send(Text.of("The gods demand more lemon pledge!"));	
		}
		logger.debug("Demand for lemon pledge from source: " + src);
		
        return CommandResult.success();
	}

}
