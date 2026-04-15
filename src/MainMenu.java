import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu {
    private StackPane root;
    private TextField nameField;

    /**
     * Constructs a new main menu
     */
    public MainMenu(GameClient client) {

        Image jungleFrame= new Image("file:../assets/jungle_frame.png");
        ImageView frameView= new ImageView(jungleFrame);
        frameView.setFitWidth(1600);
        frameView.setFitHeight(800);
        frameView.setPreserveRatio(false);


        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(60));
        box.setStyle("-fx-background-color: #1e2f23;");

        Label title = new Label("Tiger Wars");
        title.setFont(new Font("Georgia", 48));
        title.setStyle("-fx-text-fill: #ffd700;" + "fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 2);");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label prompt = new Label("Enter your name, or generate a random one.");
        prompt.setFont(new Font("Georgia", 18));
        prompt.setStyle("-fx-text-fill: #e6dbc6;");
        prompt.setMaxWidth(Double.MAX_VALUE);
        prompt.setAlignment(Pos.CENTER);

        nameField= new TextField();
        nameField.setPromptText("Your name...");
        nameField.setMaxWidth(260);
        nameField.setStyle(
            "-fx-background-color: #f0e5d8;" +
            "-fx-text-fill: #2b1f1b;" +
            "-fx-font-family: Georgia;" +
            "-fx-border-color: #5c4033;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 6 8;");

        Button generateButton= new Button("Generate");
        generateButton.setStyle(
            "-fx-background-color: #3a5f3a;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: Georgia;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 16;"+
            "-fx-cursor: hand;");
        generateButton.setOnAction(e -> {
            nameField.setText(Name.random());
        });

        Button playButton= new Button("PLAY");
        playButton.setStyle(
            "-fx-font-size: 20pt;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: Georgia;" +
            "-fx-background-color: #5c4033;" +
            "-fx-text-fill: #ffd700;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 28;" + 
            "-fx-cursor: hand;");
        
        playButton.setOnAction(e -> {
            String chosenName = nameField.getText().trim();
            if (!chosenName.isEmpty()) {
                client.setName(chosenName);
            }
        });

        DropShadow shadow= new DropShadow();
        shadow.setColor(Color.web("#ffd700"));

        generateButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> generateButton.setEffect(shadow));
        generateButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> generateButton.setEffect(null));
        
        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> playButton.setEffect(shadow));
        playButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> playButton.setEffect(null));

        box.getChildren().addAll(title, prompt, nameField, generateButton, playButton);

        root = new StackPane();
        root.getChildren().addAll(box, frameView);
    }

    /**
     * Gets the HBox containing the MainMenu's UI elements.
     * @return the HBox containing the MainMenu's UI elements
     */
    public StackPane getGUI() {
        return root;
    }
}
