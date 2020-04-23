import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PassengerQueue{

    private static LinkedList<String> queueSeats = new LinkedList<>();  //seats of the passengers in queue
    private static LinkedList<String> trainQueue = new LinkedList<>();  //names of the passengers in queue
    private static int maxStayInQueue;
    private int queueLength;

    public LinkedList<String> getTrainQueue()
    {
        return trainQueue;
    }

    public LinkedList<String> getQueueSeats()
    {
        return queueSeats;
    }

    public void setMaxStayInQueue(int length) {
        if (maxStayInQueue < length)
            maxStayInQueue = length;    //if the queue length is larger than the MaxLength, queue length is assigned to MaxLength
    }

    public void setQueueLength(int length) {
        this.queueLength = length;
        setMaxStayInQueue(queueLength);
    }

    public int getMaxStayInQueue()
    {
        return maxStayInQueue;
    }

    public Boolean isEmpty(){
        return trainQueue.isEmpty();
    }   //returns true if queue is empty

    public Boolean isFull(){
        return queueLength>=21;
    }   // Maximum length of the queue that could be obtained is set to 21

    public List<String> waiting_time(LinkedList<Integer> secondsInQueue) {

        String avg = avg_waiting_time(secondsInQueue)+" seconds";
        String max = max_waiting_time(secondsInQueue)+" seconds";
        String min = min_waiting_time(secondsInQueue)+" seconds";

        return Arrays.asList(avg, max, min);  //average, maximum and minimum waiting times are returned in an array

    }

    public int max_waiting_time(LinkedList<Integer> secondsInQueue) {
        return Collections.max(secondsInQueue);
    }

    public int min_waiting_time(LinkedList<Integer> secondsInQueue) {
        return Collections.min(secondsInQueue);
    }

    public float avg_waiting_time(LinkedList<Integer> secondsInQueue){

        float sum = 0;
        float avg;

        for (Integer integer : secondsInQueue) {
            sum += integer;     //total of the values in secondsinQueue is calculated
        }

        avg = sum/secondsInQueue.size();   //average is calculated
        return avg;
    }

    public void add(ArrayList<String> waiting_passengers, String opt) {

            if (opt.equals("a")) {

                try {

                    int die1 = (int) (Math.random() * 6 + 1);   // calculate random number for the number of passengers joining train queue

                    for (int i = 0; i < die1; i++) {
                        if (!isFull()){
                            if (waiting_passengers.isEmpty()) {
                                System.out.println("❖❖❖❖❖ No Passengers in waiting room. ❖❖❖❖❖ ");
                                break;
                            } else {

                                String name = waiting_passengers.get(0);
                                String seat = waiting_passengers.get(1);
                                waiting_passengers.remove(0);
                                waiting_passengers.remove(0);
                                trainQueue.add(name);
                                queueSeats.add(seat);
                                setQueueLength(trainQueue.size());
                            }
                        }else {
                            System.out.println("❖❖❖❖❖ Queue is full! ❖❖❖❖❖");
                            break;
                        }
                    }

                } catch (Exception e) {
                    System.out.println("❖❖❖❖❖ No passengers in waiting room. ❖❖❖❖❖");
                }

            }

        }
        

    public void remove() {

        System.out.println("Enter passenger name : ");
        Scanner input = new Scanner(System.in);
        String delpassenger = input.nextLine().trim().toLowerCase();

        ListIterator<String> iterator = trainQueue.listIterator();
        int i=0;
        boolean found = false;
        while (iterator.hasNext())
        {
            if((iterator.next().toLowerCase()).equals(delpassenger)){
                queueSeats.remove(i);
                trainQueue.remove(i);
                System.out.println(delpassenger +" was removed from queue.");
                found=true;
                break;   //if entered name is in the queue, found is set to true
            }
            i++;
        }


       if(!found) {System.out.println("❖❖❖❖❖ Passenger not found! The name you entered is not in queue.❖❖❖❖❖");}

        System.out.println("Passengers in queue: "+trainQueue);
    }

    public void StoreInFile(String selecteddate, String selectedroute) throws InterruptedException {

        //Connecting to Mongo Database
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase Database1 = mongoClient.getDatabase("Train_Seat_Reservation");
        MongoCollection<Document> mongoCollection6 = Database1.getCollection("myCollection6");


        TimeUnit.SECONDS.sleep(1);

        if(!isEmpty()) {
            System.out.println("❖❖❖❖❖ Data successfully stored ! ❖❖❖❖❖");

            for (int i = 0 ; i<trainQueue.size();i++) {

                Document data1 = new Document();  //Details of a passenger is entered to a new document
                data1.put("Passenger name", trainQueue.get(i));
                data1.put("Seat_no", queueSeats.get(i));
                data1.put("Date", selecteddate);
                data1.put("Route", selectedroute);

                mongoCollection6.insertOne(data1);

                System.out.println(data1);  //Stored data is displayed in console
            }

        }else{
            System.out.println("❖❖❖❖❖ No data was found! ❖❖❖❖❖");
        }

    }


}

