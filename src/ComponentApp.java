import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ComponentApp {
    public static HttpURLConnection connectionToPointercrate;
    public static void main(String[] args){

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {

            URL url = new URL("https://pointercrate.com/api/v2/demons/listed/?limit=100");
            connectionToPointercrate = (HttpURLConnection) url.openConnection();

            connectionToPointercrate.setRequestMethod("GET");
            connectionToPointercrate.setConnectTimeout(5000);
            connectionToPointercrate.setReadTimeout(5000);

            int status = connectionToPointercrate.getResponseCode();
            //System.out.println(status);

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connectionToPointercrate.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connectionToPointercrate.getInputStream()));
            }
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            connectionToPointercrate.disconnect();
        }

        parse(responseContent.toString(), randomList());

    }

    public static Integer[] randomList(){
        Integer[] arr = new Integer[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }


    public static void parse(String responseBody, Integer[] list ){
        JSONArray demons = new JSONArray(responseBody);
        int percent = 0;
        int score = 0;
        Scanner input = new Scanner(System.in);
        for(int i = 0; i<demons.length(); i++){
            JSONObject demon = demons.getJSONObject(list[i]);
            int levelID = demon.getInt("level_id");
            int position = demon.getInt("position");
            String name = demon.getString("name");
            System.out.println(position+". "+name+" ID : "+levelID);
            System.out.println("You need at least "+(percent+1)+"% to unlock the next level");
            System.out.print("What did you get: ");
            percent = input.nextInt();
            while(percent >100) {
                System.out.println("Input a valid percentage: ");
                percent = input.nextInt();
            }
            score++;
            if(percent==100){
                System.out.println("Well done! You scored " + score);
                break;
            }
        }
    }
}
