package LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import java.nio.Buffer;
import java.nio.ByteBuffer;


/**To use LavaPlayer with JDA 4, you would need an instance of AudioSendHandler**/
public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private  ByteBuffer buffer;
    private  MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        Buffer buf = ((Buffer) this.buffer).flip();
        return (ByteBuffer) buf;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
