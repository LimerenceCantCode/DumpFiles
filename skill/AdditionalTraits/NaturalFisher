package limerence.AdditionalTraits.skill.AdditionalTraits;

import java.lang.reflect.Field;

import codersafterdark.reskillable.api.unlockable.Trait;
import limerence.AdditionalTraits.api.data.MessageCastLine;
import limerence.AdditionalTraits.api.data.PacketHandler;
import limerence.AdditionalTraits.api.unlockable.EventInterface;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
		 

		 
	 
	 
	 
	 
	 
	
	
	public static void CastLine(final EntityPlayer player, final boolean isClient){
		
		if (player.fishEntity != null) {player.fishEntity.handleHookRetraction();}
		else {
		
		EnumHand hand = player.getActiveHand();

		//boolean isMain = player.getHeldItemMainhand().getItem() == null;
		
		RayTraceResult rt = player.rayTrace(3, 2);
		BlockPos pos = rt.getBlockPos();
		BlockPos offPos = new BlockPos(pos);

		if (player.world.isAirBlock(offPos)) {System.out.println("Success at " +offPos);}

		if (isClient == true) {System.out.println("This is a client event");
		if (player.fishEntity != null) {player.fishEntity.handleHookRetraction();}
		else {
		
		PacketHandler.instance.sendToServer(new MessageCastLine());
		EntityFishHook MakeShiftHook = new EntityFishHook(player.world, player);
		player.world.spawnEntity(MakeShiftHook);
		
		}
		}
		else {System.out.println("This is a server event");
			
		
			if (player.fishEntity != null) {player.swingArm(hand);
			player.fishEntity.handleHookRetraction();
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
			}
			else {
		
			
				ItemStack DefaultFishingRod = new ItemStack(Items.FISHING_ROD);

				EntityFishHook MakeShiftHook = new EntityFishHook(player.world, player, player.posX, player.posY, player.posZ);
				
				int SpeedBonus = EnchantmentHelper.getFishingSpeedBonus(DefaultFishingRod);
				int LuckBonus = EnchantmentHelper.getFishingLuckBonus(DefaultFishingRod);
				
				if (SpeedBonus > 0) {MakeShiftHook.setLureSpeed(SpeedBonus);}
				if (LuckBonus > 0) {MakeShiftHook.setLuck(LuckBonus);}

				
				player.world.spawnEntity(MakeShiftHook);

				
				//MakeShiftHook.handleHookRetraction();

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
				player.swingArm(hand);
		
			}
			}	
		}
	}
}
