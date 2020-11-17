package commands.Games;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class GameCommand extends Command {

    public GameCommand() {
        super.category = new Category(":video_game:  Games and fun");
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
