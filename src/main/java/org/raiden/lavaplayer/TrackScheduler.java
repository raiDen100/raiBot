package org.raiden.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.commands.utils.ButtonCreator;
import org.raiden.commands.utils.EmbedCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);

    public final AudioPlayer player;
    public BlockingQueue<AudioTrack> queue;
    private TextChannel textChannel;
    public Message lastMessage;
    public boolean repeating = false;
    public boolean repeatingQueue = false;
    public int counter = 1;
    private int retries = 3;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track){
        if(!this.player.startTrack(track, true)){
            this.queue.offer(track);
        }
    }

    public void shuffle(){
        List<AudioTrack> audioTrackList = new ArrayList<>();
        audioTrackList.addAll(queue);

        Collections.shuffle(audioTrackList);

        queue.clear();

        for (AudioTrack audioTrack : audioTrackList) {
            queue.offer(audioTrack);
        }
    }

    public void nextTrack(){
        AudioTrack audioTrack = this.queue.poll();
        this.counter = 1;
        this.player.startTrack(audioTrack, false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack audioTrack) {

        User author = (User) audioTrack.getUserData();

        MessageEmbed messageEmbed = EmbedCreator.nowPlayingEmbed(audioTrack, author.getIdLong());
        List<Button> buttonList = ButtonCreator.createNowPlayingButtons(this);

       textChannel.sendMessageEmbeds(messageEmbed).setActionRow(buttonList).queue(message -> this.lastMessage = message);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(this.lastMessage != null)
            this.lastMessage.delete().queue();
        if(endReason.mayStartNext){
            retries = 3;
            if(this.repeating){
                this.player.startTrack(track.makeClone(), false);
                this.counter++;
                return;
            }

            if(this.repeatingQueue){
                this.queue.offer(track.makeClone());
            }

            nextTrack();
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        this.repeating = false;
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        //this.repeating = true;
        LOGGER.error(retries + " retrying to play: " + track.getInfo().title);
        if(this.lastMessage != null)
            this.lastMessage.delete().queue();

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }

        retries -= 1;
        if(retries == 0){
            retries = 3;

            MessageEmbed messageEmbed = EmbedCreator.playbackFailedEmbed(track);
            textChannel.sendMessageEmbeds(messageEmbed).queue();
            this.repeating = false;
        }

    }

    public void setTextChannel(TextChannel c){
        this.textChannel = c;
    }
}
