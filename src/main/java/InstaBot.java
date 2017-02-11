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

public class InstaBot extends TelegramLongPollingBot {
    private static final String TOKEN = "285523816:AAEEvJKTs-AovM1-xLQzIIyFD8q13dx-Sho";
    private static final String NAME = "InstaGetPhotoBot";
    private Instagram instagram = new Instagram();

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

        Account account;
        try {
            account = instagram.getAccountByUsername("durov");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
        Media media = null;
        try {
            media = instagram.getMediaByUrl("https://www.instagram.com/p/BP8FP56FA1l/");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }

        if (message != null && message.hasText()) {
            if (message.hasText() && message.getText().toLowerCase().equals("durov")) {
                sendMsg(message, "https://www.instagram.com/p/BP8FP56FA1l/");
            } else {
                sendMsg(message, "Error! Try again");
            }
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