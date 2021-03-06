package de.tobiyas.racesandclasses.commands.level;

import static de.tobiyas.racesandclasses.translation.languages.Keys.number_not_readable;
import static de.tobiyas.racesandclasses.translation.languages.Keys.player_not_exist;
import static de.tobiyas.racesandclasses.translation.languages.Keys.success;
import static de.tobiyas.racesandclasses.translation.languages.Keys.value_0_not_allowed;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.APIs.LanguageAPI;
import de.tobiyas.racesandclasses.playermanagement.leveling.PlayerLevelManager;

public class Command_RACLevel implements CommandExecutor {

	/**
	 * The plugin called stuff upon
	 */
	private RacesAndClasses plugin;
	
	
	/**
	 * Registers the Command "class" to the plugin.
	 */
	public Command_RACLevel(){
		plugin = RacesAndClasses.getPlugin();

		String command = "raclevel";
		if(plugin.getConfigManager().getGeneralConfig().getConfig_general_disable_commands().contains(command)) return;
		
		try{
			plugin.getCommand(command).setExecutor(this);
		}catch(Exception e){
			plugin.log("ERROR: Could not register command /" + command + ".");
		}
	}	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length != 3){
			pasteHelp(sender);
			return true;
		}
		
		String subCommand = args[1];
		String player = args[0];
		int value = 0;
		
		try{
			value = Integer.valueOf(args[2]);
		}catch(NumberFormatException exp){
			LanguageAPI.sendTranslatedMessage(sender, number_not_readable);
			return true;
		}
		
		PlayerLevelManager manager = plugin.getPlayerManager().getPlayerLevelManager(player);
		if(manager == null){
			LanguageAPI.sendTranslatedMessage(sender, player_not_exist,
					"player", player);
			return true;
		}
		
		if(value == 0){
			LanguageAPI.sendTranslatedMessage(sender, value_0_not_allowed,
					"player", player);
			return true;
		}
		
		
		if("exp".equalsIgnoreCase(subCommand)){
			if(value < 0){
				manager.removeExp(-value);
			}
			
			if(value > 0){
				manager.addExp(value);
			}
			
			LanguageAPI.sendTranslatedMessage(sender, success);
			return true;
		}
		
		if("lvl".equalsIgnoreCase(subCommand)){
			int newLevel = manager.getCurrentLevel() + value;
			manager.setCurrentLevel(newLevel);
			
			LanguageAPI.sendTranslatedMessage(sender, success);
			return true;
		}
		
		pasteHelp(sender);
		return true;
	}

	/**
	 * Paste the help for the Level command
	 * 
	 * @param sender to send to
	 */
	private void pasteHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "=== RAC Level ===");
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "/raclevel <playerName> exp <value>" 
				+ ChatColor.YELLOW + "  Adds / Removes EXP to player.");
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "/raclevel <playerName> lvl <value>"
				+ ChatColor.YELLOW + "  Adds / Removes LEVELS to player.");
	}

}
