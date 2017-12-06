package me.vankleef.telegram.simon.modules

import org.simonscode.telegrambots.framework.Bot
import org.simonscode.telegrambots.framework.Module
import org.simonscode.telegrambots.framework.Utils
import org.telegram.telegrambots.api.methods.ParseMode
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update

class AdventOfCodeModule : Module {

    override val name: String
        get() {
            return "AdventOfCodeModule"
        }

    override val version: String
        get() {
            return "1.0"
        }

    /**
     * This method will be called when your module has been loaded.
     * The parameter state will contain whatever the method saveState returned last time the bot was shut down.
     *
     * @param state Previously saved state of the module
     */
    override fun setup(state: Any?) {

    }

    /**
     * This method is called every time Telegram has an update and is sent to every module in parallel
     *
     * @param sender    The bot that sent the update
     * @param update The update containing information about what happened
     */
    override fun processUpdate(sender: Bot, update: Update) {

        var message = Utils.checkForCommand(update, "/aocsession", true)
        if (message != null) {
            AdventOfCodeAPIClient.session = message.text.split(" ")[1]
            Utils.reply(sender, message, "Session set!")
            return
        }
        message = Utils.checkForCommand(update, "/aoc", true)

        if (message != null) {
            val msg = SendMessage()
            try {
                msg.text = "AdventOfCode scoreboard\n```\n" +
                        "Name           |  \uD83D\uDCB0 | ⭐️\n" +
                        "---------------+-----+-----\n" +
                        AdventOfCodeAPIClient.get().reversed().joinToString("\n") +
                        "```"
                msg.setParseMode(ParseMode.MARKDOWN)
            } catch (e: Exception) {
                e.printStackTrace()
                msg.text = e.message
            }
            msg.chatId = message.chatId.toString()
            sender.execute(msg)
        }

    }

    /**
     * This method will be called when your module is about to be unloaded.
     * Either it was disabled or the bot is about to shut down.
     *
     * @return Object that contains the state of this module
     */
    override fun saveState(): Any? {
        return null
    }

    override fun getHelpText(args: Array<String>?): String? {
        return "No help text, yet."
    }
}