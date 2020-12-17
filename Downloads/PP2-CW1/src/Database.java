import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Database {

    MongoClient mongoClient = new MongoClient("localhost", 27017);
    MongoDatabase Database1 = mongoClient.getDatabase("Train_Seat_Reservation");
    MongoCollection<Document> mongoCollection5 = Database1.getCollection("myCollection5");
    String[] routes = {"Colombo to Badulla", "Badulla to Colombo"};



        public void AddData(Map<String, String> reservedseats, int selectdate, int selectroute, String[] dates){

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!reservedseats.isEmpty()) {
                String thedate = (String) Array.get(dates, selectdate - 1);   //Date from dates array is retrieved based on value of selected date
                String route = (String) Array.get(routes, selectroute - 1);
                System.out.println(reservedseats);
                System.out.println("Date:" + thedate);
                System.out.println("Route:" + route);
                System.out.println("Records Successfully Stored...");

                DateFormat date = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date dateobj = new Date();
                reservedseats.forEach((key, value) -> {
                    Document data1 = new Document();    //for each pair in hashmap a new document is created
                    data1.put("name", toCamelCase(key));
                    data1.put("seat_no", value);
                    data1.put("Date", thedate);
                    data1.put("Route", route);
                    data1.put("added", date.format(dateobj));
                    mongoCollection5.insertOne(data1);  //document is inserted into collection
                });
            }else{
                System.out.println("No data was found!");
            }

        }

        private Object toCamelCase (String key){
                    //This is a method to convert string(name) into uppercamel case before inserting into Database

            StringBuilder name = new StringBuilder(key.length());
            for (final String word : key.split(" ")) {
                //the name is split by whitespace

                if (!word.isEmpty()) {
                    //if the split part is not empty the first letter is set to capital and the rest to simple

                    name.append(Character.toUpperCase(word.charAt(0)));
                    name.append(word.substring(1).toLowerCase());
                }

                if (!(name.length() == key.length()))
                    //if the name length is not equal to the key length a space is added to the name
                    name.append(" ");
            }
            return name.toString();

        }

        public Map<String, String> LoadData(int selectdate, int selectroute, HashMap<String, String> reservedseats, String[] dates){

            Iterator<Document> iterator = mongoCollection5.find().projection(Projections.excludeId()).iterator();

            try {
                System.out.println("Data Loaded into program successfully!");
                while (iterator.hasNext()) {
                                //Iterates through the Collection following each document
                    Document document = iterator.next();
                    String mydate = (String)document.get("Date");   //For each document Date and Route values are selected
                    String myroute = (String)document.get("Route");

                    if((mydate.equals(Array.get(dates, selectdate-1))) && (myroute.equals(Array.get(routes, selectroute-1)))){
                        //Date and route values are compared with the given parameters to check if it needs to be loaded
                        System.out.println(document);
                        String val = (String) document.get("name");
                        String val2 = (String) document.get("seat_no");
                        reservedseats.put(val, val2);  //the selected customers and seats are added to update the existing hashmap
                    }
                }
            } catch (Exception e) {
                System.out.println("Error while Loading....");
            }return  reservedseats;

        }
    }




