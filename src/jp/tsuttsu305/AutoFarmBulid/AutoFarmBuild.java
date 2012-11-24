package jp.tsuttsu305.AutoFarmBulid;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AutoFarmBuild extends JavaPlugin {
	public static AutoFarmBuild plugin;
	Logger logger = Logger.getLogger("Minecraft");
	public boolean wgs = false;
	public boolean hawkeyeFlag = false;

	public void onEnable(){
		//WorldGuardチェック
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard")){
			//WorldGuard使用フラグ
			wgs = true;
			logger.info("AutoFarmBulid hooked to WorldGuard");
		}else{
			wgs = false;
		}

		//HawkEyeチェック
		if (getServer().getPluginManager().isPluginEnabled("HawkEye")){
			//HawkEye Flag
			hawkeyeFlag = true;
			logger.info("AutoFarmBulid hooked to Hawkeye");
		}else{
			hawkeyeFlag = false;
		}
		if(hawkeyeFlag){
			getServer().getPluginManager().registerEvents(new PlayerClick(this), this);
		}else{
			getServer().getPluginManager().registerEvents(new PlayerClickNoneHawkEye(this), this);
		}
	}
	//WorldGuard使用時
	private WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}
		return (WorldGuardPlugin) plugin;
	}
	public WorldGuardPlugin wg(){
		return getWorldGuard();

	}

	//HawkAPI
	private HawkEyeAPI getHawkAPI(){
		Plugin plugin = getServer().getPluginManager().getPlugin("HawkEye");
		if (plugin == null || !(plugin instanceof HawkEyeAPI)) {
			return null; // Maybe you want throw an exception instead
		}
		return (HawkEyeAPI) plugin;
	}
	public HawkEyeAPI gethw(){
		return getHawkAPI();
	}
}
