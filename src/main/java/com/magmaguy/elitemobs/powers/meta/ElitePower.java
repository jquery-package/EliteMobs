package com.magmaguy.elitemobs.powers.meta;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.api.EliteMobDamagedByPlayerEvent;
import com.magmaguy.elitemobs.api.PlayerDamagedByEliteMobEvent;
import com.magmaguy.elitemobs.config.CustomConfigFields;
import com.magmaguy.elitemobs.config.powers.PowersConfigFields;
import com.magmaguy.elitemobs.mobconstructor.EliteEntity;
import com.magmaguy.elitemobs.powers.scripts.EliteScript;
import com.magmaguy.elitemobs.utils.WarningMessage;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import com.magmaguy.elitemobs.utils.ClassFinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;

public class ElitePower {

    @Getter
    private static final HashMap<String, CustomConfigFields> elitePowers = new HashMap<>();
    @Getter
    private static final HashSet<CustomConfigFields> bossPowers = new HashSet<>();
    @Getter
    private static final HashSet<CustomConfigFields> majorPowers = new HashSet<>();
    @Getter
    private static final HashSet<CustomConfigFields> defensivePowers = new HashSet<>();
    @Getter
    private static final HashSet<CustomConfigFields> miscellaneousPowers = new HashSet<>();
    @Getter
    private static final HashSet<CustomConfigFields> offensivePowers = new HashSet<>();
    @Getter
    private static final HashSet<CustomConfigFields> specialPowers = new HashSet<>();

    @Getter
    private final String fileName;
    @Getter
    private final CustomConfigFields powersConfigFields;
    @Getter
    private String trail = null;
    @Getter
    @Setter
    private int powerCooldownTime = 0;
    @Getter
    @Setter
    private int globalCooldownTime = 0;
    @Getter
    @Setter
    private boolean inGlobalCooldown = false;
    @Getter
    private boolean powerCooldownActive = false;
    @Getter
    @Setter
    private boolean isFiring = false;


    //Constructor for scripts
    public ElitePower(CustomConfigFields customConfigFields) {
        this.fileName = customConfigFields.getFilename();
        this.powersConfigFields = customConfigFields;
    }

    //Costructor for classic powers
    public ElitePower(PowersConfigFields powersConfigFields) {
        this.powersConfigFields = powersConfigFields;
        this.fileName = powersConfigFields.getFilename();
        this.trail = powersConfigFields.getEffect();
        this.powerCooldownTime = powersConfigFields.getPowerCooldown();
        this.globalCooldownTime = powersConfigFields.getGlobalCooldown();
    }

    public static void addPower(EliteEntity eliteEntity, PowersConfigFields configFields) {
        if (configFields.getEliteScriptBlueprints().isEmpty())
            try {
                ElitePower elitePower = configFields.getElitePowerClass().newInstance();
                eliteEntity.getElitePowers().add(elitePower);
                elitePower.applyPowers(eliteEntity.getLivingEntity());
            } catch (Exception ex) {
                new WarningMessage("Failed to assign power for config field " + configFields.getFilename());
            }
        else
            eliteEntity.getElitePowers().addAll(EliteScript.generateBossScripts(configFields.getEliteScriptBlueprints()));
    }

    public static void initializePowers() {
        List<Class<?>> classes;
        try {
            classes = ClassFinder.find("com.magmaguy.elitemobs.powers");
        } catch (IOException ex) {
            new WarningMessage("Failed to initialize powers");
            ex.printStackTrace();
            return;
        }
        classes.forEach(power -> {
            if (ElitePower.class.isAssignableFrom(power)) {
                try {
				   ElitePower thisPower = (ElitePower)power.newInstance();
                   switch (((PowersConfigFields) thisPower.getPowersConfigFields()).getPowerType()) {
                       case DEFENSIVE -> defensivePowers.add(thisPower.getPowersConfigFields());
                       case OFFENSIVE -> offensivePowers.add(thisPower.getPowersConfigFields());
                       case MAJOR_BLAZE, MAJOR_ENDERMAN, MAJOR_SKELETON, MAJOR_GHAST, MAJOR_ZOMBIE ->
                               majorPowers.add(thisPower.getPowersConfigFields());
                       case MISCELLANEOUS -> miscellaneousPowers.add(thisPower.getPowersConfigFields());
                   }
                   elitePowers.put(thisPower.getFileName(), thisPower.getPowersConfigFields());
                } catch (Exception ex) {
                    //Not sure why stuff in the meta package is getting scanned, seems like the package scan isn't working as intended
                    //todo: figure out why package scanning is getting more than what is in the packages here
                    //
                }
            }
        });
    }

