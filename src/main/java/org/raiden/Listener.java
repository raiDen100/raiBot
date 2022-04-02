package org.raiden;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.raiden.commands.music.filters.utils.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(ReadyEvent event) {

        LOGGER.info("Bot is ready!");
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        manager.handle(event);
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        User user = event.getUser();

        if(user.isBot()){
            return;
        }
        manager.handle(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        User user = event.getAuthor();

        if(user.isBot() || event.isWebhookMessage()){
            return;
        }

        String prefix = "r-";
        String raw = event.getMessage().getContentRaw();


        if(raw.startsWith(prefix)){
            manager.handle(event);
        }

    }
}
