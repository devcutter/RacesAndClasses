package de.tobiyas.races.commands.general;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.tobiyas.races.Races;
import de.tobiyas.races.datacontainer.health.HealthManager;

public class CommandExecutor_HP implements CommandExecutor {

	private Races plugin;
	
	public CommandExecutor_HP(){
		plugin = Races.getPlugin();
		try{
			plugin.getCommand("playerhealth").setExecutor(this);
		}catch(Exception e){
			plugin.log("ERROR: Could not register command /playerhealth.");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only Players can use this command.");
			return true;
		}
		Player player = (Player) sender;
		if(!HealthManager.getHealthManager().displayHealth(player.getName()))
			player.sendMessage(ChatColor.RED + "Something gone Wrong. No healthcontainer found for you.");
		return true;
	}

}