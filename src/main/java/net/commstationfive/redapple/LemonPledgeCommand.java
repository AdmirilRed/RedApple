package net.commstationfive.redapple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.google.inject.Inject;

public class LemonPledgeCommand implements CommandExecutor {
	
	@Inject
	private static final Logger logger = LoggerFactory.getLogger(LemonPledgeCommand.class);

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
	
	public Logger getLogger() {
		
		return logger;
	}

}
