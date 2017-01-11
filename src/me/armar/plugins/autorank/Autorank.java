package me.armar.plugins.autorank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.armar.plugins.autorank.addons.AddOnManager;
import me.armar.plugins.autorank.api.API;
import me.armar.plugins.autorank.backup.BackupManager;
import me.armar.plugins.autorank.commands.manager.CommandsManager;
import me.armar.plugins.autorank.config.InternalPropertiesConfig;
import me.armar.plugins.autorank.config.PathsConfig;
import me.armar.plugins.autorank.config.PlayerDataConfig;
import me.armar.plugins.autorank.config.SettingsConfig;
import me.armar.plugins.autorank.data.mysql.MySQLManager;
import me.armar.plugins.autorank.debugger.Debugger;
import me.armar.plugins.autorank.hooks.DependencyManager;
import me.armar.plugins.autorank.language.LanguageHandler;
import me.armar.plugins.autorank.leaderboard.LeaderboardHandler;
import me.armar.plugins.autorank.listeners.PlayerJoinListener;
import me.armar.plugins.autorank.pathbuilder.PathManager;
import me.armar.plugins.autorank.pathbuilder.builders.RequirementBuilder;
import me.armar.plugins.autorank.pathbuilder.builders.ResultBuilder;
import me.armar.plugins.autorank.pathbuilder.requirement.ASkyBlockLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.AchievementRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.AcidIslandLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.BlocksBrokenRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.BlocksMovedRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.BlocksPlacedRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.DamageTakenRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.EssentialsGeoIPRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.ExpRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.FactionPowerRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.FishCaughtRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.FoodEatenRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GamemodeRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GlobalTimeRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GriefPreventionBonusBlocksRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GriefPreventionClaimedBlocksRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GriefPreventionClaimsCountRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.GriefPreventionRemainingBlocksRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.HasItemRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.InBiomeRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.ItemsCraftedRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.JavaScriptRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.JobsCurrentPointsRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.JobsExperienceRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.JobsLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.JobsTotalPointsRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.LocationRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.McMMOPowerLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.McMMOSkillLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.MobKillsRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.MoneyRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.PermissionRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.PlayerKillsRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.RPGMeCombatLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.RPGMeSkillLevelRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.Requirement;
import me.armar.plugins.autorank.pathbuilder.requirement.TimeRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.TimesShearedRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.TotalTimeRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.TotalVotesRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.WorldGuardRegionRequirement;
import me.armar.plugins.autorank.pathbuilder.requirement.WorldRequirement;
import me.armar.plugins.autorank.pathbuilder.result.CommandResult;
import me.armar.plugins.autorank.pathbuilder.result.EffectResult;
import me.armar.plugins.autorank.pathbuilder.result.MessageResult;
import me.armar.plugins.autorank.pathbuilder.result.RankChangeResult;
import me.armar.plugins.autorank.pathbuilder.result.Result;
import me.armar.plugins.autorank.pathbuilder.result.SpawnFireworkResult;
import me.armar.plugins.autorank.pathbuilder.result.TeleportResult;
import me.armar.plugins.autorank.permissions.PermissionsPluginManager;
import me.armar.plugins.autorank.playerchecker.PlayerChecker;
import me.armar.plugins.autorank.playtimes.Playtimes;
import me.armar.plugins.autorank.statsmanager.StatsPlugin;
import me.armar.plugins.autorank.statsmanager.handlers.DummyHandler;
import me.armar.plugins.autorank.updater.UpdateHandler;
import me.armar.plugins.autorank.updater.Updater;
import me.armar.plugins.autorank.util.uuid.storage.UUIDStorage;
import me.armar.plugins.autorank.validations.ValidateHandler;
import me.armar.plugins.autorank.warningmanager.WarningManager;

/**
 * 
 * Main class of Autorank
 * <p>
 * Date created: 18:34:00 13 jan. 2014
 * 
 * @author Staartvin
 * 
 */
public class Autorank extends JavaPlugin {

	public static Autorank getAutorank() {
		return (Autorank) Bukkit.getPluginManager().getPlugin("Autorank");
	}

	// ---------- INITIALIZING VARIABLES ---------- \\
	// 
	//
	//

