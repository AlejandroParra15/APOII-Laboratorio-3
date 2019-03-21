package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BestScores {

	ArrayList<Player> players;
	
	public BestScores() {
		players = new ArrayList<>();
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public String messageScore() {
		String msg = "";
		System.out.println(players.size());
		if (players.size() > 2) {
			Collections.sort(players, new Comparator<Player>() {

				@Override
				public int compare(Player p1, Player p2) {
					return new Integer(p1.getScore()).compareTo(new Integer(p2.getScore()));
				}

			});
		}
		for (int i = 0; i < players.size(); i++) {
			msg += "\n" + (i + 1) + ") " + players.get(i).getName() + "  -  " + players.get(i).getScore();
		}
		return msg;
	}
	
}
