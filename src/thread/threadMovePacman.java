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
		while (gc.getStop() != true) {
			if (i < gc.getPacmans().size()) {
				try {
					gc.movePacman(i);
					if (gc.getLevel() == 0)
						sleep(10);
					if (gc.getLevel() == 1)
						sleep(15);
					if (gc.getLevel() == 2)
						sleep(5);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			} else {
				i = 0;
			}
		}
	}

}
