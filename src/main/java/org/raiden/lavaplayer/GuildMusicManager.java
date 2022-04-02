package org.raiden.lavaplayer;

import com.github.natanbc.lavadsp.distortion.DistortionPcmAudioFilter;
import com.github.natanbc.lavadsp.karaoke.KaraokePcmAudioFilter;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
import com.github.natanbc.lavadsp.volume.VolumePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.AudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.ResamplingPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.Equalizer;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    //private EqualizerFactory equalizer;

    private final AudioPlayerSendHandler sendHandler;
    static AudioConfiguration configuration = new AudioConfiguration();

    private float speed = 1f;
    private float bassboost = 0f;
    private float nightcore = 1.0F;
    private float pitch = 0;
    private float distortion = 0;
    private float volume = 1;

    private boolean speedChanged = false;
    private boolean bassboostChanged = false;
    private boolean nightcoreChanged = false;
    private boolean vaporwaveChanged = false;
    private boolean distortionChanged = false;
    private boolean volumeChanged = false;

    private volatile boolean shouldRebuild;
    private volatile List<AudioFilter> lastChain;

    private static final float[] BASS_BOOST = {-0.05F, 0.07F, 0.16F, 0.03F};//{-0.05F, 0.07f, 0.16f};//{-0.05F, 0.07F, 0.16F, 0.03F, -0.05F};


    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
       // this.equalizer = new EqualizerFactory();
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        this.volumeChanged = true;
        setFilters();
    }

    public void bassBoost(float multiplier) {
        this.bassboost = multiplier;
        this.bassboostChanged = true;
        setFilters();

    }

    public void setSpeed(float value) {
        this.speed = value;
        this.speedChanged = true;
        setFilters();
    }

    public void setNightcore(float value) {
        nightcoreChanged = true;
        nightcore = value;
        setFilters();
    }

    public void setVaporwave(float value){
        this.speed = value;
        vaporwaveChanged = true;
        setFilters();
    }

    public void setDistortion(float value){
        this.distortion = value;
        this.distortionChanged = true;
        setFilters();
    }


    private List<AudioFilter> buildChain(AudioTrack audioTrack, AudioDataFormat format, UniversalPcmAudioFilter downstream) {
        List<AudioFilter> filterList = new ArrayList<>();
        FloatPcmAudioFilter filter = downstream;


        if(speedChanged) {
            var timescale = new TimescalePcmAudioFilter(downstream, format.channelCount, format.sampleRate);
            timescale.setSpeed(this.speed);

            filterList.add(timescale);
            filter = timescale;

        }
        if (bassboostChanged) {
            var equalizer = new Equalizer(format.channelCount, filter);
            for (int i = 0; i < BASS_BOOST.length; i++) {
                equalizer.setGain(i, BASS_BOOST[i] * this.bassboost);
            }

            filter = equalizer;
            filterList.add(equalizer);
        }

        if (nightcoreChanged) {
            var resamplingFilter = new ResamplingPcmAudioFilter(configuration, format.channelCount, filter,
                    format.sampleRate, (int) (format.sampleRate / nightcore));

            filterList.add(resamplingFilter);
            filter = resamplingFilter;
        }

        if (vaporwaveChanged) {
            var timescale = new TimescalePcmAudioFilter(filter, format.channelCount, format.sampleRate);
            timescale.setSpeed(this.speed).setPitchSemiTones(pitch - 7.0);
            filterList.add(timescale);
            filter = timescale;
        }
        if(distortionChanged){
            var distortion = new DistortionPcmAudioFilter(filter, format.channelCount);
            distortion.setScale(this.distortion);
            filterList.add(distortion);
            filter = distortion;
        }
        if(volumeChanged){
            var volumeFilter = new VolumePcmAudioFilter(filter, format.channelCount);
            volumeFilter.setVolume(this.volume);

            filterList.add(volumeFilter);
            filter = volumeFilter;
        }

        Collections.reverse(filterList);
        return filterList;
    }

    public void resetVolume(){
        this.volume = 1;
        this.volumeChanged = false;
        setFilters();
    }

    public void resetBassboost(){
        this.bassboostChanged = false;
        this.bassboost = 0f;
        setFilters();
    }

    public void resetSpeed(){

        this.speedChanged = false;
        this.speed = 100f;
        setFilters();
    }
    public void resetVaporwave(){
        this.vaporwaveChanged = false;
        this.speed = 1;
        setFilters();
    }

    public void resetNightcore(){
        this.nightcoreChanged= false;
        this.nightcore = 1.0F;
        setFilters();
    }

    public  void resetDistortion(){
        this.distortion = 0;
        this.distortionChanged = false;
        setFilters();
    }


    private void setFilters() {

        if (hasFiltersEnabled()) {
            shouldRebuild = true;
            this.audioPlayer.setFilterFactory(null);

            try{
                Thread.sleep(100);
            }catch(InterruptedException e){

            }

            this.audioPlayer.setFilterFactory(((track, format, output) -> buildChain(track, format, output)));
        } else {
            this.audioPlayer.setFilterFactory(null);

        }
    }

    private List<AudioFilter> getFiltersOrRebuild(AudioTrack audioTrack, AudioDataFormat audioDataFormat, UniversalPcmAudioFilter downstream) {
        if (shouldRebuild) {
            lastChain = buildChain(audioTrack, audioDataFormat, downstream);
            shouldRebuild = false;
        }

        return lastChain;
    }

    public boolean hasFiltersEnabled() {
        return speedChanged || bassboostChanged ||nightcoreChanged || vaporwaveChanged || distortionChanged ||volumeChanged;
    }


    public void resetFilters(){
        this.audioPlayer.setFilterFactory(null);
        this.speedChanged = false;
        this.bassboostChanged = false;
        this.nightcoreChanged = false;
        this.vaporwaveChanged = false;
        this.pitch = 0;
        this.speed = 1F;
        this.bassboost = 0f;
        this.nightcore = 1f;
        this.distortion = 0;
        this.distortionChanged = false;
        this.volume = 1;
        this.volumeChanged = false;
    }


}

