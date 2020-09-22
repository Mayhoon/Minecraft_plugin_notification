package packeins;


import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        sendIp();
        getLogger().info(ChatColor.AQUA + "Enabled the best Plugin in the world!");
//        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Plugin disabled");
    }

    public static void sendIp() {
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

                EmailServer emailServer = new EmailServer();
                emailServer.sendMail("Server restart", "Neue server ip: " + serverIp);
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
}