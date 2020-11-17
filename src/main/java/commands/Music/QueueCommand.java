package commands.Music;

import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class QueueCommand extends MusicCommands {
    public QueueCommand() {
        super.name = "queue";
        super.help = "Показывает первые 3 песни в очереди (со ссылками на них)";
    }


    @Override
    protected void execute(CommandEvent event) {

        String bangBang = ":bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang::bangbang:";
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;


        if (queue.isEmpty()) {
            event.reply(":exclamation: В очереди нету песен!");
            return;
        }

        final int trackCount = Math.min(queue.size(), 3);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final StringBuilder messageAction = new StringBuilder("**Песни в очереди**\n");

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append("#")
                    .append((i + 1))
                    .append(" ")
                    .append(info.uri)
                    .append("\n");


        }
        if (trackList.size() > trackCount){
            messageAction.append("И остальные `")
                    .append((trackList.size() - trackCount))
                    .append("` ");
        }

        event.getChannel().sendMessage(bangBang + "\n" + " :bangbang:**__Данное сообщение будет удалено через 30 секунд__**:bangbang:\n" + bangBang + "\n"
                + messageAction.toString()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));







    }

}
