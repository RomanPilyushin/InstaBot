import InstagramScraper.Instagram;
import InstagramScraper.exception.InstagramException;
import InstagramScraper.model.Account;
import InstagramScraper.model.Media;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public class InstaBot extends TelegramLongPollingBot {
    private static final String TOKEN = "285523816:AAEEvJKTs-AovM1-xLQzIIyFD8q13dx-Sho";
    private static final String NAME = "InstaGetPhotoBot";
    private static Instagram instagram = new Instagram();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new InstaBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String accountName = message.getText();
        if (accountName.startsWith("@")) accountName = accountName.substring(1, accountName.length());
        Account account;

        try {
            account = instagram.getAccountByUsername(accountName);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }

        //Media media = null;
        List<Media> medias = null;
        try {
            //media = instagram.getMediaByUrl("https://www.instagram.com/p/BP8FP56FA1l/");

            medias = instagram.getMedias(accountName, 1);
            //System.out.println(medias.get(0).code);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }

        //  || message.getText().toLowerCase().equals("durov")
        if (!message.hasText()) {
            sendMsg(message, "Error! Try again");
        } else {
            //sendMsg(message, medias != null ? medias.get(0).imageHighResolutionUrl : null);
            sendMsg(message, medias != null ? "https://www.instagram.com/p/" + medias.get(0).code : null);
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}