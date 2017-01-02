package net.commstationfive.redapple;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.google.inject.Inject;

public class AFKCommand implements CommandExecutor {
	
	@Inject
	private static final Logger logger = LoggerFactory.getLogger(LemonPledgeCommand.class);
	
	private static final Set<UUID> AFK_PLAYERS = new HashSet<>();
	private static final MessageChannel BROADCAST = MessageChannel.TO_ALL;

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			if(!AFK_PLAYERS.contains(player.getUniqueId())) {
				
				BROADCAST.send(Text.of(player.getName() + " is now AFK."));
				AFK_PLAYERS.add(player.getUniqueId());
				
				return CommandResult.success();
			}
		}
		
        return CommandResult.empty();
	}
	
	@Listener
	private void onPlayerMove(MoveEntityEvent event) {
		
		Entity entity = event.getTargetEntity();
		
		if(entity instanceof Player) {
			
			Player player = (Player) entity;
			if(AFK_PLAYERS.contains(player.getUniqueId())) {
				
				BROADCAST.send(Text.of(player.getName() + " has returned!"));
				AFK_PLAYERS.remove(player.getUniqueId());
				
			}	
		}
		
	}
	
	public Logger getLogger() {
		
		return logger;
	}
	
}
