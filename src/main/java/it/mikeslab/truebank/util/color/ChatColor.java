package it.mikeslab.truebank.util.color;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ChatColor {


    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    public static String color(String text){

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++){
            if (texts[i].equalsIgnoreCase("&")){
                i++;
                if (texts[i].charAt(0) == '#'){
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                }else{
                    finalText.append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            }else{
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }


    public static List<String> color(List<String> lines) {
        return lines.stream().map(ChatColor::color).collect(Collectors.toList());
    }


}
