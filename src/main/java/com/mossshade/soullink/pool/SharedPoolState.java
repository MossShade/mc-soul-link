package com.mossshade.soullink.pool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mossshade.soullink.utils.Constants;
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


	public SharedPoolState() {
		this.maxHealth = calculateMaxHealth();
		this.maxFoodLevel = calculateMaxFoodLevel();
		this.health = this.maxHealth;
		this.foodLevel = this.maxFoodLevel;
	}


	public static final Codec<SharedPoolState> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.FLOAT.fieldOf(Constants.HEALTH_TAG).forGetter(state -> state.health),
					Codec.INT.fieldOf(Constants.FOOD_LEVEL_TAG).forGetter(state -> state.foodLevel)
			).apply(instance, (health, foodLevel) -> {
				SharedPoolState state = new SharedPoolState();
				state.health = health;
				state.foodLevel = foodLevel;
				return state;
			})
	);

	public static final PersistentStateType<SharedPoolState> TYPE = new PersistentStateType<>(
			Constants.SHARED_POOL,
			SharedPoolState::new,
			CODEC,
			DataFixTypes.SAVED_DATA_COMMAND_STORAGE);

	public static SharedPoolState getServerState(MinecraftServer server) {
		ServerWorld serverWorld = server.getWorld(ServerWorld.OVERWORLD);
		assert serverWorld != null;
		return serverWorld.getPersistentStateManager().getOrCreate(TYPE);
	}


	private float calculateMaxHealth() {
		float maxHealth = Constants.MAX_HEALTH;

		RegistryEntry<EntityAttribute> maxHealthRegistry = EntityAttributes.MAX_HEALTH;
		if (maxHealthRegistry != null) {
			EntityAttribute maxHealthAttribute = maxHealthRegistry.value();
			if (maxHealthAttribute != null) {
				double maxHealthValue = maxHealthAttribute.getDefaultValue();
				if (maxHealthValue != maxHealth) {
					return (float) maxHealthValue;
				}
			}
		}

		return maxHealth;
	}

	private int calculateMaxFoodLevel() {
		return Constants.MAX_FOOD_LEVEL;
	}

	public void setHealth(float health) {
		this.health = MathHelper.clamp(health, 0.0F, this.maxHealth);
	}

	public void setFoodLevel(int foodLevel) {
		this.foodLevel = MathHelper.clamp(foodLevel, 0, this.maxFoodLevel);
	}

	public float getHealth() {
		return this.health;
	}
	public int getFoodLevel() {
		return this.foodLevel;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public int getMaxFoodLevel() {
		return maxFoodLevel;
	}

	@Override
	public String toString() {
		return "SharedPoolState [health=" + health + ", foodLevel=" + foodLevel + "]";
	}

}
