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
    public static HttpURLConnection connectionToRandom;
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
            System.out.println(responseContent);
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


    public static String parse(String responseBody, Integer[] list ){
        JSONArray demons = new JSONArray(responseBody);
        for(int i = 0; i<demons.length(); i++){
            JSONObject demon = demons.getJSONObject(list[i]);
            int levelID = demon.getInt("level_id");
            int position = demon.getInt("position");
            String name = demon.getString("name");
            System.out.println(position+". "+name+" ID : "+levelID);
        }
        return null;
    }

}
