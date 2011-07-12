package net.milkbowl.combatevents.pvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.util.config.Configuration;

public class Config {
	private static Logger log = Logger.getLogger("Minecraft");
	private static Configuration config;
	
	//Our default configuration values
	private static String rewardType = "percent";
	private static double reward = 5;
	
	private static String killerMessage = "You have stolen %REWARD% for killing %PLAYER%";
	private static String deathMessage = "You have lost %REWARD% for dying to %PLAYER%";
	private static boolean isGlobalPvPMessage = true;
	private static boolean punish = true;

	private static List<String> globalPvPMessages = new ArrayList<String>(6); 
	static { 
		globalPvPMessages.add("%ATTACKER% has slaughtered %KILLED%!");
		globalPvPMessages.add("%KILLED% found death in %ATTACKER%'s presence!");
		globalPvPMessages.add("%KILLED% couldn't escape %ATTACKER%'s onslaught!");
		globalPvPMessages.add("%ATTACKER% sent %KILLED% to their creator.");
		globalPvPMessages.add("%ATTACKER% proved their worth against %KILLED%");
	}
	
	public static void initialize(CombatEventsPvP plugin) {
		//Check to see if there is a configuration file.
		File yml = new File(plugin.getDataFolder()+"/config.yml");

		if (!yml.exists()) {
			new File(plugin.getDataFolder().toString()).mkdir();
			try {
				yml.createNewFile();
			}
			catch (IOException ex) {
				log.info(CombatEventsPvP.plugName + " - Cannot create configuration file. And none to load, using defaults.");
			}
		}
		
		config = plugin.getConfiguration();
		//If the configuration is empty, lets load our defaults
		if (config.getKeys(null).isEmpty()) {
			config.setProperty("reward-type", rewardType);
			config.setProperty("reward", reward);
			config.setProperty("punish.enabled", punish);
			config.setProperty("messages.killer", killerMessage);
			config.setProperty("messages.death", deathMessage);
			config.setProperty("messages.global-messages", isGlobalPvPMessage);
			config.setProperty("messages.globalpvp", globalPvPMessages);
		}
		
		//Load our options now
		rewardType = config.getString("reward-type").toLowerCase();
		verifyRewardType();
		reward = config.getDouble("reward", reward);
		verifyReward();
		killerMessage = config.getString("killer-message", killerMessage);
		deathMessage = config.getString("death-message", deathMessage);
		punish = config.getBoolean("punish.enabled", punish);
		isGlobalPvPMessage = config.getBoolean("messages.global-messages", isGlobalPvPMessage);
		globalPvPMessages = config.getStringList("messages.globalpvp", globalPvPMessages);
		config.save();		
		
		
	}
	
	private static void verifyRewardType() {
		if (!rewardType.equals("percent") || !rewardType.equals("flat"))
			rewardType.equals("percent");
	}
	
	/**
	 * Resets the reward value to a valid value
	 * percent rewards need to be from 0-100
	 * 
	 */
	private static void verifyReward() {
		if (rewardType.equals("percent")) {
			if (reward < 0)
				reward = 0;
			if (reward > 100)
				reward = 100;
		} else if (rewardType.equals("flat")) {
			if (reward < 0)
				reward = 0;
		}
	}

	public static String getRewardType() {
		return rewardType;
	}

	public static double getReward() {
		return reward;
	}
	
	public static String getKillerMessage() {
		return killerMessage;
	}

	public static String getDeathMessage() {
		return deathMessage;
	}
	
	public static boolean isGlobalPvPMessage() {
		return isGlobalPvPMessage;
	}

	public static List<String> getGlobalPvPMessages() {
		return globalPvPMessages;
	}

	public static boolean isPunish() {
		return punish;
	}
}
