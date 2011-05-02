package com.compomics.mslims.util.fileio;


import com.compomics.mslims.db.accessors.LCRun;
import com.compomics.mslims.db.accessors.Spectrum_file;
import com.compomics.mslims.gui.progressbars.DefaultProgressBar;
import com.compomics.mslims.util.fileio.interfaces.SpectrumStorageEngine;

import com.compomics.mslims.util.workers.LoadFourierDTAWorker;
import com.compomics.util.interfaces.Flamable;
import com.compomics.util.io.FilenameExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 02/05/11
 * Time: 13:50
 */
public class FourierSpectrumStorageEngine {



    /**
     * This method takes care of loading all the LC runs from the file system, while displaying a progressbar.
     * <b><i>Please note</i></b> that the 'aFoundLCRuns' Vector is a reference parameter that will contain the Lcrun
     * instances after completion of the method!
     *
     * @param aList File[]  with the listing of the top-level directory to browse through.
     * @param aStoredLCRuns   Vector    with the Lcrun instances that were retrieved from the database.
     *                                  When found on the filesystem, these will not be included in the 'aNames'
     *                                  Vector with the results,s ince they are already stored.
     * @param aFoundLCRuns    Vector    that will contain the new (not yet in DB) Lcrun instances found on
     *                                  the local harddrive.
     * @param aParent   Flamable    with the paent that will do the error handling.
     * @param aProgress DefaulProgressBar   to display the progress on.
     */
    public void findAllLCRunsFromFileSystem(File[] aList, Vector aStoredLCRuns, Vector aFoundLCRuns, Flamable aParent, DefaultProgressBar aProgress) {
        LoadFourierDTAWorker lmw = new LoadFourierDTAWorker(aList, aStoredLCRuns, aFoundLCRuns, aParent, aProgress);
        lmw.start();
        aProgress.setVisible(true);
    }

    /**
     * This method actually takes care of finding all the spectrumfiles for the indiciated LCRun and
     * transforming these into Spectrumfile instances for storage in the database over the specified connection.
     *
     * @param aLCRun    LCRun instances for which the spectrumfiles need to be found and stored.
     * @param aProjectid    long with the projectid to associate the Spectrumfiles with.
     * @param aInstrumentid long with the instrumentid to associate the spectrumfiles with.
     * @param aConn Connection  on which to write the Spectrumfiles.
     * @return  int with the number of spectra stored.
     * @throws java.io.IOException  when the filereading goes wrong.
     * @throws java.sql.SQLException when the DB storage goes wrong.
     */
    public int loadAndStoreSpectrumFiles(LCRun aLCRun, long aProjectid, long aInstrumentid, Connection aConn) throws IOException, SQLException {
            // Get a hanlde to the parent folder.
        File parent = new File(aLCRun.getPathname());

        // List all dta files in the parent.
        File[] files = parent.listFiles(new FilenameExtensionFilter(".dta"));
        Vector spectra = new Vector(files.length);

        // Iterate over all files and load them into a Vector, subsequently convert them in
        // Spectrumfiles and store each.
        for (int i = 0; i < files.length; i++) {
            File lFile = files[i];
            DTAFile dta = new DTAFile(lFile);
            spectra.add(dta);
        }
        // Cycle through all the dta files, storing each in the DB with the appropriate
        // links (L_LCRUNID and L_PROJECTID). The SEARCHED and IDENTIFIED flags default to '0'.
        // Filename and data are provided through the DTAFile class, but note that we
        // convert the file contents from a String into a byte[] using the platforms default encoding.
        int liSize = spectra.size();
        int counter = 0;
        for(int i = 0; i < liSize; i++) {
            DTAFile dta = (DTAFile)spectra.elementAt(i);
            String mgfFileContents = dta.getMGFContents();
            String filename = dta.getFilename();
            MascotGenericFile lMascotGenericFile = new MascotGenericFile(filename, mgfFileContents);
            HashMap data = new HashMap(9);
            data.put(Spectrumfile.L_INSTRUMENTID, new Long(aInstrumentid));
            // The links.
            data.put(Spectrumfile.L_LCRUNID, new Long(aLCRun.getLcrunid()));
            data.put(Spectrumfile.L_PROJECTID, new Long(aProjectid));
            // The flags.
            data.put(Spectrumfile.IDENTIFIED, new Long(0));
            data.put(Spectrumfile.SEARCHED, new Long(0));
            // The filename.
            data.put(Spectrumfile.FILENAME, lMascotGenericFile.getFilename());
            // Create the database object.
            Spectrumfile dbObject = new Spectrumfile(data);
            // Read the contents for the file into a byte[].
            byte[] fileContents = lMascotGenericFile.toString().getBytes();
            // Set the byte[].
            dbObject.setUnzippedFile(fileContents);
            dbObject.persist(aConn);
            counter++;
        }
        return counter;
    }
}