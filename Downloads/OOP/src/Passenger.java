import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Passenger extends PassengerQueue{

    private String firstName;
    private String surName;
    private LinkedList<Integer> seconds_in_queue = new LinkedList();
    private LinkedList<String> queue = getQueue();
    private LinkedList<String> seats = getSeats();
    private HashMap<String, String> onBoard = new HashMap<>();



    public void setFirstName(String Name) {
        int i = Name.indexOf(' ');
        this.firstName = Name.substring(0, i);   //First name is obtained using substring from index 0 till the index of whitespace
    }

    public void setSurName(String Name) {
        int i = Name.indexOf(' ');
        this.surName = Name.substring(i+1);   //Sur name is obtained using the substring from the index of whitespace
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSeconds_in_queue() {

        int addingTime=0;   //This is the waiting time of the preceding passenger
        for(int i=seconds_in_queue.size() ; i< onBoard.size();i++){

            int die1 = (int) (Math.random() * 6 + 1);
            int die2 = (int) (Math.random() * 6 + 1);
            int die3 = (int) (Math.random() * 6 + 1);
            int delay = die1 + die2 + die3 + addingTime;

            seconds_in_queue.add(delay);  //waiting time is calculated for each passenger in the queue
            addingTime = delay;   //adding time is increased by the preceding passenger's waiting time
        }

    }

    public LinkedList<String> getSeats() {
        return getQueueSeats();
    }

    public LinkedList<String> getQueue() {
        return getTrainQueue();
    }


    public HashMap<String, String> getOnBoard() {
        return onBoard;
    }


    public LinkedList<Integer> getSeconds_in_queue(){
        return seconds_in_queue;
    }


    public void runSimulation(String date, String route) throws IOException {

        if (queue.isEmpty()) {
            System.out.println("❖❖❖❖❖ There are no passengers in train queue. ❖❖❖❖❖");

        } else {

            int i = 0;
            while (i < queue.size()) {
                onBoard.put(queue.get(i),seats.get(i));
                i++;
            }

            System.out.println("Passengers on Board : " + onBoard);

            setSeconds_in_queue();
            List<String> wait_time_details = waiting_time(getSeconds_in_queue());  // get the calculated avg,max,min waiting times for each passenger in train queue

            queue.clear();     //empty train queue after boarding passengers into train
            seats.clear();     //empty seats from train queue passengers

            final int[] x = {0};
            File file = new File("File.txt"); // File object to represent a file in the hard disk
            PrintWriter pw;
            FileWriter fw;
            fw = new FileWriter(file, false); // To overwrite the file (append mode false)
            pw = new PrintWriter(fw, true); //To open the file in auto flush mode

            //Writes in file

            pw.printf("%-30.40s  %-30.40s%n","Date : "+ date,"Route : "+route);
            pw.println("-------------------------------------------------------");
            pw.println("Maximum length of queue : " + getMaxStayInQueue());    // get the calculated  maximum number of passengers waited in queue
            pw.println("Maximum waiting time : " + wait_time_details.get(1));
            pw.println("Minimum waiting time : " + wait_time_details.get(2));
            pw.println("Average waiting time : " + wait_time_details.get(0));
            pw.println("-------------------------------------------------------");
            pw.println("Passenger Details :");

            onBoard.forEach((key, value) -> {

                setFirstName(key);
                setSurName(key);

                //Writes in file

                pw.println("\nPassenger Name : " + getFirstName());
                pw.println("     SurName : " + getSurName());
                pw.println("     Seat No: " + value);
                pw.println("     Waiting Time : " + seconds_in_queue.get(x[0]) + " seconds");
                pw.flush();
                x[0]++;

            });

        }


    }

    public void LoadFromFile(String selecteddate, String selectedroute) throws InterruptedException {

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase Database1 = mongoClient.getDatabase("Train_Seat_Reservation");
        MongoCollection<Document> mongoCollection6 = Database1.getCollection("myCollection6");

        TimeUnit.SECONDS.sleep(1);
        Iterator<Document> iterator = mongoCollection6.find().projection(Projections.excludeId()).iterator();


        try {
            System.out.println("❖❖❖❖❖ Data Loaded into program successfully! ❖❖❖❖❖");

            //All the existing lists are emptied
            queue.clear();
            seats.clear();
            onBoard.clear();

            while (iterator.hasNext()) {
                //Iterates through the Collection following each document

                Document document = iterator.next();
                String dbdate = (String)document.get("Date");   //For each document Date and Route values are selected
                String dbroute = (String)document.get("Route");

                if(dbdate.equals(selecteddate) && dbroute.equals(selectedroute)){
                    //Date and route values are compared with the given parameters to check if it needs to be loaded
                    String passenger_name = (String) document.get("Passenger name");
                    String passenger_seat = (String) document.get("Seat_no");

                    queue.add(passenger_name);
                    seats.add(passenger_seat);
                }
            }
        } catch (Exception e) {
            System.out.println("❖❖❖❖❖ Error while Loading.... ❖❖❖❖❖");
        }

    }


}
