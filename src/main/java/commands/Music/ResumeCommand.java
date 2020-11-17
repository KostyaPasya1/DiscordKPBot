package commands.Music;

import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class ResumeCommand extends MusicCommands {
    public ResumeCommand() {
        super.name = "start";
        super.help = "Снимает паузу с плеера";
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
            musicManager.scheduler.resume();
        }
    }
}
