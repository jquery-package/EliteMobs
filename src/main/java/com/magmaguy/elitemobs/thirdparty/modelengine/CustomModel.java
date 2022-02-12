package com.magmaguy.elitemobs.thirdparty.modelengine;

import com.magmaguy.elitemobs.entitytracker.EntityTracker;
import com.magmaguy.elitemobs.mobconstructor.EliteEntity;
import com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity;
import com.magmaguy.elitemobs.utils.InfoMessage;
import com.magmaguy.elitemobs.utils.WarningMessage;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class CustomModel {

    @Getter
    private boolean success = false;

    private CustomModel(LivingEntity livingEntity, String modelName, String nametagName) {
        try {
            if (ModelEngineAPI.api.getModelManager().getModelRegistry().getModelBlueprint(modelName) == null)
                new InfoMessage("Model " + modelName + " was not found! Make sure you install the model correctly if you have it. This entry will be skipped!");
        } catch (NoSuchMethodError ex) {
            new WarningMessage("Model Engine API version is not supported. Currently Elitemobs can only support Model Engine R2.2.0, documentation for other versions doesn't exist.");
        }

        activeModel = ModelEngineAPI.api.getModelManager().createActiveModel(modelName);
        if (activeModel == null) {
            new WarningMessage("Failed to load model from " + modelName + " ! Is the model name correct, and has the model been installed correctly?");
            return;
        }

        try {
            if (activeModel.getModelId() == null) return;
        } catch (Exception exception) {
            new WarningMessage("Well fuck me I guess");
            return;
        }

        modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(livingEntity);
        if (modeledEntity == null) {
            new WarningMessage("Failed to create model entity " + modelName + " ! Is the model name correct, and has the model been installed correctly?");
            return;
        }

        try {
            try {
                modeledEntity.addActiveModel(activeModel);
                modeledEntity.detectPlayers();
                modeledEntity.setInvisible(true);
                setName(nametagName, true);
                success = true;
            } catch (Exception exception) {
                modeledEntity.removeModel(modelName);
                new WarningMessage("Failed to make model entity " + modelName + " ! Is the model name correct, and has the model been installed correctly?");
            }
        } catch (NoSuchMethodError error) {
            new WarningMessage("Failed to make model entity " + modelName + " ! Is the model name correct, and has the model been installed correctly?");
        }
    }

    public static CustomModel generateCustomModel(LivingEntity livingEntity, String modelName, String nametagName) {
        CustomModel customModel = new CustomModel(livingEntity, modelName, nametagName);
        return customModel.isSuccess() ? customModel : null;
    }


    ActiveModel activeModel;
    ModeledEntity modeledEntity;

    public static void reloadModels() {
        try {
            ModelEngineAPI.api.getModelManager().registerModels();
        } catch (Exception ex) {
            new WarningMessage("Model Engine API version is not supported. Currently Elitemobs can only support Model Engine R2.2.0, documentation for other versions doesn't exist.");
        }
    }

    public void shoot() {
        if (activeModel == null) return;
        if (activeModel.getState("attack_ranged") != null)
            activeModel.addState("attack_ranged", 1, 1, 1);
        else
            activeModel.addState("attack", 1, 1, 1);
    }

    public void melee() {
        if (activeModel == null) return;
        if (activeModel.getState("attack_melee") != null)
            activeModel.addState("attack_melee", 1, 1, 1);
        else
            activeModel.addState("attack", 1, 1, 1);
    }

    public void setName(String nametagName, boolean visible) {
        if (modeledEntity == null) return;
        modeledEntity.getNametagHandler().setCustomName("hitbox", nametagName);
        modeledEntity.getNametagHandler().setCustomNameVisibility("hitbox", true);
    }

    public void switchPhase() {
        activeModel.removeState("death", true);
    }

    public void setNameVisible(boolean visible) {
        modeledEntity.getNametagHandler().setCustomNameVisibility("hitbox", visible);
    }

    public static class ModelEntityEvents implements Listener {
        @EventHandler
        public void onMeleeHit(EntityDamageByEntityEvent event) {
            EliteEntity eliteEntity = EntityTracker.getEliteMobEntity(event.getDamager());
            if (!(eliteEntity instanceof CustomBossEntity)) return;
            if (((CustomBossEntity) eliteEntity).getCustomModel() == null) return;
            ((CustomBossEntity) eliteEntity).getCustomModel().melee();
        }

        @EventHandler
        public void onRangedShot(EntitySpawnEvent event) {
            if (!(event.getEntity() instanceof Projectile)) return;
            if (!(((Projectile) event.getEntity()).getShooter() instanceof LivingEntity)) return;
            EliteEntity eliteEntity = EntityTracker.getEliteMobEntity((LivingEntity) ((Projectile) event.getEntity()).getShooter());
            if (!(eliteEntity instanceof CustomBossEntity)) return;
            if (((CustomBossEntity) eliteEntity).getCustomModel() == null) return;
            ((CustomBossEntity) eliteEntity).getCustomModel().shoot();
        }
    }
}