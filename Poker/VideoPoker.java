/*
 * Andrea De La Cruz, Eric Barrow, Sacha Le Clainff Alonzo
 * Group Project
 * 4/23/2020
 * COSC 1174
 * 
 * This is a poker game that involves 5 cards. The payouts for specific hands are given at the top right of the screen.
 * At the beginning of the game, the player is asked to select the number of decks that will be used in the game.
 * Then the player must place a bet and deal themselves 5 cards. Once a hand has been dealt to the player, the bet amount is subtracted from the player's credits.
 * At this point, the player must choose which cards to discard and then hits draw to replace the discarded cards.
 * If no cards need to be discarded the player can just hit draw with no cards selected for discard. Once the game has gone through a deal and draw phase, the game tests
 * the player's hand to see if it is a winning hand. Depending on if a winning hand is found or not, the player is rewarded or not rewarded and can deal themselves another hand.
 * The game display a "Game Over" message if the player hits 0 credits.
 */

import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VideoPoker extends Application {
	
	private int credits = 200;							// keeps track of the player's current money
	private int bet = 0;								// keeps track of the player's current bet
	private int handrank = 0;							// keeps track of the rank of the player's current hand
	private int handsPlayed = 0;						// keeps track of how many hands have been played
	private int avatarSelected = 0;						// keeps track of which avatar is selected
	private int shuffleTracker = 0;						// keeps track of if the deck is being shuffled
	private boolean firstTheme = true;					// keeps track of if its the first time a theme has been applied
	private String[] cardImgString = new String[6];		// keeps track of image string for each card in the player's hand
	private Text handNames = new Text();				// creates text that displays all of the odds for winning hands

	private StackPane background = new StackPane();		// creates a background pane to place all other panes into
	
	//creates a deck of cards
	private static final Deck deck1 = new Deck();
	private static final Deck deck2 = new Deck(2);
	private static final Deck deck4 = new Deck(4);
	private Deck selectedDeck;

	//creates hand of cards
	private static final Card [] card = new Card[5];	

	//creates textboxes for bets
	private TextField pocketTextBox = new TextField("" + credits);
	private TextField betTextBox = new TextField("0");

	//creates all the card images
	private ImageView [] cardIMG = new ImageView[6];
	private ImageView [] cardBack = new ImageView[7];
	private Image cardBackIMG = new Image("card/b1fv.png");
	
	//creates all the avatar images
	private ImageView[] bigAvatars = new ImageView[6];
	private ImageView[] smallAvatars = new ImageView[6];

	//creates all the Buttons
	private Button btDEAL = new Button(" Deal ");
	private Button btDRAW = new Button(" Draw ");
	private Button btShuffle = new Button("Shuffle");
	private Button betOne = new Button("Bet 1");
	private Button betMax = new Button("Bet Max");
	private Button betReset = new Button ("Reset");
	private Button btNewGame = new Button ("New Game");
	private Button theme1 = new Button("Blue");
	private Button theme2 = new Button("Purp");
	private Button theme3 = new Button("Dark");

	//creates radio buttons for picking how many decks to play with and the toggle group to place them all in
	private ToggleGroup numDecks = new ToggleGroup();
	private RadioButton pickDeck1 = new RadioButton("1 Deck ");
	private RadioButton pickDeck2 = new RadioButton("2 Decks");
	private RadioButton pickDeck4 = new RadioButton("4 Decks");

	//creates path transitions for animating card movements and creating delays
	private PathTransition[] cardToBottom = new PathTransition[6];
	private PathTransition[] cardFromTop = new PathTransition[6];
	private PathTransition delay = new PathTransition();
	private PathTransition delay2 = new PathTransition();
	private PathTransition delay3 = new PathTransition();
	private PathTransition[] shuffle = new PathTransition[6];
	private PathTransition[] shuffle2 = new PathTransition[6];
	
	//creates text and a circle that displays different messages to the player or label a certain section
	private Circle bigWinCircle = new Circle (52);
	private Text bigWinText = new Text("BIG WIN");
	private Circle bigWinCircle2 = new Circle (52);
	private Text bigWinText2 = new Text("BIG WIN");
	private Text bigWords = new Text("Select the number of decks on the left. \n"
			+ "Then hit deal to begin.");
	private Label creditsLbl = new Label("Credits:");
	private Label betsLbl = new Label("Bet:");
	
	private Rectangle selector = new Rectangle(45,45);				// creates the rectangle that moves over the selected avatar
	private Button right = new Button("");							// creates the right arrow button for avatar selection
	private Button left = new Button("");							// creates the left arrow button for avatar selection
	private Text avatarText = new Text("Avatars");					// creates the text for the avatar selection area
	
	public void start(Stage primaryStage) {
		
		DropShadow shadow = new DropShadow();		// creates the shadows for everything
		
		GridPane board = new GridPane();			// creates a grid pane that most things will be placed into
		GridPane topGrid = new GridPane();			// creates the grid that holds the betting odds/payouts
		GridPane topMainGrid = new GridPane();		// creates the grid that holds the avatars, messages, and payouts
		GridPane messageGrid = new GridPane();		// creates the grid that holds the big wins animations and messages
		GridPane payoutGrid = new GridPane();		// creates the grid that holds the payout odds and theme selectors
		GridPane themeGrid = new GridPane();		// creates the grid that holds the theme buttons
		GridPane bigWordsGrid = new GridPane();		// creates the grid that displays messages
		GridPane cardGrid = new GridPane();			// creates the grid that holds the deck buttons and cards
		GridPane handGrid = new GridPane();			// creates the grid that holds the cards in the player's hand
		GridPane bottomGrid = new GridPane();		// creates the grid that holds the credits,bets,and deal/draw/shuffle buttons
		GridPane betsGrid = new GridPane();			// creates the grid that holds the bets and credits
		GridPane buttonsGrid = new GridPane();		// creates the grid that hold the deal/draw/shuffle/new game buttons
		GridPane deckGrid = new GridPane();			// creates the grid that holds the radio buttons for # of decks selection and the deck
		GridPane deckSelGrid = new GridPane();		// creates the gird that holds the radio buttons for # of decks selection
		GridPane betBtnGrid = new GridPane();		// creates the grid that holds the bet1, betMax, and reset buttons
		GridPane avatarGrid = new GridPane();		// creates the grid that holds the images of the big avatar, small avatars, and arrow buttons
		GridPane bigAvatarGrid = new GridPane();	// creates the grid that holds just the images of the big avatar
		GridPane avatarImageGrid = new GridPane();	// creates the grid that holds just the images for the small avatars
		GridPane avatarSelGrid = new GridPane();	// creates the grid that holds just the arrows for the small avatars selection
		StackPane bigWinPane = new StackPane();		// creates the stack pane that is used to stack the big win circle and big win text
		StackPane bigWinPane2 = new StackPane();	// creates the stack pane that is used to stack the big win circle and big win text
		
		/*
		 * Creates the 5 card image views that are the player's hand
		 * also initializes the cardImgString which holds the string for the path of the card's .png
		 */
		for (int i=1; i<=5; i++) {
			cardIMG[i] = new ImageView();
			GridPane.setHalignment(cardIMG[i], HPos.CENTER);
			cardIMG[i].setFitHeight(180);
			cardIMG[i].setFitWidth(130);
			card[i-1] = new Card();
			cardImgString[i] = "";
		}
		
		/*
		 * CARD PATH TRANSITIONS(ANIMATIONS) AND DELAY PATH TRANSITIONS(ANIMATIONS)
		 * initializes and sets up the animations for each card to be discarded and dealt a new card
		 * also sets up the delay animations that are used to delay the execution of code during certain scenarios
		 */
		for (int i=1; i<=5; i++) {
			cardToBottom[i] = new PathTransition(Duration.millis(375), new Line(75, 90, 75, 700), cardIMG[i]);
			cardFromTop[i]  = new PathTransition(Duration.millis(375 + (i * 100)), new Line(75, -600, 75, 90), cardIMG[i]);
		}
		delay.setDuration(Duration.seconds(1.5));
		delay.setNode(new Line(0,0,200,200));
		delay.setPath(new Line(0,0,100,100));
		delay2.setDuration(Duration.seconds(0.8));
		delay2.setNode(new Line(0,0,200,200));
		delay2.setPath(new Line(0,0,100,100));
		delay3.setDuration(Duration.seconds(0.6));
		delay3.setNode(new Line(0,0,200,200));
		delay3.setPath(new Line(0,0,100,100));
		
		/*
		 * AVATARS
		 * below are most variables used in the creation of the avatars and the animations involving the avatars
		 */
		Line[] avatarLineRight = new Line[5];					// creates the line used for the animation of moving the selector right
		Line[] avatarLineLeft = new Line[5];					// creates the line used for the animation of moving the selector left
		PathTransition[] sliderRight = new PathTransition[5];	// creates the animation for the selector moving right
		PathTransition[] sliderLeft = new PathTransition[5];	// creates the animation for the selector moving left
		/*
		 * creates the images for the avatars, sizes them, and places them in panes
		 */
		for(int i = 0; i < 6; i++) {
			smallAvatars[i] = new ImageView();
			bigAvatars[i] = new ImageView();
			smallAvatars[i].setImage(new Image("avatar/avatar-" + (i+1) + ".png"));
			bigAvatars[i].setImage(new Image("avatar/avatar-" + (i+1) + ".png"));
			avatarImageGrid.add(smallAvatars[i], i, 0);
			bigAvatarGrid.add(bigAvatars[i], 0, 0);
			smallAvatars[i].setFitHeight(40);
			smallAvatars[i].setFitWidth(40);
			bigAvatars[i].setFitHeight(150);
			bigAvatars[i].setFitWidth(150);
			bigAvatars[i].setVisible(false);
		}
		bigAvatars[0].setVisible(true);
		/*
		 * creates the path transitions for the rectangle that shifts over the selected avatar
		 */
		selector.setTranslateX(-629);
		selector.setTranslateY(-171);
		int nextStart = 0;
		int previousEnd = 240;
		for (int i = 0; i < 5; i++) {
			avatarLineRight[i] = new Line(nextStart,0,nextStart + 48,0);
			nextStart = nextStart + 48;
			avatarLineLeft[i] = new Line(previousEnd,0,previousEnd - 48,0);
			previousEnd = previousEnd - 48;
			avatarLineRight[i].setTranslateX(-605);
			avatarLineLeft[i].setTranslateX(-605);
			avatarLineRight[i].setTranslateY(-149);
			avatarLineLeft[i].setTranslateY(-149);
			sliderRight[i] = new PathTransition(Duration.seconds(.3), avatarLineRight[i], selector);
			sliderLeft[i] = new PathTransition(Duration.seconds(.3), avatarLineLeft[i], selector);
		}
		
		/*
		 * BIG WIN ANIMATION
		 * here are most of the variables and method calls for creating the big win animations
		 * the points and used in the polyline, which is used to create a path for the circles to run along over 5.5 seconds
		 * the animations are then set to play and are set to never end
		 * then a timer was created that changes the color of the circles to a random color every 500ms
		 */
		double [] points = {50, 75,
				680, 75,
				50, 75,};
		double [] points2 = {680, 28,
				50, 28,
				680, 28,};
		Polyline polyline = new Polyline(points);
		Polyline polyline2 = new Polyline(points2);
		PathTransition pt = new PathTransition();
		PathTransition pt2 = new PathTransition();
		pt.setNode(bigWinPane);
		pt.setDuration(Duration.seconds(5.5));
		pt.setPath(polyline);
		pt.setCycleCount(PathTransition.INDEFINITE);
		pt.play();
		pt2.setNode(bigWinPane2);
		pt2.setDuration(Duration.seconds(5.5));
		pt2.setPath(polyline2);
		pt2.setCycleCount(PathTransition.INDEFINITE);
		pt2.play();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {      	
				Color col = new Color(Math.random(), Math.random(), Math.random(), 0.9);
				bigWinCircle.setFill(col);
				bigWinCircle2.setFill(col);
			}
		}, 0, 500);
	
		/*
		 * Styling for text and buttons
		 */
		bigWinText.setFill(Color.PINK);
		bigWinText.setStroke(Color.BLACK);
		bigWinText2.setFill(Color.PINK);
		bigWinText2.setStroke(Color.BLACK);
		bigWinCircle.setStroke(Color.BLACK);
		bigWinCircle2.setStroke(Color.BLACK);
		bigWinText.setFont(Font.font("Consolas", 25));
		bigWinText2.setFont(Font.font("Consolas", 25));
		handNames.setFont(Font.font("Consolas", 14));
		creditsLbl.setFont(Font.font("Consolas", 20));
		betsLbl.setFont(Font.font("Consolas", 20));
		bigWords.setFont(Font.font("Consolas", 28));
		avatarText.setFont(Font.font("Consolas", 24));
		bigWords.setTextAlignment(TextAlignment.CENTER);
		btDEAL.setMinWidth(75);
		btDEAL.setMinHeight(40);
		btDRAW.setMinWidth(75);
		btDRAW.setMinHeight(40);
		btShuffle.setMinWidth(75);
		btShuffle.setMinHeight(40);
		btNewGame.setMinWidth(75);
		btNewGame.setMinHeight(40);
		pickDeck1.setSelected(true);
		pickDeck1.setToggleGroup(numDecks);
		pickDeck2.setToggleGroup(numDecks);
		pickDeck4.setToggleGroup(numDecks);
		shadow.setHeight(4);
		shadow.setWidth(4);
		shadow.setOffsetX(2); 
		shadow.setOffsetY(1);
		left.setRotate(270);
		right.setRotate(90);
		selector.setFill(null);
		handNames.setText("Royal Flush........250 x bet\n"
				+ "Straight Flush......50 x bet\n"
				+ "4 of a Kind.........25 x bet\n"
				+ "Full House...........9 x bet\n"
				+ "Flush................6 x bet\n"
				+ "Straight.............4 x bet\n"
				+ "3 of a Kind..........3 x bet\n"
				+ "2 Pair...............2 x bet\n"
				+ "Pair, Jacks+.........1 x bet\n");
		
		/*
		 * Styles the panes mainly by setting alignments and gaps inbetween objects in the panes
		 */
		background.setPadding(new Insets(20,40,20,40));
		board.setEffect(shadow);
		board.setAlignment(Pos.CENTER);
		board.setHgap(5);
		board.setVgap(5);
		topGrid.setAlignment(Pos.CENTER);
		topMainGrid.setAlignment(Pos.CENTER_LEFT);
		topMainGrid.setHgap(50);
		avatarImageGrid.setHgap(8);
		avatarImageGrid.setAlignment(Pos.CENTER);
		bigAvatarGrid.setAlignment(Pos.CENTER);
		avatarSelGrid.setHgap(20);
		avatarSelGrid.setAlignment(Pos.CENTER);
		avatarGrid.setVgap(10);
		avatarGrid.setAlignment(Pos.CENTER_LEFT);
		messageGrid.setAlignment(Pos.CENTER);
		messageGrid.setVgap(10);
		payoutGrid.setAlignment(Pos.CENTER);
		payoutGrid.setVgap(10);
		themeGrid.setAlignment(Pos.CENTER);
		themeGrid.setHgap(5);
		bigWordsGrid.setAlignment(Pos.CENTER);
		buttonsGrid.setAlignment(Pos.CENTER);
		buttonsGrid.setHgap(10);
		cardGrid.setAlignment(Pos.CENTER);
		cardGrid.setHgap(10);
		handGrid.setAlignment(Pos.CENTER_LEFT);
		handGrid.setHgap(8);
		bottomGrid.setAlignment(Pos.CENTER);
		bottomGrid.setHgap(15);
		bottomGrid.setVgap(10);
		betsGrid.setAlignment(Pos.CENTER);
		betsGrid.setHgap(15);
		betsGrid.setVgap(10);
		deckGrid.setAlignment(Pos.CENTER);
		deckGrid.setHgap(15);
		deckSelGrid.setAlignment(Pos.CENTER_LEFT);
		deckSelGrid.setVgap(8);
		betBtnGrid.setHgap(5);
		betBtnGrid.setAlignment(Pos.CENTER);
		
		/*
		 * Creates the imageviews and path transitions needed for displaying and shuffling the deck
		 */
		Line shuffleLine = new Line(0,0,200,0);
		Line shuffleLineBack = new Line (200,0,0,0);
		for (int i=0; i<7; i++) {
			cardBack[i] = new ImageView(cardBackIMG);
			cardBack[i].setFitHeight(180);
			cardBack[i].setFitWidth(130);
		}
		shuffleLine.setTranslateY(90);
		shuffleLine.setTranslateX(70);
		shuffleLineBack.setTranslateY(90);
		shuffleLineBack.setTranslateX(64);
		for (int i = 0; i < 6; i++) {
			shuffle[i] = new PathTransition(Duration.seconds((i+1)*0.1), shuffleLine, cardBack[5-i]);
			shuffle2[i] = new PathTransition(Duration.seconds((i+1)*0.1), shuffleLineBack, cardBack[i]);
		}
		
		/*
		 * Placing everything into panes and creates rectangles in main areas that can be set to visible to display the area they cover
		 */
		Rectangle top = new Rectangle(1400,320);		// rectangle displayed as a background to the avatars, messages, and payouts
		Rectangle cards = new Rectangle(1100,250);		// can set visible to see the area the cards grid covers
		Rectangle bottom = new Rectangle(1100,200);		// can set visible to see the area the bottom grid covers
		Rectangle messageRec = new Rectangle(700,130);	// can set visible to see the area the message grid covers
		Rectangle decksGridRec = new Rectangle(350,240);// can set visible to see the area the deck grid covers
		Rectangle handRec = new Rectangle(900,240);		// can set visible to see the area the hand grid covers
		top.getStyleClass().add("top-board");
		top.setStroke(Color.SILVER);
		top.setVisible(true);
		cards.setVisible(false);
		bottom.setVisible(false);
		messageRec.setVisible(false);
		decksGridRec.setVisible(false);
		handRec.setVisible(false);
		background.getChildren().addAll(board, selector);
		board.add(top, 0, 0);
		board.add(topGrid, 0, 0);
		board.add(cards, 0, 1);
		board.add(cardGrid, 0, 1);
		board.add(bottom, 0, 2);
		board.add(bottomGrid, 0, 2);
		bigWinPane.getChildren().addAll(bigWinCircle, bigWinText);
		bigWinPane2.getChildren().addAll(bigWinCircle2, bigWinText2);
		bigWinPane.setMaxWidth(100);
		bigWinPane2.setMaxWidth(100);
		topGrid.add(topMainGrid, 0, 0);
		topMainGrid.add(avatarGrid, 0, 0);
		topMainGrid.add(messageGrid, 1, 0);
		topMainGrid.add(payoutGrid, 2, 0);
		payoutGrid.add(handNames, 0, 0);
		payoutGrid.add(themeGrid, 0, 1);
		themeGrid.add(theme1, 0, 0);
		themeGrid.add(theme2, 1, 0);
		themeGrid.add(theme3, 2, 0);
		avatarGrid.add(bigAvatarGrid, 0, 0);
		avatarGrid.add(avatarImageGrid, 0, 1);
		avatarGrid.add(avatarSelGrid, 0, 2);
		avatarSelGrid.add(right,2,0);
		avatarSelGrid.add(avatarText, 1, 0);
		avatarSelGrid.add(left, 0, 0);
		messageGrid.add(bigWinPane, 0, 0);
		messageGrid.add(messageRec, 0, 1);
		messageGrid.add(bigWordsGrid, 0, 1);
		messageGrid.add(bigWinPane2, 0, 2);
		bigWordsGrid.add(bigWords, 0, 0);
		deckGrid.add(deckSelGrid, 0, 0);
		deckGrid.add(cardBack[0], 1, 0);
		deckGrid.add(cardBack[1], 1, 0);
		deckGrid.add(cardBack[2], 1, 0);
		deckGrid.add(cardBack[3], 1, 0);
		deckGrid.add(cardBack[4], 1, 0);
		deckGrid.add(cardBack[5], 1, 0);
		deckGrid.add(cardBack[6], 1, 0);
		deckSelGrid.add(pickDeck1, 0, 0);
		deckSelGrid.add(pickDeck2, 0, 1);
		deckSelGrid.add(pickDeck4, 0, 2);
		cardGrid.add(decksGridRec, 0, 0);
		cardGrid.add(deckGrid, 0, 0);
		cardGrid.add(handRec, 1, 0);
		cardGrid.add(handGrid, 1, 0);
		handGrid.add(cardIMG[1], 0, 0);
		handGrid.add(cardIMG[2], 1, 0);
		handGrid.add(cardIMG[3], 2, 0);
		handGrid.add(cardIMG[4], 3, 0);
		handGrid.add(cardIMG[5], 4, 0);
		buttonsGrid.add(btDRAW, 0, 0);
		buttonsGrid.add(btDEAL, 1, 0);
		buttonsGrid.add(btShuffle, 2, 0);
		buttonsGrid.add(btNewGame, 3, 0);
		bottomGrid.add(betsGrid, 0, 0);
		betsGrid.add(creditsLbl, 0, 0);
		betsGrid.add(pocketTextBox, 1, 0);
		betsGrid.add(betsLbl, 2, 0);
		betsGrid.add(betTextBox, 3, 0);
		betsGrid.add(buttonsGrid, 4, 0);
		betsGrid.add(betBtnGrid, 3, 1);
		betBtnGrid.add(betOne, 0, 0);
		betBtnGrid.add(betMax, 1, 0);
		betBtnGrid.add(betReset, 2, 0);		
		
		//sets all variables to default values as if starting a new game
		newGame();
		themeSelector(2);
		bigWinCircle.setVisible(false);
		bigWinText.setVisible(false);
		bigWinCircle2.setVisible(false);
		bigWinText2.setVisible(false);
		
		//******************************************
		//			START OF HANDLERS
		//******************************************
		/*
		 * WHEN A CARD IMAGE IS CLICKED:
		 * checks to see if the card's discard value is true or false
		 * if true:
		 * 		set the discard value to false and set the image back to the card's original image 
		 * if false:
		 * 		set the discard value to true and set the image to "back of card" image
		 */
		cardIMG[1].setOnMouseClicked(t -> {
			if (btDRAW.isDisable() == false) {
				if (card[0].discard == true) {
					card[0].discard = false;	
					cardIMG[1].setImage(new Image(cardImgString[1]));
				}
				else {
					card[0].discard = true;	
					cardIMG[1].setImage(cardBackIMG);
				}
			}
		});
		cardIMG[2].setOnMouseClicked(t -> {
			if (btDRAW.isDisabled() == false) {
				if (card[1].discard == true) {
					card[1].discard = false;	
					cardIMG[2].setImage(new Image(cardImgString[2]));
				}
				else {
					card[1].discard = true;
					cardIMG[2].setImage(cardBackIMG);
				}
			}
		});
		cardIMG[3].setOnMouseClicked(t -> {
			if (btDRAW.isDisabled() == false) {
				if (card[2].discard == true) {
					card[2].discard = false;	
					cardIMG[3].setImage(new Image(cardImgString[3]));
				}
				else {
					card[2].discard = true;	
					cardIMG[3].setImage(cardBackIMG);
				}
			}
		});
		cardIMG[4].setOnMouseClicked(t -> {
			if (btDRAW.isDisabled() == false) {
				if (card[3].discard == true) {
					card[3].discard = false;	
					cardIMG[4].setImage(new Image(cardImgString[4]));
				}
				else {
					card[3].discard = true;	
					cardIMG[4].setImage(cardBackIMG);
				}
			}
		});
		cardIMG[5].setOnMouseClicked(t -> {
			if (btDRAW.isDisabled() == false) {
				if (card[4].discard == true) {
					card[4].discard = false;	
					cardIMG[5].setImage(new Image(cardImgString[5]));
				}
				else {
					card[4].discard = true;	
					cardIMG[5].setImage(cardBackIMG);
				}
			}
		});
		
		/*
		 * WHEN A SMALL AVATAR IMAGE IS CLICKED:
		 * changes the location of the selector rectangle to be over the selected avatar
		 * hides all of the big avatar images by calling the hideAvatars method
		 * then sets the selected big avatar image visible and sets the avatarSelected variable to selected avatar
		 */
		smallAvatars[0].setOnMouseClicked(t -> {
			selector.setTranslateX(-629);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 0;
			bigAvatars[0].setVisible(true);
		});
		smallAvatars[1].setOnMouseClicked(t -> {
			selector.setTranslateX(-581);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 1;
			bigAvatars[1].setVisible(true);
		});
		smallAvatars[2].setOnMouseClicked(t -> {
			selector.setTranslateX(-533);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 2;
			bigAvatars[2].setVisible(true);
		});
		smallAvatars[3].setOnMouseClicked(t -> {
			selector.setTranslateX(-485);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 3;
			bigAvatars[3].setVisible(true);
		});
		smallAvatars[4].setOnMouseClicked(t -> {
			selector.setTranslateX(-437);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 4;
			bigAvatars[4].setVisible(true);
		});
		smallAvatars[5].setOnMouseClicked(t -> {
			selector.setTranslateX(-389);
			selector.setTranslateY(-171);
			hideAvatars();
			avatarSelected = 5;
			bigAvatars[5].setVisible(true);
		});
		
		/*
		 * BET BUTTON ACTIONS:
		 * bet1:
		 * 		checks to make sure that the bet is less than the player's current credits
		 * 		if yes, then increase the bet amount by 1 and update the text field
		 * 		if no, tell the player that the bet is at the max already
		 * betmax:
		 * 		sets the bet to the player's current credits and updates the text field
		 * betreset:
		 * 		sets the bet to 0 and updates the text field
		 */
		betOne.setOnAction(e -> {
			getBet();
			if (bet < credits) {
				bet = bet + 1;
				betTextBox.setText("" + (bet));
			}
			if (bet == credits) {
				bigWords.setText("At maximum bet");
				bigWords.setVisible(true);
			}
		});
		betMax.setOnAction(e -> {
			bet = credits;
			betTextBox.setText("" + (bet));
		});
		betReset.setOnAction(e -> {
			bet = 0;
			betTextBox.setText("" + (bet));
		});

		/*
		 * DRAW BUTTON ACTIONS:
		 * calls takeCards method, then plays a delay to give cards time to exit screen, then call giveCards method
		 * then plays a delay animation to give the new cards time to slide into position before checking for and displaying a win/loss
		 * finally, specific buttons are enabled/disabled accordingly by calling disable buttons method
		 */
		btDRAW.setOnAction(e -> {
			takeCards();
			delay2.setOnFinished(t -> {
				giveCards();
			});
			delay2.play();
			delay.setOnFinished(t -> {
				payout(Card.checkRank(card));
				printPayout(handrank);
				handrank = 0;
				disableButtons(btDRAW);
			});
			delay.play();
		});
		
		/*
		 * DEAL BUTTON ACTIONS:
		 * removes the big win animation from the screen if it was showing
		 * if it is the first hand of the game, uses the getDecks method to select the number of decks to include in the game
		 * checks to see if the deck needs to be shuffled, if yes then call the resetDeck and shuffle methods and increment the shuffleTracker
		 * then call the getBet method that checks the bet text field
		 * then checks to see if the player has money, if not then display game over message
		 * if the player does have money, make sure the bet is less than player's current money and more than 0... then:
		 * 		-check to see if the deck is being shuffled
		 * 			if yes, then wait for it to finish shuffling and then call the deal method
		 * 			if no, call the deal method
		 * if the player's bet was higher than their current credits, then display a "bet too high" message
		 * if the player's bet was 0, then display a "need to bet" message
		 */
		btDEAL.setOnAction(e -> {
			bigWinText.setVisible(false);
			bigWinCircle.setVisible(false);
			bigWinText2.setVisible(false);
			bigWinCircle2.setVisible(false);
			if (handsPlayed == 0)
				getDecks();
			if (selectedDeck.getCount() >= selectedDeck.deck.length * 0.75) {
				selectedDeck.resetDeck();
				shuffle();
				shuffleTracker++;
			}
			getBet();
			if (credits <= 0) {
				bigWords.setText("Game Over");
				bigWords.setVisible(true);
			}
			else if (bet > 0 && bet <= credits) {
				if (shuffleTracker == 1) {
					delay.setOnFinished(t -> {
						deal();
					});
					delay.play();
				}
				else
					deal();
			}
			else if (bet > credits)	{
				bigWords.setText("Bet is too high");
				bigWords.setVisible(true);
			}
			else {
				bigWords.setText("You have to place a bet first");
				bigWords.setVisible(true);
				betTextBox.requestFocus();
			}
		});
		
		/*
		 * SHUFFLE BUTTON ACTIONS:
		 * calls the shuffle method which shuffles the selected deck and plays an animation
		 */
		btShuffle.setOnAction(e -> {
			shuffle();
		});
		
		/*
		 * NEW GAME BUTTON ACTIONS:
		 * calls on the newGame method to reset the game back to a fresh game
		 */
		btNewGame.setOnAction(e -> {
			newGame();
		});
		
		/*
		 * BET TEXT FIELD ACTIONS:
		 * every time a key is pressed in the text field, the getBet method is called
		 * which parses the text field and attempts to set the bet equal to the text field
		 */
		betTextBox.setOnKeyTyped(e -> {
			getBet();
		});
		
		/*
		 * RIGHT AND LEFT AVATAR SELECTOR ACTIONS:
		 * there is a variable called avatarSelected that keeps track of which avatar is selected
		 * each time one of the arrows is pressed, the variable is checked so that the correct animation for the rectangle is played
		 * when the correct animation is found, the rectangle is moved to the next avatar, the variable is updated, and the big avatar image is changed
		 */
		right.setOnAction(e -> {
			if (avatarSelected == 0) {
				sliderRight[0].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected++;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 1) {
				sliderRight[1].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected++;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 2) {
				sliderRight[2].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected++;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 3) {
				sliderRight[3].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected++;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 4) {
				sliderRight[4].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected++;
				bigAvatars[avatarSelected].setVisible(true);
			}
		});
		left.setOnAction(e -> {
			if (avatarSelected == 5) {
				sliderLeft[0].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected--;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if(avatarSelected == 4) {
				sliderLeft[1].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected--;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 3) {
				sliderLeft[2].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected--;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 2) {
				sliderLeft[3].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected--;
				bigAvatars[avatarSelected].setVisible(true);
			}
			else if (avatarSelected == 1) {
				sliderLeft[4].play();
				bigAvatars[avatarSelected].setVisible(false);
				avatarSelected--;
				bigAvatars[avatarSelected].setVisible(true);
			}
		});
		
		/*
		 * THEME BUTTON ACTIONS:
		 * call the themeSelector method with a specific value depending on which button is pressed
		 * 1 is blue, 2 is purp, 3 is dark
		 */
		theme1.setOnAction(e -> {
			themeSelector(1);
		});
		theme2.setOnAction(e -> {
			themeSelector(2);
		});
		theme3.setOnAction(e -> {
			themeSelector(3);
		});
		
		/*
		 * There was a problem of the program not terminating properly after the timer was put in the code
		 * So, to fix this... an action was created that terminated the program upon closing the window
		 */
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		//******************************************
		//			END OF HANDLERS
		//******************************************
		
		/*******************************************************************************************************************
		 * 			TEST BUTTONS			TEST BUTTONS			TEST BUTTONS			TEST BUTTONS
		 *******************************************************************************************************************/
		Button btPair = new Button ("Pair");
		Button btThree = new Button("Three");
		Button btTwoP = new Button("Two Pair");
		Button btFull = new Button("Full");
		Button btStraight = new Button("Straight");
		Button btFlush = new Button("Flush");
		Button btFour = new Button("Four");
		Button btStFlush = new Button("Str Flush");
		Button btRStraight = new Button("R Straight");
		Button btRSFlush = new Button("RS Flush");
		Button btGiveJoker = new Button("Give Jokers");
		GridPane testGrid = new GridPane();
		
		bottomGrid.add(testGrid, 0, 1);
		testGrid.add(btPair, 0, 0);
		testGrid.add(btTwoP, 1, 0);
		testGrid.add(btThree, 2, 0);
		testGrid.add(btFour, 3, 0);
		testGrid.add(btStraight, 4, 0);
		testGrid.add(btFlush, 5, 0);
		testGrid.add(btFull, 6, 0);
		testGrid.add(btStFlush, 7, 0);
		testGrid.add(btRStraight, 8, 0);
		testGrid.add(btRSFlush, 9, 0);
		testGrid.add(btGiveJoker, 10, 0);
		testGrid.setHgap(10);
		testGrid.setAlignment(Pos.CENTER);
		
		btPair.setOnAction(e -> {							//Pair
			card[0].assignCard(2);
			card[1].assignCard(2);
			card[2].assignCard(4);
			card[3].assignCard(6);
			card[4].assignCard(21);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 2 + ".png";
			cardImgString[3] ="card/" + 4 + ".png";
			cardImgString[4] ="card/" + 6 + ".png";
			cardImgString[5] ="card/" + 21 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btTwoP.setOnAction(e -> {							//Two Pair
			card[0].assignCard(2);
			card[1].assignCard(2);
			card[2].assignCard(3);
			card[3].assignCard(3);
			card[4].assignCard(21);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 2 + ".png";
			cardImgString[3] ="card/" + 3 + ".png";
			cardImgString[4] ="card/" + 3 + ".png";
			cardImgString[5] ="card/" + 21 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btThree.setOnAction(e -> {							//Three of a Kind
			card[0].assignCard(2);
			card[1].assignCard(2);
			card[2].assignCard(2);
			card[3].assignCard(20);
			card[4].assignCard(21);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 2 + ".png";
			cardImgString[3] ="card/" + 2 + ".png";
			cardImgString[4] ="card/" + 20 + ".png";
			cardImgString[5] ="card/" + 21 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btFour.setOnAction(e -> {							//Three of a Kind
			card[0].assignCard(2);
			card[1].assignCard(2);
			card[2].assignCard(2);
			card[3].assignCard(2);
			card[4].assignCard(21);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 2 + ".png";
			cardImgString[3] ="card/" + 2 + ".png";
			cardImgString[4] ="card/" + 2 + ".png";
			cardImgString[5] ="card/" + 21 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btFull.setOnAction(e -> {							//Full House
			card[0].assignCard(2);
			card[1].assignCard(2);
			card[2].assignCard(2);
			card[3].assignCard(20);
			card[4].assignCard(20);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 2 + ".png";
			cardImgString[3] ="card/" + 2 + ".png";
			cardImgString[4] ="card/" + 20 + ".png";
			cardImgString[5] ="card/" + 20 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btStraight.setOnAction(e -> {						//Straight
			card[0].assignCard(2);
			card[1].assignCard(3);
			card[2].assignCard(4);
			card[3].assignCard(18);
			card[4].assignCard(19);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 3 + ".png";
			cardImgString[3] ="card/" + 4 + ".png";
			cardImgString[4] ="card/" + 18 + ".png";
			cardImgString[5] ="card/" + 19 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btFlush.setOnAction(e -> {							//Flush
			card[0].assignCard(2);
			card[1].assignCard(3);
			card[2].assignCard(4);
			card[3].assignCard(7);
			card[4].assignCard(8);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 3 + ".png";
			cardImgString[3] ="card/" + 4 + ".png";
			cardImgString[4] ="card/" + 7 + ".png";
			cardImgString[5] ="card/" + 8 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btStFlush.setOnAction(e -> {						//Straight Flush
			card[0].assignCard(2);
			card[1].assignCard(3);
			card[2].assignCard(4);
			card[3].assignCard(5);
			card[4].assignCard(6);
			cardImgString[1] = "card/" + 2 + ".png";
			cardImgString[2] = "card/" + 3 + ".png";
			cardImgString[3] ="card/" + 4 + ".png";
			cardImgString[4] ="card/" + 5 + ".png";
			cardImgString[5] ="card/" + 6 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btRStraight.setOnAction(e -> {						//Royal Straight
			card[0].assignCard(10);
			card[1].assignCard(11);
			card[2].assignCard(12);
			card[3].assignCard(13);
			card[4].assignCard(14);
			cardImgString[1] = "card/" + 10 + ".png";
			cardImgString[2] = "card/" + 11 + ".png";
			cardImgString[3] ="card/" + 12 + ".png";
			cardImgString[4] ="card/" + 13 + ".png";
			cardImgString[5] ="card/" + 14 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btRSFlush.setOnAction(e -> {						//Royal Straight Flush
			card[0].assignCard(10);
			card[1].assignCard(11);
			card[2].assignCard(12);
			card[3].assignCard(13);
			card[4].assignCard(1);
			cardImgString[1] = "card/" + 10 + ".png";
			cardImgString[2] = "card/" + 11 + ".png";
			cardImgString[3] ="card/" + 12 + ".png";
			cardImgString[4] ="card/" + 13 + ".png";
			cardImgString[5] ="card/" + 1 + ".png";
			for (int i = 1; i <= 5; i++) {
				cardIMG[i].setImage(new Image(cardImgString[i]));
				cardFromTop[i].play();
			}
			payout(Card.checkRank(card));
			printPayout(Card.checkRank(card));
		});
		btGiveJoker.setOnAction(e ->{
			takeCards();
			delay2.setOnFinished(t -> {
				giveJokers();
			});
			delay2.play();
			disableButtons(btDRAW);
			delay.setOnFinished(t -> {
				payout(Card.checkRank(card));
				printPayout(handrank);
				handrank = 0;
			});
			delay.play();
		});
		/*******************************************************************************************************************
		 * 		END OF TEST BUTTONS			END OF TEST BUTTONS			END OF TEST BUTTONS			END OF TEST BUTTONS
		 *******************************************************************************************************************/
		
		Scene scene = new Scene(background);		// Create a scene and place it in the stage
		scene.getStylesheets().add("Styles.css");	// Add styling file to the scene
		primaryStage.setTitle("Video Poker");		// Set the stage title
		primaryStage.setScene(scene); 		  		// Place the scene in the stage
		primaryStage.show(); 				 		// Display the stage
	}
	
	/*
	 * GIVECARDS METHOD ACTIONS:
	 * cycles through each card in hand and checks its dicard value for true or false
	 * if true, then the card has already been discarded and needs to be replaced:
	 * 		-assign the value of the next card in the deck to card[i] (the card being replaced)
	 * 		-set the card's image and play cardFromTop animation
	 * 		-then set discard value to false
	 * 		-then increment the deck's count variable which moves down a card in the deck
	 */
	public void giveCards() {
		for (int i = 0; i < 5; i++) {
			if (card[i].discard == true) {
				card[i].assignCard(selectedDeck.deck[selectedDeck.count]);
				cardImgString[i+1] = "card/" + selectedDeck.deck[selectedDeck.count] + ".png";
				cardIMG[i+1].setImage(new Image(cardImgString[i+1]));
				cardFromTop[i+1].play();
				card[i].discard = false;
				selectedDeck.count++;
			}
		}
	}
	
	/*
	 * GIVEJOKERS METHOD ACTIONS:
	 * if a card has been discarded:
	 * 		-set the new card image to the joker card image
	 * 		-assign it the joker card value by using assignCard method
	 * 		-play animation for the card to enter from the top
	 *		-set the discard back to false and increment the deck
	 */
	public void giveJokers() {
		for (int i = 0; i < 5; i++) {
			if (card[i].discard == true) {
				cardImgString[i+1] = "card/53.png";
				card[i].assignCard(53);
				cardIMG[i+1].setImage(new Image(cardImgString[i+1]));
				cardFromTop[i+1].play();
				card[i].discard = false;
				selectedDeck.count++;
			}
		}
	}
	
	/*
	 * TAKECARDS METHOD ACTIONS:
	 * cycles through each card in hand and checks the discard value for true or false, if true:
	 * 		-play set the card image to the "back of card" image
	 * 		-play the cardToBottom animation which discards the card
	 */
	public void takeCards() {
		for (int i = 0; i < 5; i++) {
			if (card[i].discard == true) {
				cardIMG[i+1].setImage(cardBackIMG);
				cardToBottom[i+1].play();
			}
		}
	}
	
	/*
	 * DEAL METHOD ACTIONS:
	 * sets all discard values to true, so that all cards will be replaced
	 * then checks to see if this is the first hand of the game
	 * 		if yes, call giveCards method, update message to player and disable deck selectors
	 * 		if no, 
	 * 			-call takeCards method to remove cards
	 * 			-call a delay animation to give the old cards time to exit the screen
	 * 			-then call the giveCards method and update the message to the player
	 * then update the players credits, disable/enable certain buttons, increment the hands played, and reset the shuffleTracker to 0
	 */
	public void deal() {
		for (int i = 0; i < 5; i++) {
			card[i].discard = true;
		}
		if (handsPlayed > 0) {
			takeCards();
			delay2.setOnFinished(t -> {
				giveCards();
				bigWords.setText("Select the cards you wish to discard.");
			});
			delay2.play();
		}
		else {
			giveCards();
			bigWords.setText("Select the cards you wish to discard.");
			pickDeck1.setDisable(true);
			pickDeck2.setDisable(true);
			pickDeck4.setDisable(true);
		}
		credits = credits - bet;
		pocketTextBox.setText("" + credits);
		disableButtons(btDEAL);
		handsPlayed++;
		shuffleTracker = 0;
	}
	
	/*
	 * SHUFFLE METHOD ACTIONS:
	 * disable the shuffle button once it has been pressed
	 * then call the method that shuffles the currently selected deck
	 * set all discard values to true so that if there are cards on the table they will be removed before the shuffle animation happens
	 * remove all cards off the table by calling the takeCards method
	 * then reset the handsPlayed variable to 0 so that when the deal button is pressed, it wont play the remove cards animation again
	 * next, play the shuffle animations mixed in with some delays that are used for timing the animations together
	 * finally, enable the deal and new game button and display a message telling the player that the deck has been shuffled
	 */
	public void shuffle() {
		disableButtons(btShuffle);
		selectedDeck.resetDeck();
		for (int i = 0; i < 5; i++) {
			card[i].discard = true;
		}
		takeCards();
		handsPlayed = 0;
		delay2.setOnFinished(t -> {
			delay3.setOnFinished(p -> {
				shuffle2[0].play();
				shuffle2[1].play();
				shuffle2[2].play();
				shuffle2[3].play();
				shuffle2[4].play();
				shuffle2[5].play();
				bigWords.setText("Deck(s) shuffled!");
				btDEAL.setDisable(false);
				btNewGame.setDisable(false);
			});
			shuffle[0].play();
			shuffle[1].play();
			shuffle[2].play();
			shuffle[3].play();
			shuffle[4].play();
			shuffle[5].play();
			delay3.play();
		});
		delay2.play();
	}
	
	/*
	 * PAYOUT METHOD ACTIONS:
	 * receives the hand rank from the method call and makes sure it is greater than 0
	 * 		-if it is greater than 0, then do an equation to get the player's new credit value
	 * 			then update the text field with new credit value
	 * 		-if the hand rank is 5 or greater, display the big win animations
	 */
	public void payout(int handRank) {
		handrank = handRank;
		if (handRank > 0) {
			credits = (credits + bet * Deck.payAMT[handRank]);
			pocketTextBox.setText("" + credits);
		}
		if (handrank >= 5) {
			bigWinCircle.setVisible(true);
			bigWinText.setVisible(true);
			bigWinCircle2.setVisible(true);
			bigWinText2.setVisible(true);
		}
	}

	/*
	 * PRINTPAYOUT METHOD ACTIONS:
	 * checks the hand rank received from method call to see if its greater than 0
	 * 		-if yes, update bigWords to display details about win and set visible
	 * 		-if no, update bigWords to display a loss and set visible
	 */
	public void printPayout(int handRank) {
		if(handRank > 0) {
			bigWords.setText(Deck.handRank[handRank] + (bet * Deck.payAMT[handRank]) + " \nPress deal to play again.");
		}
		else {
			bigWords.setText(Deck.handRank[handRank] + " \nPress deal to play again.");
		}
		bigWords.setVisible(true);
	}
	
	/*
	 * GETDECKS METHOD ACTIONS:
	 * checks the deck selection radio buttons values and sets the selectedDeck value to the selected value
	 */
	public void getDecks() {
		if (pickDeck1.isSelected() == true)
			selectedDeck = deck1;
		else if (pickDeck2.isSelected() == true)
			selectedDeck = deck2;
		else
			selectedDeck = deck4;
	}
	
	/* NEW GAME METHOD ACTIONS:
	 * resets important variable values to simulate starting a new game
	 * also checks to see if there are cards on the table, if yes set discard values to true and call takeCards method
	 * then display start of game message again
	 */
	public void newGame() {
		deck1.resetDeck();
		deck2.resetDeck();
		deck4.resetDeck();
		credits = 200;
		bet = 0;
		disableButtons(btNewGame);
		betTextBox.setText("" + bet);
		pocketTextBox.setText("" + credits);
		if (handsPlayed > 0) {
			for (int i = 0; i < 5; i++) {
				card[i].discard = true;
			}
			takeCards();
		}
		handsPlayed = 0;
		bigWords.setText("Select the number of decks on the left. \n"
			+ "Then hit deal to begin.");
		bigWords.setVisible(true);
		btDEAL.requestFocus();
	}
	
	/*
	 * GET BET METHOD ACTIONS:
	 * trys to convert the string in the bet text field to an integer
	 * if it succeeds then set the bet to that integer
	 * if it fails, tell the player that the bet must be a number and set the value of bet back in text field
	 */
	public void getBet() {
		try {
			int text = Integer.parseInt(betTextBox.getText());
			if (bet != text) {
				bet = text;
			}
		}
		catch (Exception ex) {
			bigWords.setText("Bet must be a number");
			bigWords.setVisible(true);
			betTextBox.setText("" + bet);
		}
	}
	
	/*
	 * HIDEAVATARS METHOD ACTIONS:
	 * hide all of the big avatar images to clear the way for a new big avatar image to be displayed
	 */
	public void hideAvatars() {
		bigAvatars[0].setVisible(false);
		bigAvatars[1].setVisible(false);
		bigAvatars[2].setVisible(false);
		bigAvatars[3].setVisible(false);
		bigAvatars[4].setVisible(false);
		bigAvatars[5].setVisible(false);
	}
	
	/*
	 * DISABLEBUTTONS METHOD ACTIONS:
	 * the method will disable certain buttons and enable others depending on which button is taken in
	 * created this method just to clean up and make the button actions easier to read
	 */
	public void disableButtons(Button x) {
		Button btn = x;
		if (btn == btDRAW) {
			btDEAL.setDisable(false);
			btDRAW.setDisable(true);
			btShuffle.setDisable(false);
			betTextBox.setDisable(false);
			betOne.setDisable(false);
			betMax.setDisable(false);
			betReset.setDisable(false);
			btDEAL.requestFocus();
		}
		if (btn == btDEAL) {
			btDEAL.setDisable(true);
			btShuffle.setDisable(true);
			btDRAW.setDisable(false);
			betTextBox.setDisable(true);
			betOne.setDisable(true);
			betMax.setDisable(true);
			betReset.setDisable(true);
			btDRAW.requestFocus();
		}
		if (btn == btNewGame) {
			btDRAW.setDisable(true);
			btDEAL.setDisable(false);
			btShuffle.setDisable(true);
			pickDeck1.setDisable(false);
			pickDeck2.setDisable(false);
			pickDeck4.setDisable(false);
			betTextBox.setDisable(false);
			betOne.setDisable(false);
			betMax.setDisable(false);
			betReset.setDisable(false);
		}
		if (btn == btShuffle) {
			btDEAL.setDisable(true);
			btShuffle.setDisable(true);
			btNewGame.setDisable(true);
		}
	}
	
	/*
	 * THEME SELECTOR METHOD ACTIONS:
	 * changes the theme of the game by attaching different style classes from Styles.css
	 * first checks to see if its the first time the program is applying the style by checking the firstTheme value
	 * if its the first time, then just add the styles
	 * if its not the first time, remove old style then add new style
	 */
	public void themeSelector(int theme) {
		int i = theme;
		if (firstTheme == true) {
			pickDeck1.getStyleClass().add("radio-button" + i);
			pickDeck2.getStyleClass().add("radio-button" + i);
			pickDeck4.getStyleClass().add("radio-button" + i);
			betReset.getStyleClass().add("button" + i);
			betMax.getStyleClass().add("button" + i);
			betOne.getStyleClass().add("button" + i);
			btShuffle.getStyleClass().add("button" + i);
			btNewGame.getStyleClass().add("button" + i);
			btDRAW.getStyleClass().add("button" + i);
			btDEAL.getStyleClass().add("button" + i);
			background.getStyleClass().add("root" + i);
			selector.getStyleClass().add("selector" + i);
			right.getStyleClass().add("triangle-button" + i);
			left.getStyleClass().add("triangle-button" + i);
			pocketTextBox.getStyleClass().add("custom" + i);
			betTextBox.getStyleClass().add("custom" + i);
			theme1.getStyleClass().add("button" + i);
			theme2.getStyleClass().add("button" + i);
			theme3.getStyleClass().add("button" + i);
			if (i == 1) {
				bigWords.setFill(Color.web("#4bbde7"));
				avatarText.setFill(Color.web("#4bbde7"));
				betsLbl.setTextFill(Color.web("#4bbde7"));
				creditsLbl.setTextFill(Color.web("#4bbde7"));
				handNames.setFill(Color.web("#4bbde7"));
			}
			if (i == 2)	 {
				bigWords.setFill(Color.web("#d4cfd1"));
				avatarText.setFill(Color.web("#d4cfd1"));
				betsLbl.setTextFill(Color.web("#d4cfd1"));
				creditsLbl.setTextFill(Color.web("#d4cfd1"));
				handNames.setFill(Color.web("#d4cfd1"));
			}
			if (i == 3) {
				bigWords.setFill(Color.web("#999999"));
				avatarText.setFill(Color.web("#999999"));
				betsLbl.setTextFill(Color.web("#999999"));
				creditsLbl.setTextFill(Color.web("#999999"));
				handNames.setFill(Color.web("#999999"));
			}
			firstTheme = false;
		}
		else {
			pickDeck1.getStyleClass().remove(1);
			pickDeck1.getStyleClass().add("radio-button" + i);
			pickDeck2.getStyleClass().remove(1);
			pickDeck2.getStyleClass().add("radio-button" + i);
			pickDeck4.getStyleClass().remove(1);
			pickDeck4.getStyleClass().add("radio-button" + i);
			theme1.getStyleClass().remove(1);
			theme1.getStyleClass().add("button" + i);
			theme2.getStyleClass().remove(1);
			theme2.getStyleClass().add("button" + i);
			theme3.getStyleClass().remove(1);
			theme3.getStyleClass().add("button" + i);
			betReset.getStyleClass().remove(1);
			betReset.getStyleClass().add("button" + i);
			betMax.getStyleClass().remove(1);
			betMax.getStyleClass().add("button" + i);
			betOne.getStyleClass().remove(1);
			betOne.getStyleClass().add("button" + i);
			btShuffle.getStyleClass().remove(1);
			btShuffle.getStyleClass().add("button" + i);
			btNewGame.getStyleClass().remove(1);
			btNewGame.getStyleClass().add("button" + i);
			btDRAW.getStyleClass().remove(1);
			btDRAW.getStyleClass().add("button" + i);
			btDEAL.getStyleClass().remove(1);
			btDEAL.getStyleClass().add("button" + i);
			background.getStyleClass().remove(1);
			background.getStyleClass().add("root" + i);
			selector.getStyleClass().remove(0);
			selector.getStyleClass().add("selector" + i);
			right.getStyleClass().remove(1);
			right.getStyleClass().add("triangle-button" + i);
			left.getStyleClass().remove(1);
			left.getStyleClass().add("triangle-button" + i);
			pocketTextBox.getStyleClass().remove(2);
			pocketTextBox.getStyleClass().add("custom" + i);
			betTextBox.getStyleClass().remove(2);
			betTextBox.getStyleClass().add("custom" + i);
			if (i == 1) {
				bigWords.setFill(Color.web("#4bbde7"));
				avatarText.setFill(Color.web("#4bbde7"));
				betsLbl.setTextFill(Color.web("#4bbde7"));
				creditsLbl.setTextFill(Color.web("#4bbde7"));
				handNames.setFill(Color.web("#4bbde7"));
			}
			if (i == 2)	 {
				bigWords.setFill(Color.web("#d4cfd1"));
				avatarText.setFill(Color.web("#d4cfd1"));
				betsLbl.setTextFill(Color.web("#d4cfd1"));
				creditsLbl.setTextFill(Color.web("#d4cfd1"));
				handNames.setFill(Color.web("#d4cfd1"));
			}
			if (i == 3) {
				bigWords.setFill(Color.web("#999999"));
				avatarText.setFill(Color.web("#999999"));
				betsLbl.setTextFill(Color.web("#999999"));
				creditsLbl.setTextFill(Color.web("#999999"));
				handNames.setFill(Color.web("#999999"));
			}
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}