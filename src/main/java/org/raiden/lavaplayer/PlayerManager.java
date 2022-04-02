package org.raiden.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeHttpContextFilter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.http.HttpContextFilter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.apache.hc.core5.reactor.Command;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager extends DefaultAudioPlayerManager{

    private final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private final Map<Long, AudioTrack> tempQueue = new HashMap<>();
    private final List<Integer> notFoundItemIds = new ArrayList<>();

    private int currentOrderNumber = 0;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();



        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

        YoutubeHttpContextFilter.setPAPISID("N_p4QrcnbB1MNRTO/AUYQ0Re_8oeuu_IB9");
        YoutubeHttpContextFilter.setPSID("IQgnE5HCW6EKTjsKOq7lN1TmFXTYD__ZHHG_9IIS5h-6jheCPfgtD45PkReMlg1QDnZmVw.");
        //audioPlayerManager.source(YoutubeAudioSourceManager.class).getMainHttpConfiguration().setHttpContextFilter(httpContextFilter);
    }


    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(CommandContext ctx, String trackUrl, User author){
       final GuildMusicManager musicManager =  this.getMusicManager(ctx.getChannel().getGuild());
       this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
           @Override
           public void trackLoaded(AudioTrack audioTrack) {

               EmbedBuilder eb = new EmbedBuilder();

               audioTrack.setUserData(author);

               if(musicManager.audioPlayer.getPlayingTrack() != null || ctx.getSlashEvent() != null){
                   eb.setColor(Color.magenta)
                           .setTitle("Queued!")
                           .setDescription("[" + audioTrack.getInfo().title + "](" + audioTrack.getInfo().uri + ") [<@" + author.getId() + ">]");
                   ctx.sendEventReply(eb.build());
               }

               musicManager.scheduler.queue(audioTrack);

           }

           @Override
           public void playlistLoaded(AudioPlaylist audioPlaylist) {
               List<AudioTrack> tracks = audioPlaylist.getTracks();

               if(audioPlaylist.isSearchResult()){
                    AudioTrack audioTrack = tracks.get(0);


                   EmbedBuilder eb = new EmbedBuilder();

                   if(musicManager.audioPlayer.getPlayingTrack() != null  || ctx.getSlashEvent() != null){

                       eb.setColor(Color.magenta)
                               .setTitle("Queued!")
                               .setDescription("[" + audioTrack.getInfo().title + "](" + audioTrack.getInfo().uri + ") [<@" + author.getId() + ">]");
                       ctx.sendEventReply(eb.build());
                   }

                   audioTrack.setUserData(author);

                   musicManager.scheduler.queue(audioTrack);

                   return;
               }

               EmbedBuilder eb = new EmbedBuilder();
               eb.setColor(Color.magenta)
                       .setDescription("Queued **" + Integer.toString(tracks.size()) + "** tracks");

               ctx.sendEventReply(eb.build());

               for(final AudioTrack track : tracks){
                   track.setUserData(author);
                   musicManager.scheduler.queue(track);
               }
           }

           @Override
           public void noMatches() {

           }

           @Override
           public void loadFailed(FriendlyException e) {

           }
       });
    }


    private void addItemsToQueue(CommandContext ctx){
        if (notFoundItemIds.contains(currentOrderNumber))
            currentOrderNumber++;
        while (tempQueue.containsKey((long)currentOrderNumber)){
            final GuildMusicManager musicManager =  this.getMusicManager(ctx.getChannel().getGuild());
            musicManager.scheduler.queue(tempQueue.get((long) currentOrderNumber));
            tempQueue.remove((long) currentOrderNumber);
            currentOrderNumber++;
        }
    }

    public void loadAndPlayFromSpotify(CommandContext ctx, String trackUrl, User author, int orderNumber){
        final GuildMusicManager musicManager =  this.getMusicManager(ctx.getChannel().getGuild());

        audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {

            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if(orderNumber == 0){
                    currentOrderNumber = 0;
                    notFoundItemIds.clear();
                }
                List<AudioTrack> tracks = audioPlaylist.getTracks();


                AudioTrack audioTrack = tracks.get(0);

                audioTrack.setUserData(author);
                System.out.println(orderNumber);

                tempQueue.put((long) orderNumber, audioTrack);
                addItemsToQueue(ctx);
            }

            @Override
            public void noMatches() {
                notFoundItemIds.add(orderNumber);
            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
