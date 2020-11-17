package commands.Games;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MemeCommand extends GameCommand {

    public MemeCommand() {
        super.name = "meme";
        super.help = "Выдает рандомный мем (in English)";
    }

    @Override
    protected void execute(CommandEvent event) {
        JSONParser parser = new JSONParser();
        String postLink = "";
        String title = "";
        String url = "";



        try {
            URL memeUrl = new URL("https://meme-api.herokuapp.com/gimme");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(memeUrl.openConnection().getInputStream()));

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {

                JSONArray jsonArray = new JSONArray();
                jsonArray.add(parser.parse(lines));


                for (Object o: jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;

                    postLink = (String) jsonObject.get("postLink");
                    title = (String) jsonObject.get("title");
                    url = (String) jsonObject.get("url");
                }
            }
            bufferedReader.close();

            event.getMessage().delete().queue();


            event.reply("/meme для " + event.getAuthor().getAsMention() );
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.cyan)
                    .setTitle("Источник: " + title, postLink)
                    .setImage(url);

            event.reply(embedBuilder.build());




        } catch (Exception e) {
            e.printStackTrace();
            event.reply(":no_entry: `Что-то пошло не так!`");
        }


    }
}
