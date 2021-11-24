import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectionBCurlCSV {

    public static void main(String[] args){
        String today = LocalDate.now().toString().replace("-","_");
        try (FileWriter writer = new FileWriter(String.format("usd_currency_rates_%s.csv",today)))
        {
            JOptionPane.showMessageDialog(null,
                    "Please, wait a little before opening the file, this may take a few seconds."
                            +"\nA popup like this one will tell you when it's done.");
            String command =
                    "curl -X GET https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml?5105e8233f9433cf70ac379d6ccc5775";
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            processBuilder.directory(new File(".\\"));
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            StringBuilder xml = new StringBuilder();
            int data = inputStream.read();
            while (data != -1) {
                xml.append((char) data);
                data = inputStream.read();
            }
            Pattern currencyPattern = Pattern.compile("currency='(.*?)'");
            Matcher currencyMatcher = currencyPattern.matcher(xml);
            Pattern ratePattern = Pattern.compile("rate='(.*?)'");
            Matcher rateMatcher = ratePattern.matcher(xml);
            StringBuilder output = new StringBuilder();
            output.append("Currency Code,Rate");
            output.append(System.lineSeparator());
            while (currencyMatcher.find()) {
                rateMatcher.find();
                String match = currencyMatcher.group();
                output.append(match, 10, match.length() - 1);
                output.append(",");
                match = rateMatcher.group();
                output.append(match, 6, match.length() - 1);
                output.append(System.lineSeparator());
            }
            writer.write(output.toString());
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    String.format("File Couldn't be created for some reason. The message was:\n%s\nThe cause was: %s",
                            ex.getMessage(),ex.getCause().toString()));
        }
        JOptionPane.showMessageDialog(null,
                "The file was written successfully");

    }

}
