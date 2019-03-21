package userinterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.BestScores;
import model.PacMan;
import model.Player;
import thread.threadColision;
import thread.threadMovePacman;
import thread.threadOpenPacMan;

public class GameController {

	PacMan pacMan;

	Player player;

	BestScores bestScores = new BestScores();

	ArrayList<PacMan> pacmans;

	int rebounds = 0;

	int level = 0;

	boolean charged = false;

	@FXML
	private Pane gamePane;

	@FXML
	private Label lbRebounds;

	@FXML
	private HBox boxMessage;

	@FXML
	private Label lbMessage;

	@FXML
	public void initialize() {
		boxMessage.setVisible(false);
		lbRebounds.setText(String.valueOf(rebounds));
	}

	@FXML
	void Exit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void LoadGame() {
		pacmans = new ArrayList<PacMan>();
		String filePath = "";
		BufferedReader br = null;
		FileReader fr = null;

		if (level == 0) {
			filePath = "C:/Users/David/eclipse-workspace/Laboratorio2_Parra_David/src/resources/configurationLv0.txt";
		} else if (level == 1) {
			filePath = "C:/Users/David/eclipse-workspace/Laboratorio2_Parra_David/src/resources/configurationLv1.txt";
		} else if (level == 2)
			filePath = "C:/Users/David/eclipse-workspace/Laboratorio2_Parra_David/src/resources/configurationLv2.txt";

		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				if (sCurrentLine.charAt(0) != '#') {
					if (sCurrentLine.length() > 1) {
						String[] parts = sCurrentLine.split(" ");
						double radius = Double.parseDouble(parts[0]);
						double x = Double.parseDouble(parts[1]);
						double y = Double.parseDouble(parts[2]);
						int wait = Integer.parseInt(parts[3]);
						String direction = parts[4];
						int rebounds = Integer.parseInt(parts[5]);
						boolean stopped = Boolean.parseBoolean(parts[5]);
						pacMan = new PacMan(radius, x, y, direction, wait, rebounds, stopped);
						pacmans.add(pacMan);
					} else {
						level = Integer.parseInt(sCurrentLine);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		charged = true;
	}

	@FXML
	void SaveGame(ActionEvent event) {
		String name = JOptionPane.showInputDialog("Ingrese su nombre: ");
		int score = Integer.parseInt(lbRebounds.getText());
		System.out.println(name);
		System.out.println(score);
		player = new Player(name, score);
		bestScores.addPlayer(player);
	}

	@FXML
	void showBestScores(ActionEvent event) {
		JOptionPane.showMessageDialog(null, bestScores.messageScore(), "Best Scores", JOptionPane.INFORMATION_MESSAGE);
	}

	@FXML
	void StartGame(ActionEvent event) {
		if (charged) {
			for (int i = 0; i < pacmans.size(); i++) {
				gamePane.getChildren().add(pacmans.get(i).getArcPac());
			}
			threadOpenPacMan threadOpen = new threadOpenPacMan(this);
			threadMovePacman movePac = new threadMovePacman(this);
			threadColision threadColision = new threadColision(this);
			threadOpen.setDaemon(true);
			movePac.setDaemon(true);
			threadColision.setDaemon(true);
			threadOpen.start();
			movePac.start();
			threadColision.start();
		} else {
			lbMessage.setText("Se debe cargar una partida antes de jugar");
			boxMessage.setVisible(true);
		}
	}

	public void openPacman() {
		for (int i = 0; i < pacmans.size(); i++) {
			if (pacmans.get(i).getStop() == false) {
				pacmans.get(i).getArcPac().setStartAngle(45);
				pacmans.get(i).getArcPac().setLength(290);
			}
		}
	}

	public void closePacman() {
		for (int i = 0; i < pacmans.size(); i++) {
			if (pacmans.get(i).getStop() == false) {
				pacmans.get(i).getArcPac().setStartAngle(0);
				pacmans.get(i).getArcPac().setLength(500);
			}
		}
	}

	@FXML
	public void onPressed(MouseEvent event) {

		for (int i = 0; i < pacmans.size(); i++) {
			if (pacmans.get(i).getStop() != true) {
				double x = pacmans.get(i).getArcPac().getLayoutX();
				double y = pacmans.get(i).getArcPac().getLayoutY();
				double r = pacmans.get(i).getRadius();

				if (event.getSceneX() < (x + r + 100) && event.getSceneX() > (x - r)
						&& event.getSceneY() < (y + r + 100) && event.getSceneY() > (y - r)) {
					pacmans.get(i).setStop(true);
				}
			}
		}
	}

	public void movePacman(int index) {

		if (pacmans.get(index).getStop() == false) {
			if (pacmans.get(index).getDirection().equals(PacMan.UP)) {
				pacmans.get(index).getArcPac().setRotate(90);
				pacmans.get(index).getArcPac().setLayoutY(pacmans.get(index).getArcPac().getLayoutY() + 17);
			}
			if (pacmans.get(index).getDirection().equals(PacMan.DOWN)) {
				pacmans.get(index).getArcPac().setRotate(-90);
				pacmans.get(index).getArcPac().setLayoutY(pacmans.get(index).getArcPac().getLayoutY() - 17);
			}
			if (pacmans.get(index).getDirection().equals(PacMan.RIGHT)) {
				pacmans.get(index).getArcPac().setRotate(180);
				pacmans.get(index).getArcPac().setLayoutX(pacmans.get(index).getArcPac().getLayoutX() - 17);
			}
			if (pacmans.get(index).getDirection().equals(PacMan.LEFT)) {
				pacmans.get(index).getArcPac().setRotate(0);
				pacmans.get(index).getArcPac().setLayoutX(pacmans.get(index).getArcPac().getLayoutX() + 17);
			}

			if (pacmans.get(index).getArcPac().getLayoutX() > gamePane.getWidth()) {
				pacmans.get(index).setDirection(pacmans.get(index).opposite(pacmans.get(index).getDirection()));
				pacmans.get(index).setRebounds(pacmans.get(index).getRebounds() + 1);
				rebounds++;
			}

			if (pacmans.get(index).getArcPac().getLayoutX() < 0) {
				pacmans.get(index).setDirection(pacmans.get(index).opposite(pacmans.get(index).getDirection()));
				rebounds++;
			}

			if (pacmans.get(index).getArcPac().getLayoutY() > gamePane.getHeight()) {
				pacmans.get(index).setDirection(pacmans.get(index).opposite(pacmans.get(index).getDirection()));
				pacmans.get(index).setRebounds(pacmans.get(index).getRebounds() + 1);
				rebounds++;
			}

			if (pacmans.get(index).getArcPac().getLayoutY() < 0) {
				pacmans.get(index).setDirection(pacmans.get(index).opposite(pacmans.get(index).getDirection()));
				pacmans.get(index).setRebounds(pacmans.get(index).getRebounds() + 1);
				rebounds++;
			}
		}

		setRebounds();

	}

	public void Colision() {
		// círculo 1 con centro en (cx1,cy1) y radio r1
		// círculo 2 con centro en (cx2,cy2) y radio r2
		double distancia = 0;
		double cx1 = 0;
		double cy1 = 0;
		double r1 = 0;
		double cx2 = 0;
		double cy2 = 0;
		double r2 = 0;
		for (int i = 0; i < pacmans.size(); i++) {
			for (int j = 0; j < pacmans.size(); j++) {
				if (pacmans.get(i) != pacmans.get(j)) {
					if (pacmans.get(i).getArcPac().getCenterX() > pacmans.get(j).getArcPac().getCenterX()
							&& pacmans.get(i).getArcPac().getCenterY() > pacmans.get(j).getArcPac().getCenterY()) {
						cx1 = pacmans.get(i).getArcPac().getLayoutX();
						cy1 = pacmans.get(i).getArcPac().getLayoutY();
						r1 = pacmans.get(i).getArcPac().getRadiusX();
						cx2 = pacmans.get(j).getArcPac().getLayoutX();
						cy2 = pacmans.get(j).getArcPac().getLayoutY();
						r2 = pacmans.get(j).getArcPac().getRadiusX();
					} else {
						cx1 = pacmans.get(j).getArcPac().getLayoutX();
						cy1 = pacmans.get(j).getArcPac().getLayoutY();
						r1 = pacmans.get(j).getArcPac().getRadiusX();
						cx2 = pacmans.get(i).getArcPac().getLayoutX();
						cy2 = pacmans.get(i).getArcPac().getLayoutY();
						r2 = pacmans.get(i).getArcPac().getRadiusX();
					}
					distancia = Math.sqrt((cx1 - cx2) * (cx1 - cx2) + (cy1 - cy2) * (cy1 - cy2));
					if (distancia < r1 + r2) {
						if (pacmans.get(i).getDirection().equals(PacMan.UP)) {
							pacmans.get(i).getArcPac().setLayoutY(pacmans.get(i).getArcPac().getLayoutY()
									- (pacmans.get(i).getArcPac().getRadiusY() + 4));
						}
						if (pacmans.get(i).getDirection().equals(PacMan.DOWN)) {
							pacmans.get(i).getArcPac().setLayoutY(pacmans.get(i).getArcPac().getLayoutY()
									+ (pacmans.get(i).getArcPac().getRadiusY() + 4));
						}
						if (pacmans.get(i).getDirection().equals(PacMan.RIGHT)) {
							pacmans.get(i).getArcPac().setLayoutX(pacmans.get(i).getArcPac().getLayoutX()
									+ (pacmans.get(i).getArcPac().getRadiusX() + 4));
						}
						if (pacmans.get(i).getDirection().equals(PacMan.LEFT)) {
							pacmans.get(i).getArcPac().setLayoutX(pacmans.get(i).getArcPac().getLayoutX()
									- (pacmans.get(i).getArcPac().getRadiusX() + 4));
						}
						if (pacmans.get(j).getDirection().equals(PacMan.UP)) {
							pacmans.get(j).getArcPac().setLayoutY(pacmans.get(i).getArcPac().getLayoutY()
									- (pacmans.get(i).getArcPac().getRadiusY() + 4));
						}
						if (pacmans.get(j).getDirection().equals(PacMan.DOWN)) {
							pacmans.get(j).getArcPac().setLayoutY(pacmans.get(i).getArcPac().getLayoutY()
									+ (pacmans.get(i).getArcPac().getRadiusY() + 4));
						}
						if (pacmans.get(j).getDirection().equals(PacMan.RIGHT)) {
							pacmans.get(j).getArcPac().setLayoutX(pacmans.get(i).getArcPac().getLayoutX()
									+ (pacmans.get(i).getArcPac().getRadiusX() + 4));
						}
						if (pacmans.get(j).getDirection().equals(PacMan.LEFT)) {
							pacmans.get(j).getArcPac().setLayoutX(pacmans.get(i).getArcPac().getLayoutX()
									- (pacmans.get(i).getArcPac().getRadiusX() + 4));
						}
						pacmans.get(i).setDirection(pacmans.get(i).opposite(pacmans.get(i).getDirection()));
						pacmans.get(j).setDirection(pacmans.get(j).opposite(pacmans.get(j).getDirection()));
					}
				}
			}
		}

	}

	public void setRebounds() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lbRebounds.setText(String.valueOf(rebounds));
			}
		});
	}

	public ArrayList<PacMan> getPacmans() {
		return pacmans;
	}

	@FXML
	public void LevelZero(ActionEvent event) {
		level = 0;
		LoadGame();
	}

	@FXML
	public void LevelOne(ActionEvent event) {
		level = 1;
		LoadGame();
	}

	@FXML
	public void LevelTwo(ActionEvent event) {
		level = 2;
		LoadGame();
	}

}
