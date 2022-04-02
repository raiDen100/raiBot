package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
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

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("Bassboost " + " removed!")
                    .setColor(new Color(0, 255, 0));

            ctx.sendEventReply(eb.build());
            return;
        }
        float parsedArg = Float.parseFloat(args);


        if(parsedArg < 0){
            parsedArg = 0;
        }

        float bassboostValue = parsedArg / 100;
        guildMusicManager.bassBoost(bassboostValue);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Bassboost " + Float.toString(parsedArg) + "/100 will be applied")
        .setColor(new Color(0, 255, 0));

        ctx.sendEventReply(eb.build());
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
