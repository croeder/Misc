public class Shutdown {
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new CleanupThread());

		// do something for a while
		for (int i=0; i<60; i++) {
			try {
				Thread.sleep(1000);
			}
			catch (Exception x) {
				// Ignore Exception
				System.out.println("cuaght : " + x);
			}
		}
	}
}

class CleanupThread extends Thread {
	public void run() {
		System.out.println("caught the shutdown event");
	}
}
