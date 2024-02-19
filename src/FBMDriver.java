public class FBMDriver {		
	
	public static void main(String[]args) {
		DiscordBot bot = new DiscordBot();
		
		
		/*FBMarketplace fb = new FBMarketplace();
		while(true) {
			fb.run();
			wait(120);
		}*/
	}
	
	@SuppressWarnings("unused")
	private static void wait(int seconds) {
		try {
			Thread.sleep(Math.round(seconds * 1000.00));
		} catch (InterruptedException e) {
			System.out.println("Error in pause(int) method.");
		}
	}
}
