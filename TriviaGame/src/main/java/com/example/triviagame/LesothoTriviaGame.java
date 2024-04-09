package com.example.triviagame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LesothoTriviaGame extends Application {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel;
    private ImageView imageView;
    private List<Button> answerButtons;
    private Label progressLabel;
    private Label scoreLabel;
    private BorderPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeQuestions();

        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: white;");

        questionLabel = new Label();
        questionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");
        imageView = new ImageView();
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);

        VBox centerBox = new VBox(20, questionLabel, imageView);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        answerButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Button answerButton = new Button();
            answerButton.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 18px; -fx-pref-width: 200px;");
            answerButton.setOnAction(e -> handleAnswer(answerButton.getText()));
            answerButtons.add(answerButton);
        }
        VBox answerBox = new VBox(10, answerButtons.toArray(new Button[0]));
        answerBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(20);
        Button prevButton = new Button("Previous");
        prevButton.setStyle("-fx-background-color: #008000; -fx-text-fill: white; -fx-font-size: 16px;");
        prevButton.setOnAction(e -> previousQuestion());
        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #008000; -fx-text-fill: white; -fx-font-size: 16px;");
        nextButton.setOnAction(e -> nextQuestion());
        buttonBox.getChildren().addAll(prevButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(20, answerBox, buttonBox);
        mainBox.setAlignment(Pos.CENTER);
        root.setBottom(mainBox);

        progressLabel = new Label();
        progressLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        HBox statusBar = new HBox(40, progressLabel, scoreLabel);
        statusBar.setAlignment(Pos.CENTER);
        root.setTop(statusBar);

        displayQuestion();

        // Set the scene and show the stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();
    }

    private void initializeQuestions() {
        // Initialize trivia questions
        questions = new ArrayList<>();

        // Add question 1
        Question question1 = new Question("What is the highest waterfall in Lesotho?", "Maletsunyane Falls",  "Sehlabathebe Falls", "Moshoeshoe Falls", "Tsoelikane Falls");
        questions.add(question1);

        // Add question 2
        Question question2 = new Question("What is a traditional dance performed by Basotho people during celebrations?", "Gumboot Dance", "Mokete Dance", "Waltz", "Capoeira");
        questions.add(question2);

        // Add question 3
        Question question3 = new Question("Lesotho is known for its mountainous terrain. What percentage of the country is located above 1,800 meters (5,900 ft)?", "10%", "30%", "60%", "80%");
        questions.add(question3);

        // Add question 4
        Question question4 = new Question("What is the traditional conical hat worn by Basotho men called?", "Fez", "Basotho Hat", "Sombrero", "Kippah");
        questions.add(question4);

        // Add question 5
        Question question5 = new Question("Where is the Lesotho National Museum located?", "Maseru", "Mafeteng", "Qacha's Nek", "Mohale's Hoek");
        questions.add(question5);


        root = new BorderPane(); // Initialize root BorderPane
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: white;");
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());
            imageView.setImage(new Image(getClass().getResource(currentQuestion.getImageURL()).toString()));

            List<String> answers = currentQuestion.getShuffledAnswers();
            for (int i = 0; i < answerButtons.size(); i++) {
                answerButtons.get(i).setText(answers.get(i));
            }

            progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
            scoreLabel.setText("Score: " + score);
        } else {
            // Game over
            showGameOver();
        }
    }

    private void handleAnswer(String selectedAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (currentQuestion.isCorrect(selectedAnswer)) {
            score++;
        }
        // Move to the next question after updating the score
        currentQuestionIndex++;
        displayQuestion();
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void showGameOver() {
        // Display game over message with final score
        questionLabel.setText("Game Over");
        imageView.setImage(new Image(getClass().getResource("/com/example/triviagame/flag.png").toString()));
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        for (Button button : answerButtons) {
            button.setVisible(false);
        }
        progressLabel.setText("");
        scoreLabel.setText("Final Score: " + score);

        // Add a "Restart" button
        Button restartButton = new Button("Restart");
        restartButton.setStyle("-fx-background-color: black; -fx-text-fill: black; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-pref-height: 40px;");
        restartButton.setOnAction(e -> restartGame());

        VBox buttonBox = new VBox(20, restartButton);
        buttonBox.setAlignment(Pos.CENTER);
        root.setBottom(buttonBox); // Set the restart button at the bottom of the root pane
    }

    private void restartGame() {
        currentQuestionIndex = 0;
        score = 0;
        displayQuestion();
    }

    static class Question {
        private String question;
        private String correctAnswer;
        private List<String> incorrectAnswers;
        private String imageURL;

        public Question(String question, String correctAnswer, String... incorrectAnswers) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.incorrectAnswers = new ArrayList<>();
            Collections.addAll(this.incorrectAnswers, incorrectAnswers);
            this.imageURL = "/com/example/triviagame/flag.png"; // Provide image URL for the question
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getShuffledAnswers() {
            List<String> answers = new ArrayList<>(incorrectAnswers);
            answers.add(correctAnswer);
            Collections.shuffle(answers);
            return answers;
        }

        public String getImageURL() {
            return this.imageURL;
        }

        public boolean isCorrect(String selectedAnswer) {
            return correctAnswer.equals(selectedAnswer);
        }
    }
}
