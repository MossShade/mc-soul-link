package com.mossshade.soullink.pool;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.damage.SoullinkDamageTypes;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.mixin.ServerPlayerEntityAccessor;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.overrides.SharedHungerManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class SharedPoolManager implements IPoolManager<ServerPlayerEntity> {

	private final MinecraftServer minecraftServer;

	private final SharedPoolState pool;

	public final DirtyTracker<UUID> dirtyTracker;

	private final PoolMockPlayer mockPlayer;

	private final SharedHungerManager hungerManager;

	public SharedPoolManager(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
		this.pool = SharedPoolState.getServerState(minecraftServer);
		this.dirtyTracker = new DirtyTracker<>();

		this.mockPlayer = new PoolMockPlayer(minecraftServer, minecraftServer.getWorld(ServerWorld.OVERWORLD), this);
		this.hungerManager = new SharedHungerManager();
	}

	public float getPoolHealth() {
		return this.pool.getHealth();
	}

	public float getMaxHealth() {
		return this.pool.getMaxHealth();
	}

	public int getMaxFoodLevel() {
		return this.pool.getMaxFoodLevel();
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
		this.pool.markDirty();
		this.dirtyTracker.markDirty(null);

		syncEveryone();
	}

	@Override
	public void reset() {
		this.dirtyTracker.clean();

		this.pool.initValues();

		this.propagatePool();
	}


	@Override
	public void syncEveryone() {
		this.applyToEveryone(this::syncEntity, false);
	}

	@Override
	public void syncEntity(ServerPlayerEntity player) {
		player.setHealth(this.pool.getHealth());

		ServerPlayerEntityAccessor serverPlayerEntityAccessor = (ServerPlayerEntityAccessor) player;
		if (serverPlayerEntityAccessor.getSyncedHealth() > player.getHealth()) {
			if (this.dirtyTracker.getDirt() == null || this.dirtyTracker.getDirt() != player.getUuid()) {
				((ServerPlayerEntityAccessor) player).setSyncedHealth(this.pool.getHealth());
			}
		}

		player.getHungerManager().setFoodLevel(this.pool.getFoodLevel());
		player.getHungerManager().setSaturationLevel(this.pool.getSaturationLevel());
		HungerManagerAccess hungerManagerAccess = (HungerManagerAccess) player.getHungerManager();
		hungerManagerAccess.soullink$setExhaustion(this.pool.getExhaustion());
		hungerManagerAccess.soullink$setFoodTickTimer(this.pool.getFoodTickTimer());
	}

	@Override
	public void applyToEveryone(EveryoneOperation<ServerPlayerEntity> function, boolean skipSourcePlayer) {
		for (ServerWorld world : this.minecraftServer.getWorlds()) {
			for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
				if (!skipSourcePlayer && this.dirtyTracker.isDirty() && this.dirtyTracker.getDirt() == serverPlayerEntity.getUuid()) continue;

				function.apply(serverPlayerEntity);
			}
		}
	}

	public void killEveryone(DamageSource source) {
		Soullink.LOGGER.debug("killEveryone due to {}", source);

		this.applyToEveryone((player) -> {
			player.getDamageTracker().onDamage(SoullinkDamageTypes.SOUL_FRAGMENTATION, 0f);
			player.onDeath(SoullinkDamageTypes.SOUL_FRAGMENTATION);
		} , true);
	}

	public void propagateHealth(float health) {
		this.pool.setHealth(health);

		this.propagatePool();
	}

	public void propagateFoodLevel(int food) {
		this.pool.setFoodLevel(food);

		this.propagatePool();
	}

	public void propagateSaturationLevel(float saturation) {
		this.pool.setSaturationLevel(saturation);

		this.propagatePool();
	}

	public void propagateExhaustion(float exhaustion) {
		this.pool.setExhaustion(exhaustion);

		this.propagatePool();
	}

	public void propagateFoodTickTimer(int foodTickTimer) {
		this.pool.setFoodTickTimer(foodTickTimer);

		this.propagatePool();
	}

	public void addDamage(float damage) {
		this.pool.setHealth(this.pool.getHealth() - damage);

		this.propagatePool();
	}

	public void addHeal(float heal) {
		this.pool.setHealth(this.pool.getHealth() + heal);

		this.propagatePool();
	}

	public void addExhaustion(float exhaustion) {
		this.pool.setExhaustion(this.pool.getExhaustion() + exhaustion);

		this.propagatePool();
	}

	public void addFood(int nutrition, float saturation) {
		this.pool.setFoodLevel(this.pool.getFoodLevel() + nutrition);
		this.pool.setSaturationLevel(this.pool.getSaturationLevel() + saturation);

		this.propagatePool();
	}

	public void tickSharedHunger() {
		if (ConfigManager.isDisabled()) return;

		this.dirtyTracker.clean();
		this.hungerManager.setValues(this);
		this.mockPlayer.setHealth(this.pool.getHealth());

		hungerManager.update(this.mockPlayer);

		this.pool.setHealth(this.mockPlayer.getHealth());
		this.pool.setFoodLevel(this.hungerManager.getFoodLevel());
		this.pool.setSaturationLevel(this.hungerManager.getSaturationLevel());
		this.pool.setExhaustion(this.hungerManager.getAccess().soullink$getExhaustion());
		this.pool.setFoodTickTimer(this.hungerManager.getAccess().soullink$getFoodTickTimer());

		this.propagatePool();
	}

	@Override
	public String toString() {
		return "SharedPoolManager{" +
				"pool=" + pool +
				'}';
	}
}
