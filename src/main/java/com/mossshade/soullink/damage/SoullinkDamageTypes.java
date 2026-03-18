package com.mossshade.soullink.damage;

import com.mossshade.soullink.Constants;
import net.minecraft.entity.damage.*;
import net.minecraft.registry.entry.RegistryEntry;

public class SoullinkDamageTypes {

	private static final DamageType damageType = new DamageType(
			Constants.SOULLINK_FRAGMENTATION_DAMAGE_TYPE,
			DamageScaling.NEVER,
			0,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
	);

	public static final DamageSource SOUL_FRAGMENTATION = new DamageSource(RegistryEntry.of(damageType));

}
