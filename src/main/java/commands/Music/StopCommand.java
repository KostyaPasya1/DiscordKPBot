package commands.Music;

import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class StopCommand extends MusicCommands{

    public StopCommand() {
        super.name = "stop";
        super.help = "Ставит плеер на паузу";
    }

    @Override
    protected void execute(CommandEvent event) {

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        assert memberVoiceState != null;
        if(!memberVoiceState.inVoiceChannel()){
            event.getChannel().sendMessage(":x: " + event.getAuthor().getAsMention() + ", ты должен быть в голосовом канале, чтобы пользоваться этой командой!").queue();
        } else {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            musicManager.scheduler.pause();
        }

    }
}
