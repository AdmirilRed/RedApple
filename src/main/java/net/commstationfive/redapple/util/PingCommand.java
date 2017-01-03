package net.commstationfive.redapple.util;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PingCommand implements CommandExecutor {

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		Text message = Text.builder("Pong!").color(TextColors.YELLOW).build();
		src.sendMessage(message);
		
		return CommandResult.success();
	}
	
	

}
