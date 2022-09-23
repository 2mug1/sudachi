package net.iamtakagi.sudachi;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 外部提供される静的中核クラス
 */
public final class Sudachi {

    @Getter
    private static final Map<UUID, Board> playerBoards = new HashMap<>();


    @Getter
    private static BoardAdapter boardAdapter;

    /**
     * ボード更新タスク
     * なんかのときに使えそうなので、一応変数化して置いておく
     */
    @Getter
    private static BukkitTask boardUpdateTask;


    /**
     * 初期化処理を行う関数
     * 外部から Sudachi.init(org.bukkit.plugin.Plugin); を呼び出すことで処理される
     * @param plugin
     */
    public static void init(Plugin plugin, BoardAdapter boardAdapter) {
        boardAdapter = boardAdapter;
        boardUpdateTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new BoardUpdateRunnable(plugin), boardAdapter.getInterval(), boardAdapter.getInterval());
    }
}
