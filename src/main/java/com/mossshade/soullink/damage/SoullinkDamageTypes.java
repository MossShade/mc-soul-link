package com.mossshade.soullink.damage;

import com.mossshade.soullink.Constants;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.*;

public class SoullinkDamageTypes {

	private static final DamageType damageType = new DamageType(
			Constants.SOULLINK_FRAGMENTATION_DAMAGE_TYPE,
			DamageScaling.NEVER,
			0,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
	);

	public static final DamageSource SOUL_FRAGMENTATION = new DamageSource(Holder.direct(damageType));

}
