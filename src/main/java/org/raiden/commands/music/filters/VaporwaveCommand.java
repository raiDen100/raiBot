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

public class VaporwaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            MessageEmbed messageEmbed = EmbedCreator.actionFailedEmbed("You need to provide a number");
            ctx.sendEventReply(messageEmbed);
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        String args = String.join("", ctx.getArgs());

        if(args.contains("reset")){
            guildMusicManager.resetVaporwave();

            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Vaporwave removed!");
            ctx.sendEventReply(messageEmbed);
            return;
        }

        float parsedArg = Float.parseFloat(args);

        if(parsedArg >= 100){
            parsedArg = 100;
        }
        else if(parsedArg < 10){
            parsedArg = 10;
        }

        float bassboostValue = parsedArg / 100;
        guildMusicManager.setVaporwave(bassboostValue);

        MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Vaporwave set!");
        ctx.sendEventReply(messageEmbed);
    }

    @Override
    public String getName() {
        return "vaporwave";
    }

    @Override
    public List<String> getAliases() {
        return List.of("vw");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
