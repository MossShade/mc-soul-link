package com.mossshade.soullink.overrides;

import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.entity.player.HungerManager;

public class SharedHungerManager extends HungerManager {

	private final HungerManagerAccess access = (HungerManagerAccess) this;

	public void setValues(SharedPoolManager poolManager) {
		this.setFoodLevel(poolManager.getPoolFoodLevel());
		this.setSaturationLevel(poolManager.getPoolSaturationLevel());

		this.access.soullink$setExhaustion(poolManager.getPoolExhaustion());
		this.access.soullink$setFoodTickTimer(poolManager.getPoolFoodTickTimer());
	}

	public HungerManagerAccess getAccess() {
		return this.access;
	}

}
