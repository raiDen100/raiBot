package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.commands.utils.ButtonCreator;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class LoopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

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

        final boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;
        musicManager.scheduler.repeatingQueue = false;

        if(ctx.getButtonEvent() != null){
            List<Button> buttonList = ButtonCreator.createNowPlayingButtons(musicManager.scheduler);

            ctx.getButtonEvent().editMessage(ctx.getButtonEvent().getMessage()).setActionRow(buttonList).queue();
            return;
        }

        MessageEmbed messageEmbed;
        if(newRepeating)
            messageEmbed = EmbedCreator.actionSuccessfulEmbed("Looping current song!");
        else
            messageEmbed = EmbedCreator.actionSuccessfulEmbed("Loop disabled!");

        ctx.sendEventReply(messageEmbed);
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public List<String> getAliases() {
        return List.of("repeat");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
