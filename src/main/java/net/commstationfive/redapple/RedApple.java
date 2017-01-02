package net.commstationfive.redapple;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

@Plugin(id = "redapple", name = "RedApple", version = "1.0")
public class RedApple {
	
	private static final Logger logger = LoggerFactory.getLogger(RedApple.class);
	private static final CommandManager cmdManager = Sponge.getCommandManager();
	private static final EventManager evntManager = Sponge.getEventManager();
	
	@SuppressWarnings("unused")
	@Inject
	private Game game;
	
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;
	
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    	
    	CommandSpec lemonPledgeCommandSpec = CommandSpec.builder()
    		    .description(Text.of("Demand lemon pledge!"))
    		    .permission("redapple.command.lemonpledge")
    		    .executor(new LemonPledgeCommand())
    		    .build();
    	
    	CommandSpec afkCommandSpec = CommandSpec.builder()
    			.description(Text.of("Let other players know you are not at the keyboard. Lock your movement using \"/afk lock\"."))
    			.permission("redapple.command.afk")
    			.arguments(GenericArguments.optional(GenericArguments.literal(Text.of("Lock movement"), "lock")))
    			.executor(new AFKCommand())
    			.build();
    	
    	CommandSpec pingCommandSpec = CommandSpec.builder()
    			.description(Text.of("Test the response time of the server"))
    			.permission("redapple.command.ping")
    			.executor(new PingCommand())
    			.build();
       	
    	cmdManager.register(this, lemonPledgeCommandSpec, "lemonpledge"); 
    	cmdManager.register(this, afkCommandSpec, "afk");
    	cmdManager.register(this, pingCommandSpec, "ping");

    	evntManager.registerListeners(this, new AFKCommand());

        logger.info("RedApple loaded!");
    }
    
    public static Logger getLogger() {
    	
    	return logger;
    }
    
}
