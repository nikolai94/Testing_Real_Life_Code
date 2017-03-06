package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.hamcrest.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.xml.sax.SAXException;

/**
 *
 * @author nikolai
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private String plateCorrect, plateResult;
    
    public RecognitionAllIT(String plateCorrect, String plateResult) {
        this.plateCorrect = plateCorrect;
        this.plateResult = plateResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException, ParserConfigurationException, SAXException {

        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);

        File snapshotDir;
        File[] snapshots = null;
      
        resultsStream.close();

        snapshotDir = new File(snapshotDirPath);
        snapshots = snapshotDir.listFiles();
        
        Intelligence intel = new Intelligence();

        Collection<Object[]> listOfResults = new ArrayList();
        for (File snap : snapshots) {
                CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));

                String snapName = snap.getName();
                String plateCorrect = properties.getProperty(snapName);

                String numberPlate = intel.recognize(carSnap, false);

                listOfResults.add(new Object[]{plateCorrect, numberPlate});

                carSnap.close();
        }
        return listOfResults;
    }

    @Test
    public void testAllSnapshotsNew() {
//        assertEquals(plateCorrect, plateResult);
        assertThat(plateCorrect, is(equalTo(plateResult)));
    }
}
