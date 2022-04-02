package org.raiden;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.raiden.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);


    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        System.setProperty("active.profile", "development");
        if (args.length > 0){
            if (args[0].equals("production")) {
                System.setProperty("active.profile", "production");
            }
        }

        LOGGER.info("Running on " + System.getProperty("active.profile") + " config");

        PropertiesReader propertiesReader = new PropertiesReader();
        String token = propertiesReader.getPropertyValue("discord.token");
        //String activityStatus = propertiesReader.getPropertyValue("bot.activity");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.listening("🎵"));
        builder.addEventListeners(new Listener());
        builder.build();
    }
}
