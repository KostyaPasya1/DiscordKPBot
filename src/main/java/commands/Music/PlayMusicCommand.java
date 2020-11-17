package commands.Music;


import LavaPlayer.PlayerManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import jdk.nashorn.api.scripting.URLReader;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;

import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayMusicCommand extends MusicCommands {


    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";


    public PlayMusicCommand() {
        super.name = "play";
        super.help = "Подключается к вашему голосовому каналу и воспроизводит заказанную музыку";
        super.arguments = "[сылка/название песни или видео (добавляет в очередь первое видео из спика после поиска по названию на сайте YouTube)]";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void execute(CommandEvent event) {
        URL videoUrl;

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        //To openAudioConnection
        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();



        //Обработка строки
        String args = event.getArgs();
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(args);


        if(!memberVoiceState.inVoiceChannel()){
            event.getChannel().sendMessage(":x: " + event.getAuthor().getAsMention() + ", ты должен быть в голосовом канале, чтобы пользоваться этой командой!").queue();
        } else {
            if (args.isEmpty()) {
                event.reply(":exclamation:После команды `/play` введите название видео/песни (*поиск будет проводиться на YouTube*) или ссылку на это видео/песню!");
                return;
            }
            audioManager.openAudioConnection(memberChannel);
            if(matcher.find()) {
                //Если введенный аргумент пользователя URL, то открываем AudioConnection и воспроизводим песню по ссылке
                PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), args);
            } else {
                //Если введеный аргумент не ссылка (название видео) через youTube API берем videoId
                try {
                    String[] userUrl = event.getArgs().split(" ");
                    StringBuilder stringUrl = new StringBuilder();
                    for (String s : userUrl) {
                        stringUrl.append(" ").append(s);
                    }
                    //System.out.println("userUrl = " + stringUrl);

                    //Кодируем все не цифробуквенные символы в знак (%), за которым следует два шестнадцатеричных числа для URL
                    //Пробелы кодировались в "+", а  URL считывает пробел исключительно как "%20", поэтому через метод replace заменяем "+" на "%20"
                    StringBuilder url =  new StringBuilder (URLEncoder.encode(stringUrl.toString(), "UTF-8").replace("+", "%20"));
                    //System.out.println(url);

                    URL youtubeApi = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&key=AIzaSyD9cp6G6bJ0u4dYHpF5uiCMoBn1E672x4k&type=video&q=" + url);
                    //System.out.println(youtubeApi);


                    JSONParser parse = new JSONParser();
                    JSONObject obj = (JSONObject) parse.parse(new URLReader(youtubeApi));
                    //System.out.println(obj);

                    JSONArray items = (JSONArray) obj.get("items");
                    if(items.size() > 0) {
                        JSONObject id = (JSONObject) ((JSONObject) items.get(0)).get("id");
                        String videoId = (String) id.get("videoId");
                        videoUrl = new URL("https://www.youtube.com/watch?v=" + videoId);
                        //System.out.println("url2 = " + videoUrl.toString());
                    } else {
                        event.getTextChannel().sendMessage(":x:Ничего не найдено!").queue();
                        return;
                    }
                } catch (IOException | ParseException ex) {
                    event.getTextChannel().sendMessage(":x:Что-то пошло не так!").queue();
                    ex.printStackTrace();
                    return;
                }
                PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), videoUrl.toString());
            }

        }

        event.getMessage().delete().queue();

    }






}

