package thread;

import userinterface.GameController;

public class threadOpenPacMan extends Thread{

	GameController gc;
	
	public threadOpenPacMan(GameController gmc) {
		gc = gmc;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				gc.openPacman();
				sleep(400);
				gc.closePacman();
				sleep(400);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
