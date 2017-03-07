/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.Before;
import org.xml.sax.SAXException;

/**
 *
 * @author nikolai
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private String plateCorrect;
    private File plate;

    private CarSnapshot carSnap;

    public RecognitionAllIT(String plateCorrect, File plate) {
        this.plateCorrect = plateCorrect;
        this.plate = plate;
    }

    @Before
    public void init() throws IOException {
        carSnap = new CarSnapshot(new FileInputStream(plate));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException, ParserConfigurationException, SAXException {

        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);
        assertThat(properties.size(), greaterThan(0));

        File snapshotDir;
        File[] snapshots = null;

        resultsStream.close();

        snapshotDir = new File(snapshotDirPath);
        snapshots = snapshotDir.listFiles();
        assertThat(snapshots.length, greaterThan(0));

        Collection<Object[]> listOfResults = new ArrayList();
        for (File snap : snapshots) {
            String snapName = snap.getName();
            String plateCorrect = properties.getProperty(snapName);
            
            listOfResults.add(new Object[]{plateCorrect, snap});
            
        }
        return listOfResults;
    }

    @Test
    public void testAllSnapshotsNew() throws Exception {
        Intelligence intel = new Intelligence();
        assertThat(intel, is(notNullValue()));
        assertThat(carSnap, is(notNullValue()));
        String numberPlate = intel.recognize(carSnap, false);
        assertThat(plateCorrect, is(equalTo(numberPlate)));
        carSnap.close();
    }
}
