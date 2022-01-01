package splitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import splitter.exceptions.CommandException;
import splitter.exceptions.ExitException;
import splitter.services.CommandService;
import splitter.config.Utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import static splitter.config.Utils.OUTPUT_PATH;

@Slf4j
@SpringBootApplication
public class SplitterApplication implements CommandLineRunner {

    @Autowired
    private CommandService commandService;

    public static void main(String[] args) {
        SpringApplication.run(SplitterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        PrintWriter out = new PrintWriter(
            new FileWriter(OUTPUT_PATH, false)
        );
        out.print("");
        out.close();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            try {
                commandService
                    .get(scanner.nextLine())
                    .execute();
            } catch (ExitException e) {
                if (Utils.printStackTrace()) {
                    e.printStackTrace();
                }
                break;
            } catch (CommandException e) {
                if (Utils.printStackTrace()) {
                    e.printStackTrace();
                }
                System.out.println(e.getMessage());
            } catch (Exception e) {
                if (Utils.printStackTrace()) {
                    e.printStackTrace();
                }
                System.out.println(e.getMessage());
            }
        }
    }
}