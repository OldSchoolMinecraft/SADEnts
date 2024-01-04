package net.oldschoolminecraft.se;

import net.minecraft.server.EntityTypes;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Arrays;
import java.util.List;

public class ChunkListener implements Listener
{
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event)
    {
        if (event.isNewChunk()) return;

        SADEnts.instance.cleanup(event.getChunk().getEntities(), false);
    }
}
