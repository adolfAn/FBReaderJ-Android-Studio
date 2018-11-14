package dyg.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoveFamousBookExecutor {

    private ExecutorService executors = null;

    private LoveFamousBookExecutor() {
        executors = Executors.newFixedThreadPool(4);
    }

    public LoveFamousBookExecutor getInstance() {
        return Singleton.executor;
    }

    private static class Singleton {
        private static final LoveFamousBookExecutor executor = new LoveFamousBookExecutor();
    }

    public void exec(Runnable run) {
        executors.submit(run);
    }

}
