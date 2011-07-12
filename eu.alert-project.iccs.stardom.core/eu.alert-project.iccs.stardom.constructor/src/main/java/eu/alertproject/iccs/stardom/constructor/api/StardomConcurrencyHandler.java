package eu.alertproject.iccs.stardom.constructor.api;

import java.util.List;
import java.util.concurrent.*;

/**
 * The following class takes care of controlling the type of thread pool, that
 * will be used.
 *
 */
public enum StardomConcurrencyHandler {
	
	Instance;
	
	private ExecutorService threadPool;

	private StardomConcurrencyHandler() {
		threadPool = Executors.newFixedThreadPool(15);
	}
	
	
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return threadPool.awaitTermination(timeout, unit);
	}


	public void execute(Runnable command) {
		threadPool.execute(command);
	}


	public boolean isShutdown() {
		return threadPool.isShutdown();
	}


	public boolean isTerminated() {
		return threadPool.isTerminated();
	}


	public void shutdown() {
		threadPool.shutdown();
	}


	public List<Runnable> shutdownNow() {
		return threadPool.shutdownNow();
	}


	public <T> Future<T> submit(Callable<T> task) {
		return threadPool.submit(task);
	}


	public <T> Future<T> submit(Runnable task, T result) {
		return threadPool.submit(task, result);
	}


	public Future<?> submit(Runnable task) {
		return threadPool.submit(task);
	}

}
