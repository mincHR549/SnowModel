package snowymc.top.snowmodel.config;

import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class FileWatcher implements Runnable {

    private final Path base;
    private final Consumer<Path> onChange;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread thread;

    public FileWatcher(Path base, Consumer<Path> onChange) {
        this.base = base;
        this.onChange = onChange;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            thread = new Thread(this, "Snow-Model-FileWatcher");
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void stop() {
        running.set(false);
        if (thread != null) thread.interrupt();
    }

    @Override
    public void run() {
        try (WatchService ws = FileSystems.getDefault().newWatchService()) {
            base.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            while (running.get()) {
                WatchKey key = ws.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = base.resolve((Path) event.context());
                    if (changed.toString().endsWith(".yml")) {
                        onChange.accept(changed);
                    }
                }
                key.reset();
            }
        } catch (Exception ignored) {}
    }
}
