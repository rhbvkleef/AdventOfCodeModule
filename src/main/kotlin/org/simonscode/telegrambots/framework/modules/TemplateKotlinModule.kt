package org.simonscode.telegrambots.framework.modules

import org.simonscode.telegrambots.framework.Bot
import org.simonscode.telegrambots.framework.Module
import org.simonscode.telegrambots.framework.Utils
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.exceptions.TelegramApiException

class TemplateKotlinModule : Module {

    private var myValue: String? = null

    override val name: String
        get() {
            println("TemplateKotlinModule.getName")
            return "TemplateKotlinModule"
        }

    override val version: String
        get() {
            println("TemplateKotlinModule.getVersion")
            return "1.0"
        }

    /**
     * This method will be called when your module has been loaded.
     * The parameter state will contain whatever the method saveState returned last time the bot was shut down.
     *
     * @param state Previously saved state of the module
     */
    override fun setup(state: Any?) {
        println("TemplateKotlinModule.setup")
        myValue = if (state != null) {
            state as String?
        } else {
            "default value"
        }
    }

    /**
     * This method is called every time Telegram has an update and is sent to every module in parallel
     *
     * @param sender    The bot that sent the update
     * @param update The update containing information about what happened
     */
    override fun processUpdate(sender: Bot, update: Update) {
        println("TemplateKotlinModule.processUpdate")
        var message: Message?

        // Check if the update is a message and if it starts with "/test"
        message = Utils.checkForCommand(update, "/test", true)
        // Will return the message if the condition applies or null if not

        if (message != null) {
            // Another of my utils functions that make life easier.
            Utils.reply(sender, update, "You sent: " + message.text)
        }

        // Here is the same code without the use of the Utils class

        // First, check if the update contains a text-message (edited or not)
        if (update.hasMessage()) {
            message = update.message
        } else if (update.hasEditedMessage()) {
            message = update.editedMessage
        }

        // Check if the message has text and if the text is what we want
        if (message != null && message.hasText() && message.text.startsWith("/state")) {

            // Send a reply
            try {
                sender.execute(SendMessage().setText("My state is: " + myValue!!).setReplyToMessageId(message.messageId).setChatId(message.chatId!!))
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        }


        // The equivalent function without the utils-class:
        try {
            sender.execute(SendMessage().setText("You sent: " + message!!.text).setReplyToMessageId(message.messageId).setChatId(message.chatId!!))
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }

    }

    /**
     * This method will be called when your module is about to be unloaded.
     * Either it was disabled or the bot is about to shut down.
     *
     * @return Object that contains the state of this module
     */
    override fun saveState(): Any? {
        println("TemplateKotlinModule.saveState")
        return "new state"
    }

    override fun getHelpText(args: Array<String>?): String? {
        return "No help text, yet."
    }
}