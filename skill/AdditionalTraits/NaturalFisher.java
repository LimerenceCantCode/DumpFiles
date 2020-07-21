package limerence.AdditionalTraits.skill.AdditionalTraits;

import codersafterdark.reskillable.api.unlockable.Trait;
import limerence.AdditionalTraits.api.data.MessageCastLine;
import limerence.AdditionalTraits.api.data.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TraitNaturalFisher extends Trait { 

	public TraitNaturalFisher() {
		super(new ResourceLocation("AdditionalTraits", "natural_fisher"), 1, 3, 
				new ResourceLocation("AdditionalTraits", "AdditionalTraits"), 
				8, new String[]{"reskillable:attack|1", "reskillable:defense|1", "reskillable:agility|1"});
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
		}
	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEmptyClick(RightClickEmpty event) {
	EntityPlayer player = event.getEntityPlayer();

	if (player.isSneaking() && player.getHeldItemMainhand().isEmpty()) {
		PacketHandler.instance.sendToServer(new MessageCastLine());
		CastLine(player, true);

		}
	}
	
	 @SideOnly(Side.CLIENT)
	    public boolean shouldRotateAroundWhenRendering() {
	        return true;
	 }
	
	 
	// private boolean shouldStopFishing = false;
	 
	 //public Private(boolean shouldStopFishing) {
		// this.shouldStopFishing = shouldStopFishing;
		 

		 
	 
	 
	 
	//	PacketHandler.instance.sendToServer(new MessageCastLine());			
	 
		
	 //, player.posX, player.posY, player.posZ

	 //int lvt_5_1_ = fishEntity.handleHookRetraction();
		
	//, x, y, z
	
	//public ActionResult<ItemStack> onClickEmpty(World world, EntityPlayer player, EnumHand hand){
		
		//ItemStack DefaultRod = new ItemStack(Items.FISHING_ROD);
		
		//return new ActionResult(EnumActionResult.SUCCESS, Items.FISHING_ROD);}
	
	
	public static void CastLine(EntityPlayer player, boolean isClient){
	if(isClient == false) {
		if(player.fishEntity == null) {
			EntityFishHook MakeShiftHook = new EntityFishHook(player.world, player);
			player.world.spawnEntity(MakeShiftHook);
		}
	}
	
	else {
		if(player.fishEntity == null) {
			EntityFishHook MakeShiftHook = new EntityFishHook(player.world, player);	
			player.world.spawnEntity(MakeShiftHook); 
	
		}
	}
		
			
	System.out.println("This is Clientside: " +isClient);	
		
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

				
	

				
				
				





	//&& player.isElytraFlying() || !player.onGround || player.capabilities.isFlying
	


