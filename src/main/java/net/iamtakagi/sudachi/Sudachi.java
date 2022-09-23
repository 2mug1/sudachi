package net.iamtakagi.sudachi;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 外部提供される静的中核クラス
 */
public final class Sudachi {

    /**
     * Player UUID と ボードインスタンスを格納しておく HashMap
     */
    @Getter
    private static final Map<UUID, Board> playerBoards = new HashMap<>();


   // 実装済みの Board Adapter を宣言する変数
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
    public static void init(Plugin plugin, BoardAdapter adapter) {
        plugin.getLogger().info("[sudachi] 初期化中...");
        plugin.getLogger().info("[sudachi] BoardAdapter を登録しています...");
        boardAdapter = adapter;
        plugin.getLogger().info("[sudachi] BoardAdapter を登録しました");
        plugin.getLogger().info("[sudachi] イベントリスナーを登録しています...");
        // 匿名イベントリスナークラスを作って鯖入退出時の処理を登録する
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            /**
             * プレイヤーがサーバーに参加したとき、ボードインスタンスを生成して HashMap に格納する
             * @param event
             */
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                playerBoards.put(event.getPlayer().getUniqueId(), new Board(plugin, player, boardAdapter));
            }

            /**
             * プレイヤーがサーバーから退出したとき、HashMap に格納されているボードインスタンスを削除する
             * @param event
             */
            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                Player player = event.getPlayer();
                playerBoards.remove(player.getUniqueId());
            }
        }, plugin);
        plugin.getLogger().info("[sudachi] BoardUpdateRunnable を起動しています...");
        boardUpdateTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new BoardUpdateRunnable(plugin), boardAdapter.getInterval(), boardAdapter.getInterval());
        plugin.getLogger().info("[sudachi] BoardUpdateRunnable を起動しました");
        plugin.getLogger().info("[sudachi] 初期化が完了しました");
    }
}
