package mc.wordrc.randomvault.utils;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;


public class ColorUtils {

    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    public static String translateColorCodes(String text){

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++){
            if (texts[i].equalsIgnoreCase("&")){
                //get the next string
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

    public static TextComponent translateColorCodesToTextComponent(String text){

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        ComponentBuilder builder = new ComponentBuilder();

        for (int i = 0; i < texts.length; i++){
            TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")){
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#'){
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)));
                    builder.append(subComponent);
                }else{
                    if (texts[i].length() > 1){
                        subComponent.setText(texts[i].substring(1));
                    }else{
                        subComponent.setText(" ");
                    }

                    switch (texts[i].charAt(0)){
                        case '0':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.BLACK);
                            break;
                        case '1':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
                            break;
                        case '2':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                            break;
                        case '3':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
                            break;
                        case '4':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
                            break;
                        case '5':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
                            break;
                        case '6':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                            break;
                        case '7':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                            break;
                        case '8':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                            break;
                        case '9':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                            break;
                        case 'a':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                            break;
                        case 'b':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                            break;
                        case 'c':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                            break;
                        case 'd':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
                            break;
                        case 'e':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                            break;
                        case 'f':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                            break;
                        case 'k':
                            subComponent.setObfuscated(true);
                            break;
                        case 'l':
                            subComponent.setBold(true);
                            break;
                        case 'm':
                            subComponent.setStrikethrough(true);
                            break;
                        case 'n':
                            subComponent.setUnderlined(true);
                            break;
                        case 'o':
                            subComponent.setItalic(true);
                            break;
                        case 'r':
                            subComponent.setColor(net.md_5.bungee.api.ChatColor.RESET);
                            break;
                    }

                    builder.append(subComponent);
                }
            }else{
                builder.append(texts[i]);
            }
        }

        return new TextComponent(builder.create());

    }

}
