package commands.Games;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LolCommand extends GameCommand {
    public LolCommand() {
        super.name = "lolinfo";
        super.help = "Показывает основную информацию о призываетеле";
        super.arguments = "[Summoner Name] [сервер (eun или euw, или ru)]";
    }



    final JSONParser jsonParser = new JSONParser();
    final DecimalFormat decimalFormat = new DecimalFormat("###.##");


    //24 hours
    final String api_key = "RGAPI-7051e558-9a9b-4657-b4ea-d406a8b8274b";

    String encryptedSummonerId = "";
    long summonerLevel = 1;



    @Override
    protected void execute(CommandEvent event) {
        //Имя пользователя, который ввел пользователь
        String[] fullUserMessage = event.getMessage().getContentRaw().split(" ");
        //Для JSON объектов
        List<String> queueType = new ArrayList<>();
        List<String> tier = new ArrayList<>();
        List<String> rank = new ArrayList<>();
        List<Long> wins = new ArrayList<>();
        List<Long> losses = new ArrayList<>();
        List <Double> winRate = new ArrayList<>();



        try {

            //SUMMONER-V4 (получает JSON, парсим его и получаем  encryptedSummonerId (нужен для второго GET запроса) и summonerLevel)
            URL SUMMONER_V4_URL = new URL("https://"
                    + getRegion(fullUserMessage)
                    +".api.riotgames.com/lol/summoner/v4/summoners/by-name/"
                    + getEncryptedSummonerName(fullUserMessage)
                    + "?api_key="
                    + api_key);

            //System.out.println("SUMMONER_V4_URL = " + SUMMONER_V4_URL);


            BufferedReader bufferedReaderForSummoner = new BufferedReader(new InputStreamReader(SUMMONER_V4_URL.openConnection().getInputStream()));
            JSONObject summonerV4JsonObject = (JSONObject) jsonParser.parse(bufferedReaderForSummoner);


            encryptedSummonerId = (String) summonerV4JsonObject.get("id");
            summonerLevel = (Long) summonerV4JsonObject.get("summonerLevel");
            bufferedReaderForSummoner.close();



            //LEAGUE-V4 (в качестве параметра GET запроса вставляем encryptedSummonerId и api_key)
            URL LEAGUE_V4_URL = new URL("https://"
                    + getRegion(fullUserMessage)
                    + ".api.riotgames.com/lol/league/v4/entries/by-summoner/"
                    + encryptedSummonerId
                    + "?api_key="
                    + api_key);

            //System.out.println("LEAGUE_V4_URL = " + LEAGUE_V4_URL);




            //так как JSON ответ на LEAGUE_V4_URL является одним массивом - парсим весь JSON в JSONArray
            BufferedReader bufferedReaderForLeague = new BufferedReader(new InputStreamReader(LEAGUE_V4_URL.openConnection().getInputStream()));
            JSONArray leagueV4JsonArray = (JSONArray) jsonParser.parse(bufferedReaderForLeague);


            for (Object o: leagueV4JsonArray) {
                JSONObject jsonObject = (JSONObject) o;

                queueType.add(jsonObject.get("queueType").toString());
                tier.add(jsonObject.get("tier").toString());
                rank.add(jsonObject.get("rank").toString());

                wins.add((Long) jsonObject.get("wins"));
                losses.add((Long) jsonObject.get("losses"));

            }
            bufferedReaderForLeague.close();


            //Отрисовка и заполнение EmbedBuilder
            if (queueType.size() == 1) {
                //Если пользователь не прошел калибровку в какой либо соревновательной очереди, отразяться свединия лишь по одной очереди
                winRate.add((wins.get(0) * 100.0) / (wins.get(0)+losses.get(0)));

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Информация по призывателю: `" + event.getArgs() + " (" + summonerLevel + " lvl)`");


                //Раздел "body"
                embedBuilder
                        .addField("Вид очереди", queueType.get(0), true);

                embedBuilder
                        .addField("Звание", tier.get(0)+ " " + rank.get(0), true);

                embedBuilder
                        .addField("Победы/Поражения (Win rate)", wins.get(0) + "/" + losses.get(0) + " (" + decimalFormat.format(winRate.get(0)) + " %)", true);

                event.reply(embedBuilder.build());



            } else if (queueType.size() == 0) {
                event.reply(":no_entry: Чтобы получить информацию по призываетелью, он должен пройти *все калибровочные игры* хотя бы в одной из соревновательных очередей!");

            } else {
                //Пользователь прошел все калибровочные игры во всех соревновательных очередях
                winRate.add((wins.get(0) * 100.0) / (wins.get(0)+losses.get(0)));
                winRate.add((wins.get(1) * 100.0) / (wins.get(1)+losses.get(1)));





                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Информация по призывателю: `" + event.getArgs() + " (" + summonerLevel + " lvl)`");


                //Раздел "body"
                embedBuilder
                        .addField("Вид очереди", queueType.get(1) + "\n\n"
                                + queueType.get(0), true);

                embedBuilder
                        .addField("Звание", tier.get(1)+ " " + rank.get(1) + "\n\n"
                                + tier.get(0)+ " " + rank.get(0), true);

                embedBuilder
                        .addField("Победы/Поражения (Win rate)", wins.get(1) + "/" + losses.get(1) + " (" + decimalFormat.format(winRate.get(1)) + " %)" + "\n\n"
                                +wins.get(0) + "   /   " + losses.get(0) + " (" + decimalFormat.format(winRate.get(0)) + " %)" , true);

                event.reply(embedBuilder.build());


            }

        } catch (Exception ex) {
            ex.getStackTrace();
            event.getChannel().sendMessage(":no_entry:"+ event.getAuthor().getAsMention() + ", что-то пошло не так, проверте правильность введенных аргументов:\n" +
                    ":exclamation:/lolinfo [Summoner Name] [region (eun или euw или ru)]").queue();
            event.getChannel().sendMessage(":robot:Если уверенны в правильности введенной команды, то закончился срок службы `RIOT Api`, напишите <@350764863641092107>").queue();

        }


    }




    public StringBuilder getEncryptedSummonerName (String[] fullUserMessage) throws UnsupportedEncodingException {
        //удаляем последний арргумент (region)
        List<String> sumName = new ArrayList  (Arrays.asList(fullUserMessage)) ;
        sumName.remove(fullUserMessage.length-1);


        //Переводим массив в один String
        StringBuilder stringSummonerName = new StringBuilder();
        for (int i = 1; i < sumName.size(); i++) {
            stringSummonerName.append(" ").append(sumName.get(i));
        }
        //System.out.println("stringSummonerName = " + stringSummonerName);

        //Кодируем все не цифробуквенные символы в знак (%), за которым следует два шестнадцатеричных числа для URL
        //Пробелы кодировались в "+", а  URL считывает пробел исключительно как "%20", поэтому через метод replace заменяем "+" на "%20"
        return new StringBuilder(URLEncoder.encode(stringSummonerName.toString(), "UTF-8").replace("+", "%20"));
    }
    public String getRegion (String[] fullUserMessage) {

        String region = fullUserMessage[fullUserMessage.length - 1];

        if (region.equals("eun") || region.equals("euw")){
            region = region + "1";
        }
        return region;
    }


}
