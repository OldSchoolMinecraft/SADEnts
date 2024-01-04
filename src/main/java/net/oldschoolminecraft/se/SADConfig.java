package net.oldschoolminecraft.se;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

import java.io.File;

public class SADConfig extends Configuration
{
    public SADConfig(Plugin plugin)
    {
        super(new File(plugin.getDataFolder(), "config.yml"));
        this.reload();
    }

    private void write()
    {
        //Setting

        generateConfigOption("enableAutoCleanup", true);
        generateConfigOption("autoCleanupIntervalTicks", 2400L);
        generateConfigOption("removalCountLogThreshold", 15);
        generateList("items", Material.CACTUS.ordinal());
        generateList("creatures", "CraftZombie", "CraftSpider", "CraftSkeleton");
    }

    private void generateList(String key, Object... values)
    {
        generateConfigOption(key, values);
    }

    private void generateConfigOption(String key, Object defaultValue)
    {
        if (this.getProperty(key) == null)
        {
            this.setProperty(key, defaultValue);
        }
        final Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    //Getters Start
    public Object getConfigOption(String key)
    {
        return this.getProperty(key);
    }

    public String getConfigString(String key)
    {
        return String.valueOf(getConfigOption(key));
    }

    public Integer getConfigInteger(String key)
    {
        return Integer.valueOf(getConfigString(key));
    }

    public Long getConfigLong(String key)
    {
        return Long.valueOf(getConfigString(key));
    }

    public Double getConfigDouble(String key)
    {
        return Double.valueOf(getConfigString(key));
    }

    public Boolean getConfigBoolean(String key)
    {
        return Boolean.valueOf(getConfigString(key));
    }


    //Getters End


    public void reload()
    {
        this.load();
        this.write();
        this.save();
    }
}
