package LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**Events are handled by event handlers added to an __AudioPlayer__ instance.
 The simplest way for creating the handler is to extend the __AudioEventAdapter class__.**/

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public final Guild guild;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = guild;
    }

    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        AudioTrack track = queue.poll();
        if(track == null) {
            stop();
        } else {
            player.startTrack(track, false);
        }
    }

    public void stop() {
        queue.clear();
        player.stopTrack();
        guild.getAudioManager().closeAudioConnection();
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nextTrack();
        }
    }

}
