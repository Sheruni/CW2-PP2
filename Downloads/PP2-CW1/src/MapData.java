import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MapData {
    //hashmaps for relevant dates and routes
    Map<String, String> reservedseats1 = new HashMap<>();
    Map<String, String> reservedseats2 = new HashMap<>();
    Map<String, String> reservedseats3 = new HashMap<>();
    Map<String, String> reservedseats4 = new HashMap<>();
    Map<String, String> reservedseats5 = new HashMap<>();
    Map<String, String> reservedseats6 = new HashMap<>();
    Map<String, String> reservedseats7 = new HashMap<>();
    Map<String, String> reservedseats8 = new HashMap<>();
    Map<String, String> reservedseats9 = new HashMap<>();
    Map<String, String> reservedseats10 = new HashMap<>();


    public HashMap<String, String> selectMap(String date, String route) throws ParseException {
        //This method changes the seat data using relevant hashmap after choicebox selection

        String dt = String.valueOf((java.time.LocalDate.now()));   //dt is the current date value
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));  //c is the formatted date value
        String dates[] = new String[5];  //Array which holds 5 consecutive future dates

        for(int i=0;i<5;i++) {
            c.add(Calendar.DATE, 1);
            dt = sdf.format(c.getTime());
            dates[i]=dt;

        }

        HashMap x = new HashMap<>();
        try {
            if (route.equals("Colombo to Badulla")) {
                if (date.equals(String.valueOf(dates[0]))) {
                    x = (HashMap) reservedseats1;
                } else if (date.equals(String.valueOf(dates[1]))) {
                    x = (HashMap) reservedseats2;
                } else if (date.equals(String.valueOf(dates[2]))) {
                    x = (HashMap) reservedseats3;
                } else if (date.equals(String.valueOf(dates[3]))) {
                    x = (HashMap) reservedseats4;
                }else {
                    x = (HashMap) reservedseats5;
                }

            } else if (route.equals("Badulla to Colombo")) {
                if (date.equals(String.valueOf(dates[0]))) {
                    x = (HashMap) reservedseats6;
                } else if (date.equals(String.valueOf(dates[1]))) {
                    x = (HashMap) reservedseats7;
                } else if (date.equals(String.valueOf(dates[2]))){
                    x = (HashMap) reservedseats8;
                } else if (date.equals(String.valueOf(dates[3]))) {
                    x = (HashMap) reservedseats9;
                } else {
                    x = (HashMap) reservedseats10;
                }
            } else System.out.println();

        } catch (Exception e) {
        }
        return x; //Returns hashmap to SeatReservation Class
    }
    public HashMap findMap(int date, int route){
        //This method returns relevant hashmap for add,delete,find,store,order,load options
        HashMap x = new HashMap<>();
        try {
            if (route==1) {
                switch (date){
                    case 1:
                    x = (HashMap) reservedseats1;
                    break;
                    case 2:
                    x = (HashMap) reservedseats2;
                    break;
                    case 3:
                    x = (HashMap) reservedseats3;
                    break;
                    case 4:
                    x = (HashMap) reservedseats4;
                    break;
                    case 5:
                    x = (HashMap) reservedseats5;
                    break;
                }

            } else if (route==2) {
                switch (date){
                    case 1:
                    x = (HashMap) reservedseats6;
                    break;
                    case 2:
                    x = (HashMap) reservedseats7;
                    break;
                    case 3:
                    x = (HashMap) reservedseats8;
                    break;
                    case 4:
                    x = (HashMap) reservedseats9;
                    break;
                    case 5:
                    x = (HashMap) reservedseats10;
                    break;
                }
            } else {
                System.out.println("error!");;
            }

        } catch (Exception e) {
            System.out.println("Error!");

        }
        return x; //Returns hashmap to SeatReservation Class
    }
}
