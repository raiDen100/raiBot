package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand implements ICommand {

    public int page = 0;
    Logger LOGGER = LoggerFactory.getLogger(QueueCommand.class);

    public Message queueMessage;

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            return;
        }

        AudioTrack currentTrack = audioPlayer.getPlayingTrack();

        List<AudioTrack> currentQueue = new ArrayList<>();
        currentQueue.addAll(musicManager.scheduler.queue);
        currentQueue.add(0, currentTrack);

        EmbedBuilder queueEmbed = new EmbedBuilder();
        String queueMessagePage = "\n";

        if(ctx.getButtonEvent() != null && !ctx.getArgs().isEmpty()){
            ButtonClickEvent buttonEvent = ctx.getButtonEvent();

            String buttonAction = ctx.getArgs().get(0);
            if(buttonAction.equals("first")){
                this.page = 0;
            }
            else if(buttonAction.equals("back") && this.page > 0){
                this.page -= 1;
            }
            else if(buttonAction.equals("next") && this.page < Math.ceil(Double.parseDouble(Integer.toString(currentQueue.size())) / 10)-1){
                this.page += 1;
            }
            else if(buttonAction.equals("last")){
                this.page = (int)Math.ceil(Double.parseDouble(Integer.toString(currentQueue.size())) / 10)-1;
            }
        }


        for (int i= (10 * page) + 1; i <= (page + 1) * 10 && i <= currentQueue.size(); i++) {
            AudioTrack queueTrack = currentQueue.get(i-1);
            String trackCock = trackTime(queueTrack.getDuration());
            String trackTitle = queueTrack.getInfo().title;

            int space = 45;
            if(i == 10)
                space -= 1;

            if(trackTitle.length() > 40) {
                trackTitle = queueTrack.getInfo().title.substring(0, 40);
            }

            space -= trackTitle.length();
            for(int y = 0; y < space; y++){
                trackTitle += " ";
            }


            queueMessagePage += i + ". " + trackTitle + trackCock + "\n";
        }

        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.secondary("r-queue first", "First"));
        buttonList.add(Button.secondary("r-queue back", "Back"));
        buttonList.add(Button.secondary("r-queue next", "Next"));
        buttonList.add(Button.secondary("r-queue last", "Last"));
        if(ctx.getButtonEvent() != null && !ctx.getArgs().isEmpty()){

            ActionRow actionRow = ActionRow.of(buttonList);

            MessageBuilder messageBuilder = new MessageBuilder();

            messageBuilder.setContent("```ml" + queueMessagePage + "\n```");
            messageBuilder.setActionRows(actionRow);

            Message msg = messageBuilder.build();


            ctx.getButtonEvent().editMessage("```ml" + queueMessagePage + "\n```").setActionRow(buttonList).queue();
            return;
        }


        if(ctx.getButtonEvent() != null && ctx.getArgs().isEmpty()){
            List<net.dv8tion.jda.api.interactions.components.Button> buttonList2 = new ArrayList<>();

            buttonList2.add(net.dv8tion.jda.api.interactions.components.Button.secondary("r-queue", "Queue"));
            buttonList2.add(net.dv8tion.jda.api.interactions.components.Button.secondary("r-skip", "Skip"));
            buttonList2.add(net.dv8tion.jda.api.interactions.components.Button.secondary("r-resume", "Play"));
            buttonList2.add(net.dv8tion.jda.api.interactions.components.Button.secondary("r-pause", "Pause"));
            buttonList2.add(net.dv8tion.jda.api.interactions.components.Button.secondary("r-loop", "Loop"));

            ctx.getButtonEvent().editMessage(ctx.getButtonEvent().getMessage()).setActionRow(buttonList2).queue();
        }

        ActionRow actionRow = ActionRow.of(buttonList);

        MessageBuilder messageBuilder = new MessageBuilder();

        messageBuilder.setContent("```ml" + queueMessagePage + "\n```");
        messageBuilder.setActionRows(actionRow);

        Message msg = messageBuilder.build();

        if(ctx.getSlashEvent() != null){
            ctx.sendEventReply(msg);
            return;
        }
        channel.sendMessage(msg).queue();

    }

    public String trackTime(long duration){
        long durationInSeconds = duration / 1000;

        long trackMinutes = durationInSeconds / 60;
        long trackSeconds = durationInSeconds % 60;
        return Long.toString(trackMinutes) + ":" + (trackSeconds < 10 ? "0" + trackSeconds : trackSeconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("q");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
