package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class BassboostCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide a number");
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        String args = String.join("", ctx.getArgs());
        if(args.contains("reset")){
            guildMusicManager.resetBassboost();

            String description = "Bassboost removed!";
            ctx.sendEventReply(EmbedCreator.actionSuccessfulEmbed(description));
            return;
        }
        float bassboostValue = Float.parseFloat(args);

        if(bassboostValue <= 0){
            guildMusicManager.resetBassboost();

            String description = "Bassboost removed!";
            ctx.sendEventReply(EmbedCreator.actionSuccessfulEmbed(description));
            return;
        }

        guildMusicManager.bassBoost(bassboostValue);

        String description = "Bassboost " + Math.round(bassboostValue) + " will be applied";
        MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed(description);

        ctx.sendEventReply(messageEmbed);
    }

    @Override
    public String getName() {
        return "bassboost";
    }

    @Override
    public List<String> getAliases() {
        return List.of("bb");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
