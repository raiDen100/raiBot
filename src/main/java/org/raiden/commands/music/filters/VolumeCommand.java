package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            MessageEmbed messageEmbed = EmbedCreator.actionFailedEmbed("You need to provide a number");
            ctx.sendEventReply(messageEmbed);
            return;
        }
        String args = String.join("", ctx.getArgs());
        if(args.contains("reset")){
            PlayerManager.getInstance().getMusicManager(ctx.getGuild()).resetVolume();

            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Volume removed!");
            ctx.sendEventReply(messageEmbed);
            return;
        }

        float volume = Float.parseFloat(String.join(" ", ctx.getArgs()));
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).setVolume(volume/100);

        MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Volume " + volume + "% set!");
        ctx.sendEventReply(messageEmbed);
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public List<String> getAliases() {
        return List.of("er", "earrape");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
