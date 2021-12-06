package com.magmaguy.elitemobs.quests;

import com.magmaguy.elitemobs.ChatColorConverter;
import com.magmaguy.elitemobs.api.QuestAcceptEvent;
import com.magmaguy.elitemobs.config.customquests.CustomQuestsConfig;
import com.magmaguy.elitemobs.config.customquests.CustomQuestsConfigFields;
import com.magmaguy.elitemobs.playerdata.PlayerData;
import com.magmaguy.elitemobs.quests.objectives.QuestObjectives;
import com.magmaguy.elitemobs.quests.rewards.QuestReward;
import com.magmaguy.elitemobs.utils.EventCaller;
import com.magmaguy.elitemobs.utils.WarningMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CustomQuest extends Quest {

    @Getter
    private final String configurationFilename;
    private transient CustomQuestsConfigFields customQuestsConfigFields;

    public CustomQuest(Player player, CustomQuestsConfigFields customQuestsConfigFields) {
        super(player, new QuestObjectives(new QuestReward(customQuestsConfigFields, player)), customQuestsConfigFields.getQuestLevel());
        this.customQuestsConfigFields = customQuestsConfigFields;
        this.configurationFilename = customQuestsConfigFields.getFilename();
        super.questObjectives.setQuest(this);
        super.questName = customQuestsConfigFields.getQuestName();
        super.questTaker = customQuestsConfigFields.getTurnInNPC();
    }

    public static CustomQuest getQuest(String questFilename, Player player) {
        if (CustomQuestsConfig.getCustomQuests().get(questFilename) == null) return null;
        Quest quest = null;
        for (Quest iteratedQuest : PlayerData.getQuests(player.getUniqueId()))
            if (iteratedQuest instanceof CustomQuest && ((CustomQuest) iteratedQuest).getConfigurationFilename().equals(questFilename)) {
                quest = iteratedQuest;
                break;
            }
        if (quest != null)
            return (CustomQuest) quest;
        else
            return new CustomQuest(player, CustomQuestsConfig.getCustomQuests().get(questFilename));
    }

    public static Quest startQuest(String questID, Player player) {
        Quest quest = null;
        for (Quest iteratedQuest : pendingPlayerQuests.get(player.getUniqueId()))
            if (iteratedQuest.getQuestID().equals(UUID.fromString(questID))) {
                quest = iteratedQuest;
                break;
            }
        if (quest == null) player.sendMessage(ChatColorConverter.convert("&8[EliteMobs] &cInvalid quest ID!"));
        QuestAcceptEvent questAcceptEvent = new QuestAcceptEvent(player, quest);
        new EventCaller(questAcceptEvent);
        if (questAcceptEvent.isCancelled()) return null;
        return quest;
    }

    public CustomQuestsConfigFields getCustomQuestsConfigFields() {
        if (customQuestsConfigFields == null)
            this.customQuestsConfigFields = CustomQuestsConfig.getCustomQuests().get(configurationFilename);
        if (customQuestsConfigFields == null) {
            new WarningMessage("Detected that Custom Quest " + configurationFilename + " got removed even though player "
                    + Bukkit.getPlayer(getPlayerUUID()).getName() + " is still trying to complete it. This player's quest will now be wiped.");
            PlayerData.removeQuest(getPlayerUUID(), this);
            return null;
        }
        return customQuestsConfigFields;
    }

}
