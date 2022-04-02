package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.raiden.commands.music.filters.utils.ButtonCreator;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final AudioManager audioManager = ctx.getGuild().getAudioManager();

        if(!audioManager.isConnected()){
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()){
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        audioPlayer.setPaused(true);

        if(ctx.getButtonEvent() != null){
            List<Button> buttonList = ButtonCreator.createNowPlayingButtons(musicManager.scheduler);

            ctx.getButtonEvent().editMessage(ctx.getButtonEvent().getMessage()).setActionRow(buttonList).queue();
            return;
        }

        ctx.addEventReaction("⏸");
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public List<String> getAliases() {
        return List.of("halt");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
