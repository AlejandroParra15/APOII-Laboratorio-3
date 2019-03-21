package thread;

import userinterface.GameController;

public class threadMovePacman extends Thread {

	GameController gc;

	public threadMovePacman(GameController gmc) {
		gc = gmc;
	}

	@Override
	public void run() {
		int i = 0;
		while (true) {
			if(i<gc.getPacmans().size()) {
				try {
					gc.movePacman(i);
					sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}else {
				i=0;
			}
		}
	}

}
