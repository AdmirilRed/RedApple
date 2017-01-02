package net.commstationfive.redapple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

public class AFKCommand implements CommandExecutor {
	
	
	@SuppressWarnings("unused")
	private static final Logger logger = RedApple.getLogger();
	private static final Map<UUID, Boolean> AFK_PLAYERS = new HashMap<>();
	private static final MessageChannel BROADCAST = MessageChannel.TO_ALL;
	
	private void goAFK(Player player, boolean lockMovement) {
		
		Text message = Text.builder(player.getName() + " is now AFK").color(TextColors.RED).build();
		BROADCAST.send(message);
		AFK_PLAYERS.put(player.getUniqueId(), lockMovement);
		
	}
	
	private void returnFromAFK(Player player) {
		
		Text message = Text.builder(player.getName() + " has returned!").color(TextColors.GREEN).build();
		BROADCAST.send(message);
		AFK_PLAYERS.remove(player.getUniqueId());
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		boolean lockMovement = false;
		
		if(args.hasAny("Lock movement"))
			lockMovement = true;
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			if(!AFK_PLAYERS.keySet().contains(player.getUniqueId())) {
				
				goAFK(player, lockMovement);
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
		
		Entity entity = event.getTargetEntity();
		
		if(entity instanceof Player) {
			
			Player player = (Player) entity;
			UUID id = player.getUniqueId();
			
			if(AFK_PLAYERS.get(id) != null) {
				
				boolean lockMovement = AFK_PLAYERS.get(id);
				
				if(lockMovement) {
					
					event.setCancelled(true);
					
					Text message = Text.builder("You are AFK! Type /afk to re-enable movement.").color(TextColors.AQUA).build();
					player.sendMessage(message);
				} else {
					
					returnFromAFK(player);
				}
				
			}
			
		}
	}
	
	@Listener
	public void onPlayerChat(MessageChannelEvent.Chat event) {
		
		Cause cause = event.getCause();
		
		if(cause.containsType(Player.class)) {
			
			List<Player> involvedPlayers = cause.allOf(Player.class);
			for(Player player : involvedPlayers) {
				
				if(AFK_PLAYERS.get(player.getUniqueId()) != null) {
					
					returnFromAFK(player);
				}
			}
		}

	}
	
}
