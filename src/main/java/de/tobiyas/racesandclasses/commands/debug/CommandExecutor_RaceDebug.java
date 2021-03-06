package de.tobiyas.racesandclasses.commands.debug;

import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.util.consts.Consts;
import de.tobiyas.racesandclasses.util.consts.PermissionNode;

public class CommandExecutor_RaceDebug implements CommandExecutor {

	private RacesAndClasses plugin;
	
	public CommandExecutor_RaceDebug(){
		plugin = RacesAndClasses.getPlugin();

		String command = "racedebug";
		if(plugin.getConfigManager().getGeneralConfig().getConfig_general_disable_commands().contains(command)) return;
		
		try{
			plugin.getCommand(command).setExecutor(this);
		}catch(Exception e){
			plugin.log("ERROR: Could not register command /" + command + ".");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		
		if(!plugin.getPermissionManager().checkPermissions(sender, PermissionNode.debug)) return true;
		
		if(args.length == 1){
			String commandString = args[0];
			if(commandString.equalsIgnoreCase("scan")){
				long timeTook = runDebugScan();
				sender.sendMessage(ChatColor.GREEN + "Full Scan finished (" + timeTook + "ms) and logged in Debug file.");
				return true;
			}
			
			if(commandString.equalsIgnoreCase("timing")){
				sender.sendMessage(ChatColor.GREEN + "Profiling Event-Timings. Results in " + ChatColor.LIGHT_PURPLE + 
									Consts.timingLength + ChatColor.GREEN + " seconds.");
				new DebugTimingEvents(sender);
				return true;
			}
			
			if(commandString.equalsIgnoreCase("testerror")){ //Produces Error! Only for testing!
				if(sender instanceof Player && !((Player)sender).isOp()){
					sender.sendMessage(ChatColor.RED + "You may not use this command!");
					return true;
				}
				
				sender.sendMessage(ChatColor.GREEN + "Error is beeing fired!");
				try{
					throw new NullPointerException("useless generated exeption.");
				}catch(NullPointerException e){
					plugin.getDebugLogger().logStackTrace(e);
					sender.sendMessage(ChatColor.GREEN + "Worked!");
					return true;
				}
			}			
			
			sender.sendMessage(ChatColor.RED + "No debug command found for: " + ChatColor.LIGHT_PURPLE + commandString);
			return true;
		}
		
		postHelp(sender);
		return true;
	}
	
	private void postHelp(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "Wrong usage. The correct usage is one of the following:");
		sender.sendMessage(ChatColor.RED + "/racedebug " + ChatColor.LIGHT_PURPLE + "scan");
		sender.sendMessage(ChatColor.RED + "/racedebug " + ChatColor.LIGHT_PURPLE + "timing");
		sender.sendMessage(ChatColor.RED + "/racedebug " + ChatColor.LIGHT_PURPLE + "testerror");
		sender.sendMessage(ChatColor.RED + "Others coming soon.");
	}
	
	private long runDebugScan(){
		long startTime = System.currentTimeMillis();
		plugin.getDebugLogger().log("------------------------------------------------------------------");
		plugin.getDebugLogger().log("Running Full debug Scan");
		
		Properties props = System.getProperties();
		plugin.getDebugLogger().log("============System Properties============");
		for(Object objProp : props.keySet()){
			String prop = (String) objProp;
			String value = props.getProperty(prop);
			plugin.getDebugLogger().log("Property: " + prop + " value: " + value);
		}
		
		long timeTook = System.currentTimeMillis() - startTime;
		plugin.getDebugLogger().log("Full debug scan finished. It took: " + timeTook + "ms.");
		plugin.getDebugLogger().log("------------------------------------------------------------------");
		
		return timeTook;
	}

}
