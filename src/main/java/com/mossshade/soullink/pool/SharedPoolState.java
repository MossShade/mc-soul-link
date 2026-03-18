package com.mossshade.soullink.pool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mossshade.soullink.Constants;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

public class SharedPoolState extends PersistentState {

	private final float maxHealth;
	private final int maxFoodLevel;

	private float health;
	private int foodLevel;
	private float saturationLevel;
	private float exhaustion;
	private int foodTickTimer;

	public static final Codec<SharedPoolState> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.FLOAT.fieldOf(Constants.HEALTH_TAG).forGetter(state -> state.health),
					Codec.INT.fieldOf(Constants.FOOD_LEVEL_TAG).forGetter(state -> state.foodLevel),
					Codec.FLOAT.fieldOf(Constants.SATURATION_LEVEL_TAG).forGetter(state -> state.saturationLevel),
					Codec.FLOAT.fieldOf(Constants.EXHAUSTION_TAG).forGetter(state -> state.exhaustion),
					Codec.INT.fieldOf(Constants.FOOD_TICK_TIMER_TAG).forGetter(state -> state.foodTickTimer)
			).apply(instance, (health, foodLevel, saturationLevel, exhaustion, foodTickTimer) -> {
				SharedPoolState state = new SharedPoolState();
				state.health = health;
				state.foodLevel = foodLevel;
				state.saturationLevel = saturationLevel;
				state.exhaustion = exhaustion;
				state.foodTickTimer = foodTickTimer;
				return state;
			})
	);

	public static final PersistentStateType<SharedPoolState> TYPE = new PersistentStateType<>(
			Constants.SHARED_POOL,
			SharedPoolState::new,
			CODEC,
			DataFixTypes.SAVED_DATA_COMMAND_STORAGE);


	public SharedPoolState() {
		RegistryEntry<EntityAttribute> maxHealthRegistry = EntityAttributes.MAX_HEALTH;
		if (maxHealthRegistry != null && maxHealthRegistry.value() != null && maxHealthRegistry.value() != null) {
			this.maxHealth = (float) maxHealthRegistry.value().getDefaultValue();
		} else {
			this.maxHealth = Constants.DEFAULT_MAX_HEALTH;
		}

		this.maxFoodLevel = Constants.DEFAULT_MAX_FOOD_LEVEL;

		this.initValues();
	}

	public void initValues() {
		this.health = this.maxHealth;
		this.foodLevel = this.maxFoodLevel;
		this.saturationLevel = Constants.DEFAULT_SATURATION_LEVEL;
		this.exhaustion = Constants.DEFAULT_EXHAUSTION;
		this.foodTickTimer = Constants.DEFAULT_FOOD_TICK_TIMER;
	}

	public static SharedPoolState getServerState(MinecraftServer server) {
		ServerWorld serverWorld = server.getWorld(ServerWorld.OVERWORLD);
		assert serverWorld != null;
		return serverWorld.getPersistentStateManager().getOrCreate(TYPE);
	}


	public float getMaxHealth() {
		return maxHealth;
	}

	public int getMaxFoodLevel() {
		return maxFoodLevel;
	}

	public float getHealth() {
		return this.health;
	}

	public void setHealth(float health) {
		this.health = MathHelper.clamp(health, 0.0F, this.maxHealth);
	}

	public int getFoodLevel() {
		return this.foodLevel;
	}

	public void setFoodLevel(int foodLevel) {
		this.foodLevel = MathHelper.clamp(foodLevel, 0, this.maxFoodLevel);
	}

	public float getSaturationLevel() {
		return saturationLevel;
	}

	public void setSaturationLevel(float saturationLevel) {
		this.saturationLevel = MathHelper.clamp(saturationLevel, 0.0F, (float)this.foodLevel);
	}

	public float getExhaustion() {
		return exhaustion;
	}

	public void setExhaustion(float exhaustion) {
		this.exhaustion = MathHelper.clamp(exhaustion, 0, 40.0F);
	}

	public int getFoodTickTimer() {
		return foodTickTimer;
	}

	public void setFoodTickTimer(int foodTickTimer) {
		this.foodTickTimer = foodTickTimer;
	}

	@Override
	public String toString() {
		return "SharedPoolState{" +
				"health=" + health +
				", foodLevel=" + foodLevel +
				", saturationLevel=" + saturationLevel +
				", exhaustion=" + exhaustion +
				", foodTickTimer=" + foodTickTimer +
				'}';
	}

}
