package me.vankleef.telegram.simon.modules

import org.simonscode.telegrambots.framework.Bot
import org.simonscode.telegrambots.framework.Module
import org.simonscode.telegrambots.framework.Utils
import org.telegram.telegrambots.api.methods.ParseMode
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update

class AdventOfCodeModule : Module {

    var session = "53616c7465645f5f6e3be38ddbc21fdde38bc55c2fbb5c80c74121dcf9ffb73963ed203ccee9f99207288553c83273d7"
    var url = "http://adventofcode.com/2017/leaderboard/private/view/211171.json"

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
        val message = Utils.checkForCommand(update, "/aoc", true)

        if (message != null) {
            val msg = SendMessage()
            msg.text = "AdventOfCode scoreboard\n```\n" +
                    "Name                 | Score | Stars\n" +
                    "---------------------+-------+-------\n" +
                    AdventOfCodeAPIClient.get().reversed().joinToString("\n") +
                    "```"
            msg.chatId = message.chatId.toString()
            msg.setParseMode(ParseMode.MARKDOWN)
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
        return "new state"
    }

    override fun getHelpText(args: Array<String>?): String? {
        return "No help text, yet."
    }
}