package me.vankleef.telegram.simon.modules

import com.google.gson.JsonParser
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

/**
 * @author rolf
 * Created on 12/1/17
 */
object AdventOfCodeAPIClient {

    var session = "53616c7465645f5f6e3be38ddbc21fdde38bc55c2fbb5c80c74121dcf9ffb73963ed203ccee9f99207288553c83273d7"

    class Person(private var name: String, var score: Int, private var stars: Int) {

        override fun toString(): String {
            return String.format("%-14s | %3d | %2d", this.name.substring(0, Math.min(this.name.length, 14)), this.score, this.stars)
        }
    }

    @Throws(IOException::class)
    fun get(): List<Person> {
        val url = URL("http://adventofcode.com/2017/leaderboard/private/view/211171.json")

        val con = url.openConnection()
        con.doOutput = true
        con.setRequestProperty("Cookie", "session=$session")
        con.connect()

        val parser = JsonParser()

        val obj = parser.parse(InputStreamReader(con.getInputStream()))

        val participants = obj.asJsonObject.get("members").asJsonObject.entrySet()

        val persons: ArrayList<Person> = ArrayList(participants.size)
        for (participant in participants) {
            val pcp = participant.value.asJsonObject
            val name = pcp.get("name").asString
            val stars = pcp.get("stars").asInt
            val score = pcp.get("local_score").asInt

            persons.add(Person(userNameMatrix.getOrDefault(name, name), score, stars))
        }

        return persons.sortedBy { it.score }
    }

    private val userNameMatrix = mapOf(
            "sniperrifle2004" to "Wilfried",
            "nanderv" to "Nander",
            "swordiemen" to "Tim Block"
    )
}