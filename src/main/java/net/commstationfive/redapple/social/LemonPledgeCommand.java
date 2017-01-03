package net.commstationfive.redapple.social;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import net.commstationfive.redapple.RedApple;

public class LemonPledgeCommand implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private static final Logger logger = RedApple.getLogger();

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		MessageChannel broadcast = MessageChannel.TO_ALL;
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			
			Text message = Text.builder(player.getName() + " demands more lemon pledge!").color(TextColors.GOLD).build();
			broadcast.send(message);		
			
		} else {
			
			Text message = Text.builder("The gods demand more lemon pledge!").color(TextColors.GOLD).build();
			broadcast.send(message);	
		}
		
        return CommandResult.success();
	}

}
