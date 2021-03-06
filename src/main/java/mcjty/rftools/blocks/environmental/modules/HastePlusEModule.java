package mcjty.rftools.blocks.environmental.modules;

import mcjty.rftools.PlayerBuff;
import mcjty.rftools.blocks.environmental.EnvironmentalConfiguration;

public class HastePlusEModule extends PotionEffectModule {

    public HastePlusEModule() {
        super("haste", 2);
    }

    @Override
    public float getRfPerTick() {
        return EnvironmentalConfiguration.HASTEPLUS_RFPERTICK;
    }

    @Override
    protected PlayerBuff getBuff() {
        return PlayerBuff.BUFF_HASTEPLUS;
    }
}
