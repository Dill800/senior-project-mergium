package projectPackage;

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.glass.events.KeyEvent;
import com.sun.glass.ui.Robot;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.application.*;

public class Mergium extends Application {

	Media audioFile;
	MediaPlayer audio;
	double volume = .25; // .25
	int textSpeed = 600;

	private final int width = 500;
	private final int height = 700;

	private Shop shop = new Shop();

	FileOutputStream f;
	ObjectOutputStream o;
	FileInputStream fIn;
	ObjectInputStream oIn;

	private Player player = new Player();
	private Bag bag = new Bag();

	// General Button Glow Effect
	Glow glow;
	Glow nullGlow;

	// General Column/Row Constraints
	private ColumnConstraints fullColConstraint = new ColumnConstraints();
	private ColumnConstraints halfColConstraint = new ColumnConstraints();
	private RowConstraints halfRowConstraint = new RowConstraints();
	private RowConstraints fullRowConstraint = new RowConstraints();
	private RowConstraints majorRowConstraint = new RowConstraints();
	private RowConstraints minorRowConstraint = new RowConstraints();

	private Label playerCreatureName;
	private Label enemyCreatureName;

	@SuppressWarnings("restriction")
	@Override
	public void start(Stage stage) throws Exception {

		// Removes Window
		stage.initStyle(StageStyle.UNDECORATED);

		stage.setOnCloseRequest(e -> {
			try {
				this.stop();
			} catch (Exception e2) {

			}
		});

		// Minimizes all windows before opening Mergium
//		Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
//		robot.keyPress(KeyEvent.VK_WINDOWS);
//		robot.keyPress(KeyEvent.VK_D);
//		robot.keyRelease(KeyEvent.VK_D);
//		robot.keyRelease(KeyEvent.VK_WINDOWS);

		// Assign value to button glows
		glow = new Glow();
		glow.setLevel(.5);
		nullGlow = new Glow();
		nullGlow.setLevel(0);

		// Assigns value to general Row/Column constraints
		fullColConstraint.setPercentWidth(100);
		halfColConstraint.setPercentWidth(50);
		halfRowConstraint.setPercentHeight(50);
		fullRowConstraint.setPercentHeight(100);
		majorRowConstraint.setPercentHeight(45);
		minorRowConstraint.setPercentHeight(10);

		// Non-resizable frame
		stage.setResizable(false);
		stage.setTitle("Mergium");

		// Initializes Home Screen
		toHomeScreen(stage, true);
	}

	// Home Screen Setup
	public void toHomeScreen(Stage stage, boolean restartMusic) throws Exception {

		// Assign .mp3 file to Media and MediaPlayer
		if (restartMusic) {
			audioFile = new Media(getClass().getResource("/resources/titleAudio.mp3").toURI().toString());
			audio = new MediaPlayer(audioFile);
			// Loops audio
			audio.setOnEndOfMedia(new Runnable() {
				public void run() {
					audio.seek(Duration.ZERO);
				}
			});
			audio.setVolume(volume);
			audio.play();
		}

		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		BorderPane topPane = new BorderPane();
		VBox bottomPane = new VBox();

		Image title = new Image(getClass().getResource("/resources/TITLE.GIF").toURI().toString());
		ImageView titleView = new ImageView(title);

		topPane.setCenter(titleView);

		// Initialize Buttons (New Game, Continue, Instructions)
		homeButtonSelection(stage, bottomPane);

		GridPane.setHalignment(topPane, HPos.CENTER);
		rootPane.add(topPane, 0, 0);
		bottomPane.setAlignment(Pos.CENTER);
		rootPane.add(bottomPane, 0, 1);
		Scene mainScene = new Scene(rootPane, width, height);
		stage.setScene(mainScene);
		stage.show();

	}

	// Checks to level up
	public void checkLevelUp() {

		for (Creature c : bag.getCreatures()) {
			if (c.getXp() >= c.getXpCeil()) {
				c.levelUp();
			}
		}

	}

