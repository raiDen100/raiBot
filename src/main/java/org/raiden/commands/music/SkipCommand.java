package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.commands.utils.ButtonCreator;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()){
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            return;
        }


        int counter = musicManager.scheduler.counter;

        if(counter >= 15){
            String description = "Played this song for " + counter + " times";

            ctx.sendEventReply(EmbedCreator.actionSuccessfulEmbed(description));
        }


        musicManager.scheduler.nextTrack();

        if(ctx.getButtonEvent() != null){
            List<Button> buttonList = ButtonCreator.createNowPlayingButtons(musicManager.scheduler);

            ctx.getButtonEvent().editMessage(ctx.getButtonEvent().getMessage()).setActionRow(buttonList).queue();
            return;
        }

        ctx.addEventReaction("⏭");
    }

    String getNumberSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    @Override
    public String getName() {
        return "skip";
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
