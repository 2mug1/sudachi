package net.iamtakagi.sudachi;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


@AllArgsConstructor
public class BoardUpdateRunnable implements Runnable {

	private Plugin plugin;


	@Override
	public void run() {
		Sudachi.getBoardAdapter().preLoop();

		for (Player player : plugin.getServer().getOnlinePlayers()) {
			Board board = Sudachi.getPlayerBoards().get(player.getUniqueId());

			if (board == null) {
				continue;
			}

			try {
				Scoreboard scoreboard = board.getScoreboard();

				List<String> scores = Sudachi.getBoardAdapter().getScoreboard(player, board);

				if (scores != null) {
					Collections.reverse(scores);

					Objective objective = board.getObjective();

					if (!objective.getDisplayName().equals(Sudachi.getBoardAdapter().getTitle(player))) {
						objective.setDisplayName(Sudachi.getBoardAdapter().getTitle(player));
					}

					if (scores.isEmpty()) {
						Iterator<BoardEntry> iter = board.getEntries().iterator();
						while (iter.hasNext()) {
							BoardEntry boardEntry = iter.next();
							boardEntry.remove();
							iter.remove();
						}
						continue;
					}

					forILoop:
					for (int i = 0; i < scores.size(); i++) {
						String text = scores.get(i);
						int position = i + 1;

						for (BoardEntry boardEntry : new LinkedList<>(board.getEntries())) {
							Score score = objective.getScore(boardEntry.getKey());

							if (score != null && boardEntry.getText().equals(text)) {
								if (score.getScore() == position) {
									continue forILoop;
								}
							}
						}

						Iterator<BoardEntry> iter = board.getEntries().iterator();
						while (iter.hasNext()) {
							BoardEntry boardEntry = iter.next();
							int entryPosition = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(boardEntry
									.getKey()).getScore();
							if (entryPosition > scores.size()) {
								boardEntry.remove();
								iter.remove();
							}
						}

						int positionToSearch = position - 1;

						BoardEntry entry = board.getByPosition(positionToSearch);
						if (entry == null) {
							new BoardEntry(board, text).send(position);
						} else {
							entry.setText(text).setup().send(position);
						}

						if (board.getEntries().size() > scores.size()) {
							iter = board.getEntries().iterator();
							while (iter.hasNext()) {
								BoardEntry boardEntry = iter.next();
								if (!scores.contains(boardEntry.getText()) || Collections.frequency(board
										.getBoardEntriesFormatted(), boardEntry.getText()) > 1) {
									boardEntry.remove();
									iter.remove();
								}
							}
						}
					}
				} else {
					if (!board.getEntries().isEmpty()) {
						board.getEntries().forEach(BoardEntry::remove);
						board.getEntries().clear();
					}
				}

				Sudachi.getBoardAdapter().onScoreboardCreate(player, scoreboard);

				player.setScoreboard(scoreboard);
			} catch (Exception e) {
				e.printStackTrace();

				plugin.getLogger().severe("Something went wrong while updating " + player.getName() + "'s scoreboard " +
				                          "" + board + " - " + board.getAdapter() + ")");
			}
		}
	}

}