	// Displays the 3 initial Home buttons
	public void homeButtonSelection(Stage stage, VBox bottomPane) throws Exception {

		// Clear bottomPane
		bottomPane.getChildren().clear();

		// Sets spacing
		bottomPane.setSpacing(10);

		// New Game Button
		Image newGameButton = new Image(getClass().getResource("/resources/newGameButtton.png").toURI().toString());
		ImageView newGameButtonView = new ImageView(newGameButton);
		Button newGame = new Button(null, newGameButtonView);
		final double newGameWidth = newGameButton.getWidth();
		newGameButtonView.setPreserveRatio(true);
		newGame.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		// Expand/contract button
		newGame.setOnMouseEntered(e -> {
			newGameButtonView.setFitWidth(newGameWidth + 20);
		});
		newGame.setOnMouseExited(e -> {
			newGameButtonView.setFitWidth(newGameWidth);
		});

		// Continue Button
		Image continueGameButton = new Image(
				getClass().getResource("/resources/continueButton.png").toURI().toString());
		ImageView continueGameButtonView = new ImageView(continueGameButton);
		Button continueGame = new Button(null, continueGameButtonView);
		final double continueGameWidth = continueGameButton.getWidth();
		continueGameButtonView.setPreserveRatio(true);
		continueGame.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		// Expand/Contract button
		continueGame.setOnMouseEntered(e -> {
			continueGameButtonView.setFitWidth(continueGameWidth + 20);
		});
		continueGame.setOnMouseExited(e -> {
			continueGameButtonView.setFitWidth(continueGameWidth);
		});
		BufferedReader br = new BufferedReader(new FileReader("data.txt"));
		if (br.readLine() == null) {
			continueGame.setDisable(true);
		}

		// Instructions Button
		Image instructionsButton = new Image(
				getClass().getResource("/resources/instructionsButton.png").toURI().toString());
		ImageView instructionsButtonView = new ImageView(instructionsButton);
		Button instructions = new Button(null, instructionsButtonView);
		final double instructionsWidth = instructionsButton.getWidth();
		;
		instructionsButtonView.setPreserveRatio(true);
		instructions.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		// Expand/Contract button
		instructions.setOnMouseEntered(e -> {
			instructionsButtonView.setFitWidth(instructionsWidth + 20);
		});
		instructions.setOnMouseExited(e -> {
			instructionsButtonView.setFitWidth(instructionsWidth);
		});
		;

		// Settings Button
		Image settings = new Image(getClass().getResource("/resources/settings3.png").toURI().toString());
		ImageView settingsView = new ImageView(settings);
		settingsView.setPreserveRatio(true);
		settingsView.setFitWidth(30);
		Button settingsButton = new Button(null, settingsView);
		VBox.setMargin(settingsButton, new Insets(0, 0, 0, 0));
		settingsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		settingsButton.setOnAction(e -> {
			toSettingsScene(stage, true);
		});
		settingsButton.setOnMouseEntered(e -> {
			settingsButton.setEffect(glow);
		});
		settingsButton.setOnMouseExited(e -> {
			settingsButton.setEffect(nullGlow);
		});

		// Add Buttons
		bottomPane.getChildren().addAll(newGame, continueGame, instructions, settingsButton);

		// Clicked New Game
		newGame.setOnAction(e -> {
			try {
				newGameConfirmation(stage, bottomPane);
			} catch (Exception exception) {

			}

		});

		// Clicked Continue
		continueGame.setOnAction(e -> {
			try {

				retrieveData();

				toMainScene(stage, true);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// Clicked Instructions
		instructions.setOnAction(e -> {
			instructionsScene(stage);
		});
	}

	// All information dealing with the Main Scene
	public void toMainScene(Stage stage, boolean restartMusic) throws Exception {

		// Ends game after Town 5
		if (player.getTownNumber() == 6) {

			endGameScene(stage);
			return;
		}

		// Set Music
		if (restartMusic) {
			audio.stop();
			try {
				audioFile = new Media(getClass().getResource("/resources/gameAudio.mp3").toURI().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			audio = new MediaPlayer(audioFile);
			// Loops audio
			audio.setOnEndOfMedia(new Runnable() {
				public void run() {
					audio.seek(Duration.ZERO);
				}
			});
			audio.setVolume(volume / 5);
			audio.play();
		}

		// Checks to see if Creatures in bag can level up
		checkLevelUp();

		// Updates shop
		shop.updateShop(player.getTownNumber());

		// Create rootPane for Scene
		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(156, 226, 221), CornerRadii.EMPTY, Insets.EMPTY)));

		// Specific Row/Column Constraints
		RowConstraints row1Constraint = new RowConstraints();
		row1Constraint.setPercentHeight(40);
		RowConstraints row2Constraint = new RowConstraints();
		row2Constraint.setPercentHeight(40);
		RowConstraints row3Constraint = new RowConstraints();
		row3Constraint.setPercentHeight(20);

		BorderPane topPane = new BorderPane();
		GridPane bottomPane = new GridPane();

		// Borders
		bottomPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Top of topPane
		GridPane topBox = new GridPane();

		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(100 / 3.0);

		topBox.getRowConstraints().addAll(fullRowConstraint);
		topBox.getColumnConstraints().addAll(c, c, c);

		topBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Creatures in Bag Label
		Label creatureCount = new Label("Creatures: " + bag.getCreatures().size() + "");
		creatureCount.setFont(new Font("Arial Black", 14));
		GridPane.setHalignment(creatureCount, HPos.CENTER);

		// Current Town Label (Only one with insets since all 3 labels on top portion of
		// BorderPane
		Label currentTown = new Label(player.getTownName());
		currentTown.setFont(new Font("Arial Black", 14));
		currentTown.setPadding(new Insets(10, 10, 10, 10));
		GridPane.setHalignment(currentTown, HPos.CENTER);

		// Items in Bag Label
		Label itemCount = new Label("Items: " + bag.getItems().size() + "");
		itemCount.setFont(new Font("Arial Black", 14));
		GridPane.setHalignment(itemCount, HPos.CENTER);

		topBox.add(creatureCount, 0, 0);
		topBox.add(currentTown, 1, 0);
		topBox.add(itemCount, 2, 0);

		topPane.setTop(topBox);
		// topPane.setCenter(new Label("(insert town picture)"));
		ImageView imageView = new ImageView(player.getTownImage());
		imageView.setPreserveRatio(true);
		// imageView.setFitHeight(50);
		imageView.setFitWidth(width - 40);
		topPane.setCenter(imageView);

		// 2x2 bottom grid for buttons
		bottomPane.getRowConstraints().addAll(row1Constraint, row2Constraint, row3Constraint);
		bottomPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		// Fight in Forest Button
		Image tree = new Image(getClass().getResource("/resources/tree.png").toURI().toString());
		ImageView treeView = new ImageView(tree);
		treeView.setPreserveRatio(true);
		treeView.setFitHeight(50);

		Button fightButton = new Button("Fight in Forest", treeView);
		fightButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		fightButton.setContentDisplay(ContentDisplay.BOTTOM);
		fightButton.setMinSize(width / 2 - 85, height / 4 - 70);
		fightButton.setFont(new Font("Arial Black", 20));
		fightButton.setOnAction(e -> {
			// Fight Scene
			try {
				toFightScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		fightButton.setOnMouseEntered(e -> {
			treeView.setEffect(glow);
		});
		fightButton.setOnMouseExited(e -> {
			treeView.setEffect(nullGlow);
		});

		// Battle Town Leader Button
		Image swords = new Image(getClass().getResource("/resources/swords.png").toURI().toString());
		ImageView swordsView = new ImageView(swords);
		swordsView.setPreserveRatio(true);
		swordsView.setFitHeight(50);

		Button leaderButton = new Button("Battle Leader", swordsView);
		leaderButton.setWrapText(true);
		leaderButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		leaderButton.setContentDisplay(ContentDisplay.BOTTOM);
		leaderButton.setMinSize(width / 2 - 85, height / 4 - 70);
		leaderButton.setFont(new Font("Arial Black", 20));
		leaderButton.setOnAction(e -> {
			// Battle Town Leader Scene
			try {
				toBattleScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		leaderButton.setOnMouseEntered(e -> {
			swordsView.setEffect(glow);
		});
		leaderButton.setOnMouseExited(e -> {
			swordsView.setEffect(nullGlow);
		});

		// Bag Button
		Image bag = new Image(getClass().getResource("/resources/bag.png").toURI().toString());
		ImageView bagView = new ImageView(bag);
		bagView.setPreserveRatio(true);
		bagView.setFitWidth(60);

		Button bagButton = new Button("Check Bag", bagView);
		bagButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		bagButton.setContentDisplay(ContentDisplay.BOTTOM);
		bagButton.setMinSize(width / 2 - 85, height / 4 - 70);
		bagButton.setFont(new Font("Arial Black", 20));
		bagButton.setOnAction(e -> {
			// Check Bag
			try {
				toBagScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		bagButton.setOnMouseEntered(e -> {
			bagView.setEffect(glow);
		});
		bagButton.setOnMouseExited(e -> {
			bagView.setEffect(nullGlow);
		});

		// Shop Button
		Image coin = new Image(getClass().getResource("/resources/coin.png").toURI().toString());
		ImageView coinView = new ImageView(coin);
		coinView.setPreserveRatio(true);
		coinView.setFitWidth(60);

		Button shopButton = new Button("Shop", coinView);
		shopButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		shopButton.setContentDisplay(ContentDisplay.BOTTOM);
		shopButton.setMinSize(width / 2 - 85, height / 4 - 70);
		shopButton.setFont(new Font("Arial Black", 20));
		shopButton.setOnAction(e -> {
			// Shop
			try {
				toShopScene(stage, false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		shopButton.setOnMouseEntered(e -> {
			coinView.setEffect(glow);
		});
		shopButton.setOnMouseExited(e -> {
			coinView.setEffect(nullGlow);
		});

		// Settings Button
		Image settings = new Image(getClass().getResource("/resources/settings3.png").toURI().toString());
		ImageView settingsView = new ImageView(settings);
		settingsView.setPreserveRatio(true);
		settingsView.setFitWidth(30);
		Button settingsButton = new Button(null, settingsView);
		VBox.setMargin(settingsButton, new Insets(0, 0, 0, 0));
		settingsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		settingsButton.setOnAction(e -> {
			toSettingsScene(stage, false);
		});
		settingsButton.setOnMouseEntered(e -> {
			settingsButton.setEffect(glow);
		});
		settingsButton.setOnMouseExited(e -> {
			settingsButton.setEffect(nullGlow);
		});
		Button exitButton = new Button("Save and Exit");
		exitButton.setFont(new Font("Arial Black", 14));
		exitButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		exitButton.setMinSize(width / 8, height / 14 - 10);
		exitButton.setOnAction(e -> {
			// Save and Quit
			saveData();
			stage.close();
		});
		exitButton.setOnMouseEntered(e -> {
			exitButton.setFont(new Font("Arial Black", 16));
		});
		exitButton.setOnMouseExited(e -> {
			exitButton.setFont(new Font("Arial Black", 14));
		});

		// Centering and adding buttons
		GridPane.setHalignment(fightButton, HPos.CENTER);
		GridPane.setHalignment(leaderButton, HPos.CENTER);
		GridPane.setHalignment(bagButton, HPos.CENTER);
		GridPane.setHalignment(shopButton, HPos.CENTER);
		GridPane.setHalignment(settingsButton, HPos.CENTER);
		GridPane.setHalignment(exitButton, HPos.CENTER);

		// Column, Row
		bottomPane.add(fightButton, 0, 0);
		bottomPane.add(leaderButton, 1, 0);
		bottomPane.add(bagButton, 0, 1);
		bottomPane.add(shopButton, 1, 1);
		bottomPane.add(settingsButton, 0, 2);
		bottomPane.add(exitButton, 1, 2);

		// Adding top and bottom panes to rootPane (GridPane)
		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);

		// Adding rootPane to Scene
		Scene newScene = new Scene(rootPane, width, height);
		stage.setScene(newScene);
	}

	// Fighting
	public void toFightScene(Stage stage) throws Exception {

		// Creatures
		Creature enemyCreature = Creature.createRandomCreature(player);
		enemyCreature.setLevel(5 + ((player.getTownNumber() - 1) * 2) + (int) (Math.random() * player.getTownNumber()));

		Creature playerCreature = bag.getCreatures().get(0);

		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(46, 165, 102), CornerRadii.EMPTY, Insets.EMPTY)));

		BorderPane topPane = new BorderPane();

		GridPane bottomPane = new GridPane();

		// Top Pane Setup
		GridPane textPane = new GridPane();
		textPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		textPane.getRowConstraints().add(fullRowConstraint);
		textPane.getColumnConstraints().add(fullColConstraint);

		Label text = new Label();
		text.setFont(new Font("Arial Black", 15));
		textPane.add(text, 0, 0);
		textPane.setPrefHeight(50);
		GridPane.setHalignment(text, HPos.LEFT);
		GridPane.setValignment(text, VPos.TOP);

		topPane.setBottom(textPane);

		GridPane healthStatus = new GridPane();
		topPane.setTop(healthStatus);
		healthStatus.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);
		healthStatus.getRowConstraints().add(fullRowConstraint);

		// Player Creature Info
		VBox playerCreatureInfo = new VBox();
		Label playerCreatureName = new Label(playerCreature.getName() + " lvl. " + playerCreature.getLevel());
		playerCreatureName.setFont(new Font("Arial Black", 16));
		Label playerCreatureHealth = new Label(
				"Health: " + playerCreature.getHealth() + "/" + playerCreature.getMaxHealth());
		playerCreatureHealth.setFont(new Font("Arial Black", 16));
		playerCreatureInfo.getChildren().addAll(playerCreatureName, playerCreatureHealth);
		healthStatus.add(playerCreatureInfo, 0, 0);

		// Enemy Creature Info
		VBox enemyCreatureInfo = new VBox();
		enemyCreatureInfo.setAlignment(Pos.TOP_RIGHT);
		Label enemyCreatureName = new Label(enemyCreature.getName() + " lvl. " + enemyCreature.getLevel());
		enemyCreatureName.setFont(new Font("Arial Black", 16));
		Label enemyCreatureHealth = new Label(
				"Health: " + enemyCreature.getHealth() + "/" + enemyCreature.getMaxHealth());
		enemyCreatureHealth.setFont(new Font("Arial Black", 16));
		enemyCreatureInfo.getChildren().addAll(enemyCreatureName, enemyCreatureHealth);
		healthStatus.add(enemyCreatureInfo, 1, 0);

		this.playerCreatureName = playerCreatureName;
		this.enemyCreatureName = enemyCreatureName;

		// Player Image Pane
		StackPane playerPane = new StackPane();
		topPane.setLeft(playerPane);
		ImageView playerCreatureImage = playerCreature.getGraphic();
		// Width > height constrict width
		if (playerCreatureImage.prefWidth(-1) > playerCreatureImage.prefHeight(-1)) {
			playerCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			playerCreatureImage.setFitHeight(200);
		}
		playerCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(playerCreatureImage, Pos.CENTER);
		playerPane.getChildren().add(playerCreatureImage);

		StackPane enemyPane = new StackPane();
		topPane.setRight(enemyPane);
		ImageView enemyCreatureImage = enemyCreature.getGraphic();
		// Width > height constrict width
		if (enemyCreatureImage.prefWidth(-1) > enemyCreatureImage.prefHeight(-1)) {
			enemyCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			enemyCreatureImage.setFitHeight(200);
		}
		enemyCreatureImage.setScaleX(-1);
		enemyCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(enemyCreatureImage, Pos.CENTER);
		enemyPane.getChildren().add(enemyCreatureImage);

		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);

		// False because not gym battle
		playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, true, enemyCreatureHealth,
				playerCreatureHealth, false);

		Scene scene = new Scene(rootPane, width, height);
		stage.setScene(scene);
	}

	public void playerTurn(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, boolean displayText, Label enemyCreatureHealth, Label playerCreatureHealth,
			boolean gymBattle) {

		if (playerCreature.getHealth() == 0) {

			// Delete Image
			topPane.getLeft().setVisible(false);

			continueFight(stage, topPane, bottomPane, text, playerCreature, enemyCreature, false, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
			bag.removeCreature(playerCreature);
			return;
		}

		clearGridPane(bottomPane);
		bottomPane.setDisable(false);

		if (displayText) {
			text.setText("");
		}

		updateLabel(text, "What will " + playerCreature.getName() + " do?", 1, this.textSpeed);

		// Format Bottom Pane
		bottomPane.getRowConstraints().addAll(halfRowConstraint, halfRowConstraint);
		bottomPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		Button moveButton = new Button("Moves");
		moveButton.setFont(new Font("Arial Black", 20));
		moveButton.setOnAction(e -> {
			moveSelected(stage, topPane, bottomPane, text, playerCreature, enemyCreature, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(moveButton, HPos.CENTER);
		Button runButton = new Button("Run");
		runButton.setFont(new Font("Arial Black", 20));
		runButton.setOnAction(e -> {
			runSelected(stage, topPane, bottomPane, text, playerCreature, enemyCreature, enemyCreatureHealth,
					playerCreatureHealth);
		});
		if (gymBattle) {
			runButton.setDisable(true);
		}
		GridPane.setHalignment(runButton, HPos.CENTER);
		Button itemsButton = new Button("Items");
		itemsButton.setFont(new Font("Arial Black", 20));
		itemsButton.setOnAction(e -> {
			itemsSelected(stage, topPane, bottomPane, text, playerCreature, enemyCreature, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(itemsButton, HPos.CENTER);
		Button switchButton = new Button("Switch");
		switchButton.setFont(new Font("Arial Black", 20));
		switchButton.setOnAction(e -> {
			switchSelected(stage, topPane, bottomPane, text, playerCreature, enemyCreature, enemyCreatureHealth,
					playerCreatureHealth, true, gymBattle);
		});
		GridPane.setHalignment(switchButton, HPos.CENTER);

		colorStartButtons(playerCreature, moveButton, runButton, switchButton, itemsButton);

		bottomPane.add(moveButton, 0, 0);
		bottomPane.add(runButton, 1, 0);
		bottomPane.add(switchButton, 0, 1);
		bottomPane.add(itemsButton, 1, 1);

	}

	public void colorStartButtons(Creature playerCreature, Button... buttons) {

		switch (playerCreature.getType()) {

		case "Elemental":
			for (Button e : buttons) {
				e.setStyle("-fx-background-color: rgba(153, 255, 166, 100);");
			}
			break;
		case "Primitive":
			for (Button e : buttons) {
				e.setStyle("-fx-background-color: rgba(255, 196, 89, 100);");
			}
			break;
		case "Technological":
			for (Button e : buttons) {
				e.setStyle("-fx-background-color: rgba(115, 199, 244, 100);");
			}
			break;

		}

	}

	public void moveSelected(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth, boolean gymBattle) {

		// re-format bottom pane
		clearGridPane(bottomPane);

		bottomPane.getRowConstraints().addAll(majorRowConstraint, majorRowConstraint, minorRowConstraint);
		bottomPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		Button move1 = new Button();
		move1.setFont(new Font("Arial Black", 16));
		move1.setText(playerCreature.getMoves()[0].getName());
		colorMoveButton(playerCreature.getMoves()[0], move1);
		move1.setOnAction(e -> {
			attack(stage, topPane, bottomPane, text, playerCreature, enemyCreature, playerCreature.getMoves()[0],
					enemyCreatureHealth, playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(move1, HPos.CENTER);
		Button move2 = new Button();
		move2.setFont(new Font("Arial Black", 16));
		move2.setText(playerCreature.getMoves()[1].getName());
		colorMoveButton(playerCreature.getMoves()[1], move2);
		move2.setOnAction(e -> {
			attack(stage, topPane, bottomPane, text, playerCreature, enemyCreature, playerCreature.getMoves()[1],
					enemyCreatureHealth, playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(move2, HPos.CENTER);
		Button move3 = new Button();
		move3.setFont(new Font("Arial Black", 16));
		move3.setText(playerCreature.getMoves()[2].getName());
		colorMoveButton(playerCreature.getMoves()[2], move3);
		move3.setOnAction(e -> {
			attack(stage, topPane, bottomPane, text, playerCreature, enemyCreature, playerCreature.getMoves()[2],
					enemyCreatureHealth, playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(move3, HPos.CENTER);
		Button move4 = new Button();
		move4.setFont(new Font("Arial Black", 16));
		move4.setText(playerCreature.getMoves()[3].getName());
		colorMoveButton(playerCreature.getMoves()[3], move4);
		move4.setOnAction(e -> {
			attack(stage, topPane, bottomPane, text, playerCreature, enemyCreature, playerCreature.getMoves()[3],
					enemyCreatureHealth, playerCreatureHealth, gymBattle);
		});
		GridPane.setHalignment(move4, HPos.CENTER);
		Button back = new Button("Back");
		back.setFont(new Font("Arial Black", 12));
		back.setStyle("-fx-background-color: rgba(167, 186, 185, 100);");
		back.setOnAction(e -> {
			clearGridPane(bottomPane);
			playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, false, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
		});

		bottomPane.add(move1, 0, 0);
		bottomPane.add(move2, 1, 0);
		bottomPane.add(move3, 0, 1);
		bottomPane.add(move4, 1, 1);
		bottomPane.add(back, 0, 2);

		// Blur if unable to attack
		if (playerCreature.getMoves()[0].getStaminaDrain() > playerCreature.getStamina()) {
			move1.setDisable(true);
		} else {
			move1.setDisable(false);
		}
		if (playerCreature.getMoves()[1].getStaminaDrain() > playerCreature.getStamina()) {
			move2.setDisable(true);
		} else {
			move2.setDisable(false);
		}
		if (playerCreature.getMoves()[2].getStaminaDrain() > playerCreature.getStamina()) {
			move3.setDisable(true);
		} else {
			move3.setDisable(false);
		}
		if (playerCreature.getMoves()[3].getStaminaDrain() > playerCreature.getStamina()) {
			move4.setDisable(true);
		} else {
			move4.setDisable(false);
		}

		// If all moves disabled, empty attack
		if (move1.isDisabled() && move2.isDisabled() && move3.isDisabled() && move4.isDisabled()) {
			attack(stage, topPane, bottomPane, text, playerCreature, enemyCreature, new Move(), enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
		}

	}

	public void colorMoveButton(Move move, Button button) {

		switch (move.getTier()) {
		case 1:
			button.setStyle("-fx-background-color: rgba(144, 255, 122, 100);");
			break;
		case 2:
			button.setStyle("-fx-background-color: rgba(102, 221, 255, 100);");
			break;
		case 3:
			button.setStyle("-fx-background-color: rgba(219, 76, 96, 100);");
			break;
		}

	}

	// [Damage, Critical Hit, Enough Stamina, Missed/Hit]
	private void attack(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Move usedMove, Label enemyCreatureHealth, Label playerCreatureHealth,
			boolean gymBattle) {

		Object[] attackInfo = playerCreature.attack(usedMove, enemyCreature);

		// Overwrite attack outcome for Helpless Wail
		if (usedMove.getName().equals("Helpless Wail")) {
			attackInfo[0] = 0;
			attackInfo[1] = false;
			attackInfo[2] = true;
			attackInfo[3] = true;
		}

		clearGridPane(bottomPane);

		text.setText("");
		updateLabel(text, playerCreature.getName() + " used " + usedMove.getName() + "!", 1, this.textSpeed);

		int delay1 = 0;

		// Hit
		if ((boolean) (attackInfo[3])) {

			// Critical Hit
			if ((boolean) (attackInfo[1])) {

				delay1 = textSpeed * 2;

				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					public void run() {
						Platform.runLater(new Runnable() {
							public void run() {
								try {
									updateLabel(text, "It was a critical hit!", 1, textSpeed);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}

				}, textSpeed * 2);

			}

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								enemyCreatureHealth.setText(
										"Health: " + enemyCreature.getHealth() + "/" + enemyCreature.getMaxHealth());
								updateLabel(text, "It did " + attackInfo[0] + " damage!", 1, textSpeed);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 2 + delay1);

		} else {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								updateLabel(text, "But it missed!", 1, textSpeed);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 2);

		}

		// Start of enemy turn
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							enemyTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature,
									enemyCreatureHealth, playerCreatureHealth, gymBattle);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 4 + delay1);

	}

	// Run button clicked
	public void runSelected(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth) {

		// bottomPane.setDisable(true);
		clearGridPane(bottomPane);

		updateLabel(text, playerCreature.getName() + " attempted to run...", 1, textSpeed);

		if ((int) (Math.random() * 2) == 0) {

			displayDelayedText(text, textSpeed, textSpeed * 2, playerCreature.getName() + " got away safely!");

			Timer timer3 = new Timer();
			timer3.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {

								toMainScene(stage, false);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 4);

		} else {

			displayDelayedText(text, textSpeed, textSpeed * 2, playerCreature.getName() + " was unable to flee!");

			Timer timer3 = new Timer();
			timer3.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {

								enemyTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature,
										enemyCreatureHealth, playerCreatureHealth, false);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 4);

		}

	}

	public void itemsSelected(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth, boolean gymBattle) {
		clearGridPane(bottomPane);

		bottomPane.getRowConstraints().addAll(halfRowConstraint, halfRowConstraint);
		bottomPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		VBox selectionBox = new VBox(50);
		selectionBox.setAlignment(Pos.CENTER);

		GridPane itemPane = new GridPane();
		itemPane.getRowConstraints().add(fullRowConstraint);
		itemPane.getColumnConstraints().add(fullColConstraint);

		GridPane itemText = new GridPane();
		itemText.getRowConstraints().add(fullRowConstraint);
		itemText.getColumnConstraints().add(fullColConstraint);

		// Add Items combobox
		ComboBox<String> items = new ComboBox<>(FXCollections.observableList(bag.getItemNames()));
		formatCBox(items);
		items.setOnAction(e -> {

			Item selectedItem = bag.getItems().get(items.getSelectionModel().getSelectedIndex());
			ImageView selectedItemImage = new ImageView(selectedItem.getGraphic());
			GridPane.setHalignment(selectedItemImage, HPos.CENTER);
			GridPane.setValignment(selectedItemImage, VPos.CENTER);
			selectedItemImage.setPreserveRatio(true);

			if (selectedItem.getGraphic().getHeight() < selectedItem.getGraphic().getWidth()) {
				selectedItemImage.setFitHeight(width / 4);
			} else {
				selectedItemImage.setFitWidth(height / 6);
			}

			// Update Image
			itemPane.getChildren().clear();
			itemPane.add(selectedItemImage, 0, 0);

			itemText.getChildren().clear();

			// Update Text
			Label label = new Label("Description: \n" + selectedItem.getDescription());
			label.setAlignment(Pos.TOP_CENTER);
			label.setFont(new Font("Arial Black", 14));
			label.setWrapText(true);
			itemText.add(label, 0, 0);

		});

		// Add use button
		Button use = new Button("Use");
		use.setFont(new Font("Arial Black", 14));
		use.setStyle("-fx-background-color: rgba(88, 151, 252, 100);");

		items.getSelectionModel().select(0);

		use.setOnAction(e -> {

			if (gymBattle && bag.getItems().get(items.getSelectionModel().getSelectedIndex()) instanceof Capsule) {

				displayDelayedText(text, textSpeed, 0, "You can't use a capsule during a gym battle!");
				clearGridPane(bottomPane);

				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					public void run() {
						Platform.runLater(new Runnable() {
							public void run() {
								try {

									playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, true,
											enemyCreatureHealth, playerCreatureHealth, gymBattle);

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}

				}, textSpeed * 2);

				return;
			}

			// If something is selected
			if (items.getSelectionModel().getSelectedItem() != null) {

				Item selectedItem = bag.getItems().get(items.getSelectionModel().getSelectedIndex());

				clearGridPane(bottomPane);

				String capsuleText = player.getName() + " used a " + selectedItem.getClass().getSimpleName() + " on "
						+ enemyCreature.getName();
				String notCapsuleText = player.getName() + " used a " + selectedItem.getClass().getSimpleName() + " on "
						+ playerCreature.getName();
				int capsuleCounter = 0;

				selectedItem.use(playerCreature, bag);

				switch (selectedItem.getClass().getSimpleName()) {

				case "Banana":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Fruit) selectedItem).getHealthIncrement() + " health");

					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									try {

										// Update the health label
										playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
												+ playerCreature.getMaxHealth());

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

					}, textSpeed * 3);

					break;
				case "ChickenSoup":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Soup) selectedItem).getHealthIncrement() + " health");
					timer = new Timer();
					timer.schedule(new TimerTask() {

						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									try {

										// Update the health label
										playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
												+ playerCreature.getMaxHealth());

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

					}, textSpeed * 3);
					break;
				case "CleanCapsule":

					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, textSpeed * 2, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, textSpeed * 5, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}

					break;
				case "DirtyCapsule":
					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, textSpeed * 2, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, textSpeed * 5, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}
					break;
				case "ElementalCapsule":
					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, 2 * textSpeed, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, 5000, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}
					break;
				case "Gatorade":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Beverage) selectedItem).getStaminaIncrement() + " stamina");
					playerCreature.increaseStamina(((Beverage) selectedItem).getStaminaIncrement());
					break;
				case "Lemonade":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Beverage) selectedItem).getStaminaIncrement() + " stamina");
					playerCreature.increaseStamina(((Beverage) selectedItem).getStaminaIncrement());
					break;
				case "Pineapple":
					if (((Pineapple) selectedItem).isPositive()) {
						displayDelayedText(text, textSpeed, 0, notCapsuleText,
								"It restored " + ((Pineapple) selectedItem).getHealthIncrement() + " health!");
						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											// Update the health label
											playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
													+ playerCreature.getMaxHealth());

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 3);
					} else {
						displayDelayedText(text, textSpeed, 0, notCapsuleText,
								playerCreature.getName() + " got hurt by the spikes!", playerCreature.getName()
										+ "'s health was reduced by " + ((Pineapple) selectedItem).healthIncrement);
						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
													+ playerCreature.getMaxHealth());

											if (playerCreature.getHealth() > 0) {
												enemyTurn(stage, topPane, bottomPane, text, playerCreature,
														enemyCreature, enemyCreatureHealth, playerCreatureHealth,
														gymBattle);
											} else {
												// Deletes image
												topPane.getLeft().setVisible(false);
												continueFight(stage, topPane, bottomPane, text, playerCreature,
														enemyCreature, false, enemyCreatureHealth, playerCreatureHealth,
														gymBattle);
												bag.removeCreature(playerCreature);
											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 6);
						return;
					}

					break;
				case "PotatoSoup":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Soup) selectedItem).getHealthIncrement() + " health");
					timer = new Timer();
					timer.schedule(new TimerTask() {

						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									try {

										// Update the health label
										playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
												+ playerCreature.getMaxHealth());

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

					}, textSpeed * 3);
					break;
				case "PrimitiveCapsule":
					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, textSpeed * 2, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, 5000, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}
					break;
				case "PristineCapsule":
					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, textSpeed * 2, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, textSpeed * 5, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}
					break;
				case "RamenSoup":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Soup) selectedItem).getHealthIncrement() + " health");
					timer = new Timer();
					timer.schedule(new TimerTask() {

						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									try {

										// Update the health label
										playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
												+ playerCreature.getMaxHealth());

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

					}, textSpeed * 3);
					break;
				case "Starfruit":

