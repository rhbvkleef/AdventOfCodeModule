package org.simonscode.telegrambots.framework.modules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simonscode.telegrambots.framework.Bot;
import org.simonscode.telegrambots.framework.Module;
import org.simonscode.telegrambots.framework.Utils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TemplateJavaModule implements Module {

    private String myValue;

    @NotNull
    @Override
    public String getName() {
        System.out.println("TemplateJavaModule.getName");
        return "TemplateJavaModule";
    }

    @NotNull
    @Override
    public String getVersion() {
        System.out.println("TemplateJavaModule.getVersion");
        return "1.0";
    }

    /**
     * This method will be called when your module has been loaded.
     * The parameter state will contain whatever the method saveState returned last time the bot was shut down.
     *
     * @param state Previously saved state of the module
     */
    @Override
    public void setup(Object state) {
        System.out.println("TemplateJavaModule.setup");
        if (state != null) {
            myValue = (String) state;
        } else {
            myValue = "default value";
        }
    }

    /**
     * This method is called every time Telegram has an update and is sent to every module in parallel
     *
     * @param sender The bot that sent the update
     * @param update The update containing information about what happened
     */
    @Override
    public void processUpdate(Bot sender, Update update) {
        System.out.println("TemplateJavaModule.processUpdate");
        Message message;

        // Check if the update is a message and if it starts with "/test"
        message = Utils.INSTANCE.checkForCommand(update, "/test", true);
        // Will return the message if the condition applies or null if not

        if (message != null) {
            // Another of my utils functions that make life easier.
            Utils.INSTANCE.reply(sender, update, "You sent: " + message.getText());
        }

        // Here is the same code without the use of the Utils class

        // First, check if the update contains a text-message (edited or not)
        if (update.hasMessage()) {
            message = update.getMessage();
        } else if (update.hasEditedMessage()) {
            message = update.getEditedMessage();
        }

        // Check if the message has text and if the text is what we want
        if (message != null && message.hasText() && message.getText().startsWith("/state")) {

            // Send a reply
            try {
                sender.execute(new SendMessage().setText("My state is: " + myValue).setReplyToMessageId(message.getMessageId()).setChatId(message.getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


        // The equivalent function without the utils-class:
        try {
            sender.execute(new SendMessage().setText("You sent: " + message.getText()).setReplyToMessageId(message.getMessageId()).setChatId(message.getChatId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will be called when your module is about to be unloaded.
     * Either it was disabled or the bot is about to shut down.
     *
     * @return Object that contains the state of this module
     */
    @Nullable
    @Override
    public Object saveState() {
        System.out.println("TemplateJavaModule.saveState");
        return "new state";
    }

    @Nullable
    @Override
    public String getHelpText(String[] args) {
        return "No help text, yet.";
    }
}