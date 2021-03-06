package org.raiden;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.raiden.commands.utils.CommandManager;
import org.raiden.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        if(user.isBot() || event.isWebhookMessage())
            return;

        String raw = event.getMessage().getContentRaw();
        PropertiesReader propertiesReader = new PropertiesReader();
        String prefix = propertiesReader.getPropertyValue("bot.prefix");

        if(raw.startsWith(prefix))
            manager.handle(event);
    }
}