					if (((Starfruit) selectedItem).isPositive()) {
						displayDelayedText(text, textSpeed, 0, notCapsuleText,
								"It restored " + playerCreature.getName() + "'s health completely!");
						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											// Update the health label
											playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
													+ playerCreature.getMaxHealth());

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 3);
					} else {
						displayDelayedText(text, textSpeed, 0, notCapsuleText,
								"Unfortunately he died shortly after eating it");
						// enemyWon(topPane, bottomPane, text, playerCreature, enemyCreature);
						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											// Deletes image
											topPane.getLeft().setVisible(false);

											// Must check if you can switch
											continueFight(stage, topPane, bottomPane, text, playerCreature,
													enemyCreature, false, enemyCreatureHealth, playerCreatureHealth,
													gymBattle);

											bag.removeCreature(playerCreature);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 4);
						return;
					}

					break;
				case "StrangeDrink":
					if (((StrangeDrink) selectedItem).isPositive()) {
						displayDelayedText(text, textSpeed, 0, notCapsuleText,
								"It maxed out " + playerCreature.getName() + "'s stamina!");

					} else {
						displayDelayedText(text, textSpeed, 0, notCapsuleText, playerCreature.getName()
								+ "'s health was reduced by " + ((StrangeDrink) selectedItem).getHealthDecrement());
						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/"
													+ playerCreature.getMaxHealth());

											if (playerCreature.getHealth() > 0) {
												enemyTurn(stage, topPane, bottomPane, text, playerCreature,
														enemyCreature, enemyCreatureHealth, playerCreatureHealth,
														gymBattle);
											} else {
												// Deletes image
												topPane.getLeft().setVisible(false);
												continueFight(stage, topPane, bottomPane, text, playerCreature,
														enemyCreature, false, enemyCreatureHealth, playerCreatureHealth,
														gymBattle);
												bag.removeCreature(playerCreature);
											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 4);
						return;
					}

					break;
				case "TechCapsule":
					capsuleCounter = textSpeed * 3;

					displayDelayedText(text, textSpeed, 0, capsuleText);

					displayDelayedText(text, textSpeed / 2, textSpeed * 2, "One...", "Two...", "Three...");
					if (((Capsule) selectedItem).didCatch()) {
						displayDelayedText(text, textSpeed, textSpeed * 5, enemyCreature.getName() + " was caught!");
						bag.addCreature(enemyCreature);

						timer = new Timer();
						timer.schedule(new TimerTask() {

							public void run() {
								Platform.runLater(new Runnable() {
									public void run() {
										try {

											toMainScene(stage, false);

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}

						}, textSpeed * 7);

						return;
					} else {
						displayDelayedText(text, textSpeed, textSpeed * 5,
								enemyCreature.getName() + " broke out of the capsule!");
					}
					break;
				case "Water":
					displayDelayedText(text, textSpeed, 0, notCapsuleText,
							"It restored " + ((Beverage) selectedItem).getStaminaIncrement() + " stamina");
					playerCreature.increaseStamina(((Beverage) selectedItem).getStaminaIncrement());
					break;

				}

				Timer timer3 = new Timer();
				timer3.schedule(new TimerTask() {

					public void run() {
						Platform.runLater(new Runnable() {
							public void run() {
								try {

									enemyTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature,
											enemyCreatureHealth, playerCreatureHealth, gymBattle);

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}

				}, textSpeed * 4 + capsuleCounter);

			}

		});

		// Back Button
		Button back = new Button("Back");
		back.setFont(new Font("Arial Black", 14));
		back.setStyle("-fx-background-color: rgba(88, 151, 252, 100);");
		back.setOnAction(e -> {
			playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, false, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);

		});

		selectionBox.getChildren().addAll(use, back);
		selectionBox.setAlignment(Pos.TOP_CENTER);
		VBox itemsBox = new VBox();
		itemsBox.getChildren().add(items);
		itemsBox.setAlignment(Pos.CENTER);

		bottomPane.add(itemText, 0, 1);
		bottomPane.add(itemPane, 0, 0);
		bottomPane.add(itemsBox, 1, 0);
		bottomPane.add(selectionBox, 1, 1);
	}

	public void switchSelected(Stage stage, BorderPane topPane, GridPane bottomPane, Label text,
			Creature playerCreature, Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth,
			boolean backable, boolean gymBattle) {
		// Format bottom pane
		clearGridPane(bottomPane);

		bottomPane.getRowConstraints().add(fullRowConstraint);
		bottomPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		VBox selectionBox = new VBox(50);
		VBox statBox = new VBox(5);
		statBox.setAlignment(Pos.CENTER);

		bottomPane.add(statBox, 0, 0);
		bottomPane.add(selectionBox, 1, 0);

		selectionBox.setAlignment(Pos.CENTER);

		ComboBox<String> creatures = new ComboBox<>(FXCollections.observableList(bag.getCreatureNames()));
		creatures.getSelectionModel().select(0);
		formatCBox(creatures);
		creatures.setOnAction(e -> {

			Creature selectedCreature = bag.getCreatures().get(creatures.getSelectionModel().getSelectedIndex());

			statBox.getChildren().clear();
			statBox.getChildren().add(new Label("Name: " + selectedCreature.getName()));
			statBox.getChildren().add(new Label("Type: " + selectedCreature.getType()));
			statBox.getChildren().add(new Label("Level: " + selectedCreature.getLevel()));
			statBox.getChildren()
					.add(new Label("Health: " + selectedCreature.getHealth() + "/" + selectedCreature.getMaxHealth()));
			statBox.getChildren().add(new Label("Stamina: " + selectedCreature.getStamina()));
			statBox.getChildren()
					.add(new Label(selectedCreature.getMoves()[0].getName() + ":\t"
							+ selectedCreature.getMoves()[0].getMoveAttack() + "/"
							+ selectedCreature.getMoves()[0].getAccuracy()));
			statBox.getChildren()
					.add(new Label(selectedCreature.getMoves()[1].getName() + ":\t"
							+ selectedCreature.getMoves()[1].getMoveAttack() + "/"
							+ selectedCreature.getMoves()[1].getAccuracy()));
			statBox.getChildren()
					.add(new Label(selectedCreature.getMoves()[2].getName() + ":\t"
							+ selectedCreature.getMoves()[2].getMoveAttack() + "/"
							+ selectedCreature.getMoves()[2].getAccuracy()));
			statBox.getChildren()
					.add(new Label(selectedCreature.getMoves()[3].getName() + ":\t"
							+ selectedCreature.getMoves()[3].getMoveAttack() + "/"
							+ selectedCreature.getMoves()[3].getAccuracy()));

		});

		Button switchButton = new Button("Switch");
		switchButton.setFont(new Font("Arial Black", 14));
		switchButton.setStyle("-fx-background-color: rgba(1, 219, 212, 100);");
		switchButton.setOnAction(e -> {

			// Cannot switch current creature in
			if (bag.getCreatures().get(creatures.getSelectionModel().getSelectedIndex()).equals(playerCreature)) {
				statBox.getChildren().clear();
				statBox.getChildren().add(new Label(playerCreature.getName() + " is already fighting!"));
			} else {

				swap(stage, topPane, bottomPane, text,
						bag.getCreatures().get(creatures.getSelectionModel().getSelectedIndex()), enemyCreature,
						enemyCreatureHealth, playerCreatureHealth, gymBattle);
			}

		});

		Button backButton = new Button("Back");
		backButton.setFont(new Font("Arial Black", 14));
		backButton.setStyle("-fx-background-color: rgba(1, 219, 212, 100);");
		backButton.setOnAction(e -> {
			playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, false, enemyCreatureHealth,
					playerCreatureHealth, gymBattle);
		});

		selectionBox.getChildren().addAll(creatures, switchButton);
		if (backable) {
			selectionBox.getChildren().add(backButton);

		} else {

			// remove creature from combobox
			creatures.getItems().remove(bag.getCreatures().indexOf(playerCreature));

			// Select first option
			creatures.getSelectionModel().select(0);

		}

	}

	public void swap(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth, boolean gymBattle) {

		// Carry out the action of swapping the creatures
		topPane.setLeft(null);

		StackPane imagePane = new StackPane();

		ImageView playerCreatureImage = playerCreature.getGraphic();
		// Width > height constrict width
		if (playerCreatureImage.prefWidth(-1) > playerCreatureImage.prefHeight(-1)) {
			playerCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			playerCreatureImage.setFitHeight(200);
		}
		playerCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(playerCreatureImage, Pos.CENTER);

		imagePane.getChildren().add(playerCreatureImage);

		topPane.setLeft(imagePane);
		playerCreatureHealth.setText("Health: " + playerCreature.getHealth() + "/" + playerCreature.getMaxHealth());

		this.playerCreatureName.setText(playerCreature.getName() + " lvl." + playerCreature.getLevel());

		displayDelayedText(text, textSpeed, 0, "Lets go, " + playerCreature.getName() + "!");

		clearGridPane(bottomPane);

		Timer timer3 = new Timer();
		timer3.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {

							enemyTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature,
									enemyCreatureHealth, playerCreatureHealth, gymBattle);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 3);

	}

	public void swapEnemyCreature(Stage stage, BorderPane topPane, GridPane bottomPane, Label text,
			Creature playerCreature, Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth,
			boolean gymBattle) {

		topPane.setRight(null);
		StackPane imagePane = new StackPane();

		ImageView enemyCreatureImage = enemyCreature.getGraphic();
		enemyCreatureImage.setScaleX(-1);

		enemyCreatureHealth.setText("Health: " + enemyCreature.getHealth() + "/" + enemyCreature.getMaxHealth());

		this.enemyCreatureName.setText(enemyCreature.getName() + " lvl." + enemyCreature.getLevel());

		displayDelayedText(text, textSpeed, 0, enemyCreature.getName() + " was sent out!");

		if (enemyCreatureImage.prefWidth(-1) > enemyCreatureImage.prefHeight(-1)) {
			enemyCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			enemyCreatureImage.setFitHeight(200);
		}
		enemyCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(enemyCreatureImage, Pos.CENTER);

		imagePane.getChildren().add(enemyCreatureImage);
		topPane.setRight(imagePane);

		Timer timer3 = new Timer();
		timer3.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {

							playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, true,
									enemyCreatureHealth, playerCreatureHealth, gymBattle);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 3);

	}

	// [Damage, Critical Hit, Enough Stamina, Missed/Hit]
	public void enemyTurn(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, Label enemyCreatureHealth, Label playerCreatureHealth, boolean gymBattle) {

		// Only used for gym battles
		int creatureIndex = player.getTownNumber();
		for (int i = 0; i < player.getTownCreatures()[player.getTownNumber() - 1].length; i++) {
			if (player.getTownCreatures()[player.getTownNumber() - 1][i] == enemyCreature) {
				creatureIndex = i;
			}
		}

		// Regular fight in forest win
		if (enemyCreature.getHealth() == 0 && !gymBattle) {
			playerWon(stage, topPane, bottomPane, text, playerCreature, enemyCreature);
			return;
		}

		// Gym battle
		if (enemyCreature.getHealth() == 0 && gymBattle) {

			// Creatures left
			if (creatureIndex < player.getTownNumber()) {

				// Need to swap creatures
				swapEnemyCreature(stage, topPane, bottomPane, text, playerCreature,
						player.getTownCreatures()[player.getTownNumber() - 1][creatureIndex + 1], enemyCreatureHealth,
						playerCreatureHealth, gymBattle);

				topPane.setRight(null);
				StackPane imagePane = new StackPane();

				ImageView enemyCreatureImage = player.getTownCreatures()[player.getTownNumber() - 1][creatureIndex + 1]
						.getGraphic();

				if (enemyCreatureImage.prefWidth(-1) > enemyCreatureImage.prefHeight(-1)) {
					enemyCreatureImage.setFitWidth(width / 2 - 10);
				} else {
					enemyCreatureImage.setFitHeight(200);
				}
				enemyCreatureImage.setPreserveRatio(true);
				StackPane.setAlignment(enemyCreatureImage, Pos.CENTER);

				imagePane.getChildren().add(enemyCreatureImage);
				topPane.setRight(imagePane);

				return;
			} else {

				// Go to next town
				player.incrementTownNumber();
				try {
					displayDelayedText(text, textSpeed, 0, "You have been promoted to the next town!");

					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									try {
										toMainScene(stage, false);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

					}, textSpeed * 2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}

		}

		Move usedMove = enemyCreature.getMoves()[(int) (Math.random() * 4)];
		Object[] attackInfo = enemyCreature.attack(usedMove, playerCreature);

		updateLabel(text, enemyCreature.getName() + " used " + usedMove.getName() + "!", 1, textSpeed);

		int delay1 = 0;

		// Hit
		if ((boolean) attackInfo[3]) {

			// Crit Hit
			if ((boolean) attackInfo[1]) {

				delay1 = textSpeed * 2;

				displayDelayedText(text, textSpeed, textSpeed * 2, "It was a critical hit!");

			}

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								playerCreatureHealth.setText(
										"Health: " + playerCreature.getHealth() + "/" + playerCreature.getMaxHealth());
								updateLabel(text, "It did " + attackInfo[0] + " damage!", 1, textSpeed);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 2 + delay1);

		} else {

			displayDelayedText(text, textSpeed, textSpeed * 2, "But it missed!");

		}

		// Start of player turn
		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, true,
									enemyCreatureHealth, playerCreatureHealth, gymBattle);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 5 + delay1);

	}

	// Defeat Enemy Creature
	public void playerWon(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature) {

		clearGridPane(bottomPane);

		playerCreature.setHealth(playerCreature.getMaxHealth());

		int reward = ((int) (Math.random() * 2) + 2) * 50 + (enemyCreature.getLevel() - 5) * 50;
		int experience = (enemyCreature.getLevel() - 4) * 200;

		updateLabel(text, enemyCreature.getName() + " has met their maker!", 1, textSpeed);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {

							updateLabel(text, "You gained " + reward + " coins", 1, textSpeed);
							player.deposit(reward);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 2);

		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {

							updateLabel(text, playerCreature.getName() + " gained " + experience + " experience!", 1,
									textSpeed);
							playerCreature.incrementXp(experience);

							// Silently add in Merge Points
							playerCreature.incrementMergePoints((int) (Math.random() * 4 + 1));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 4);

		if (playerCreature.getXp() >= playerCreature.getXpCeil()) {
			playerCreature.levelUp();

			Timer timer4 = new Timer();
			timer4.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {

								updateLabel(text, playerCreature.getName() + " leveled up to level "
										+ playerCreature.getLevel() + "!", 1, textSpeed);
								playerCreature.incrementXp(experience);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, textSpeed * 4);

		}

		Timer timer3 = new Timer();
		timer3.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {

							toMainScene(stage, false);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, textSpeed * 7);

	}

	public void gameOver(Stage stage) {

		// Clear data
		try {
			PrintWriter writer = new PrintWriter(new File("data.txt"));
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		GridPane pane = new GridPane();

		Image title = null;

		try {
			title = new Image(getClass().getResource("/resources/gameOver.gif").toURI().toString());
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ImageView titleView = new ImageView(title);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(60);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(40);

		pane.getRowConstraints().addAll(r1, r2);
		pane.getColumnConstraints().add(fullColConstraint);

		pane.add(titleView, 0, 0);

		Button homeButton = new Button("Home Screen");
		homeButton.setStyle("-fx-background-color: rgba(66, 244, 241, 100);");
		homeButton.setOnAction(e -> {

			try {
				audio.stop();
				toHomeScreen(stage, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		pane.add(homeButton, 0, 1);

		GridPane.setHalignment(homeButton, HPos.CENTER);
		GridPane.setValignment(homeButton, VPos.CENTER);
		GridPane.setHalignment(titleView, HPos.CENTER);
		GridPane.setValignment(titleView, VPos.CENTER);

		pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.show();

	}

	public void continueFight(Stage stage, BorderPane topPane, GridPane bottomPane, Label text, Creature playerCreature,
			Creature enemyCreature, boolean displayText, Label enemyCreatureHealth, Label playerCreatureHealth,
			boolean gymBattle) {

		if (bag.getCreatures().size() != 1) {

			displayDelayedText(text, textSpeed, 0, "Select creature");
			switchSelected(stage, topPane, bottomPane, text, playerCreature, enemyCreature, enemyCreatureHealth,
					playerCreatureHealth, false, gymBattle);

		} else {

			// no creatures left
			gameOver(stage);
		}

	}

	public void clearGridPane(GridPane pane) {
		pane.getColumnConstraints().clear();
		pane.getRowConstraints().clear();
		pane.getChildren().clear();
	}

	// Displays text
	public void displayDelayedText(Label text, int delay, int startingTime, String... message) {

		for (int i = 0; i < message.length; i++) {

			final int iFinal = i;

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {

								updateLabel(text, message[iFinal], 1, delay);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, 2 * delay * i + startingTime);

		}

	}

	// Slowly adds text to label given time in milliseconds
	public void updateLabel(Label label, String message, int counter, int milliseconds) {

		if (label.getText().equals(message)) {
			return;
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						label.setText(message.substring(0, counter));
						updateLabel(label, message, counter + 1, milliseconds);
					}
				});
			}

		}, milliseconds / message.length());

	}

	public void toBagScene(Stage stage) throws Exception {
		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(233, 216, 255), CornerRadii.EMPTY, Insets.EMPTY)));

		// Top Pane
		GridPane topPane = new GridPane();
		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		topPane.getRowConstraints().add(fullRowConstraint);
		topPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);

		Label left = new Label("Choose a Creat");
		GridPane.setHalignment(left, HPos.RIGHT);
		left.setFont(new Font("Times New Roman", 30));
		Label right = new Label("ure or an Item");
		GridPane.setHalignment(right, HPos.LEFT);
		right.setFont(new Font("Times New Roman", 30));
		topPane.add(left, 0, 0);
		topPane.add(right, 1, 0);

		// Bottom Pane
		GridPane bottomPane = new GridPane();
		bottomPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		bottomPane.getRowConstraints().addAll(majorRowConstraint, majorRowConstraint, minorRowConstraint);
		bottomPane.getColumnConstraints().add(fullColConstraint);

		// Combo Boxes (Initialized before to allow ComboBoxs to reference each other)
		ComboBox<String> creatures = new ComboBox<>(FXCollections.observableList(bag.getCreatureNames()));
		ComboBox<String> items = new ComboBox<>(FXCollections.observableList(bag.getItemNames()));
		formatCBox(creatures);
		formatCBox(items);
		// Selecting Creatures
		VBox selectCreatures = new VBox();
		selectCreatures.setAlignment(Pos.TOP_CENTER);
		// Adds creature names in bag to combobox
		creatures.setValue("Size: " + bag.getCreatures().size());

		/* Future Cell Factory Reserach */

		// Displays ImageView & stats for selected creature
		creatures.setOnAction(e -> {

			topPane.getChildren().clear();

			// Creature selected in ComboBox
			Creature selectedCreature = bag.getCreatures().get(creatures.getSelectionModel().getSelectedIndex());

			// Image of Creature
			ImageView iv = selectedCreature.getGraphic();
			iv.setScaleX(1);

			iv.setPreserveRatio(true);
			iv.setFitWidth(width / 3 + 60);
			GridPane.setHalignment(iv, HPos.CENTER);
			GridPane.setValignment(iv, VPos.CENTER);
			topPane.add(iv, 0, 0);

			// Stats of Creature
			VBox stats = new VBox();
			GridPane.setHalignment(stats, HPos.RIGHT);
			GridPane.setValignment(stats, VPos.TOP);

			Label intro = new Label(selectedCreature.getName() + " lvl." + selectedCreature.getLevel());
			intro.setFont(new Font("Arial Black", 20));
			Label buildInfo = new Label(selectedCreature.getType() + " " + selectedCreature.getInstanceType());
			buildInfo.setFont(new Font("Arial Black", 12));
			Label health = new Label("Health: " + selectedCreature.getHealth() + "/" + selectedCreature.getMaxHealth());
			health.setFont(new Font("Arial Black", 14));
			Label stamina = new Label(
					"Stamina: " + selectedCreature.getStamina() + "/" + selectedCreature.getStaminaCeil());
			stamina.setFont(new Font("Arial Black", 14));
			Label xp = new Label("Experience: " + selectedCreature.getXp() + "/" + selectedCreature.getXpCeil());
			xp.setFont(new Font("Arial Black", 14));
			Label mergePoints = new Label(
					"Merge Points: " + selectedCreature.getMergePoints() + "/" + selectedCreature.getMaxMergePoints());
			mergePoints.setFont(new Font("Arial Black", 14));
			Label attack = new Label("Attack: " + selectedCreature.getAttack());
			attack.setFont(new Font("Arial Black", 14));
			Label defense = new Label("Defense: " + selectedCreature.getDefense());
			defense.setFont(new Font("Arial Black", 14));
			Label speed = new Label("Speed: " + selectedCreature.getSpeed());
			speed.setFont(new Font("Arial Black", 14));
			Label critChance = new Label("Crit Chance: " + selectedCreature.getCritChance());
			critChance.setFont(new Font("Arial Black", 14));
			Label moveIntro = new Label("Moves: (Attack, Accuracy)");
			moveIntro.setFont(new Font("Arial Black", 15));
			Label move1 = new Label(
					selectedCreature.getMoves()[0].getName() + " (" + selectedCreature.getMoves()[0].getMoveAttack()
							+ ", " + selectedCreature.getMoves()[0].getAccuracy() + ")");
			move1.setFont(new Font("Arial Black", 13));
			Label move2 = new Label(
					selectedCreature.getMoves()[1].getName() + " (" + selectedCreature.getMoves()[1].getMoveAttack()
							+ ", " + selectedCreature.getMoves()[1].getAccuracy() + ")");
			move2.setFont(new Font("Arial Black", 13));
			Label move3 = new Label(
					selectedCreature.getMoves()[2].getName() + " (" + selectedCreature.getMoves()[2].getMoveAttack()
							+ ", " + selectedCreature.getMoves()[2].getAccuracy() + ")");
			move3.setFont(new Font("Arial Black", 13));
			Label move4 = new Label(
					selectedCreature.getMoves()[3].getName() + " (" + selectedCreature.getMoves()[3].getMoveAttack()
							+ ", " + selectedCreature.getMoves()[3].getAccuracy() + ")");
			move4.setFont(new Font("Arial Black", 13));
			Label space = new Label("");
			space.setFont(new Font("Arial Black", 4));
			Label space2 = new Label("");
			space2.setFont(new Font("Arial Black", 4));

			// Adding labels to VBox
			stats.getChildren().addAll(intro, buildInfo, space, health, stamina, xp, mergePoints, attack, defense,
					speed, critChance, space2, moveIntro, move1, move2, move3, move4);

			// Add HBox to topPane
			topPane.add(stats, 1, 0);

			// Resets (Must reset to allow user to select same Object twice)
			creatures.setOnMouseEntered(e2 -> {
				creatures.setValue("Size: " + bag.getCreatures().size());
				items.setValue("Size: " + bag.getItems().size());
			});

		});

		// Add Creature Label
		Label creatureLabel = new Label("Creatures");
		creatureLabel.setFont(new Font("Arial Black", 24));
		selectCreatures.getChildren().addAll(creatureLabel, creatures);
		bottomPane.add(selectCreatures, 0, 0);

		// Selecting Items
		VBox selectItems = new VBox();
		selectItems.setAlignment(Pos.TOP_CENTER);
		items.setValue("Size: " + bag.getItems().size());
		items.setOnAction(e -> {
			topPane.getChildren().clear();

			Item selectedItem = bag.getItems().get(items.getSelectionModel().getSelectedIndex());
			ImageView iv = new ImageView(selectedItem.getGraphic());
			iv.setPreserveRatio(true);
			iv.setFitWidth(width / 3 + 60);
			GridPane.setHalignment(iv, HPos.CENTER);
			GridPane.setValignment(iv, VPos.CENTER);
			topPane.add(iv, 0, 0);

			// Info
			VBox info = new VBox();
			GridPane.setHalignment(info, HPos.RIGHT);
			GridPane.setValignment(info, VPos.TOP);

			Label name = new Label(selectedItem.getName());
			name.setFont(new Font("Arial Black", 20));
			Label description = new Label("Description:");
			description.setFont(new Font("Arial Black", 16));
			Label descripted = new Label(selectedItem.getDescription());
			descripted.setFont(new Font("Arial Black", 14));
			descripted.setWrapText(true);

			// Adding labels to HBox
			info.getChildren().addAll(name, description, descripted);

			// Add HBox to topPane
			topPane.add(info, 1, 0);

			// Resets
			items.setOnMouseEntered(e2 -> {
				creatures.setValue("Size: " + bag.getCreatures().size());
				items.setValue("Size: " + bag.getItems().size());
				;
			});

		});

		// Add Items Label
		Label itemsLabel = new Label("Items:");
		itemsLabel.setFont(new Font("Arial Black", 24));
		selectItems.getChildren().addAll(itemsLabel, items);
		bottomPane.add(selectItems, 0, 1);

		// Back/Merge Buttons Area
		GridPane bottomBox = new GridPane();
		bottomBox.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);
		bottomBox.getRowConstraints().addAll(fullRowConstraint);

		// Back Button
		Image backImage = new Image(getClass().getResource("/resources/back.png").toURI().toString());
		ImageView backImageView = new ImageView(backImage);
		Button back = new Button(null, backImageView);
		backImageView.setPreserveRatio(true);
		back.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		back.setMinSize(width / 4 - 30, 35);
		back.setOnAction(e -> {
			try {
				toMainScene(stage, false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		back.setOnMouseEntered(e -> {
			back.setEffect(glow);
		});
		back.setOnMouseExited(e -> {
			back.setEffect(nullGlow);
		});
		bottomBox.add(back, 0, 0);
		GridPane.setHalignment(back, HPos.LEFT);
		GridPane.setValignment(back, VPos.BOTTOM);

		// toMerge Button
		Image alterImage = new Image(getClass().getResource("/resources/alter.png").toURI().toString());
		ImageView alterImageView = new ImageView(alterImage);
		Button alter = new Button(null, alterImageView);
		alterImageView.setPreserveRatio(true);
		alter.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		if (bag.getCreatures().size() <= 1) {
			alter.setDisable(true);
		}
		alter.setMinSize(width / 4 - 30, 35);
		alter.setOnAction(e -> {
			try {
				toMergeScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		alter.setOnMouseEntered(e -> {
			alter.setEffect(glow);
		});
		alter.setOnMouseExited(e -> {
			alter.setEffect(nullGlow);
		});
		bottomBox.add(alter, 1, 0);
		GridPane.setHalignment(alter, HPos.RIGHT);
		GridPane.setValignment(alter, VPos.BOTTOM);

		bottomPane.add(bottomBox, 0, 2);

		// Adding topPane and bottomPane to rootPane
		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);

		// Creating new scene with root and adding it to stage
		Scene scene = new Scene(rootPane, width, height);
		stage.setScene(scene);
		stage.show();
	}

	public void toMergeScene(Stage stage) throws Exception {

		// Back to home
		if (bag.getCreatures().size() <= 1) {
			try {
				toBagScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}

		// Format rootPane
		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(196, 158, 237), CornerRadii.EMPTY, Insets.EMPTY)));

		// Top Pane
		GridPane topPane = new GridPane();
		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		topPane.getRowConstraints().add(fullRowConstraint);
		topPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);
		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Bottom Pane
		GridPane bottomPane = new GridPane();
		bottomPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Left and Right Panes
		GridPane leftPane = new GridPane();
		leftPane.getColumnConstraints().add(fullColConstraint);
		leftPane.getRowConstraints().add(fullRowConstraint);

		GridPane rightPane = new GridPane();
		rightPane.getColumnConstraints().add(fullColConstraint);
		rightPane.getRowConstraints().add(fullRowConstraint);

		topPane.add(leftPane, 0, 0);
		topPane.add(rightPane, 1, 0);

		// Row/Column Constraints
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(90);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(10);
		ColumnConstraints cMajor = new ColumnConstraints();
		cMajor.setPercentWidth(40);
		ColumnConstraints cMinor = new ColumnConstraints();
		cMinor.setPercentWidth(20);

		// Add constraints to bottomPane
		bottomPane.getColumnConstraints().add(fullColConstraint);
		bottomPane.getRowConstraints().addAll(r1, r2);

		// Deals with ComboBoxes & Merge Button in Merge Scene
		VBox p1 = new VBox();

		// Format mergeOptions Pane
		GridPane mergeOptions = new GridPane();
		mergeOptions.getRowConstraints().add(fullRowConstraint);
		mergeOptions.getColumnConstraints().addAll(cMajor, cMinor, cMajor);

		// Arraylists and Comboboxes
		ArrayList<Creature> creature1AList = bag.getCreatures();
		ArrayList<Creature> creature2AList = bag.getCreatures();

		ComboBox<String> creature1 = new ComboBox<>(FXCollections.observableList(bag.getCreatureNames()));
		ComboBox<String> creature2 = new ComboBox<>(FXCollections.observableList(bag.getCreatureNames()));

		// Initial Left Creature
		creature1.getSelectionModel().selectFirst();
		ImageView c1 = bag.getCreatures().get(creature1.getSelectionModel().getSelectedIndex()).getGraphic();
		c1.setScaleX(1);
		c1.setPreserveRatio(true);
		c1.setFitWidth(width / 3 + 60);
		leftPane.add(c1, 0, 0);

		// Initial Right Creature
		creature2.getSelectionModel().select(1);
		ImageView c2 = bag.getCreatures().get(creature2.getSelectionModel().getSelectedIndex()).getGraphic();
		c2.setScaleX(-1);
		c2.setPreserveRatio(true);
		c2.setFitWidth(width / 3 + 60);
		rightPane.add(c2, 0, 0);

		// Animation Transitions
		TranslateTransition t1 = new TranslateTransition();
		t1.setDuration(Duration.seconds(2));
		t1.setNode(leftPane);
		t1.setToX(width / 3 - 45);
		t1.setAutoReverse(true);
		t1.setCycleCount(2);

		TranslateTransition t2 = new TranslateTransition();
		t2.setDuration(Duration.seconds(2));
		t2.setNode(rightPane);
		t2.setToX(-(width / 3 - 45));
		t2.setAutoReverse(true);
		t2.setCycleCount(2);

		FadeTransition f1 = new FadeTransition(Duration.seconds(2), leftPane);
		f1.setToValue(0);
		f1.setAutoReverse(true);
		f1.setCycleCount(2);

		FadeTransition f2 = new FadeTransition(Duration.seconds(2), rightPane);
		f2.setToValue(0);
		f2.setAutoReverse(true);
		f2.setCycleCount(2);

		Label creature1Warning = new Label(null);
		// Display Creature1
		creature1.setOnAction(e -> {

			// Remove Possible Warning
			creature1Warning.setText(null);

			// Two of same Creature can't be selected
			if (creature1.getSelectionModel().getSelectedItem().equals(creature2.getValue())) {
				if (creature1.getSelectionModel().getSelectedIndex() == creature1AList.size() - 1) {
					creature2.getSelectionModel().select(creature2.getSelectionModel().getSelectedIndex() - 1);
				} else {
					creature2.getSelectionModel().select(creature2.getSelectionModel().getSelectedIndex() + 1);
				}
			}

			Creature selectedCreature = bag.getCreatures().get(creature1.getSelectionModel().getSelectedIndex());
			ImageView iv = selectedCreature.getGraphic();
			// Restore Original Reflection (looking to right)
			iv.setScaleX(1);
			iv.setPreserveRatio(true);
			iv.setFitWidth(width / 3 + 60);

			// Resets graphic
			leftPane.getChildren().clear();
			leftPane.add(iv, 0, 0);
			GridPane.setHalignment(iv, HPos.CENTER);

		});
		creature1Warning.setFont(new Font("Times New Roman", 14));
		creature1Warning.setTextFill(Color.RED);
		VBox creature1Selection = new VBox();
		creature1Selection.setAlignment(Pos.CENTER);
		creature1Selection.getChildren().addAll(creature1, creature1Warning);
		Label creature2Warning = new Label(null);
		// Display Creature2
		creature2.setOnAction(e -> {

			// Reset Possible Warning
			creature2Warning.setText(null);

			// Same creature can't be selected in both boxes
			if (creature2.getSelectionModel().getSelectedItem().equals(creature1.getValue())) {
				if (creature2.getSelectionModel().getSelectedIndex() == creature2AList.size() - 1) {
					creature1.getSelectionModel().select(creature1.getSelectionModel().getSelectedIndex() - 1);
				} else {
					creature1.getSelectionModel().select(creature1.getSelectionModel().getSelectedIndex() + 1);
				}
			}

			Creature selectedCreature = bag.getCreatures().get(creature2.getSelectionModel().getSelectedIndex());
			ImageView iv = selectedCreature.getGraphic();
			// Reflect Image
			iv.setScaleX(-1);
			iv.setPreserveRatio(true);
			iv.setFitWidth(width / 3 + 60);

			// Resets graphic
			rightPane.getChildren().clear();
			rightPane.add(iv, 0, 0);
			GridPane.setHalignment(iv, HPos.CENTER);

		});
		creature2Warning.setFont(new Font("Times New Roman", 14));
		creature2Warning.setTextFill(Color.RED);
		VBox creature2Selection = new VBox();
		creature2Selection.setAlignment(Pos.CENTER);
		creature2Selection.getChildren().addAll(creature2, creature2Warning);
		Button mergeButton = new Button("Merge");
		// Merge Button Clicked
		mergeButton.setOnAction(e -> {
			Media mergeAudio = null;
			MediaPlayer mergeAudioPlayer = null;
			try {
				mergeAudio = new Media(getClass().getResource("/resources/mergeAudio.mp3").toURI().toString());
				mergeAudioPlayer = new MediaPlayer(mergeAudio);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			mergeAudioPlayer.setVolume(volume - .15);

			// Creature 1 Selected
			Creature selected1 = bag.getCreatures().get(creature1.getSelectionModel().getSelectedIndex());
			Creature selected2 = bag.getCreatures().get(creature2.getSelectionModel().getSelectedIndex());

			// Display warning if merge points arent full
			if (selected1.getMergePoints() < selected1.getMaxMergePoints()
					|| selected2.getMergePoints() < selected2.getMaxMergePoints()) {
				if (selected1.getMergePoints() < selected1.getMaxMergePoints()) {
					creature1Warning.setText("Can't Merge");
				}
				if (selected2.getMergePoints() < selected2.getMaxMergePoints()) {
					creature2Warning.setText("Can't Merge");
				}
				return;
			}

			// If both combo boxes are suitable
			if (creature1.getValue() != null) {

				// Don't restart town audio
				audio.pause();
				mergeAudioPlayer.play();

				Creature resultantCreature = Creature.merge(selected1, selected2);
				bag.addCreature(resultantCreature);

				// Remove Parents
				bag.removeCreature(selected1);
				bag.removeCreature(selected2);

				// Clear bottom pane
				bottomPane.getChildren().clear();
				Label mergeLabel = new Label("Merging " + creature1.getValue() + " and " + creature2.getValue());
				mergeLabel.setFont(new Font("Arial Black", 20));
				GridPane.setHalignment(mergeLabel, HPos.CENTER);
				GridPane.setValignment(mergeLabel, VPos.CENTER);
				GridPane.setRowSpan(mergeLabel, GridPane.REMAINING);
				GridPane.setColumnSpan(mergeLabel, GridPane.REMAINING);
				bottomPane.add(mergeLabel, 0, 0);

				// Merge Animation
				t1.play();
				f1.play();
				t2.play();
				f2.play();

				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					public void run() {
						Platform.runLater(new Runnable() {
							public void run() {
								showResultant(stage, topPane, bottomPane, resultantCreature);
							}
						});
					}

				}, 2000);

			}
		});
		Button switchButton = new Button("Switch");
		switchButton.setOnAction(e -> {
			// Display warning if no creatures selected
			if (creature1.getValue() == null) {
				creature1Warning.setText("Select Creature");
			}
			if (creature2.getValue() == null) {
				creature2Warning.setText("Select Creature");
			}

			bag.swap(bag.getCreatures().get(creature1.getSelectionModel().getSelectedIndex()),
					bag.getCreatures().get(creature2.getSelectionModel().getSelectedIndex()));

			try {
				toMergeScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		// Add button and label to pane
		p1.getChildren().addAll(mergeButton, switchButton);
		p1.setAlignment(Pos.CENTER);

		mergeOptions.add(creature1Selection, 0, 0);
		mergeOptions.add(p1, 1, 0);
		mergeOptions.add(creature2Selection, 2, 0);
		GridPane.setHalignment(creature1, HPos.CENTER);
		GridPane.setHalignment(p1, HPos.CENTER);
		GridPane.setHalignment(creature2, HPos.CENTER);

		// Add sections to bottomPane
		bottomPane.add(mergeOptions, 0, 0);

		// Back Button
		Image backImage = new Image(getClass().getResource("/resources/back.png").toURI().toString());
		ImageView backImageView = new ImageView(backImage);
		Button back = new Button(null, backImageView);
		backImageView.setPreserveRatio(true);
		back.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		back.setMinSize(width / 4 - 30, 35);
		back.setOnAction(e -> {

			// fix the image reflections
			for (int i = 0; i < bag.getCreatures().size(); i++) {
				if (bag.getCreatures().get(i).getGraphic().getScaleX() == -1) {
					bag.getCreatures().get(i).getGraphic().setScaleX(1);
				}
			}

			try {
				toBagScene(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		back.setOnMouseEntered(e -> {
			back.setEffect(glow);
		});
		back.setOnMouseExited(e -> {
			back.setEffect(nullGlow);
		});
		bottomPane.add(back, 0, 1);

		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);
		Scene scene = new Scene(rootPane, width, height);
		stage.setScene(scene);
		stage.show();
	}

	public void toShopScene(Stage stage, boolean restartMusic) throws Exception {

		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(247, 186, 86), CornerRadii.EMPTY, Insets.EMPTY)));

		// Format Top Pane
		GridPane topPane = new GridPane();
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(15);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(70);
		topPane.getRowConstraints().addAll(r1, r2, r1);
		topPane.getColumnConstraints().add(fullColConstraint);

		Label introLabel = new Label("Welcome to " + player.getTownName() + "'s Shop");
		introLabel.setFont(new Font("Arial Black", 20));
		GridPane.setHalignment(introLabel, HPos.CENTER);
		topPane.add(introLabel, 0, 0);

		GridPane itemPane = new GridPane();
		itemPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		itemPane.setAlignment(Pos.CENTER);
		itemPane.getRowConstraints().add(fullRowConstraint);
		itemPane.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);
		topPane.add(itemPane, 0, 1);

		HBox fiscalInfo = new HBox(width / 2 - 20);
		fiscalInfo.setAlignment(Pos.CENTER);
		Label currentCoins = new Label("Coins: " + player.getCoins());
		currentCoins.setFont(new Font("Arial Black", 20));
		Label cost = new Label("Cost: ");
		cost.setFont(new Font("Arial Black", 20));
		fiscalInfo.getChildren().addAll(currentCoins, cost);
		topPane.add(fiscalInfo, 0, 2);

		GridPane bottomPane = new GridPane();
		RowConstraints r3 = new RowConstraints();
		r3.setPercentHeight(40);
		RowConstraints r4 = new RowConstraints();
		r4.setPercentHeight(20);
		bottomPane.getRowConstraints().addAll(r3, r3, r4);
		bottomPane.getColumnConstraints().addAll(fullColConstraint);

		ComboBox<String> merch = new ComboBox<>(FXCollections.observableList(shop.getAvailableItemNames()));
		merch.setValue("Items");

		VBox selectItems = new VBox();
		selectItems.getChildren().addAll(new Label("Items:"), merch);
		selectItems.setAlignment(Pos.CENTER);

		merch.setOnAction(e -> {
			itemPane.getChildren().clear();
			Item selectedItem = shop.getAvailableItems().get(merch.getSelectionModel().getSelectedIndex());
			ImageView itemView = new ImageView(selectedItem.getGraphic());
			itemView.setPreserveRatio(true);
			itemView.setFitHeight((height / 2 * .7 - 60));
			GridPane.setHalignment(itemView, HPos.CENTER);
			GridPane.setValignment(itemView, VPos.CENTER);
			itemPane.add(itemView, 0, 0);
			fiscalInfo.getChildren().remove(1);
			cost.setText("Cost: " + selectedItem.getCost());
			fiscalInfo.getChildren().add(1, cost);

			Label descripted = new Label("Description:\n" + selectedItem.getDescription());
			descripted.setFont(new Font("Arial Black", 16));
			descripted.setWrapText(true);
			itemPane.add(descripted, 1, 0);
		});

		bottomPane.add(selectItems, 0, 0);

		Button buy = new Button("Buy");
		GridPane.setHalignment(buy, HPos.CENTER);
		buy.setOnAction(e -> {
			if (merch.getSelectionModel().getSelectedIndex() > -1 && player.canWithdraw(
					shop.getAvailableItems().get(merch.getSelectionModel().getSelectedIndex()).getCost())) {
				player.withdraw(shop.getAvailableItems().get(merch.getSelectionModel().getSelectedIndex()).getCost());
				// bag.addItem(shop.getAvailableItems().get(merch.getSelectionModel().getSelectedIndex()));

				switch (shop.getAvailableItems().get(merch.getSelectionModel().getSelectedIndex()).getClass()
						.getSimpleName()) {
				case "Banana":
					bag.addItem(new Banana());
					break;
				case "ChickenSoup":
					bag.addItem(new ChickenSoup());
					break;
				case "CleanCapsule":
					bag.addItem(new CleanCapsule());
					break;
				case "DirtyCapsule":
					bag.addItem(new DirtyCapsule());
					break;
				case "ElementalCapsule":
					bag.addItem(new ElementalCapsule());
					break;
				case "Gatorade":
					bag.addItem(new Gatorade());
					break;
				case "Lemonade":
					bag.addItem(new Lemonade());
					break;
				case "Pineapple":
					bag.addItem(new Pineapple());
					break;
				case "PotatoSoup":
					bag.addItem(new PotatoSoup());
					break;
				case "PrimitiveCapsule":
					bag.addItem(new PrimitiveCapsule());
					break;
				case "PristineCapsule":
					bag.addItem(new PristineCapsule());
					break;
				case "RamenSoup":
					bag.addItem(new RamenSoup());
					break;
				case "Starfruit":
					bag.addItem(new Starfruit());
					break;
				case "StrangeDrink":
					bag.addItem(new StrangeDrink());
					break;
				case "TechCapsule":
					bag.addItem(new TechCapsule());
					break;
				case "Water":
					bag.addItem(new Water());
					break;
				}

				fiscalInfo.getChildren().remove(0);
				currentCoins.setText("Coins: " + player.getCoins());
				fiscalInfo.getChildren().add(0, currentCoins);

			}
		});
		bottomPane.add(buy, 0, 1);

		Image backImage = new Image(getClass().getResource("/resources/back.png").toURI().toString());
		ImageView backImageView = new ImageView(backImage);
		Button back = new Button(null, backImageView);
		backImageView.setPreserveRatio(true);
		back.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		back.setMinSize(width / 4 - 30, 35);
		GridPane.setHalignment(back, HPos.LEFT);
		GridPane.setValignment(back, VPos.BOTTOM);
		back.setOnAction(e -> {
			try {
				toMainScene(stage, false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		back.setOnMouseEntered(e -> {
			back.setEffect(glow);
		});
		back.setOnMouseExited(e -> {
			back.setEffect(nullGlow);
		});
		bottomPane.add(back, 0, 2);

		bottomPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		GridPane.setHalignment(topPane, HPos.CENTER);
		GridPane.setHalignment(bottomPane, HPos.CENTER);

		// Adding topPane and bottomPane to rootPane
		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);

		// Creating new scene with root and adding it to stage
		Scene scene = new Scene(rootPane, width, height);
		stage.setScene(scene);

	}

	public void toBattleScene(Stage stage) throws Exception {

		// Creatures
		Creature enemyCreature = player.getTownCreatures()[player.getTownNumber() - 1][0];
		Creature playerCreature = bag.getCreatures().get(0);

		GridPane rootPane = new GridPane();
		format(rootPane);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(170, 83, 70), CornerRadii.EMPTY, Insets.EMPTY)));

		BorderPane topPane = new BorderPane();

		GridPane bottomPane = new GridPane();

		// Top Pane Setup
		GridPane textPane = new GridPane();
		textPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		textPane.getRowConstraints().add(fullRowConstraint);
		textPane.getColumnConstraints().add(fullColConstraint);

		Label text = new Label();
		text.setFont(new Font("Arial Black", 15));
		textPane.add(text, 0, 0);
		textPane.setPrefHeight(50);
		GridPane.setHalignment(text, HPos.LEFT);
		GridPane.setValignment(text, VPos.TOP);

		topPane.setBottom(textPane);

		GridPane healthStatus = new GridPane();
		topPane.setTop(healthStatus);
		healthStatus.getColumnConstraints().addAll(halfColConstraint, halfColConstraint);
		healthStatus.getRowConstraints().add(fullRowConstraint);

		// Player Creature Info
		VBox playerCreatureInfo = new VBox();
		Label playerCreatureName = new Label(playerCreature.getName() + " lvl. " + playerCreature.getLevel());
		playerCreatureName.setFont(new Font("Arial Black", 16));
		Label playerCreatureHealth = new Label(
				"Health: " + playerCreature.getHealth() + "/" + playerCreature.getMaxHealth());
		playerCreatureHealth.setFont(new Font("Arial Black", 16));
		playerCreatureInfo.getChildren().addAll(playerCreatureName, playerCreatureHealth);
		healthStatus.add(playerCreatureInfo, 0, 0);

		// Enemy Creature Info
		VBox enemyCreatureInfo = new VBox();
		enemyCreatureInfo.setAlignment(Pos.TOP_RIGHT);
		Label enemyCreatureName = new Label(enemyCreature.getName() + " lvl. " + enemyCreature.getLevel());
		enemyCreatureName.setFont(new Font("Arial Black", 16));
		Label enemyCreatureHealth = new Label(
				"Health: " + enemyCreature.getHealth() + "/" + enemyCreature.getMaxHealth());
		enemyCreatureHealth.setFont(new Font("Arial Black", 16));
		enemyCreatureInfo.getChildren().addAll(enemyCreatureName, enemyCreatureHealth);
		healthStatus.add(enemyCreatureInfo, 1, 0);

		this.playerCreatureName = playerCreatureName;
		this.enemyCreatureName = enemyCreatureName;

		// Player Image Pane
		StackPane playerPane = new StackPane();
		topPane.setLeft(playerPane);
		ImageView playerCreatureImage = playerCreature.getGraphic();
		// Width > height constrict width
		if (playerCreatureImage.prefWidth(-1) > playerCreatureImage.prefHeight(-1)) {
			playerCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			playerCreatureImage.setFitHeight(200);
		}
		playerCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(playerCreatureImage, Pos.CENTER);
		playerPane.getChildren().add(playerCreatureImage);

		StackPane enemyPane = new StackPane();
		topPane.setRight(enemyPane);
		ImageView enemyCreatureImage = enemyCreature.getGraphic();
		// Width > height constrict width
		if (enemyCreatureImage.prefWidth(-1) > enemyCreatureImage.prefHeight(-1)) {
			enemyCreatureImage.setFitWidth(width / 2 - 10);
		} else {
			enemyCreatureImage.setFitHeight(200);
		}
		enemyCreatureImage.setScaleX(-1);
		enemyCreatureImage.setPreserveRatio(true);
		StackPane.setAlignment(enemyCreatureImage, Pos.CENTER);
		enemyPane.getChildren().add(enemyCreatureImage);

		rootPane.add(topPane, 0, 0);
		rootPane.add(bottomPane, 0, 1);

		// Last arg true b/c gym battle
		playerTurn(stage, topPane, bottomPane, text, playerCreature, enemyCreature, true, enemyCreatureHealth,
				playerCreatureHealth, true);

		Scene scene = new Scene(rootPane, width, height);
		stage.setScene(scene);

	}

	public void toSettingsScene(Stage stage, boolean toHome) {
		GridPane rootPane = new GridPane();

		rootPane.setAlignment(Pos.CENTER);

		Label settingsLabel = new Label("Settings");
		settingsLabel.setFont(new Font("Arial Black", 25));

		VBox options = new VBox(30);
		options.setAlignment(Pos.CENTER);

		// Player Name
		HBox playerNameBox = new HBox(40);
		playerNameBox.setAlignment(Pos.CENTER);
		TextField nameField = new TextField();
		nameField.setText(player.getName());
		Button update = new Button("Update");
		playerNameBox.getChildren().addAll(nameField, update);
		update.setOnAction(e -> {
			player.setName(nameField.getText());
		});

		// Text Speed
		HBox textSpeedBox = new HBox(40);
		textSpeedBox.setAlignment(Pos.CENTER);
		Label textSpeedLabel = new Label("Text Speed:");
		textSpeedLabel.setFont(new Font("Arial Black", 18));
		Slider textSpeed = new Slider();
		textSpeed.setMin(250);
		textSpeed.setMax(2000);
		textSpeed.setValue(this.textSpeed / 1000.0);
		Label textSpeedValue = new Label(this.textSpeed / 1000.0 + "s");
		textSpeedValue.setFont(new Font("Arial Black", 15));
		Button setTextSpeed = new Button("Set Speed");
		textSpeed.setOnMouseDragged(e -> {

			textSpeedValue.setText(Math.round(textSpeed.getValue() / 1000 * 100.0) / 100.0 + "s");
			// Math.round(textSpeed.getValue()/1000*100.0)/100.0

		});
		textSpeedBox.getChildren().addAll(textSpeedLabel, textSpeed, textSpeedValue, setTextSpeed);
		setTextSpeed.setOnAction(e -> {
			this.textSpeed = (int) (textSpeed.getValue());
		});

		// Volume
		HBox volumeBox = new HBox(40);
		volumeBox.setAlignment(Pos.CENTER);
		Slider volume = new Slider();
		volume.setValue(Math.round(this.volume) * 100);
		volume.setMin(0);
		volume.setMax(100);
		Label volumeLabel = new Label("Volume: ");
		volumeLabel.setFont(new Font("Arial Black", 18));
		Label volumeValue = new Label(Math.round(this.volume) * 100 + "");
		volumeValue.setFont(new Font("Arial Black", 18));
		Button setVolume = new Button("Set Volume");
		volumeBox.getChildren().addAll(volumeLabel, volume, volumeValue, setVolume);

		volume.setOnMouseDragged(e -> {

			volumeValue.setText("" + Math.round(volume.getValue()));

		});

		setVolume.setOnAction(e -> {

			this.volume = volume.getValue() / 100;
			audio.setVolume(this.volume);

		});

		// Reset Button
		Button initResetButton = new Button("Reset Game");
		Button resetButton = new Button("Click to Reset");
		resetButton.setVisible(false);
		Label reset = new Label("Reset Game:");
		reset.setFont(new Font("Arial Black", 18));
		HBox resetBox = new HBox(40);
		resetBox.setAlignment(Pos.CENTER);
		resetBox.getChildren().addAll(reset, initResetButton, resetButton);
		initResetButton.setOnAction(e -> {

			resetButton.setVisible(true);
			initResetButton.setDisable(true);

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								resetButton.setVisible(false);
								initResetButton.setDisable(false);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}

			}, 3000);

		});
		resetButton.setOnAction(e -> {

			audio.stop();

			try {
				PrintWriter writer = new PrintWriter(new File("data.txt"));
				writer.print("");
				writer.close();
				toHomeScreen(stage, true);
			} catch (Exception error) {
				error.printStackTrace();
			}

		});

		// Back button
		Button back = new Button("Back");
		back.setOnAction(e -> {
			try {
				if (toHome) {
					toHomeScreen(stage, false);
				} else {
					toMainScene(stage, false);
				}

			} catch (Exception e1) {

			}
		});

		options.getChildren().addAll(settingsLabel, playerNameBox, textSpeedBox, volumeBox, resetBox, back,
				new Label("\n\n\n\n\n\n\n\n\n\n"));
		rootPane.add(options, 0, 0);
		Scene scene = new Scene(rootPane, width, height);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(80, 252, 229, .8), CornerRadii.EMPTY, Insets.EMPTY)));
		stage.setScene(scene);
		stage.show();
	}

	public void showResultant(Stage stage, GridPane topPane, GridPane bottomPane, Creature resultant) {

		// Reset TopPane
		topPane.getChildren().clear();

		topPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		topPane.getRowConstraints().add(fullRowConstraint);
		topPane.getColumnConstraints().add(fullColConstraint);
		ImageView iv = resultant.getGraphic();
		GridPane.setColumnSpan(iv, GridPane.REMAINING);
		GridPane.setRowSpan(iv, GridPane.REMAINING);
		GridPane.setHalignment(iv, HPos.CENTER);
		GridPane.setValignment(iv, VPos.CENTER);
		iv.setPreserveRatio(true);
		iv.setFitWidth(width / 3 + 60);

		FadeTransition f = new FadeTransition(Duration.seconds(2.5), iv);
		f.setFromValue(0);
		f.setToValue(1);
		f.setCycleCount(1);

		topPane.add(iv, 0, 0);
		f.play();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						GridPane.setColumnSpan(iv, 1);
						GridPane.setRowSpan(iv, 1);
						audio.play();
						try {
							toMergeScene(stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		}, 2500);

	}

	// Changes bottomPane to confirm new game
	public void newGameConfirmation(Stage stage, VBox bottomPane) throws Exception {

		bottomPane.getChildren().clear();

		bottomPane.setSpacing(40);
		Label label = new Label("Are you sure you want to start a new game?");
		label.setFont(new Font("Times New Roman", 24));
		label.setTextFill(Color.RED);

		HBox confirmation = new HBox(50);

		// No
		Image noButton = new Image(getClass().getResource("/resources/no.png").toURI().toString());
		ImageView noButtonView = new ImageView(noButton);
		Button no = new Button(null, noButtonView);
		final double noWidth = noButton.getWidth();
		noButtonView.setPreserveRatio(true);
		no.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		// Expand/contract button
		no.setOnMouseEntered(e -> {
			noButtonView.setFitWidth(noWidth + 5);
		});
		no.setOnMouseExited(e -> {
			noButtonView.setFitWidth(noWidth);
		});
		no.setOnAction(e -> {
			try {
				homeButtonSelection(stage, bottomPane);
			} catch (Exception e2) {

			}
		});

		Image yesButton = new Image(getClass().getResource("/resources/yes.png").toURI().toString());
		ImageView yesButtonView = new ImageView(yesButton);
		Button yes = new Button(null, yesButtonView);
		final double yesWidth = yesButton.getWidth();
		yesButtonView.setPreserveRatio(true);
		yes.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		// Expand/contract button
		yes.setOnMouseEntered(e -> {
			yesButtonView.setFitWidth(yesWidth + 5);
		});
		yes.setOnMouseExited(e -> {
			yesButtonView.setFitWidth(yesWidth);
		});
		yes.setOnAction(e -> {
			try {
				bag = new Bag();

				// Starting Creatures
				for (int i = 0; i < 6; i++) {
					Creature x = Creature.createRandomCreature(player);
					bag.addCreature(x);
					x.setMergePoints(x.getMaxMergePoints());
				}

				// Starting Items
				bag.addItem(new Banana());
				bag.addItem(new Pineapple());
				bag.addItem(new RamenSoup());
				bag.addItem(new Water());
				bag.addItem(new DirtyCapsule());
				bag.addItem(new Starfruit());

				PrintWriter writer = new PrintWriter(new File("data.txt"));
				writer.print("");
				writer.close();

				toMainScene(stage, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		confirmation.getChildren().addAll(yes, no);
		confirmation.setAlignment(Pos.CENTER);
		bottomPane.getChildren().addAll(label, confirmation);
	}

	// Carries out new game
	public void newGame(Stage stage) {
		// file io info to wipe text data

		try {
			toMainScene(stage, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void instructionsScene(Stage stage) {

		GridPane pane = new GridPane();

		RowConstraints rowMajor = new RowConstraints();
		rowMajor.setPercentHeight(95);
		RowConstraints rowMinor = new RowConstraints();
		rowMinor.setPercentHeight(5);

		pane.getRowConstraints().addAll(rowMajor, rowMinor);
		pane.getColumnConstraints().add(fullColConstraint);

		String message = "Advance through the towns in order to run into higher level Creatures in the forest. You win the game after beating the 5th town."
				+ "\n\nFight in Forest: Take your Creatures into the Forest, battling them against foreign Creatures. Winning gives you experience, money, and merge points."
				+ "\n\nBattle Town Leader: Fight against a group of difficult Creatures. Winning these battles allows you to advance to the next town."
				+ "\n\nShop: Buy new items."
				+ "\n\nCheck Bag: Find information on your caught Creatures and purchased Items, along with merging/switching Creatures."
				+ "\n\nIf a Creature loses a battle, it is permanently deleted. Use items wisely to prevent this from happening."
				+ "\n\nMerging 2 Creatures creates a new Creature with the best stats/moves from the parents. Creatures must have maxed merge points to merge."
				+ "\n\nTypes:\nPrimitive beats Elemental. \nElemental beats Tecnological. \nTechnological beats Primitive.";

		Label instructionsLabel = new Label(message);
		instructionsLabel.setFont(new Font("Arial Black", 14));
		instructionsLabel.setWrapText(true);

		Button back = new Button("Back");

		back.setOnAction(e -> {
			try {
				toHomeScreen(stage, false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		pane.add(instructionsLabel, 0, 0);
		pane.add(back, 0, 1);

		pane.setBackground(
				new Background(new BackgroundFill(Color.rgb(129, 175, 249), CornerRadii.EMPTY, Insets.EMPTY)));

		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.show();

	}

	public void endGameScene(Stage stage) {

		// Clear data
		try {
			PrintWriter writer = new PrintWriter(new File("data.txt"));
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		GridPane pane = new GridPane();

		Image title = null;

		try {
			title = new Image(getClass().getResource("/resources/finalImage.gif").toURI().toString());
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ImageView titleView = new ImageView(title);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(60);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(40);

		pane.getRowConstraints().addAll(r1, r2);
		pane.getColumnConstraints().add(fullColConstraint);

		pane.add(titleView, 0, 0);

		Button homeButton = new Button("End Game");
		homeButton.setStyle("-fx-background-color: rgba(66, 244, 241, 100);");
		homeButton.setOnAction(e -> {

			try {
				audio.stop();
				toHomeScreen(stage, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		pane.add(homeButton, 0, 1);

		GridPane.setHalignment(homeButton, HPos.CENTER);
		GridPane.setValignment(homeButton, VPos.CENTER);

		pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.show();
	}

	// Format a GridPane into 2 rows and 1 column
	public void format(GridPane rootPane) {
		rootPane.getRowConstraints().addAll(halfRowConstraint, halfRowConstraint);
		rootPane.getColumnConstraints().addAll(fullColConstraint);
	}

	// Formats combo boxes
	public void formatCBox(ComboBox<String> cbox) {

		cbox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> list) {

				return new ListCell<String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item);
							setFont(new Font("Arial Black", 14));
						}
					}
				};

			}

		});

	}

	public void saveData() {

		try {
			f = new FileOutputStream(new File("data.txt"));
			o = new ObjectOutputStream(f);

			// Writes bag, player, shop
			o.writeObject(bag);
			o.writeObject(player);
			o.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void retrieveData() {

		try {
			fIn = new FileInputStream(new File("data.txt"));
			oIn = new ObjectInputStream(fIn);

			bag = (Bag) oIn.readObject();
			player = (Player) oIn.readObject();

			for (int i = 0; i < bag.getCreatures().size(); i++) {
				bag.getCreatures().get(i).initializeImage();
			}

			for (int i = 0; i < bag.getItems().size(); i++) {
				bag.getItems().get(i).initializeImage();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch();

	}

}