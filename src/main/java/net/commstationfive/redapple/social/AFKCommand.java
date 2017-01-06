package net.commstationfive.redapple.social;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import net.commstationfive.redapple.RedApple;

public class AFKCommand implements CommandExecutor {
	
	
	@SuppressWarnings("unused")
	private static final Logger logger = RedApple.getLogger();
	private static final Map<Player, Boolean> AFK_PLAYERS = new HashMap<>();
	private static final Map<Player, Set<Player>> PLAYER_MENTIONS = new HashMap<>();
	private static final MessageChannel BROADCAST = MessageChannel.TO_ALL;
	
	private void goAFK(Player player, boolean lockMovement) {
		
		Text message = Text.builder(player.getName() + " is now AFK").color(TextColors.RED).build();
		BROADCAST.send(message);
		AFK_PLAYERS.put(player, lockMovement);
		
	}
	
	private void returnFromAFK(Player player) {
		
		Text message = Text.builder(player.getName() + " has returned!").color(TextColors.GREEN).build();
		BROADCAST.send(message);
		
		Set<Player> mentions = PLAYER_MENTIONS.get(player);
		if(mentions != null && mentions.size() > 0) {
			
			StringBuilder notificationBuilder = new StringBuilder();
			notificationBuilder.append("The following player(s) tried to get your attention while you were AFK: ");
			
			int remainingMentions = mentions.size();
			for(Player mention : mentions) {
				
				notificationBuilder.append(mention.getName());
				
				if(remainingMentions > 1)
					notificationBuilder.append(", ");
				
				remainingMentions--;
			}
			
			Text notification = Text.builder(notificationBuilder.toString()).color(TextColors.AQUA).build();
			player.sendMessage(notification);
			
		}
		
		removePlayer(player);
	}
	
	private void removePlayer(Player player) {
		
		AFK_PLAYERS.remove(player);
		PLAYER_MENTIONS.remove(player);
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws
	CommandException {
		
		boolean lockMovement = false;
		
		if(args.hasAny("Lock movement"))
			lockMovement = true;
		
		if(src instanceof Player) {
			
			Player player = (Player) src;
			if(!AFK_PLAYERS.keySet().contains(player)) {
				
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
		
		if(!AFK_PLAYERS.isEmpty()) {
			
			Entity entity = event.getTargetEntity();
			if(entity instanceof Player) {
				
				Player player = (Player) entity;	
				if(AFK_PLAYERS.get(player) != null) {
					
					boolean lockMovement = AFK_PLAYERS.get(player);
					
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
	}
	
	@Listener
	public void onPlayerChat(MessageChannelEvent.Chat event) {
		
		if(!AFK_PLAYERS.isEmpty()) {
			
			Cause cause = event.getCause();
			String message = event.getMessage().toString();
			
			if(cause.containsType(Player.class)) {
				
				List<Player> involvedPlayers = cause.allOf(Player.class);
				for(Player player : AFK_PLAYERS.keySet()) {
					
					if(involvedPlayers.contains(player)) {
						
						returnFromAFK(player);
						
					} else if(message.contains(player.getName())) {
						
						Set<Player> mentions = PLAYER_MENTIONS.get(player);
						if(mentions == null) {
							
							mentions = new HashSet<Player>();
							PLAYER_MENTIONS.put(player, mentions);
						}
						
						for(Player source : involvedPlayers) {
							
							StringBuilder notificationBuilder = 
									new StringBuilder(player.getName());
							notificationBuilder.append(" is AFK. They will be notified of your message when they return.");
							
							Text notification = Text.builder(notificationBuilder.toString()).color(TextColors.AQUA).build();
							source.sendMessage(notification);
							
							mentions.add(source);
						}
						
					}
				}
				
				
				
			}
		}

	}
	
	@Listener
	public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {
		
		if(!AFK_PLAYERS.isEmpty()) {
			
			Player player = event.getTargetEntity();
			removePlayer(player);
		}
	}
	
}
