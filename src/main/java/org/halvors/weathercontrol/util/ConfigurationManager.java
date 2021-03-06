/* 
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 * Copyright (C) 2011 adamonline45 <adamonline45@gmail.com>
 * 
 * This file is part of Lupi.
 * 
 * Lupi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Lupi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Lupi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.halvors.weathercontrol.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;
import org.halvors.weathercontrol.WeatherControl;

/**
 * Represents the global configuration and also delegates configuration
 * for individual worlds.
 * 
 * @author halvors
 */
public class ConfigurationManager {
    private final WeatherControl plugin;
    private final HashMap<String, WorldConfiguration> worlds;
    
    private static ConfigurationManager instance;
    
    /* Configuration data start */
    public String It_is_thundering;
    public String It_is_storming;
    public String It_is_clear;
    
    public String It_will_storm_for_another;
    public String It_will_now_storm_for;
    public String It_is_no_longer_storm;
    public String It_is_no_active_storm;
    public String Weather_is_disabled;
    
    public String It_will_thunder_for_another;
    public String It_will_now_thunder_for;
    public String It_is_no_longer_thunder;
    public String It_is_no_active_thunder;
    public String Thunder_is_disabled;
    
    public String It_is_now_clearing;
    public String It_is_already_clear;
    
    public String You_have_been_struck_by_lightning;
    public String Player_have_been_struck_by_lightning;
    public String Have_been_struck_by_lightning;
    public String Player_does_not_exist;
    public String Lightning_is_disabled;
    public String Position_has_been_struck_by_lightning;
    public String You_have_to_specify_a_valid_position;
    public String You_got_the_lightning_strike_wand;
    public String Wand_not_set_in_configuration_file;
    public String Maximum_wand_count_is;
    public String Wand_count_set_to;
    /* Configuration data end */
    
    public ConfigurationManager(WeatherControl plugin) {
        this.plugin = plugin;
        this.worlds = new HashMap<String, WorldConfiguration>();
        
        ConfigurationManager.instance = this;
    }
    
    public static ConfigurationManager getInstance() {
        return instance;
    }
    
    /**
     * Load the configuration.
     */
    public void load() {
        // Create the default configuration file
        createDefaultConfiguration(new File(plugin.getDataFolder(), "config.yml"), "config.yml");
            
        Configuration config = plugin.getConfiguration();
        config.load();

        // Load configurations for each world
        for (World world : Bukkit.getServer().getWorlds()) {
            get(world);
        }
        
        It_is_thundering = config.getString("messages.It_is_thundering", It_is_thundering);
        It_is_storming = config.getString("messages.It_is_storming", It_is_storming);
        It_is_clear = config.getString("messages.It_is_clear", It_is_clear);
        
        It_will_storm_for_another = config.getString("messages.It_will_storm_for_another", It_will_storm_for_another);
        It_will_now_storm_for = config.getString("messages.It_will_now_storm_for", It_will_now_storm_for);
        It_is_no_longer_storm = config.getString("messages.It_is_no_longer_storm", It_is_no_longer_storm);
        It_is_no_active_storm = config.getString("messages.It_is_no_active_storm", It_is_no_active_storm);
        Weather_is_disabled = config.getString("messages.Weather_is_disabled", Weather_is_disabled);
        
        It_will_thunder_for_another = config.getString("messages.It_will_thunder_for_another", It_will_thunder_for_another);
        It_will_now_thunder_for = config.getString("messages.It_will_now_thunder_for", It_will_now_thunder_for);
        It_is_no_longer_thunder = config.getString("messages.It_is_no_longer_thunder", It_is_no_longer_thunder);
        It_is_no_active_thunder = config.getString("messages.It_is_no_active_thunder", It_is_no_active_thunder);
        Thunder_is_disabled = config.getString("messages.Thunder_is_disabled", Thunder_is_disabled);
        
        It_is_now_clearing = config.getString("messages.It_is_now_clearing", It_is_now_clearing);
        It_is_already_clear = config.getString("messages.It_is_already_clear", It_is_already_clear);
        
        You_have_been_struck_by_lightning = config.getString("messages.You_have_been_struck_by_lightning", You_have_been_struck_by_lightning);
        Player_have_been_struck_by_lightning = config.getString("messages.Player_have_been_struck_by_lightning", Player_have_been_struck_by_lightning);
        Have_been_struck_by_lightning = config.getString("messages.Have_been_struck_by_lightning", Have_been_struck_by_lightning);
        Player_does_not_exist = config.getString("messages.Player_does_not_exist", Player_does_not_exist);
        Lightning_is_disabled = config.getString("messages.Lightning_is_disabled", Lightning_is_disabled);
        Position_has_been_struck_by_lightning = config.getString("messages.Position_has_been_struck_by_lightning", Position_has_been_struck_by_lightning);
        You_have_to_specify_a_valid_position = config.getString("messages.You_have_to_specify_a_valid_position", You_have_to_specify_a_valid_position);
        You_got_the_lightning_strike_wand = config.getString("messages.You_got_the_lightning_strike_wand", You_got_the_lightning_strike_wand);
        Wand_not_set_in_configuration_file = config.getString("messages.Wand_not_set_in_configuration_file", Wand_not_set_in_configuration_file);
        Maximum_wand_count_is = config.getString("messages.Maximum_wand_count_is", Maximum_wand_count_is);
        Wand_count_set_to = config.getString("messages.Wand_count_set_to", Wand_count_set_to);
        
        config.save();
    }

    /**
     * Unload the configuration.
     */
    public void unload() {
        worlds.clear();
    }
    
    /**
     * Reload the configuration.
     */
    public void reload() {
        unload();
        load();
    }

    /**
     * Get the configuration for a world.
     *
     * @param world
     * @return
     */
    public WorldConfiguration get(World world) {
        String worldName = world.getName();
        WorldConfiguration config = worlds.get(worldName);
            
        if (config == null) {
            config = new WorldConfiguration(plugin, worldName);
            worlds.put(worldName, config);
        }
        
        return config;
    }
    
    /**
    * Create a default configuration file from the .jar.
    *
    * @param actual
    * @param defaultName
    */
    public void createDefaultConfiguration(File actual, String defaultName) {
        // Make parent directories
        File parent = actual.getParentFile();
        
        if (!parent.exists()) {
            parent.mkdirs();
        }
        
        if (actual.exists()) {
            return;
        }
        
        InputStream input = ConfigurationManager.class.getResourceAsStream("/" + defaultName);
        
        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length = 0;
                
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                
                plugin.log(Level.INFO, "Default configuration file written: " + actual.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    
                }
            }
        }
    }
}
