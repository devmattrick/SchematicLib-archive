package cx.matthew.schematiclib.test;

import cx.matthew.schematiclib.adapter.Adapters;
import cx.matthew.schematiclib.nbt.NamedTag;
import cx.matthew.schematiclib.nbt.io.NBTInputStream;
import cx.matthew.schematiclib.schematic.Schematic;
import cx.matthew.schematiclib.schematic.io.SchematicReader;
import cx.matthew.schematiclib.schematic.io.SpongeV2SchematicReader;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class TestPlugin extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("Usage: /test <schematic name>");
                return true;
            }

            Location location = player.getLocation();
            File file = Paths.get(getDataFolder().getAbsolutePath(), args[0]).toFile();

            try {
                SchematicReader reader = new SpongeV2SchematicReader(file);
                Schematic schematic = reader.read();

                Adapters.getSchematicAdapter().paste(location, schematic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}
