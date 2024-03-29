package limerence.AdditionalTraits.skill.AdditionalTraits;

import java.math.BigInteger;
import java.util.UUID;

import javax.annotation.Nullable;

import codersafterdark.reskillable.api.unlockable.Trait;
import limerence.AdditionalTraits.api.data.MessageCastLine;
import limerence.AdditionalTraits.api.data.MessagePlayerServer;
import limerence.AdditionalTraits.entity.EntityNatFishHook;
import limerence.AdditionalTraits.util.handlers.PacketHandler;
import limerence.AdditionalTraits.util.init.ItemInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
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
	
	 
	
	
	@SideOnly(Side.SERVER)
	public static final String fishServer = "skillable:realBobber";
	
	@SideOnly(Side.CLIENT)
	public static final String fishClient = "skillable:fakeBobber";
	

	
	EntityNatFishHook FishEntity;
	
	//TODO: create new packet handler message to get entity player server instance. Make a new class file with all util functions for making this PoS work
	
	 @SideOnly(Side.CLIENT)
	    public boolean shouldRotateAroundWhenRendering() {
	        return true;
	 }
	//TODO: Why is hook still rendered when both sides of the entity are dead? 
	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEmptyClick(RightClickEmpty event) {
		
	EntityPlayer player = event.getEntityPlayer();
	
	//TODO Create ctx pull for EntityPlayerMP so we can pull GenericID
	int GenericID = player.getEntityData().getInteger(fishServer);
	System.out.println("GenericID is "+GenericID);


	if (!event.isCanceled() && player.isSneaking() && player.getHeldItemMainhand().isEmpty() && event.getHand() == EnumHand.MAIN_HAND && player.isSwingInProgress == false 
			|| GenericID != 0 && !event.isCanceled() && player.getHeldItemMainhand().isEmpty() && event.getHand() == EnumHand.MAIN_HAND) {

		player.swingArm(event.getHand());
		PacketHandler.instance.sendToServer(new MessageCastLine());
		
//Even with just the server side, it seems to spawn a client side version, but DOES NOT RENDER IT what the fuck.
//It spawns BOTH from the server thread, but immediately kills what is considered the client side, no flicker or anything, it DOES NOT RENDER.
		
		
		//Decider(player, true); //<-- This will spawn a client side only version of the hook, which DOES render, but cannot be killed/removed/unrendered.  

	
		
		//int ServerID = player.getEntityData().getInteger(fishServer);
				
		//player.world.getEntityByID(ServerID);

	}
	/*if (player.getHeldItemMainhand().isEmpty() && event.getHand() == EnumHand.MAIN_HAND && player.isSwingInProgress == false) {
		EntityPlayerMP ServerSide = PacketHandler.instance.sendToServer(new MessagePlayerServer());
		UUID ServerID = getID(ServerSide.getEntityData().getString(fishServer));
		if(ServerID != null && !ServerID.equals(Empty)) {
		EntityNatFishHook FishEntity = Entity
		
			
		}
		
	}*/
	}

	//, player.posX, player.posY, player.posZ
	
	
