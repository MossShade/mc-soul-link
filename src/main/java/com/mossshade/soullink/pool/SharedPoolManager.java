package com.mossshade.soullink.pool;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.damage.SoullinkDamageTypes;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.mixin.ServerPlayerEntityAccessor;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.overrides.SharedHungerManager;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import java.util.UUID;

public class SharedPoolManager implements IPoolManager<ServerPlayer> {

	private final MinecraftServer minecraftServer;

	private final SharedPoolState pool;

	public final DirtyTracker<UUID> dirtyTracker;

	private final PoolMockPlayer mockPlayer;

	private final SharedHungerManager hungerManager;

	private boolean isDead = false;

	private boolean tickedHunger = false;

	public SharedPoolManager(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
		this.pool = SharedPoolState.getServerState(minecraftServer);
		this.dirtyTracker = new DirtyTracker<>();

		this.mockPlayer = new PoolMockPlayer(minecraftServer, minecraftServer.getLevel(ServerLevel.OVERWORLD), this);
		this.hungerManager = new SharedHungerManager();
	}

	public float getPoolHealth() {
		return this.pool.getHealth();
	}

	public int getPoolFoodLevel() {
		return this.pool.getFoodLevel();
	}

	public float getPoolSaturationLevel() {
		return this.pool.getSaturationLevel();
	}

	public float getPoolExhaustion() {
		return this.pool.getExhaustion();
	}

	public int getPoolFoodTickTimer() {
		return this.pool.getFoodTickTimer();
	}

	@Override
	public void propagatePool() {
		this.pool.setDirty();
		this.dirtyTracker.markDirty(dirtyTracker.getDirt());
		this.tickedHunger = false;

		syncEveryone();
	}

	@Override
	public void reset() {
		this.dirtyTracker.clean();

		this.pool.initValues();

		isDead = false;

		this.propagatePool();
	}


	@Override
	public void syncEveryone() {
		this.applyToEveryone(this::syncEntity, false);
	}

	@Override
	public void syncEntity(ServerPlayer player) {
		if (isDead) {
			player.setHealth(0);
			return;
		}

		float previousHealth = player.getHealth();

		player.setHealth(this.pool.getHealth());
		player.getFoodData().setFoodLevel(this.pool.getFoodLevel());
		player.getFoodData().setSaturation(this.pool.getSaturationLevel());
		HungerManagerAccess hungerManagerAccess = (HungerManagerAccess) player.getFoodData();
		hungerManagerAccess.soullink$setExhaustion(this.pool.getExhaustion());
		hungerManagerAccess.soullink$setFoodTickTimer(this.pool.getFoodTickTimer());

		if (previousHealth < player.getHealth()) {
			player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));

			ServerPlayerEntityAccessor serverPlayerEntityAccessor = (ServerPlayerEntityAccessor) player;
			serverPlayerEntityAccessor.setLastSentHealth(player.getHealth());
			serverPlayerEntityAccessor.setLastSentFood(player.getFoodData().getFoodLevel());
			serverPlayerEntityAccessor.setLastFoodSaturationZero(player.getFoodData().getSaturationLevel() == 0.0F);
		}
	}

	@Override
	public void applyToEveryone(EveryoneOperation<ServerPlayer> function, boolean skipSourcePlayer) {
		for (ServerLevel world : this.minecraftServer.getAllLevels()) {
			for (ServerPlayer serverPlayerEntity : world.players()) {
				if (skipSourcePlayer && this.dirtyTracker.isDirty() && this.dirtyTracker.getDirt() == serverPlayerEntity.getUUID()) continue;

				function.apply(serverPlayerEntity);
			}
		}
	}

	public void killEveryone(DamageSource source) {
		Soullink.LOGGER.debug("killEveryone due to {}", source);

		isDead = true;

		this.applyToEveryone((player) -> {
			player.setHealth(0F);
			player.getCombatTracker().recordDamage(SoullinkDamageTypes.SOUL_FRAGMENTATION, 0f);
			player.die(SoullinkDamageTypes.SOUL_FRAGMENTATION);
		} , true);
	}

	public void propagateHealth(float health) {
		this.pool.setHealth(health);
	}

	public void addDamage(float damage) {
		this.pool.setHealth(this.pool.getHealth() - damage);
	}

	public void addHeal(float heal) {
		this.pool.setHealth(this.pool.getHealth() + heal);
	}

	public void addExhaustion(float exhaustion) {
		this.pool.setExhaustion(this.pool.getExhaustion() + exhaustion);
	}

	public void addFood(int nutrition, float saturation) {
		this.pool.setFoodLevel(this.pool.getFoodLevel() + nutrition);
		this.pool.setSaturationLevel(this.pool.getSaturationLevel() + saturation);
	}

	public void tickSharedHunger() {
		if (tickedHunger) return;
		if (isDead) return;
		if (ConfigManager.isDisabled()) return;

		tickedHunger = true;

		this.hungerTick();
	}

	private void hungerTick() {
		this.dirtyTracker.clean();
		this.hungerManager.setValues(this);
		this.mockPlayer.setHealth(this.pool.getHealth());

		hungerManager.tick(this.mockPlayer);

		this.pool.setHealth(this.mockPlayer.getHealth());
		this.pool.setFoodLevel(this.hungerManager.getFoodLevel());
		this.pool.setSaturationLevel(this.hungerManager.getSaturationLevel());
		this.pool.setExhaustion(this.hungerManager.getAccess().soullink$getExhaustion());
		this.pool.setFoodTickTimer(this.hungerManager.getAccess().soullink$getFoodTickTimer());
	}

	@Override
	public String toString() {
		return "SharedPoolManager{" +
				"pool=" + pool +
				", dirtyTracker=" + dirtyTracker +
				'}';
	}

}
