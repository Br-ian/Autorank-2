package me.armar.plugins.autorank.permissions.handlers;

/**
 * @author Staartvin This is a special permission handler that handles all work
 * from PowerfulPerms
 */
public class PowerfulPermsHandler {

//    private PowerfulPerms powerfulPerms;
//
//    public PowerfulPermsHandler(Autorank plugin) {
//        super(plugin);
//    }
//
//    /**
//     * Add a player to group
//     *
//     * @param player Player to add
//     * @param world  On a specific world
//     * @param group  Group to add the player to
//     * @return true if done, false if failed
//     */
//    public boolean addGroup(final Player player, final String world, final String group) {
//        // No known way to set via API, hence we do it the ugly route (via
//        // commands).
//        getPlugin().getServer().dispatchCommand(getPlugin().getServer().getConsoleSender(),
//                "pp user " + player.getName() + " setrank " + group);
//        return true;
//        // There is no way to check if the command was successful.
//    }
//
//    @Override
//    public Collection<String> getGroups() {
//        final com.github.gustav9797.PowerfulPermsAPI.PermissionManager permManager = powerfulPerms
//                .getPermissionManager();
//        final Map<Integer, com.github.gustav9797.PowerfulPermsAPI.Group> groups = permManager.getGroups();
//
//        List<String> groupNames = new ArrayList<>();
//
//        for (final Entry<Integer, com.github.gustav9797.PowerfulPermsAPI.Group> entry : groups.entrySet()) {
//            groupNames.add(entry.getValue().getName());
//        }
//
//        return Collections.unmodifiableCollection(groupNames);
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see me.armar.plugins.autorank.permissions.PermissionsHandler#getName()
//     */
//    @Override
//    public String getName() {
//        return "PowerfulPerms";
//    }
//
//    @Override
//    public Collection<String> getPlayerGroups(final Player player) {
//        final com.github.gustav9797.PowerfulPermsAPI.PermissionManager permManager = powerfulPerms
//                .getPermissionManager();
//        final List<com.github.gustav9797.PowerfulPermsAPI.Group> groups = permManager
//                .getPermissionPlayer(player.getUniqueId()).getGroups();
//
//        List<String> groupNames = new ArrayList<>();
//
//        for (com.github.gustav9797.PowerfulPermsAPI.Group group : groups) {
//            groupNames.add(group.getName());
//        }
//
//        return Collections.unmodifiableCollection(groupNames);
//    }
//
//    @Override
//    public Collection<String> getWorldGroups(final Player player, final String world) {
//        return this.getPlayerGroups(player); // No known world conversion.
//    }
//
//    /**
//     * Remove a player from a group
//     *
//     * @param player Player to remove
//     * @param world  On a specific world
//     * @param group  Group to remove the player from
//     * @return true if done, false if failed
//     */
//    public boolean removeGroup(final Player player, final String world, final String group) {
//        // No known way to set via API, hence we do it the ugly route (via
//        // commands).`
//        // Does not seem to work properly.
//        getPlugin().getServer().dispatchCommand(getPlugin().getServer().getConsoleSender(),
//                "pp user " + player.getName() + " removegroup " + group);
//        return true;
//        // There is no way to check if the command was successful.
//    }
//
//    @Override
//    public boolean replaceGroup(final Player player, final String world, final String oldGroup, final String
//    newGroup) {
//        return (addGroup(player, world,
//                newGroup));
//    }
//
//    @Override
//    public boolean setupPermissionsHandler() {
//        final PluginManager pluginManager = getPlugin().getServer().getPluginManager();
//        final Plugin permPlugin = pluginManager.getPlugin("PowerfulPerms");
//
//        if (permPlugin != null && permPlugin.isEnabled()) {
//
//            if (!(permPlugin instanceof PowerfulPerms)) {
//                return false;
//            }
//
//            powerfulPerms = (PowerfulPerms) permPlugin;
//        }
//
//        return powerfulPerms != null;
//    }
}
