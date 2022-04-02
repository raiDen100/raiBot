package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class NightcoreCommand implements ICommand {
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
            guildMusicManager.resetNightcore();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("Nightcore " + " removed!")
                    .setColor(new Color(0, 255, 0));

            ctx.sendEventReply(eb.build());
            return;
        }

        float parsedArg = Float.parseFloat(args);

        if(parsedArg > 300){
            parsedArg = 300;
        }
        else if(parsedArg < 0.1){
            parsedArg = 10;
        }

        float bassboostValue = parsedArg / 100;
        guildMusicManager.setNightcore(bassboostValue);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Nightcore " + " set!")
                .setColor(new Color(0, 255, 0));

        ctx.sendEventReply(eb.build());
    }

    @Override
    public String getName() {
        return "nightcore";
    }

    @Override
    public List<String> getAliases() {
        return List.of("nc");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
