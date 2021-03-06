package org.raiden.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class ResetCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).resetFilters();

        ctx.sendEventReply(EmbedCreator.actionSuccessfulEmbed("Filters will be reset"));
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
