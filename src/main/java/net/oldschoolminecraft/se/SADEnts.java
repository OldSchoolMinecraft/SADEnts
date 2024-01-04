package net.oldschoolminecraft.se;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class SADEnts extends JavaPlugin
{
    public static SADEnts instance;

    public SADConfig config;
    private int task;

    public void onEnable()
    {
        instance = this;

        config = new SADConfig(this);
        getServer().getPluginManager().registerEvents(new ChunkListener(), this);

        task = getServer().getScheduler().scheduleAsyncRepeatingTask(this, () ->
        {
            System.out.println("[SADEnts] Beginning automated cleanup...");
            for (World world : getServer().getWorlds())
                cleanup(world.getEntities().toArray(new Entity[0]), true);
            System.out.println("[SADEnts] Cleanup complete");
        }, 0L, config.getConfigLong("autoCleanupIntervalTicks"));

        System.out.println("SADEnts enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (label.equalsIgnoreCase("sereload") && (sender.hasPermission("sadents.reload") || sender.isOp()))
        {
            config.reload();
            sender.sendMessage(ChatColor.GREEN + "SADEnts configuration reloaded");
            return true;
        }

        return false;
    }

    public void cleanup(Entity[] ents, boolean itemsOnly)
    {
//        Entity[] ents = event.getChunk().getEntities();
        int removalCount = 0;
        for (Entity ent : ents)
        {
            if (ent instanceof Item)
            {
                ItemStack stack = ((Item)ent).getItemStack();
                Material itemType = stack.getType();
                List<Object> itemsList = SADEnts.instance.config.getList("items");
                if (itemsList != null && itemsList.contains(itemType.ordinal()))
                {
                    if (SADEnts.instance.isDebugEnabled()) System.out.println("[SADEnts Debug] Deleting " + "x" + stack.getAmount() + " item(s): " + itemType.name());
                    ent.remove();
                    removalCount += stack.getAmount();
                }
            }

            if (ent instanceof LivingEntity && !itemsOnly)
            {
                String creatureType = ent.getClass().getSimpleName();
                List<Object> creaturesList = SADEnts.instance.config.getList("creatures");
                if (creaturesList != null && creaturesList.contains(creatureType))
                {
                    if (SADEnts.instance.isDebugEnabled()) System.out.println("[SADEnts Debug] Deleting creature: " + creatureType);
                    ent.remove();
                    removalCount++;
                }
            }
        }

        int removalCountLogThreshold = SADEnts.instance.config.getConfigInteger("removalCountLogThreshold");
        if (removalCount >= removalCountLogThreshold) System.out.println("[SADEnts] Removed " + removalCount + " items and/or creatures.");
    }

    public boolean isDebugEnabled()
    {
        return new File(getDataFolder(), "debug.dat").exists();
    }

    public void onDisable()
    {
        System.out.println("SADEnts disabled");
    }
}