    protected static boolean eventIsValid(EliteMobDamagedByPlayerEvent event, ElitePower elitePower, boolean ignoreGlobalCooldown) {
        if (event.isCancelled()) return false;
        if (event.getEliteMobEntity().getLivingEntity() == null) return false;
        if (!event.getEliteMobEntity().getLivingEntity().hasAI()) return false;
        if (!ignoreGlobalCooldown)
            if (elitePower.isInGlobalCooldown()) return false;
        return !event.getEliteMobEntity().isInCooldown();
    }

    protected static boolean eventIsValid(EliteMobDamagedByPlayerEvent event, ElitePower elitePower) {
        if (event.isCancelled()) return false;
        if (event.getEliteMobEntity().getLivingEntity() == null) return false;
        if (!event.getEliteMobEntity().getLivingEntity().hasAI()) return false;
        if (elitePower.isInGlobalCooldown()) return false;
        if (elitePower.isInCooldown(event.getEliteMobEntity())) return false;
        return !event.getEliteMobEntity().isInCooldown();
    }

    protected static boolean eventIsValid(PlayerDamagedByEliteMobEvent event, ElitePower elitePower) {
        if (event.isCancelled()) return false;
        if (event.getEliteMobEntity().getLivingEntity() == null) return false;
        if (!event.getEliteMobEntity().getLivingEntity().hasAI()) return false;
        if (elitePower.isInGlobalCooldown()) return false;
        return !event.getEliteMobEntity().isInCooldown();
    }

    /**
     * This is overwritten by certain classes to apply powers to a living entity upon activation
     *
     * @param livingEntity
     */
    public void applyPowers(LivingEntity livingEntity) {
        //This is overwritten by certain classes to apply powers to a living entity upon activation
    }

    public boolean isInCooldown(EliteEntity eliteEntity) {
        return this.powerCooldownActive || eliteEntity.isInCooldown();
    }

    public void setInCooldown(EliteEntity eliteEntity, boolean inCooldown) {
        eliteEntity.setInCooldown(inCooldown);
        setInGlobalCooldown(inCooldown);
    }

    public void doCooldown(EliteEntity eliteEntity) {
        this.powerCooldownActive = true;
        if (globalCooldownTime < 1) return;

        eliteEntity.doGlobalPowerCooldown(globalCooldownTime * 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                powerCooldownActive = false;
            }
        }.runTaskLater(MetadataHandler.PLUGIN, powerCooldownTime * 20L);

    }

    public void doCooldownTicks(EliteEntity eliteEntity) {
        this.powerCooldownActive = true;
        if (globalCooldownTime > 0)
            eliteEntity.doGlobalPowerCooldown(globalCooldownTime);
        if (powerCooldownTime > 0)
            new BukkitRunnable() {
                @Override
                public void run() {
                    powerCooldownActive = false;
                }
            }.runTaskLater(MetadataHandler.PLUGIN, powerCooldownTime);

    }

    protected void doGlobalCooldown(int ticks, EliteEntity eliteEntity) {
        setInGlobalCooldown(true);
        eliteEntity.doCooldown();
        new BukkitRunnable() {
            @Override
            public void run() {
                setInGlobalCooldown(false);
            }
        }.runTaskLater(MetadataHandler.PLUGIN, ticks);
    }

    protected void doGlobalCooldown(int ticks) {
        setInGlobalCooldown(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                setInGlobalCooldown(false);
            }
        }.runTaskLater(MetadataHandler.PLUGIN, ticks);
    }

}
