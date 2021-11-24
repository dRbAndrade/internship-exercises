import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class SectionCJavaSeleniumCSV {

    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null,
                "Please, wait a little, this may take a few seconds."
                        +"\nA popup like this one will tell you when it's done.");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://finance.yahoo.com/quote/BTC-EUR/history/");
        StringBuilder output = new StringBuilder();
        String newLine = System.lineSeparator();
        output.append("Date,BTC Closing Value");
        output.append(newLine);

        for (int i =1;i<=10;i++){
            String xpath = "//*[@id=\"Col1-1-HistoricalDataTable-Proxy\"]/section/div[2]/table/tbody/tr[%s]/td[%s]/span";
            String date=driver.findElement(By.xpath(String.format(xpath,i,1))).getText();
            String close=driver.findElement(By.xpath(String.format(xpath,i,5))).getText();
            output.append(date.replace(",",""));
            output.append(",");
            output.append(close.replace(",",""));
            output.append(newLine);
        }
        driver.quit();

        try(FileWriter writer = new FileWriter("eur_btc_rates.csv")){
            writer.append(output);
        }catch (IOException ex){
            JOptionPane.showMessageDialog(null,
                    String.format("File Couldn't be created for some reason. The message was:\n%s\nThe cause was: %s",
                            ex.getMessage(),ex.getCause().toString()));
        }
        JOptionPane.showMessageDialog(null,
                "The file was written successfully");
    }

}
