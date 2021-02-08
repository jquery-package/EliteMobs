package com.magmaguy.elitemobs.config.configurationimporter;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.utils.DeveloperMessage;
import com.magmaguy.elitemobs.utils.UnzipFile;
import com.magmaguy.elitemobs.utils.WarningMessage;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigurationImporter {

    public static void initializeConfigs() {
        Path configurationsPath = Paths.get(MetadataHandler.PLUGIN.getDataFolder().getPath());
        if (!Files.isDirectory(Paths.get(configurationsPath.normalize().toString() + "/imports"))) {
            try {
                Files.createDirectory(Paths.get(configurationsPath.normalize().toString() + "/imports"));
            } catch (Exception exception) {
                new WarningMessage("Failed to create import directory! Tell the dev!");
                exception.printStackTrace();
            }
            return;
        }

        if ((new File(MetadataHandler.PLUGIN.getDataFolder().getPath() + "/imports")).length() == 0)
            return;

        for (File zippedFile : (new File(MetadataHandler.PLUGIN.getDataFolder().getPath() + "/imports")).listFiles()) {
            File unzippedFile;
            try {
                unzippedFile = UnzipFile.run(zippedFile.getName());
            } catch (Exception e) {
                new WarningMessage("Failed to unzip config file " + zippedFile.getName() + " ! Tell the dev!");
                e.printStackTrace();
                continue;
            }
            try {
                for (File file : unzippedFile.listFiles()) {
                    switch (file.getName()) {
                        case "custombosses":
                            moveFiles(file, Paths.get(configurationsPath.normalize().toString() + "/custombosses"));
                            break;
                        case "customitems":
                            moveFiles(file, Paths.get(configurationsPath.normalize().toString() + "/customitems"));
                            break;
                        case "customtreasurechests":
                            moveFiles(file, Paths.get(configurationsPath.normalize().toString() + "/customtreasurechests"));
                            break;
                        case "dungeonpackages":
                            moveFiles(file, Paths.get(configurationsPath.normalize().toString() + "/dungeonpackages"));
                            break;
                        case "worldcontainer":
                            moveWorlds(file);
                            break;
                        case "schematics":
                            if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                                moveFiles(file,  Paths.get(file.getParentFile().getParentFile().getParentFile().getParentFile().toString() + "/WorldEdit/schematics"));
                            } else if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
                                moveFiles(file, Paths.get(file.getParentFile().getParentFile().getParentFile().getParentFile().toString() + "/FastAsyncWorldEdit/schematics"));
                            } else
                                new WarningMessage("You need WorldGuard or FastAsyncWorldEdit to install schematic-based minidungeons!");
                            break;
                        default:
                            new WarningMessage("Directory " + file.getName() + " for zipped file " + zippedFile.getName() + " was not valid!");
                    }
                    deleteDirectory(file);
                }
            } catch (Exception e) {
                new WarningMessage("Failed to move files from " + zippedFile.getName() + " ! Tell the dev!");
                e.printStackTrace();
                continue;
            }
            try {
                unzippedFile.delete();
                zippedFile.delete();
            } catch (Exception ex) {
                new WarningMessage("Failed to delete zipped file " + zippedFile.getName() + "! Tell the dev!");
                ex.printStackTrace();
            }
        }

    }

    private static void deleteDirectory(File file) {
        for (File iteratedFile : file.listFiles())
            if (iteratedFile != null)
                deleteDirectory(iteratedFile);
        if (file != null)
            file.delete();
    }

    private static void moveWorlds(File worldcontainerFile){
        for (File file : worldcontainerFile.listFiles())
            try {
            Files.move(file.toPath(), Paths.get(Bukkit.getWorldContainer().getAbsolutePath() + "/" + file.getName()));
            } catch (Exception exception) {
                new WarningMessage("Failed to move worlds for " + file.getName() + "! Tell the dev!");
                exception.printStackTrace();
            }
    }

    private static void moveFiles(File unzippedDirectory, Path targetPath) {
        for (File file : unzippedDirectory.listFiles())
            try {
                Files.move(file.toPath(), Paths.get(targetPath.normalize().toString() + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception exception) {
                new WarningMessage("Failed to move directories for " + file.getName() + "! Tell the dev!");
                exception.printStackTrace();
            }
    }

}
