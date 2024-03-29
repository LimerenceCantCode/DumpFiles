package limerence.AdditionalTraits.entity;


import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.xml.ws.spi.Invoker;

import limerence.AdditionalTraits.api.data.MessageCastLine;
import limerence.AdditionalTraits.api.data.MessagePlayerServer;
import limerence.AdditionalTraits.entity.events.DifferentItemFishedEvent;
import limerence.AdditionalTraits.util.handlers.PacketHandler;
import limerence.AdditionalTraits.util.init.ItemInit;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityNatFishHook extends Entity implements IThrowableEntity{

	private final static DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(EntityNatFishHook.class, DataSerializers.VARINT);

		private boolean inGround;
		private int ticksInGround;
		private EntityPlayer angler;
		private int ticksInAir;
		private int ticksCatchable;
		private int ticksCaughtDelay;
		private int ticksCatchableDelay;
		private float fishApproachAngle;
		public Entity caughtEntity;
		private State currentState;
		private int luck;
		private int lureSpeed; 
		
		@Nullable
		public static final String fishClient = "skillable:fakeBobber";
		@Nullable
		public static final String fishServer = "skillable:realBobber"; 
		
	
		
		
		
	    static enum State {FLYING, HOOKED_IN_ENTITY, BOBBING;}
	    
	    @SideOnly(Side.CLIENT)
	    public EntityNatFishHook(World world, EntityPlayer player, double x, double y, double z) {
	        super(world);
	        this.init(player);
	        this.setPosition(x, y, z);
	        this.prevPosX = this.posX;
	        this.prevPosY = this.posY;
	        this.prevPosZ = this.posZ;
	    }

		public EntityNatFishHook(World world, EntityPlayer player) {
			super(world);
			this.currentState = State.FLYING;
			this.init(player);
			player.getEntityData().setInteger(fishServer, this.getEntityId());
			player.getEntityData().setInteger(fishClient, this.getEntityId());
			System.out.println("POST INIT. EntityHook is " +this.getEntityId());
			this.shoot();
		}
		
		public EntityNatFishHook(World world) {
			super(world);
		}
		
		public void setLureSpeed(int speed) {
			this.lureSpeed = speed;
		}

		public void setLuck(int luck) {
			this.luck = luck;
		}
		

		private void init(EntityPlayer player) {
			this.setSize(0.25F, 0.25F);
			this.ignoreFrustumCheck = true;
			this.angler = player;
			}


		
		private void shoot() {
			float f = angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
			float f1 = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
			float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
			float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
			float f4 = -MathHelper.cos(-f * 0.017453292F);
			float f5 = MathHelper.sin(-f * 0.017453292F);
			double d0 = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - (double) f3 * 0.3D;
			double d1 = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY)
					+ (double) this.angler.getEyeHeight();
			double d2 = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - (double) f2 * 0.3D;
			setLocationAndAngles(d0, d1, d2, f1, f);
			motionX = (double) (-f3);
			motionY = (double) MathHelper.clamp(-(f5 / f4), -5.0F, 5.0F);
			motionZ = (double) (-f2);
			float f6 = MathHelper
					.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
			motionX *= 0.6D / (double) f6 + 0.5D + rand.nextGaussian() * 0.0045D;
			motionY *= 0.6D / (double) f6 + 0.5D + rand.nextGaussian() * 0.0045D;
			motionZ *= 0.6D / (double) f6 + 0.5D + rand.nextGaussian() * 0.0045D;
			float f7 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
			rotationYaw = (float) (MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));
			rotationPitch = (float) (MathHelper.atan2(motionY, (double) f7) * (180D / Math.PI));
			prevRotationYaw = rotationYaw;
			prevRotationPitch = rotationPitch;

		}
		@Override
		protected void entityInit() {
			this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
		}
		@Override
		public void notifyDataManagerChange(DataParameter<?> key) {
			if (DATA_HOOKED_ENTITY.equals(key)) {
				int i = (Integer) this.getDataManager().get(DATA_HOOKED_ENTITY);
				this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
			}

			super.notifyDataManagerChange(key);
		}

		@SideOnly(Side.CLIENT)
		public boolean isInRangeToRenderDist(double distance) {
			double d0 = 64.0D;
			return distance < 4096.0D;
		}

		@SideOnly(Side.CLIENT)
		public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		}
		
		@Override
		public void onUpdate() {
			super.onUpdate();
			if (this.angler == null) {
				//System.out.println("Cause of death was OnUpdate");
				//TODO: Why is angler becoming null? server and client?
				//System.out.println("ONUPDATE. Is the world Clientside?" +world.isRemote);
				//System.out.println("OnUpdate death removed");
				//this.setDead();
				//this is implied to be the server side
			} else if (this.world.isRemote || !this.shouldStopFishing()) {
				if (this.inGround) {
					++this.ticksInGround;
					if (this.ticksInGround >= 1200) {
						System.out.println("Cause of death was GroundTicks");
						this.setDead();
						return;
					}
				}

				float f = 0.0F;
				BlockPos blockpos = new BlockPos(this);
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				if (iblockstate.getMaterial() == Material.WATER) {
					f = BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos);
				}

				double d1;
				if (this.currentState == State.FLYING) {
					if (this.caughtEntity != null) {
						this.motionX = 0.0D;
						this.motionY = 0.0D;
						this.motionZ = 0.0D;
						this.currentState = State.HOOKED_IN_ENTITY;
						return;
					}

					if (f > 0.0F) {
						this.motionX *= 0.3D;
						this.motionY *= 0.2D;
						this.motionZ *= 0.3D;
						this.currentState = State.BOBBING;
						return;
					}

					if (!this.world.isRemote) {
						this.checkCollision();
					}

					if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
						++this.ticksInAir;
					} else {
						this.ticksInAir = 0;
						this.motionX = 0.0D;
						this.motionY = 0.0D;
						this.motionZ = 0.0D;
					}
				} else {
					if (this.currentState == State.HOOKED_IN_ENTITY) {
						if (this.caughtEntity != null) {
							if (this.caughtEntity.isDead) {
								this.caughtEntity = null;
								this.currentState = State.FLYING;
							} else {
								this.posX = this.caughtEntity.posX;
								d1 = (double) this.caughtEntity.height;
								this.posY = this.caughtEntity.getEntityBoundingBox().minY + d1 * 0.8D;
								this.posZ = this.caughtEntity.posZ;
								this.setPosition(this.posX, this.posY, this.posZ);
							}
						}

						return;
					}

					if (this.currentState == State.BOBBING) {
						this.motionX *= 0.9D;
						this.motionZ *= 0.9D;
						d1 = this.posY + this.motionY - (double) blockpos.getY() - (double) f;
						if (Math.abs(d1) < 0.01D) {
							d1 += Math.signum(d1) * 0.1D;
						}

						this.motionY -= d1 * (double) this.rand.nextFloat() * 0.2D;
						if (!this.world.isRemote && f > 0.0F) {
							this.catchingFish(blockpos);
						}
					}
				}

				if (iblockstate.getMaterial() != Material.WATER) {
					this.motionY -= 0.03D;
				}

				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.updateRotation();
				d1 = 0.92D;
				this.motionX *= 0.92D;
				this.motionY *= 0.92D;
				this.motionZ *= 0.92D;
				this.setPosition(this.posX, this.posY, this.posZ);
			}

		}

		private boolean shouldStopFishing() {
			ItemStack NatHook = new ItemStack(ItemInit.NATHOOK);
			boolean flag = this.angler.getHeldItemMainhand().isEmpty();
			boolean flag2 = true;
			if (!this.angler.isDead && this.angler.isEntityAlive() && (flag||flag2)
					&& this.getDistanceSq(this.angler) <= 1024.0D) {
				return false;
			} else {
				this.setDead();
				System.out.println("Cause of death was shouldStopFishing");
				return true;
			}
		}

		private void updateRotation() {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);

			for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f)
					* 57.29577951308232D); this.rotationPitch
							- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		}

		private void checkCollision() {
			Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1, false, true, false);
			vec3d = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (raytraceresult != null) {
				vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
			}

			Entity entity = null;
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this,
					this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
			double d0 = 0.0D;
			Iterator<Entity> var8 = list.iterator();

			while (true) {
				Entity entity1;
				double d1;
				do {
					RayTraceResult raytraceresult1;
					do {
						do {
							do {
								if (!var8.hasNext()) {
									if (entity != null) {
										raytraceresult = new RayTraceResult(entity);
									}

									if (raytraceresult != null && raytraceresult.typeOfHit != Type.MISS) {
										if (raytraceresult.typeOfHit == Type.ENTITY) {
											this.caughtEntity = raytraceresult.entityHit;
											this.setHookedEntity();
										} else {
											this.inGround = true;
										}
									}

									return;
								}

								entity1 = (Entity) var8.next();
							} while (!this.canBeHooked(entity1));
						} while (entity1 == this.angler && this.ticksInAir < 5);

						AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
						raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
					} while (raytraceresult1 == null);

					d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
				} while (d1 >= d0 && d0 != 0.0D);

				entity = entity1;
				d0 = d1;
			}
		}

		private void setHookedEntity() {
			this.getDataManager().set(DATA_HOOKED_ENTITY, this.caughtEntity.getEntityId() + 1);
		}

		private void catchingFish(BlockPos p_190621_1_) {
			WorldServer worldserver = (WorldServer) this.world;
			int i = 1;
			BlockPos blockpos = p_190621_1_.up();
			if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockpos)) {
				++i;
			}

			if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(blockpos)) {
				--i;
			}

			if (this.ticksCatchable > 0) {
				--this.ticksCatchable;
				if (this.ticksCatchable <= 0) {
					this.ticksCaughtDelay = 0;
					this.ticksCatchableDelay = 0;
				} else {
					this.motionY -= 0.2D * (double) this.rand.nextFloat() * (double) this.rand.nextFloat();
				}
			} else {
				float f5;
				float f6;
				float f7;
				double d4;
				double d5;
				double d6;
				IBlockState state;
				if (this.ticksCatchableDelay > 0) {
					this.ticksCatchableDelay -= i;
					if (this.ticksCatchableDelay > 0) {
						this.fishApproachAngle = (float) ((double) this.fishApproachAngle
								+ this.rand.nextGaussian() * 4.0D);
						f5 = this.fishApproachAngle * 0.017453292F;
						f6 = MathHelper.sin(f5);
						f7 = MathHelper.cos(f5);
						d4 = this.posX + (double) (f6 * (float) this.ticksCatchableDelay * 0.1F);
						d5 = (double) ((float) MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
						d6 = this.posZ + (double) (f7 * (float) this.ticksCatchableDelay * 0.1F);
						state = worldserver.getBlockState(new BlockPos(d4, d5 - 1.0D, d6));
						if (state.getMaterial() == Material.WATER) {
							if (this.rand.nextFloat() < 0.15F) {
								worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d4, d5 - 0.10000000149011612D, d6,
										1, (double) f6, 0.1D, (double) f7, 0.0D, new int[0]);
							}

							float f3 = f6 * 0.04F;
							float f4 = f7 * 0.04F;
							worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d4, d5, d6, 0, (double) f4, 0.01D,
									(double) (-f3), 1.0D, new int[0]);
							worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d4, d5, d6, 0, (double) (-f4), 0.01D,
									(double) f3, 1.0D, new int[0]);
						}
					} else {
						this.motionY = (double) (-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
						this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F,
								1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
						double d3 = this.getEntityBoundingBox().minY + 0.5D;
						worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ,
								(int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width,
								0.20000000298023224D, new int[0]);
						worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ,
								(int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width,
								0.20000000298023224D, new int[0]);
						this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
					}
				} else if (this.ticksCaughtDelay > 0) {
					this.ticksCaughtDelay -= i;
					f5 = 0.15F;
					if (this.ticksCaughtDelay < 20) {
						f5 = (float) ((double) f5 + (double) (20 - this.ticksCaughtDelay) * 0.05D);
					} else if (this.ticksCaughtDelay < 40) {
						f5 = (float) ((double) f5 + (double) (40 - this.ticksCaughtDelay) * 0.02D);
					} else if (this.ticksCaughtDelay < 60) {
						f5 = (float) ((double) f5 + (double) (60 - this.ticksCaughtDelay) * 0.01D);
					}

					if (this.rand.nextFloat() < f5) {
						f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
						f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
						d4 = this.posX + (double) (MathHelper.sin(f6) * f7 * 0.1F);
						d5 = (double) ((float) MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
						d6 = this.posZ + (double) (MathHelper.cos(f6) * f7 * 0.1F);
						state = worldserver.getBlockState(new BlockPos((int) d4, (int) d5 - 1, (int) d6));
						if (state.getMaterial() == Material.WATER) {
							worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2),
									0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
						}
					}

					if (this.ticksCaughtDelay <= 0) {
						this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
						this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
					}
				} else {
					this.ticksCaughtDelay = MathHelper.getInt(this.rand, 100, 600);
					this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
				}
			}

		}

		protected boolean canBeHooked(Entity entity) {
			return entity.canBeCollidedWith() || entity instanceof EntityItem;
		}
		
		@Override
		public void writeEntityToNBT(NBTTagCompound writeTagCompound) {
		}
		
		@Override
		public void readEntityFromNBT(NBTTagCompound readTagCompound) {
		}

		public void handleHookRetraction() {
			System.out.println("Attempting Hook Retraction");
			if (!this.world.isRemote && this.angler != null) {
				
				DifferentItemFishedEvent event = null;
				
				System.out.println("Top of Event!");
				
				if (this.caughtEntity != null) {
					this.bringInHookedEntity();
					this.world.setEntityState(this, (byte) 31);
				} 
				
				
				else if (this.ticksCatchable > 0) {
					Builder lootcontext$builder = new Builder((WorldServer) this.world);
					lootcontext$builder.withLuck((float) this.luck + this.angler.getLuck()).withPlayer(this.angler)
							.withLootedEntity(this);
					
					
					List<ItemStack> result = this.world.getLootTableManager()
							.getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
							.generateLootForPools(this.rand, lootcontext$builder.build());
					
					
					event = new DifferentItemFishedEvent(result, this);
					
					MinecraftForge.EVENT_BUS.post(event);
					
					if (event.isCanceled()) {
						this.setDead();
						System.out.println("Hook Should be Retracted!");
					}

					Iterator var5 = result.iterator();

					label50 : while (true) {
						Item item;
						do {
							if (!var5.hasNext()) {
								//output
								break label50;
							}

							ItemStack itemstack = (ItemStack) var5.next();
							EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
							double d0 = this.angler.posX - this.posX;
							double d1 = this.angler.posY - this.posY;
							double d2 = this.angler.posZ - this.posZ;
							double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
							double d4 = 0.1D;
							entityitem.motionX = d0 * 0.1D;
							entityitem.motionY = d1 * 0.1D + (double) MathHelper.sqrt(d3) * 0.08D;
							entityitem.motionZ = d2 * 0.1D;
							this.world.spawnEntity(entityitem);
							this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX,
							this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
							item = itemstack.getItem();
						} while (item != Items.FISH && item != Items.COOKED_FISH);

						this.angler.addStat(StatList.FISH_CAUGHT, 1);
					}
				}

			}
			System.out.println("Cause of death was handleHookRetraction");
			this.setDead();
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void handleStatusUpdate(byte byted) {
			if (byted == 31 && this.world.isRemote && this.caughtEntity instanceof EntityPlayer
					&& ((EntityPlayer) this.caughtEntity).isUser()) {
				this.bringInHookedEntity();
			}

			super.handleStatusUpdate(byted);
		}

		protected void bringInHookedEntity() {
			if (this.angler != null) {
				double d0 = this.angler.posX - this.posX;
				double d1 = this.angler.posY - this.posY;
				double d2 = this.angler.posZ - this.posZ;
				double d3 = 0.1D;
				this.caughtEntity.motionX += d0 * 0.1D;
				this.caughtEntity.motionY += d1 * 0.1D;
				this.caughtEntity.motionZ += d2 * 0.1D;
			}

		}

		protected boolean canTriggerWalking() {
			return false;
		}
		@Override
		public void setDead() {
			super.setDead();
			System.out.println("Killed "+this);
			if (angler != null)
			System.out.println("Angler is NOT NULL. Is the world a client? " +world.isRemote);
			PacketHandler.instance.sendToServer(new MessagePlayerServer());
		}

		public EntityPlayer getAngler() {
			return this.angler;
		}
		
		public static void resetID(EntityPlayer Player) {
		System.out.println("Resetting ID");
			if (Player instanceof EntityPlayerMP) {
			Player.getEntityData().setInteger(fishServer, 0);
			}
			
			/*else if (Player instanceof EntityPlayerSP) {
			Player.getEntityData().setInteger(fishClient, 0);
			}*/
			
			}

		@Override
		public Entity getThrower() {
			// TODO Auto-generated method stub
			return this.angler;
		}

		@Override
		public void setThrower(Entity var1) {
			// TODO Auto-generated method stub
			
		}
		
		



		//UUID ServerID = getID(player.getEntityData().getString(fishServer));
		
}
