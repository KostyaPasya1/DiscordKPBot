package LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;


//Связывает 2 класса (AudioPlayerSendHandler и TrackScheduler)
//Создает нового AudioPlayer затем присваивает TrackScheduler и SendHandler, которые мы можем использовать в нашем PlayerManager
public class GuildMusicManager {

    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final Guild guild;

    public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
        this.guild = guild;
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, guild);
        player.addListener(scheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}


