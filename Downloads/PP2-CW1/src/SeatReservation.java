import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class SeatReservation extends Application {

    static final int SEATING_CAPACITY = 42;

    public static void main(String[] args) {

        launch();
    }

    @Override
    public void start(Stage primaryStage) throws ParseException{
        String dates[] = new String[5];   //Array that holds string values of 5 days from tomorrow
        String dt = String.valueOf((java.time.LocalDate.now()));  //dt is today's date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));   // c is the formatted current date

        for(int i=0;i<5;i++) {
            c.add(Calendar.DATE, 1);  //date is incremented by one
            dt = sdf.format(c.getTime());
            dates[i]=dt;  //date is added to array

        }
        menu(dates);
    }

    public void menu(String[] dates){
        MapData HashMap1 = new MapData(); //connect to MapData Class
        //HashMap1 is used throughout the program passed as parameters

        Scanner input = new Scanner(System.in);
        System.out.println("----- Denuwara Menike Seat Reservation - Colombo / Badulla -----");
        menu:
        while (true) {
            System.out.println("\nChoose Option:");
            System.out.println("\"A\" : Add a customer");
            System.out.println("\"V\" : View all seats");
            System.out.println("\"E\" : View empty seat");
            System.out.println("\"D\" : Delete customer from seat");
            System.out.println("\"F\" : Find seat for given customer");
            System.out.println("\"S\" : Store program data into file");
            System.out.println("\"L\" : Load program data from file");
            System.out.println("\"O\" : View seats ordered by alphabetical name");
            System.out.println("\"Q\" : Quit");
            String option = input.next();
            String opt = String.valueOf(option).toLowerCase();
            switch (opt) {
                case "a":
                    addCustomer(opt, HashMap1,dates);
                    continue;
                case "v":
                    viewAllSeats(HashMap1,dates);
                    continue;
                case "e":
                    viewEmptySeats(HashMap1,dates);
                    continue;
                case "o":
                case "d":
                case "s":
                case "l":
                case "f":
                    FindDeleteOrder(opt, HashMap1,dates);
                    continue;
                case "q":
                    System.exit(0);
                    break menu;
                default:
                    System.out.println("Invalid Input, Reenter!");
            }
        }
    }

    void setStage(String customer, String opt, MapData HashMap1, String[] dates){

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Train Seats Reservation");
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 710, 670);
        grid.setPadding(new Insets(20, 0, 20, 65));
        grid.setVgap(15);
        grid.setHgap(12);
        grid.getColumnConstraints().add(new ColumnConstraints(45));
        grid.getRowConstraints().add(new RowConstraints(40));

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        VBox vbox1 = new VBox(15);
        VBox vbox2 = new VBox(15);
        VBox vbox3 = new VBox(15);
        VBox vbox4 = new VBox(15);
        VBox vbox5 = new VBox(15);
        VBox vbox6 = new VBox(15);
        VBox vbox7 = new VBox(25);

        Label label1 = new Label("Denuwara Menike Seat Reservation - Colombo/Badulla");
        String[] titles = {"Denuwara Menike ( to Badulla ) - [A/C Compartment]",
                "Denuwara Menike ( to Colombo ) - [A/C Compartment]"};
        label1.setPadding(new Insets(2, 2, 2, 10));
        Label datelabel = new Label("");
        Rectangle rec1 = new Rectangle(0, 0, 35, 35);
        rec1.setFill(Color.STEELBLUE);
        Label label2 = new Label("Available Seats");
        Rectangle rec2 = new Rectangle(0, 0, 35, 35);
        rec2.setFill(Color.SALMON);
        Label label3 = new Label("Reserved Seats");
        Label label4 = new Label("   Select date:");
        label4.setPadding(new Insets(30, 0, 0, 0));

        ChoiceBox<String> cb1 = new ChoiceBox<>(FXCollections.observableArrayList(dates));
        Label label5 = new Label("   Select Route:");
        String[] route = {"Colombo to Badulla", "Badulla to Colombo"};
        ChoiceBox<String> cb2 = new ChoiceBox<>(FXCollections.observableArrayList(route));
        Button submit = new Button("Submit");
        submit.getStyleClass().add("submit");

        ToggleGroup toggleGroup = new ToggleGroup(); //ToggleGroup enables only to select one button in the group
        vbox6.getChildren().add(rec1);
        vbox6.getChildren().add(rec2);
        vbox7.getChildren().add(label2);
        vbox7.getChildren().add(label3);

        grid.add(vbox1, 0, 2);
        grid.add(vbox2, 1, 2);
        grid.add(vbox3, 4, 2);
        grid.add(vbox4, 5, 2);
        grid.add(vbox5, 6, 2);
        grid.add(label1, 1, 0, 13, 2);
        grid.add(datelabel, 6, 1, 7, 1);
        grid.add(vbox6, 12, 2);
        grid.add(vbox7, 13, 2);
        grid.add(submit, 13, 3);
        ToggleButton[] seats = new ToggleButton[SEATING_CAPACITY];

        vbox7.getChildren().add(label4);
        vbox7.getChildren().add(cb1);
        vbox7.getChildren().add(label5);
        vbox7.getChildren().add(cb2);

        int i;
        for (i = 0; i < 9; i++) {
            seats[i] = new ToggleButton("S" + (i + 1));
            vbox1.getChildren().add(seats[i]);
            seats[i].setToggleGroup(toggleGroup);
        }
        for (i = 9; i < 18; i++) {
            seats[i] = new ToggleButton("S" + (i + 1));
            vbox2.getChildren().add(seats[i]);
            seats[i].setToggleGroup(toggleGroup);
        }
        for (i = 18; i < 26; i++) {
            seats[i] = new ToggleButton("S" + (i + 1));
            vbox3.getChildren().add(seats[i]);
            seats[i].setToggleGroup(toggleGroup);
        }
        for (i = 26; i < 34; i++) {
            seats[i] = new ToggleButton("S" + (i + 1));
            vbox4.getChildren().add(seats[i]);
            seats[i].setToggleGroup(toggleGroup);
        }
        for (i = 34; i < SEATING_CAPACITY; i++) {
            seats[i] = new ToggleButton("S" + (i + 1));
            vbox5.getChildren().add(seats[i]);
            seats[i].setToggleGroup(toggleGroup);
        }

        //Event Listener for choice boxes to change the Title Label and Date appearing on GUI

        cb1.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value)
                -> datelabel.setText(dates[new_value.intValue()]));
        cb2.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value)
                -> label1.setText(titles[new_value.intValue()]));

        String[] info = new String[2];  //Array with date and route values respectively

        cb1.setOnAction(event -> {   //action handler for selecting a date on dates choicebox

            info[0] = cb1.getValue();
            info[1] = cb2.getValue();
            getGUI(info[0], info[1], seats, toggleGroup, submit, customer, opt, primaryStage, HashMap1);

        });

        cb2.setOnAction(event -> {  //action handler for selecting a date on routes choicebox

            info[0] = cb1.getValue();
            info[1] = cb2.getValue();
            getGUI(info[0], info[1], seats, toggleGroup, submit, customer, opt, primaryStage, HashMap1);

        });

        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    private void getGUI(String date, String route, ToggleButton[] seats, ToggleGroup toggleGroup,
                        Button submit, String customer, String opt, Stage primaryStage, MapData HashMap1) {

        for (int x = 0; x < SEATING_CAPACITY; x++) {
            seats[x].setStyle("-fx-background-color: Steelblue;");
            seats[x].setVisible(true);
            seats[x].setDisable(false);
            int finalX = x;
            seats[x].setOnMousePressed(event1 -> seats[finalX].setStyle("fx-background-color: GoldenRod;"));
              //Button changes color to ORANGE after clicked
        }

        try {
            HashMap<String, String> reservedseats2 = HashMap1.selectMap(date, route); //Retrieves hashmap data for selected date/route

            reservedseats2.forEach((key, thevalue) -> {      //based on the hashmap values the seats are printed
                for (int x = 0; x < SEATING_CAPACITY; x++) {
                    if (seats[x].getText().equals(thevalue)) {
                        seats[x].setDisable(true);  //Already reserved are set disabled
                        seats[x].setStyle(" -fx-background-color: #FA8072;");  //Reserved buttons appear in pink
                    }
                }
            });

            toggleGroup.selectedToggleProperty().addListener((ouv, toggle, new_toggle) ->  //Listener for the selected toggle button in toggle group
                    submit.setOnMouseClicked(t -> {
                        ToggleButton myseat = (ToggleButton) toggleGroup.getSelectedToggle();
                        String addedseatText = myseat.getText();
                        reservedseats2.put(customer, addedseatText);  //customer and seat is added to map
                        System.out.println(customer + " was successfully added to seat " + addedseatText);
                        primaryStage.close();
                    }));

            if (!opt.equals("a")) {
                for (int x = 0; x < SEATING_CAPACITY; x++) {
                    seats[x].setDisable(true);
                }
                if (opt.equals("e")) reservedseats2.forEach((key, tvalue) -> {
                    for (int x = 0; x < SEATING_CAPACITY; x++) {
                        if (seats[x].getText().equals(tvalue)) {
                            seats[x].setVisible(false);
                        }
                    }
                });
                submit.setOnMouseClicked(t -> primaryStage.close());
            }
        } catch (Exception ignored) {
        }
    }

    void addCustomer(String opt, MapData HashMap1, String[] dates){
        Scanner input = new Scanner(System.in);
        String first_name = "";
        String sur_name = "";

        while(first_name.isEmpty()) {
            System.out.println("Enter Customer First Name:");
            first_name = input.next().toLowerCase().trim();  //Input String is trimmed and converted to lowercase
        }
        while(sur_name.isEmpty()) {
            System.out.println("Enter Customer Sur Name:");
            sur_name = input.next().toLowerCase().trim();
        }

        String customer = first_name +" "+ sur_name;
        setStage(customer, opt, HashMap1, dates);
        
    }

    void viewAllSeats(MapData HashMap1, String[] dates){
        setStage("", "v", HashMap1, dates);
    }

    void viewEmptySeats(MapData HashMap1, String[] dates){
        setStage("", "e", HashMap1, dates);
    }

    void FindDeleteOrder(String opt, MapData HashMap1,String[] dates){
        String customer = "";
        Scanner input = new Scanner(System.in);
        if (opt.equals("f") || opt.equals("d")) {
            while(true){
            System.out.println("Enter Customer Name:");
            customer = input.nextLine().toLowerCase();
            if (customer.isEmpty()) {
                System.out.println("Nothing was entered. Please try again.");
            } else {
                break; }
            }
        }
            int selectdate;
            int selectroute;

            while (true) {
                try {
                    System.out.println("Enter Date (Select respective number):");
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
            while (true) {
                try {
                    System.out.println("Enter route (Colombo to Badulla-1,Badulla to Colombo-2):");
                    selectroute = input.nextInt();
                    int finalB = selectroute;
                    if (IntStream.of(1, 2).anyMatch(n -> n == finalB)) {
                        break;
                    } else {
                        System.out.println("Invalid Input...");
                    }
                } catch (InputMismatchException exception) {
                    System.out.println("Invalid input. Try again...");
                    input.next();
                }
            }

            HashMap<String, String> reservedseats3;
            reservedseats3 = HashMap1.findMap(selectdate, selectroute);  //relevant hashmap is retrieved from MapData Class

            switch (opt) {
                case "d":
                    if (reservedseats3.containsKey(customer)) {
                        String delseat = reservedseats3.get(customer);
                        reservedseats3.remove(customer);
                        System.out.println(customer + " reserved to seat " + delseat + " was removed.");
                    } else {
                        System.out.println("Customer not found! The name you entered is not reserved to any seat.");
                    }
                    break;
                case "f":
                    if (reservedseats3.containsKey(customer)) {
                        System.out.println("SEAT : " + reservedseats3.get(customer));
                    } else {
                        System.out.println("Invalid Input! The name you entered is not reserved to any seat.");
                    }
                    break;
                case "o":
                    //Using Bubble sort to arrange names in alphabetical order
                    int n = reservedseats3.size();   //n is the length of hashmap
                    String name_first;
                    List<String> order = new ArrayList<>(); //a new arraylist to store customer names in alphabetical order
                    for (Map.Entry<String, String> entry : reservedseats3.entrySet()) {
                        String key = entry.getKey();
                        order.add(key);  //all customer names are added to new arraylist
                    }
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            if (order.get(i).compareTo(order.get(j)) > 0) {  //if element at index j comes first in order
                                name_first = order.get(i);
                                    //element at index i is now the current name_first

                                order.set(i, order.get(j));
                                    //element at index j is put to index i of array

                                order.set(j, name_first);
                                    //name_first is now in the index j position (j=i+1 always)
                            }
                        }
                    }
                    for (String name : order) {
                        System.out.println(name + " : " + reservedseats3.get(name));
                    }
                    break;

                case "s":
                    Database mongoDatabase1 = new Database();
                    mongoDatabase1.AddData(reservedseats3, selectdate, selectroute,dates);
                    break;

                case "l":
                    Database mongoDatabase2 = new Database();
                    mongoDatabase2.LoadData(selectdate, selectroute, reservedseats3,dates);
                    break;
            }
    }
}