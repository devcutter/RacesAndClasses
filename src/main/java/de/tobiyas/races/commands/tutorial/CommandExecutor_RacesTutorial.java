package de.tobiyas.races.commands.tutorial;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.tobiyas.races.Races;
import de.tobiyas.races.tutorial.TutorialManager;
import de.tobiyas.races.util.tutorial.TutorialState;

public class CommandExecutor_RacesTutorial implements CommandExecutor{

	private Races plugin;
	
	public CommandExecutor_RacesTutorial(){
		plugin = Races.getPlugin();
		try{
			plugin.getCommand("racestutorial").setExecutor(this);
		}catch(Exception e){
			plugin.log("ERROR: Could not register command /racestutorial.");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		
		if(!plugin.getGeneralConfig().getConfig_tutorials_enable()){
			sender.sendMessage(ChatColor.RED + "Tutorials not enabled.");
			return true;
		}
		
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only Players can use this command.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 0){
			postHelp(player);
			return true;
		}
		
		String commandString = args[0];
		
		if(commandString.equalsIgnoreCase("set")){
			if(args.length != 2 || TutorialState.getState(args[1]) == TutorialState.none){
				player.sendMessage(ChatColor.RED + "The command needs a valid new State! Valid Stats are: ");
				String stateString = "";
				for(TutorialState state : TutorialState.values())
					if(state != TutorialState.none)
						stateString += ChatColor.AQUA + state.name() + ChatColor.RED + ", ";
				
				stateString = stateString.substring(0, stateString.length() - 2);
				player.sendMessage(stateString);
				return true;
			}
			
			setState(player, args[1]);
			return true;
		}
		
		if(commandString.equalsIgnoreCase("start")){
			
			if(args.length != 1){
				postHelp(player);
				return true;
			}

			tutorialStart(player);
			return true;
		}
		
		if(commandString.equalsIgnoreCase("stop")){
			if(args.length != 1){
				postHelp(player);
				return true;
			}
			
			tutorialStop(player);
			return true;
		}
		
		if(commandString.equalsIgnoreCase("skip")){
			if(args.length != 1){
				postHelp(player);
				return true;
			}
			
			tutorialSkip(player);
			return true;
		}
		
		if(commandString.equalsIgnoreCase("reset")){
			if(args.length != 1){
				postHelp(player);
				return true;
			}
			
			tutorialReset(player);
			return true;
		}
		
		if(commandString.equalsIgnoreCase("repost")){
			if(args.length != 1){
				postHelp(player);
				return true;
			}
			
			tutorialRepost(player);
			return true;
		}
		
		postHelp(player);
		return true;
	}
	
	private void postHelp(Player player){
		player.sendMessage(ChatColor.RED + "Tutorial Commands are:");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "start" + ChatColor.YELLOW + " : starts a new Tutorial.");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "stop" + ChatColor.YELLOW + " : stops the running Tutorial.");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "skip" + ChatColor.YELLOW + " : skips the current step.");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "reset" + ChatColor.YELLOW + " : resets the Tutorial.");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "repost" + ChatColor.YELLOW + " : reposts the current State.");
		player.sendMessage(ChatColor.AQUA + "/racestutorial " + ChatColor.LIGHT_PURPLE + "set " + ChatColor.DARK_PURPLE + "<new State>" + ChatColor.YELLOW + 
							" : sets the Tutorial to the given state.");
	}
	
	private void tutorialStart(Player player){
		if(TutorialManager.getCurrentState(player.getName()) != null){
			player.sendMessage(ChatColor.RED + "You already have a Tutorial Running.");
			return;
		}
		
		if(!TutorialManager.start(player.getName()))
			player.sendMessage(ChatColor.RED + "Could not execute this command at your current Step.");
	}
	
	private void tutorialStop(Player player){
		if(!checkHasTutorial(player)) return;
		
		if(TutorialManager.stop(player.getName()))
			player.sendMessage(ChatColor.RED + "Tutorial Stopped. To restart it, use " + ChatColor.AQUA + "/racestutorial start");
		else
			player.sendMessage(ChatColor.RED + "Could not execute this command at your current Step.");
	}
	
	private void tutorialSkip(Player player){
		if(!checkHasTutorial(player)) return;
		
		if(!TutorialManager.skip(player.getName()))
			player.sendMessage(ChatColor.RED + "Could not execute this command at your current Step.");
	}
	
	private void tutorialReset(Player player){
		if(!checkHasTutorial(player)) return;
		
		if(!TutorialManager.reset(player.getName()))
			player.sendMessage(ChatColor.RED + "Could not execute this command at your current Step.");
	}
	
	private void tutorialRepost(Player player){
		if(!checkHasTutorial(player)) return;
		
		if(!TutorialManager.repost(player.getName()))
			player.sendMessage(ChatColor.RED + "Could not execute this command at your current Step.");
	}
	
	private void setState(Player player, String state){
		if(!checkHasTutorial(player)) return;
		
		if(!TutorialManager.setState(player.getName(), state))
			player.sendMessage(ChatColor.RED + "Can not set state at this moment.");
	}
	
	private boolean checkHasTutorial(Player player){
		if(TutorialManager.getCurrentState(player.getName()) == null){
			player.sendMessage(ChatColor.RED + "You have no Tutorial running.");
			return false;
		}
		
		return true;
	}
}