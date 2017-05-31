package org.redrune.game.module;

import org.apache.commons.lang3.ArrayUtils;
import org.redrune.game.module.interaction.InteractionModule;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.module.type.ItemInteractionModule;
import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
// TODO: load modules from jars
public class ModuleRepository {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(ModuleRepository.class);
	
	/**
	 * The map of all interface modules
	 */
	private static final List<InterfaceInteractionModule> INTERFACE_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The map of all item modules
	 */
	private static final List<ItemInteractionModule> ITEM_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The map of all modules
	 */
	private static final List<NPCInteractionModule> NPC_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The map of all modules
	 */
	private static final List<ObjectInteractionModule> OBJECT_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * Registers all the modules
	 */
	public static void registerAllModules() {
		for (String directory : new ArrayList<>(Misc.getSubDirectories(InteractionModule.class))) {
			Misc.getClassesInDirectory(InteractionModule.class.getPackage().getName() + "." + directory).stream().filter(InteractionModule.class::isInstance).forEach(clazz -> registerBindings((InteractionModule) clazz));
		}
		LOGGER.info(INTERFACE_MODULES.size() + " interface modules, " + ITEM_MODULES.size() + " item modules, " + NPC_MODULES.size() + " npc modules, and " + OBJECT_MODULES.size() + " object modules loaded.");
	}
	
	/**
	 * Registers the bindings of a module
	 *
	 * @param module
	 * 		The module
	 */
	private static void registerBindings(InteractionModule module) {
		if (module instanceof InterfaceInteractionModule) {
			INTERFACE_MODULES.add((InterfaceInteractionModule) module);
		}
		if (module instanceof ItemInteractionModule) {
			ITEM_MODULES.add((ItemInteractionModule) module);
		}
		if (module instanceof NPCInteractionModule) {
			NPC_MODULES.add((NPCInteractionModule) module);
		}
		if (module instanceof ObjectInteractionModule) {
			OBJECT_MODULES.add((ObjectInteractionModule) module);
		}
	}
	
	/**
	 * Handles the interface interaction
	 *
	 * @param player
	 * 		The player clicking the interface
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id of the interface
	 * @param itemId
	 * 		The item id on the interface, -1 if none.
	 * @param slotId
	 * 		The slot id on the interface, -1 if none.
	 * @param packetId
	 * 		The packet id of the click, different ids are used for different options
	 * @return {@code True} if it was handled successfully
	 */
	public static boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		for (InterfaceInteractionModule module : getInterfaceModules(interfaceId)) {
			if (module.handle(player, interfaceId, componentId, itemId, slotId, packetId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the interface modules for an interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	private static List<InterfaceInteractionModule> getInterfaceModules(int interfaceId) {
		return INTERFACE_MODULES.stream().filter(module -> ArrayUtils.contains(module.interfaceSubscriptionIds(), interfaceId)).collect(Collectors.toList());
	}
	
}