	// Managers
	private PathManager pathManager;
	private AddOnManager addonManager;
	private BackupManager backupManager;
	private CommandsManager commandsManager;
	private DependencyManager dependencyManager;
	private LeaderboardHandler leaderboardManager;

	// Handlers
	private LanguageHandler languageHandler;
	private PermissionsPluginManager permPlugHandler;
	private UpdateHandler updateHandler;

	// Miscalleaneous
	private PlayerChecker playerChecker;
	private Playtimes playtimes;

	// Data connection
	private MySQLManager mysqlManager;

	// UUID storage
	private UUIDStorage uuidStorage;

	// Validation & Warning
	private ValidateHandler validateHandler;
	private WarningManager warningManager;
	private Debugger debugger;

	// Configs
	private SettingsConfig settingsConfig;
	private InternalPropertiesConfig internalPropertiesConfig;
	private PathsConfig pathsConfig;
	private PlayerDataConfig playerDataConfig;

	// ---------- onEnable() & onDisable() ---------- \\
	// 
	//
	//

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {

		// Make sure all tasks are cancelled after shutdown. This seems obvious,
		// but when a player /reloads, the server creates an instance of the
		// plugin which causes duplicate tasks to run.
		getServer().getScheduler().cancelTasks(this);
		
		// ------------- Save files and databases -------------
		
		playtimes.save();

		getUUIDStorage().saveAllFiles();

		// Close database connection
		this.getMySQLManager().disconnectDatabase();

		// Save playerdata.yml
		this.getPlayerDataConfig().saveConfig();
		
		// ------------- Say bye-bye -------------

		getLogger().info(String.format("Autorank %s has been disabled!", getDescription().getVersion()));
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {

		// ------------- Create files & folders -------------

		// Register configs
		setPathsConfig(new PathsConfig(this));
		setSettingsConfig(new SettingsConfig(this));
		setInternalPropertiesConfig(new InternalPropertiesConfig(this));
		setPlayerDataConfig(new PlayerDataConfig(this));

		// Create new configs
		this.getPathsConfig().createNewFile();
		this.getSettingsConfig().createNewFile();
		this.getInternalPropertiesConfig().loadFile();
		this.getPlayerDataConfig().createNewFile();

		// ------------- Initialize managers -------------

		// Create backup manager
		setBackupManager(new BackupManager(this));

		// Create warning manager
		setWarningManager(new WarningManager(this));

		// Create MySQL Wrapper
		setMySQLManager(new MySQLManager(this));

		// Load dependency manager
		setDependencyManager(new DependencyManager(this));

		// Create commands manager
		setCommandsManager(new CommandsManager(this));

		// Create Addon Manager
		setAddonManager(new AddOnManager(this));

		// ------------- Initialize handlers -------------

		// Create update handler
		setUpdateHandler(new UpdateHandler(this));

		// Create language classes
		setLanguageHandler(new LanguageHandler(this));

		// Create permission plugin handler class
		setPermPlugHandler(new PermissionsPluginManager(this));

		// Create validate handler
		setValidateHandler(new ValidateHandler(this));

		// Create leaderboard class
		setLeaderboardManager(new LeaderboardHandler(this));

		// ------------- Initialize storage -------------

		// Create uuid storage
		setUUIDStorage(new UUIDStorage(this));

		// ------------- Initialize others -------------

		// Create playtime class
		setPlaytimes(new Playtimes(this));

		// Create player check class
		setPlayerChecker(new PlayerChecker(this));

		// Set debugger
		setDebugger(new Debugger(this));

		// Load uuids - ready for new ones
		getUUIDStorage().createNewFiles();

		// ------------- Create files & folders -------------

		// Setup language file
		languageHandler.createNewFile();
		
		// Initialize paths
		getPathManager().initialiseFromConfigs();

		// Start warning task if a warning has been found
		if (getWarningManager().getHighestWarning() != null) {
			getWarningManager().startWarningTask();
		}

		// ------------- Register listeners -------------

		// Register listeners
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

		// ------------- Initialize requirements and results -------------
		this.initializeReqsAndRes();

		// ------------- Schedule tasks -------------

		// Load all third party dependencies
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				try {
					// Load dependencies
					dependencyManager.loadDependencies();

				} catch (final Throwable t) {

					// When an error occured!

					getLogger().severe("Could not hook into a dependency: \nCause: " + t.getCause());
				}
			}
		}, 1L);
		
		// ------------- Register commands -------------

		// Register command
		getCommand("autorank").setExecutor(getCommandsManager());
		
		// ------------- Log messages -------------

		// Debug message telling what plugin is used for timing.
		getLogger().info("Using timings of: " + getConfigHandler().useTimeOf().toString().toLowerCase());

		debugMessage("Autorank debug is turned on!");

		// ------------- Check version -------------
		
		// Extra warning for dev users
		if (isDevVersion()) {
			this.getLogger().warning("You're running a DEV version, be sure to backup your Autorank folder!");
			this.getLogger().warning(
					"DEV versions are not guaranteed to be stable and generally shouldn't be used on big production servers with lots of players.");
		}
		
		// ------------- Do miscalleaneous tasks -------------

		// Start automatic backup
		this.getBackupManager().startBackupSystem();

		// Try to update all leaderboards if needed.
		this.getLeaderboardManager().updateAllLeaderboards();

		// Convert all UUIDS to lowercase.
		this.getUUIDStorage().transferUUIDs();

		// Check whether the data files are still up to date.
		this.getPlaytimes().doCalendarCheck();

		// Spawn thread to check if MySQL database times are up to date
		this.getMySQLManager().refreshGlobalTime();
		
		// ------------- Say Welcome! -------------
		getLogger().info(String.format("Autorank %s has been enabled!", getDescription().getVersion()));
	}

	// ---------- CONVENIENCE METHODS ---------- \\
	// 
	//
	//

	private void initializeReqsAndRes() {
		final RequirementBuilder req = this.getPathManager().getBuilder().getRequirementBuilder();
		final ResultBuilder res = this.getPathManager().getBuilder().getResultBuilder();

		// Register 'main' requirements
		req.registerRequirement("exp", ExpRequirement.class);
		req.registerRequirement("money", MoneyRequirement.class);
		req.registerRequirement("gamemode", GamemodeRequirement.class);
		req.registerRequirement("has item", HasItemRequirement.class);
		req.registerRequirement("blocks broken", BlocksBrokenRequirement.class);
		req.registerRequirement("blocks placed", BlocksPlacedRequirement.class);
		req.registerRequirement("blocks moved", BlocksMovedRequirement.class);
		req.registerRequirement("votes", TotalVotesRequirement.class);
		req.registerRequirement("damage taken", DamageTakenRequirement.class);
		req.registerRequirement("mobs killed", MobKillsRequirement.class);
		req.registerRequirement("location", LocationRequirement.class);
		req.registerRequirement("faction power", FactionPowerRequirement.class);
		req.registerRequirement("players killed", PlayerKillsRequirement.class);
		req.registerRequirement("global time", GlobalTimeRequirement.class);
		req.registerRequirement("total time", TotalTimeRequirement.class);
		req.registerRequirement("world", WorldRequirement.class);
		req.registerRequirement("worldguard region", WorldGuardRegionRequirement.class);
		req.registerRequirement("mcmmo skill level", McMMOSkillLevelRequirement.class);
		req.registerRequirement("mcmmo power level", McMMOPowerLevelRequirement.class);
		req.registerRequirement("permission", PermissionRequirement.class);
		req.registerRequirement("fish caught", FishCaughtRequirement.class);
		req.registerRequirement("items crafted", ItemsCraftedRequirement.class);
		req.registerRequirement("time", TimeRequirement.class);
		req.registerRequirement("times sheared", TimesShearedRequirement.class);
		req.registerRequirement("essentials geoip location", EssentialsGeoIPRequirement.class);
		req.registerRequirement("in biome", InBiomeRequirement.class);
		req.registerRequirement("food eaten", FoodEatenRequirement.class);
		req.registerRequirement("acidisland level", AcidIslandLevelRequirement.class);
		req.registerRequirement("askyblock level", ASkyBlockLevelRequirement.class);
		req.registerRequirement("javascript", JavaScriptRequirement.class);
		req.registerRequirement("jobs current points", JobsCurrentPointsRequirement.class);
		req.registerRequirement("jobs total points", JobsTotalPointsRequirement.class);
		req.registerRequirement("jobs level", JobsLevelRequirement.class);
		req.registerRequirement("jobs experience", JobsExperienceRequirement.class);
		req.registerRequirement("grief prevention claims", GriefPreventionClaimsCountRequirement.class);
		req.registerRequirement("grief prevention claimed blocks", GriefPreventionClaimedBlocksRequirement.class);
		req.registerRequirement("grief prevention remaining blocks", GriefPreventionRemainingBlocksRequirement.class);
		req.registerRequirement("grief prevention bonus blocks", GriefPreventionBonusBlocksRequirement.class);
		req.registerRequirement("has achievement", AchievementRequirement.class);
		req.registerRequirement("rpgme skill level", RPGMeSkillLevelRequirement.class);
		req.registerRequirement("rpgme combat level", RPGMeCombatLevelRequirement.class);

		// Register 'main' results
		res.registerResult("command", CommandResult.class);
		res.registerResult("effect", EffectResult.class);
		res.registerResult("message", MessageResult.class);
		res.registerResult("rank change", RankChangeResult.class);
		res.registerResult("tp", TeleportResult.class);
		res.registerResult("firework", SpawnFireworkResult.class);
	}

	/**
	 * This method can only be performed from the main class as it tries to do
	 * {@link #getFile()}
	 * 
	 * @return Whether an update is available
	 */
	public boolean checkForUpdate() {

		// We are not allowed to check for new versions.
		if (!updateHandler.doCheckForNewVersion())
			return false;

		final Updater updater = new Updater(this, 34447, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
		updateHandler.setUpdater(updater);

		return (updater.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE));

	}

	/**
	 * Sends a message via the debug channel of Autorank.
	 * <br>
	 * It will only show up in console if the debug option in the Settings.yml
	 * is turned on.
	 * 
	 * @param message Message to send.
	 */
	public void debugMessage(final String message) {
		// Don't put out debug message when it is not needed.
		if (!this.getConfigHandler().useDebugOutput())
			return;

		this.getServer().getConsoleSender()
				.sendMessage("[Autorank DEBUG] " + ChatColor.translateAlternateColorCodes('&', message));
	}

	/**
	 * Get the current {@linkplain StatsPlugin} that is hooked.
	 * 
	 * @return current {@linkplain StatsPlugin} that is hooked or
	 *         {@linkplain DummyHandler} if no stats plugin is found.
	 */
	public StatsPlugin getHookedStatsPlugin() {
		return getDependencyManager().getStatsPlugin();
	}

	/**
	 * Checks whether the current version of Autorank is a DEV version.
	 * 
	 * @return true if is, false otherwise.
	 */
	public boolean isDevVersion() {
		return this.getDescription().getVersion().toLowerCase().contains("dev")
				|| this.getDescription().getVersion().toLowerCase().contains("project");
	}

	/**
	 * @see {@linkplain me.armar.plugins.autorank.api.API#registerRequirement(String, Class)
	 *      registerRequirement()}
	 */
	public void registerRequirement(final String name, final Class<? extends Requirement> requirement) {
		this.getPathManager().getBuilder().getRequirementBuilder().registerRequirement(name, requirement);
	}

	/**
	 * @see {@linkplain me.armar.plugins.autorank.api.API#registerResult(String, Class)
	 *      registerResult()}
	 */
	public void registerResult(final String name, final Class<? extends Result> result) {
		this.getPathManager().getBuilder().getResultBuilder().registerResult(name, result);
	}

	/**
	 * Reloads the Autorank plugin.
	 */
	public void reload() {
		getServer().getPluginManager().disablePlugin(this);
		getServer().getPluginManager().enablePlugin(this);
	}

	// ---------- GETTERS & SETTERS ---------- \\
	// 
	//
	//

	public LanguageHandler getLanguageHandler() {
		return languageHandler;
	}

	public PermissionsPluginManager getPermPlugHandler() {
		return permPlugHandler;
	}

	public PlayerChecker getPlayerChecker() {
		return playerChecker;
	}

	public Playtimes getPlaytimes() {
		return playtimes;
	}

	public UpdateHandler getUpdateHandler() {
		return updateHandler;
	}

	public UUIDStorage getUUIDStorage() {
		return uuidStorage;
	}

	public ValidateHandler getValidateHandler() {
		return validateHandler;
	}

	public WarningManager getWarningManager() {
		return warningManager;
	}

	public AddOnManager getAddonManager() {
		return addonManager;
	}

	public API getAPI() {
		return new API(this);
	}

	public BackupManager getBackupManager() {
		return backupManager;
	}

	public CommandsManager getCommandsManager() {
		return commandsManager;
	}

	public SettingsConfig getConfigHandler() {
		return settingsConfig;
	}

	public Debugger getDebugger() {
		return debugger;
	}

	public DependencyManager getDependencyManager() {
		return dependencyManager;
	}

	public void setAddonManager(final AddOnManager addonManager) {
		this.addonManager = addonManager;
	}

	public void setBackupManager(final BackupManager backupManager) {
		this.backupManager = backupManager;
	}

	public void setCommandsManager(final CommandsManager commandsManager) {
		this.commandsManager = commandsManager;
	}

	public void setConfigHandler(final SettingsConfig configHandler) {
		this.settingsConfig = configHandler;
	}

	public void setDebugger(final Debugger debugger) {
		this.debugger = debugger;
	}

	public void setDependencyManager(final DependencyManager dependencyManager) {
		this.dependencyManager = dependencyManager;
	}

	private void setLanguageHandler(final LanguageHandler lHandler) {
		this.languageHandler = lHandler;
	}

	/**
	 * @return the internalPropertiesConfig
	 */
	public InternalPropertiesConfig getInternalPropertiesConfig() {
		return internalPropertiesConfig;
	}

	/**
	 * @param internalPropertiesConfig the internalPropertiesConfig to set
	 */
	public void setInternalPropertiesConfig(InternalPropertiesConfig internalPropertiesConfig) {
		this.internalPropertiesConfig = internalPropertiesConfig;
	}

	public void setPermPlugHandler(final PermissionsPluginManager permPlugHandler) {
		this.permPlugHandler = permPlugHandler;
	}

	private void setPlayerChecker(final PlayerChecker playerChecker) {
		this.playerChecker = playerChecker;
	}

	private void setPlaytimes(final Playtimes playtimes) {
		this.playtimes = playtimes;
	}

	public void setUpdateHandler(final UpdateHandler updateHandler) {
		this.updateHandler = updateHandler;
	}

	public void setUUIDStorage(final UUIDStorage uuidStorage) {
		this.uuidStorage = uuidStorage;
	}

	public void setValidateHandler(final ValidateHandler validateHandler) {
		this.validateHandler = validateHandler;
	}

	public void setWarningManager(final WarningManager warningManager) {
		this.warningManager = warningManager;
	}

	/**
	 * @return the settingsConfig
	 */
	public SettingsConfig getSettingsConfig() {
		return settingsConfig;
	}

	/**
	 * @param settingsConfig the settingsConfig to set
	 */
	public void setSettingsConfig(SettingsConfig settingsConfig) {
		this.settingsConfig = settingsConfig;
	}

	public PlayerDataConfig getPlayerDataConfig() {
		return playerDataConfig;
	}

	public void setPlayerDataConfig(PlayerDataConfig playerDataConfig) {
		this.playerDataConfig = playerDataConfig;
	}

	public PathsConfig getPathsConfig() {
		return pathsConfig;
	}

	public void setPathsConfig(PathsConfig pathsConfig) {
		this.pathsConfig = pathsConfig;
	}

	public PathManager getPathManager() {
		return pathManager;
	}

	public void setPathManager(PathManager pathManager) {
		this.pathManager = pathManager;
	}

	public LeaderboardHandler getLeaderboardManager() {
		return leaderboardManager;
	}

	public void setLeaderboardManager(LeaderboardHandler leaderboardManager) {
		this.leaderboardManager = leaderboardManager;
	}

	public MySQLManager getMySQLManager() {
		return mysqlManager;
	}

	public void setMySQLManager(MySQLManager mysqlManager) {
		this.mysqlManager = mysqlManager;
	}
}
