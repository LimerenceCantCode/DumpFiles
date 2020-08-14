package limerence.AdditionalTraits.proxy;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.unlockable.AutoUnlocker;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.commands.ReskillableCmd;
import codersafterdark.reskillable.network.PacketHandler;
import limerence.AdditionalTraits.api.data.PlayerDataExtension;
import limerence.AdditionalTraits.util.handlers.PlayerDataHandlerExtension;
import limerence.AdditionalTraits.util.handlers.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
	

	public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        MinecraftForge.EVENT_BUS.register(RequirementCache.class);
		MinecraftForge.EVENT_BUS.register(PlayerDataHandlerExtension.EventHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerDataExtension.class);
		MinecraftForge.EVENT_BUS.register(RegistryHandler.class);
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        PacketHandler.preInit();
        
        
	}
	
	public void registerModelRenderers() {
	}
		
	
	
	public void init(FMLInitializationEvent event) {
        LevelLockHandler.setupLocks();
        RequirementCache.registerDirtyTypes();
        
    }
	
	public void postInit(FMLPostInitializationEvent event) {

	}
	

	
	public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReskillableCmd());
        AutoUnlocker.setUnlockables();
        MinecraftForge.EVENT_BUS.register(AutoUnlocker.class);
		
	}


		

			

	  
	


	



}
