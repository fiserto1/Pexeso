package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVFileReaderUtil {

    public static Collection<String[]> readCSVfileToCollection(String fileName, String separator) throws FileNotFoundException, IOException {

        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);

        List<String[]> records = new ArrayList<>();
        String record;

        BufferedReader file = new BufferedReader(new InputStreamReader(resourceAsStream));
        while ((record = file.readLine()) != null) {
            String fields[] = record.split(separator);
            //System.out.println(fields.length);
            records.add(fields);
        }
        file.close();
        return records;
    }
}
