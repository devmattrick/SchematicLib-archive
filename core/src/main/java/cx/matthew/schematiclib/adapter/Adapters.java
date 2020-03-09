package cx.matthew.schematiclib.adapter;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Adapters {

    public static ISchematicAdapter getSchematicAdapter() {
        try {
            String packageName = ISchematicAdapter.class.getPackage().getName();
            String internalsName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            return (ISchematicAdapter) Class.forName(packageName + "." + internalsName + ".SchematicAdapter").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not find a valid implementation for this server version.");
        }

        // TODO Fallback to sensible default
        return null;
    }

}
