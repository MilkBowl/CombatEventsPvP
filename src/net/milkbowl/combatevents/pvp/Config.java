package net.milkbowl.combatevents.pvp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.util.config.Configuration;

public class Config {
	private static Logger log = Logger.getLogger("Minecraft");
	private static Configuration config;
	
	//Our default configuration values
	private static String rewardType = "percent";
	private static double reward = 5;
	
	private static String killerMessage = "You have stolen %REWARD% for killing %KILLED%";

	private static String deathMessage = "You have lost %REWARD% for dying to %KILLER%";
	
	
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
			config.setProperty("killer-message", killerMessage);
			config.setProperty("death-message", deathMessage);
		}
		//Load our options now
		rewardType = config.getString("reward-type").toLowerCase();
		verifyRewardType();
		reward = config.getDouble("reward", reward);
		verifyReward();
		killerMessage = config.getString("killer-message", killerMessage);
		deathMessage = config.getString("death-message", deathMessage);
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
}
