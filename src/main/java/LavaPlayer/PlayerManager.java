package LavaPlayer;


import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;


import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Singleton
public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        //__computeIfAbsent__ принимает функцию сопоставления, которая вызывается для получения значения, если ключ отсутствует.
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager,guild);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }


    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                channel.sendMessage("Добавлен в очередь: ")
                        .append(track.getInfo().uri)
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                channel.sendMessage("Добавлено в очередь: `")
                        .append(String.valueOf(tracks.size()))
                        .append("` песен, название плейлиста: `")
                        .append(playlist.getName())
                        .append('`')
                        .queue();

                for (final AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Ничего не найдено " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Не могу запустить: " + exception.getMessage()).queue();
            }
        });
    }



    //Метод getInstance() создает только один экземпляр класса.
    public static PlayerManager getInstance() {
        synchronized (Singleton.class) {
            if (INSTANCE == null) {
                INSTANCE = new PlayerManager();
            }
        }
        return INSTANCE;
    }

}
