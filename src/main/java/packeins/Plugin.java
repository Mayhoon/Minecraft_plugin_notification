package packeins;


import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Plugin extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        createCustomConfig();

        sendIp();
        getLogger().info(ChatColor.AQUA + "Enabled the best Plugin in the world!");
//        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Plugin disabled");
    }

    public void sendIp() {
        URL url = null;
        try {
            url = new URL("http://localhost:4040/api/tunnels");
            JSONObject json = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            {
                for (String line; (line = reader.readLine()) != null; ) {
                    json = new JSONObject(line);
                }
                JSONArray jsonArray = json.getJSONArray("tunnels");
                String serverIp = jsonArray.getJSONObject(0).getString("public_url");

                List<String> emails = customConfig.getStringList("emails");
                EmailServer emailServer = new EmailServer();
                for (String email : emails) {
                    emailServer.sendMail(email, "Server restart", "Server ip: " + serverIp);
                    System.out.println("Send email to " + email);
                }
                System.out.println(serverIp);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}