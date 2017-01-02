package net.commstationfive.redapple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.TargetEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

public class AFKCommand implements CommandExecutor {
	
	
	@SuppressWarnings("unused")
	private static final Logger logger = RedApple.getLogger();
	private static final Set<UUID> AFK_PLAYERS = new HashSet<>();
	private static final MessageChannel BROADCAST = MessageChannel.TO_ALL;
	
	private void goAFK(Player player) {
		
		Text message = Text.builder(player.getName() + " is now AFK").color(TextColors.RED).build();
		BROADCAST.send(message);
		AFK_PLAYERS.add(player.getUniqueId());
		
	}
	
	private void returnFromAFK(Player player) {
		
		Text message = Text.builder(player.getName() + " has returned!").color(TextColors.GREEN).build();
		BROADCAST.send(message);
		AFK_PLAYERS.remove(player.getUniqueId());
	}
	
	private void returnFromAFK(TargetEntityEvent event) {
		
		Entity entity = event.getTargetEntity();
		
		if(entity instanceof Player) {
			
			Player player = (Player) entity;
			
			if(AFK_PLAYERS.contains(player.getUniqueId())) {
				
				returnFromAFK(player);
				
			}
			
		}
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			if(!AFK_PLAYERS.contains(player.getUniqueId())) {
				
				goAFK(player);
				return CommandResult.success();
				
			} else {
				
				returnFromAFK(player);
				return CommandResult.success();		
			}
		}
		
        return CommandResult.empty();
	}
	
	@Listener
	public void onPlayerMove(MoveEntityEvent event) {
		
		returnFromAFK(event);
	}
	
	@Listener
	public void onPlayerChat(MessageChannelEvent.Chat event) {
		
		Cause cause = event.getCause();
		
		if(cause.containsType(Player.class)) {
			
			List<Player> involvedPlayers = cause.allOf(Player.class);
			for(Player player : involvedPlayers) {
				
				if(AFK_PLAYERS.contains(player.getUniqueId())) {
					
					returnFromAFK(player);
				}
			}
		}

	}
	
}
