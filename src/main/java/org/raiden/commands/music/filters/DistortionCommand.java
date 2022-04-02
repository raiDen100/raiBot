package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class DistortionCommand implements ICommand {
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
            guildMusicManager.resetDistortion();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("Distortion " + " removed!")
                    .setColor(new Color(0, 255, 0));

            ctx.sendEventReply(eb.build());
            return;
        }

        float parsedArg = Float.parseFloat(args);


        float bassboostValue = parsedArg;
        guildMusicManager.setDistortion(bassboostValue);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Distortion " + " set!")
                .setColor(new Color(0, 255, 0));

        ctx.sendEventReply(eb.build());
    }

    @Override
    public String getName() {
        return "distortion";
    }

    @Override
    public List<String> getAliases() {
        return List.of("dd");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
