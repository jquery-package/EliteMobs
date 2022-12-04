package com.magmaguy.elitemobs.config.powers.premade;

import com.magmaguy.elitemobs.config.powers.PowersConfigFields;
<<<<<<< HEAD

public class FrostWalkerConfig extends PowersConfigFields {
    public FrostWalkerConfig() {
        super("frost_walker", true, "Frost Walker", null);
=======
import com.magmaguy.elitemobs.powers.FrostWalker;

public class FrostWalkerConfig extends PowersConfigFields {
    public FrostWalkerConfig() {
        super("frost_walker", true, null, FrostWalker.class, PowerType.MISCELLANEOUS);
>>>>>>> master
    }
}
