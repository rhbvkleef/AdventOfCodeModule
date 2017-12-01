package me.vankleef.telegram.simon.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author rolf
 * Created on 12/1/17
 */
public class AdventOfCodeModuleTest {

	public static class Person {
		String name;
		int score;
		int stars;

		public Person(String name, int score, int stars) {
			this.name = name;
			this.score = score;
			this.stars = stars;
		}

		public String toString() {
			return String.format("%-20s | %3d | %3d", this.name, this.score, this.stars);
		}
	}

	public static class PersonComparator implements Comparator<Person> {
		@Override
		public int compare(Person o1, Person o2) {
			return o2.score - o1.score;
		}
	}

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://adventofcode.com/2017/leaderboard/private/view/211171.json");

		URLConnection con = url.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("Cookie", "session=53616c7465645f5f6e3be38ddbc21fdde38bc55c2fbb5c80c74121dcf9ffb73963ed203ccee9f99207288553c83273d7");
		con.connect();

		JsonParser parser = new JsonParser();

		JsonElement obj = parser.parse(new InputStreamReader(con.getInputStream()));

		Set<Map.Entry<String, JsonElement>> participants = obj.getAsJsonObject().get("members").getAsJsonObject().entrySet();

		List<Person> persons = new ArrayList<>(participants.size());
		for (Map.Entry<String, JsonElement> participant : participants){
			JsonObject pcp = participant.getValue().getAsJsonObject();
			String name = pcp.get("name").getAsString();
			int stars = pcp.get("stars").getAsInt();
			int score = pcp.get("local_score").getAsInt();

			persons.add(new Person(name, score, stars));
		}

		persons.sort(new PersonComparator());

		persons.forEach(System.out::println);
	}
}
