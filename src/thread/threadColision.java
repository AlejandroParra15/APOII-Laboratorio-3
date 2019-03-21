package thread;

import userinterface.GameController;

public class threadColision extends Thread{

	GameController gc;

	public threadColision(GameController gmc) {
		gc = gmc;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				gc.Colision();
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