/*	
	@Nullable
	static UUID getID(String string){
		UUID output = null;
		try{
			output = UUID.fromString(string);
			}
		catch (IllegalArgumentException e) {System.out.println("getID returned null");
		}
		finally {System.out.println("UUID: "+output);}
		return output;
	
	}
	
*/	
	
	
	
	
	public static void Decider(EntityPlayer player, boolean isClient){
		ItemStack DefaultRod = new ItemStack(Items.FISHING_ROD);
    	int GenericID = player.getEntityData().getInteger(fishServer);
    	System.out.println("GenericID is "+GenericID);
		
		
		if (GenericID != 0) {
        int ServerID = player.getEntityData().getInteger(fishServer);
        System.out.println("Server ID is "+ServerID);	
        if (!player.world.isRemote) {
        if (ServerID != 0) {
        EntityNatFishHook Retract = (EntityNatFishHook) player.world.getEntityByID(ServerID);
        Retract.handleHookRetraction();
		player.swingArm(EnumHand.MAIN_HAND);
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, 
		SoundCategory.NEUTRAL, 1.0F, 0.4F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
        
        }	
        }
        }
		else
		{
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, 
					SoundCategory.NEUTRAL, 1.0F, 0.4F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
			player.swingArm(EnumHand.MAIN_HAND);
			if (!player.world.isRemote) {
			EntityNatFishHook MakeShiftHook = new EntityNatFishHook(player.world, player);

			int SpeedBonus = EnchantmentHelper.getFishingSpeedBonus(DefaultRod);
			int LuckBonus = EnchantmentHelper.getFishingLuckBonus(DefaultRod);

			if (SpeedBonus > 0) {MakeShiftHook.setLureSpeed(SpeedBonus);}
			if (LuckBonus > 0) {MakeShiftHook.setLuck(LuckBonus);}
			player.world.spawnEntity(MakeShiftHook);}
		}
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		System.out.println("Decider fired");
		System.out.println("Client? "+isClient);
		System.out.println("Player is "+player);
		
		
		@Nullable
		int ServerID = player.getEntityData().getInteger(fishServer);
		System.out.println("Server ID is "+ServerID);
		@Nullable
		int ClientID = player.getEntityData().getInteger(fishClient);
		System.out.println("Client ID is "+ClientID);
		

		ItemStack NatHook = new ItemStack(ItemInit.NATHOOK);
		ItemStack DefaultRod = new ItemStack(Items.FISHING_ROD);

		if (isClient == false) {
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, NatHook);
		ItemStack MainHand = player.getHeldItemMainhand();

		System.out.println("MainHand is " +MainHand);	



	MainHand.useItemRightClick(player.world, player, EnumHand.MAIN_HAND);
	}
		
		
		if (isClient == false) {
			System.out.println("Condition 1");
			if (ServerID == 0) {
				System.out.println("Condition 1A");
				EntityNatFishHook RealHook = new EntityNatFishHook(player.world, player); player.world.spawnEntity(RealHook);}
			else {System.out.println("Condition 1B");
			
			System.out.println("Current Hook is still: "+player.world.getEntityByID(ServerID));
			System.out.println("Reason: "+ServerID +" But Should be 0");}
		}

		else if (isClient == true){
			System.out.println("Condition 2");
			if (ClientID == 0) {
				System.out.println("Condition 2A");
				EntityNatFishHook FakeHook = new EntityNatFishHook(player.world, player); player.world.spawnEntity(FakeHook);}
			else {System.out.println("Condition 2B");
			System.out.println("Reason: "+ClientID);}
		}*/
		
	
	
	

}
	
	
/*

	 MainHand.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
	 
	 private boolean shouldStopFishing = false;
	 
	 public Private(boolean shouldStopFishing) {
		 this.shouldStopFishing = shouldStopFishing;
		 
		MainHand.onPlayerStoppedUsing(player.world, player, 0);
		 


	ItemStack NatHook = new ItemStack(ItemInit.NATHOOK);
	ItemStack DefaultRod = new ItemStack(Items.FISHING_ROD);

	if (isClient == false) {
	player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, NatHook);
	ItemStack MainHand = player.getHeldItemMainhand();

	System.out.println("MainHand is " +MainHand);	



MainHand.useItemRightClick(player.world, player, EnumHand.MAIN_HAND);
	 
	 
	 
		PacketHandler.instance.sendToServer(new MessageCastLine());			
	 
		
	 , player.posX, player.posY, player.posZ

	 int lvt_5_1_ = fishEntity.handleHookRetraction();
	
	, x, y, z
player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, NatHook);
setCurrentHook(realHook);
				
CurrentHook = realHook;

ItemStack NatHook = new ItemStack(ItemInit.NATHOOK);		
				



	&& player.isElytraFlying() || !player.onGround || player.capabilities.isFlying
	
*/

