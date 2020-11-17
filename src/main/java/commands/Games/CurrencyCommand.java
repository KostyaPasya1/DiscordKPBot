package commands.Games;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;

public class CurrencyCommand extends GameCommand {

    public CurrencyCommand() {
        super.name = "currency";
        super.help = "Показывает текущий курс рубля к доллару";
    }

    @Override
    protected void execute(CommandEvent event) {

        JSONParser jsonParser = new JSONParser();
        String USDRUB = "";


        try {
            //Считываем входящий поток данных (currencyUrl) и парсим его в JSON объекты
            URL currencyUrl = new URL("http://api.currencylayer.com/live?access_key=ed7cf5b30b00f0417d8b5231559b407b&format=1");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(currencyUrl.openConnection().getInputStream()));
            JSONObject jsonObject = (JSONObject) jsonParser.parse(bufferedReader);


            //Из JSON объектов выделяем структуру = "quotes" и из этой структуры вытаскиваем ключ:значение с ключем "USDRUB"
            JSONObject structure = (JSONObject) jsonObject.get("quotes");
            USDRUB = structure.get("USDRUB").toString();
            bufferedReader.close();

            event.getMessage().delete().queue();
            event.reply("/currency для " + event.getAuthor().getAsMention());
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.cyan)
                    .setTitle("Курс доллара и евро к рублю: ")
                    .addField(":dollar: Доллар", USDRUB + " рублей", true)
                    .addField(":euro: Евро", "null " + " рублей", true);


            event.reply(embedBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
            event.reply(":no_entry: `Что-то пошло не так!`");
        }

    }
}