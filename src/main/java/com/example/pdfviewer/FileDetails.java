package com.example.pdfviewer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDetails {
    private final String name;
    private final String dateModified;
    private final String size;

    public FileDetails(File file) {
        this.name = file.getName();
        this.dateModified = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(file.lastModified()));
        this.size = file.isDirectory() ? "" : file.length() + " bytes";
    }

    public String getName() {
        return name;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getSize() {
        return size;
    }
}
