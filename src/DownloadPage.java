import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class DownloadPage {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://cit.edu.in");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = null;
            FileWriter writer = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("Output has been saved to output.txt");
        }
    }
}