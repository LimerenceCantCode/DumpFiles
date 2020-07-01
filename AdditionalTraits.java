package limerence.AdditionalTraits;



import org.apache.logging.log4j.Logger;

import codersafterdark.reskillable.ReskillableModAccess;
import codersafterdark.reskillable.api.ReskillableAPI;
import limerence.AdditionalTraits.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod(modid = "at", name = "AdditionalTraits", version = "1.0", acceptedMinecraftVersions = "[1.12.2]")
public class AdditionalTraits {
	@SidedProxy(serverSide = "limerence.AdditionalTraits.proxy.CommonProxy", clientSide = "limerence.AdditionalTraits.proxy.ClientProxy")
	public static CommonProxy proxy;
	public static Logger logger;

	public AdditionalTraits() {
		ReskillableAPI.setInstance(new ReskillableAPI(new ReskillableModAccess()));
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}
}
