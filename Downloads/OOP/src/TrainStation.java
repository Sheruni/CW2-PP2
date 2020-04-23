
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class TrainStation extends Application{

    private String selectedDate;
    private String selectedRoute;
    private ArrayList<String> waitingPassengers = new ArrayList<>();

    public static void main(String[] args) {

        launch();

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase Database1 = mongoClient.getDatabase("Train_Seat_Reservation");
        MongoCollection<Document> mongoCollection5 = Database1.getCollection("myCollection5");

        Iterator<Document> iterator = mongoCollection5.find().projection(Projections.excludeId()).iterator();
        //iterates through the documents in the collection

        Scanner input = new Scanner(System.in);
        String[] dates = new String[5];   //Array that holds string values of 5 days from tomorrow
        String dt = String.valueOf((java.time.LocalDate.now()));  //dt is today's date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));   // c is the formatted current date


        for (int i = 0; i < 5; i++) {
            c.add(Calendar.DATE, 1);  //date is incremented by one
            dt = sdf.format(c.getTime());
            dates[i] = dt;  //date is added to array

        }

        int selectdate;
        int selectroute;

        while (true) {
            try {
                System.out.println("Enter route (Colombo to Badulla-1,Badulla to Colombo-2):");
                selectroute = input.nextInt();
                int finalB = selectroute;
                if (IntStream.of(1, 2).anyMatch(n -> n == finalB)) {    //checks if input integer is valid
                    break;
                } else {
                    System.out.println("Invalid Input...");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Invalid input. Try again...");     //checks for invalid data type
                input.next();
            }
        }


        while (true) {
            try {
                System.out.println("Enter date to proceed (Select respective number : ");
                System.out.println("1 : "+dates[0]);
                System.out.println("2 : "+dates[1]);
                System.out.println("3 : "+dates[2]);
                System.out.println("4 : "+dates[3]);
                System.out.println("5 : "+dates[4]);
                selectdate = input.nextInt();
                int finalA = selectdate;
                if (IntStream.of(1, 2, 3, 4, 5).anyMatch(n -> n == finalA)) {  // to checks if input integer is valid
                    break;
                } else {
                    System.out.println("Input Date Invalid...");
                }
            } catch (InputMismatchException exception) {   //checks for data type mismatch
                System.out.println("Invalid input. Try again...");
                input.next();
            }
        }

        String[] routes = {"Colombo to Badulla", "Badulla to Colombo"};


        try {
            System.out.println("❖❖❖❖❖ Data Loaded into program successfully! ❖❖❖❖❖");
            System.out.println("Passengers in Waiting Room : ");
            System.out.printf("%-25.30s  %-25.30s%n", "✽ ✽ ✽ Passenger Name ✽ ✽ ✽"," ✽ Seat No. ✽");
            while (iterator.hasNext()) {
                //Iterates through the Collection following each document

                Document document = iterator.next();
                String dbdate = (String) document.get("Date");   //For each document Date and Route values are selected
                String dbroute = (String) document.get("Route");

                selectedDate = (String) Array.get(dates, selectdate - 1);
                selectedRoute = (String) Array.get(routes, selectroute - 1);


                if (dbdate.equals(selectedDate) && dbroute.equals(selectedRoute)) {
                    //Date and route values are compared with the given parameters to check if it needs to be loaded

                    String val1 = ((String) document.get("name"));
                    String val2 = (String) document.get("seat_no");

                    waitingPassengers.add(val1);  //the selected customers and seats are added to update the existing hashmap
                    waitingPassengers.add(val2);
                    System.out.printf("%-30.30s  %-30.30s%n", val1, val2);  //display in two separate columns
                }
            }

        } catch (Exception e) {
            System.out.println("❖❖❖❖❖ Error while Loading.... ❖❖❖❖❖");
        }

        //Displays the passengers in waiting room
        TimeUnit.SECONDS.sleep(1);       //the program waits one second after loading from database


        display_menu(selectedDate,selectedRoute);
    }


    private void display_menu(String date, String route) throws IOException, InterruptedException {

        Passenger passenger = new Passenger();
        PassengerQueue pasQueue = new PassengerQueue();
        HashMap<String, String> onBoard = passenger.getOnBoard();

        Scanner input = new Scanner(System.in);
        System.out.println("\n✦ ✦ ✦ ✦ ✦ ✦ ✦ ✦ ✦  Denuwara Menike - Colombo / Badulla  ✦ ✦ ✦ ✦ ✦ ✦ ✦ ✦ ✦");
        menu:
        while (true) {
            System.out.println("\nChoose Option:");
            System.out.println("\"A\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Add a customer to train queue");
            System.out.println("\"V\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ View Train queue");
            System.out.println("\"D\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Delete customer from train queue");
            System.out.println("\"S\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Store train queue data into file");
            System.out.println("\"L\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Load queue data from text file");
            System.out.println("\"R\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Run simulation and produce report");
            System.out.println("\"Q\" ➣➣➣➣➣➣➣➣➣➣➣➣➣➣➣ Quit");
            String option = input.next();
            String opt = String.valueOf(option).toLowerCase();
            switch (opt) {

                case "a":
                    showQueue(opt);
                    continue;

                case "v":
                    showQueue(opt);    //Show train queue
                    viewseats(onBoard);  //Show boarded passengers in train
                    continue;

                case "d":
                    pasQueue.remove();
                    continue;

                case "s":
                    pasQueue.StoreInFile(date,route);
                    continue;

                case "l":
                    passenger.LoadFromFile(date,route);
                    continue;

                case "r":
                    passenger.runSimulation(date,route);   //data of passengers on board are retrieved
                    viewseats(onBoard);
                    continue;

                case "q":
                    System.exit(0);
                    break menu;
                default:

            }
        }
    }

    private static void viewseats(HashMap<String, String> onBoard) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 'v' again to view Passengers On Board");
        String inputV = input.next().toLowerCase();
        if(inputV.equals("v")){
            showSeats(onBoard);
        }
    }

    private static void showSeats(HashMap<String, String> onBoard) {

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Train Seats View");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 0, 20, 65));
        gridPane.setVgap(15);
        gridPane.setHgap(12);

        TextArea textArea = new TextArea();     //Text area to display report loaded from file.txt
        textArea.setPrefHeight(520);
        textArea.setPrefWidth(372);


        Label lb1 = new Label("- R E P O R T -");
        Scene scene = new Scene(gridPane,1220,690);
        VBox vboxForButtons1 = new VBox(10);
        VBox vboxForButtons2 = new VBox(10);
        VBox vboxForButtons3 = new VBox(5);
        vboxForButtons3.setPadding(new Insets(40));
        VBox vboxForButtons4 = new VBox(10);
        VBox vboxForButtons5 = new VBox(10);
        VBox vboxForButtons6 = new VBox(10);
        VBox vboxForButtons7 = new VBox(20);
        vboxForButtons7.setPadding(new Insets(5,10,10,40));

        vboxForButtons7.setAlignment(Pos.CENTER);

        Button close = new Button("Close");


        textArea.setEditable(false);  //text area is disabled to edit

        vboxForButtons7.getChildren().add(lb1);
        vboxForButtons7.getChildren().add(textArea);
        vboxForButtons7.getChildren().add(close);

        if(!onBoard.isEmpty()) {
            try {
                Scanner s = new Scanner(new File("File.txt")).useDelimiter("\\s+");

                while (s.hasNextLine()) {
                    textArea.appendText(s.nextLine() + "\n");    // elements in each line of the text file are added to text area
                }

            } catch (FileNotFoundException ex) {
                System.err.println("❖❖❖❖❖ File not found! ❖❖❖❖❖");
            }
        }


        scene.getStylesheets().add(TrainStation.class.getResource("style1.css").toExternalForm());
        close.setOnAction(event -> primaryStage.close());
        close.getStyleClass().add("close");

        Button[] trainseats = new Button[42];
        for (int i = 0; i < 42; i++) {


            if (i < 9 ) {
                trainseats[i] = new Button("S" + (i + 1));
                vboxForButtons1.getChildren().add(trainseats[i]);
            } else if (i < 18) {
                trainseats[i] = new Button("S" + (i + 1));
                vboxForButtons2.getChildren().add(trainseats[i]);
            } else if (i < 26) {
                trainseats[i] = new Button("S" + (i + 1));
                vboxForButtons4.getChildren().add(trainseats[i]);
            } else if (i < 34) {
                trainseats[i] = new Button("S" + (i + 1));
                vboxForButtons5.getChildren().add(trainseats[i]);
            } else {
                trainseats[i] = new Button("S" + (i + 1));
                vboxForButtons6.getChildren().add(trainseats[i]);
            }
        }


        if (onBoard.isEmpty()){
            System.out.println("❖❖❖❖❖ No passengers are boarded into train ❖❖❖❖❖");
        }
        else{
            onBoard.forEach((key, value) -> {
                for (int x = 0; x < 42; x++) {
                    trainseats[x].setDisable(true);  // set all buttons disabled
                    if (trainseats[x].getText().equals(value)) {    //if the passenger is already on board the seat colour is changed
                        trainseats[x].setStyle(" -fx-background-color: #FA8072;");
                        trainseats[x].setText(value+"\n"+key.substring(0, key.indexOf(" "))+"\n"
                                +key.substring(key.indexOf(" ")+1));
                    }
                }
            });
        }

        gridPane.add(vboxForButtons1,1,1);
        gridPane.add(vboxForButtons2,2,1);
        gridPane.add(vboxForButtons3,3,1);
        gridPane.add(vboxForButtons4,4,1);
        gridPane.add(vboxForButtons5,5,1);
        gridPane.add(vboxForButtons6,6,1);
        gridPane.add(vboxForButtons7,7,1);

        primaryStage.setScene(scene);
        primaryStage.showAndWait();

    }


    private void showQueue(String opt) {

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Train Queue");
        TilePane tilepane = new TilePane();
        tilepane.setStyle("-fx-background-color:DarkCyan;");
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color:DarkCyan;");
        Scene scene = new Scene(borderPane, 670, 690);
        scene.getStylesheets().add(TrainStation.class.getResource("style2.css").toExternalForm());

        Button close = new Button("Close");
        borderPane.setTop(close);
        BorderPane.setAlignment(close, Pos.BOTTOM_CENTER);

        borderPane.setPadding(new Insets(10,5,0,5));
        VBox vboxForButtons1 = new VBox(10);
        VBox vboxForButtons2 = new VBox(10);
        VBox vboxForButtons3 = new VBox(10);

        close.getStyleClass().add("close");
        close.setOnAction(event -> primaryStage.close());


        tilepane.setPadding(new Insets(5, 0, 0, 0));


        PassengerQueue newPassenger = new PassengerQueue();
        newPassenger.add(waitingPassengers,opt);
        LinkedList train_queue = newPassenger.getTrainQueue();
        //get the added train queue from the passenger queue class

        System.out.println("Passengers in Waiting Room : ");

        for(int x=0; x<waitingPassengers.size();x+=2) {
            System.out.printf("%-30.30s  %-30.30s%n", waitingPassengers.get(x), waitingPassengers.get(x+1));
            //Output passenger name and seat in two columns
        }

        if (train_queue.isEmpty()){
            System.out.println("❖❖❖❖❖ There are no passengers in train queue. ❖❖❖❖❖");
            return;
        }

        for (int i = 1; i <= train_queue.size(); i++) {
            ImageView imageView = new ImageView(new Image("standing-up-man-.png"));
            String name = (String) train_queue.get(i-1);
            Button btn = new Button("P"+(i)+"\n"+name, imageView);
            btn.getStyleClass().add("icon");
            btn.setContentDisplay(ContentDisplay.LEFT);    //button content is aligned left
            if (i <= 7) {
                vboxForButtons1.getChildren().add(btn);
            } else if (i <= 14) {
                vboxForButtons2.getChildren().add(btn);
            } else if (i <= 21) {
                vboxForButtons3.getChildren().add(btn);
            }

        }

        tilepane.getChildren().add(vboxForButtons1);
        tilepane.getChildren().add(vboxForButtons2);
        tilepane.getChildren().add(vboxForButtons3);

        borderPane.setCenter(tilepane);

        primaryStage.setScene(scene);
        primaryStage.showAndWait();

    }


}



