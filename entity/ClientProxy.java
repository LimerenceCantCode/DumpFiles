package limerence.AdditionalTraits.proxy;

import codersafterdark.reskillable.base.ToolTipHandler;
import codersafterdark.reskillable.client.base.ClientTickHandler;
import codersafterdark.reskillable.client.base.HUDHandler;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import limerence.AdditionalTraits.entity.EntityNatFishHook;
import limerence.AdditionalTraits.render.RenderNatFishHook;
import limerence.AdditionalTraits.util.handlers.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
	
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		this.registerModelRenderers();
	}
	
	@Override
	public void registerModelRenderers() {
	RenderingRegistry.registerEntityRenderingHandler(EntityNatFishHook.class, RenderNatFishHook::new);	 
	}

		
	@Override
	public void init(FMLInitializationEvent event) {


    }
	
	
	
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		KeyBindings.init();
		PacketHandler.init();
		MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
		MinecraftForge.EVENT_BUS.register(InventoryTabHandler.class);
		MinecraftForge.EVENT_BUS.register(HUDHandler.class);
		MinecraftForge.EVENT_BUS.register(ToolTipHandler.class);
		MinecraftForge.EVENT_BUS.register(this.getClass());
		MinecraftForge.EVENT_BUS.register(new KeyBindings());
		

    }

	
}
