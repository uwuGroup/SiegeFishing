package me.asakura_kukii.siegefishing.config.data;

import java.io.File;

public abstract class FileData {
    //information
    public String identifier;
    public String fileName;
    public FileType fT;
    public File f = null;

    public FileData (String identifier, String fileName, FileType fT) {
        this.identifier = identifier;
        this.fileName = fileName;
        this.fT = fT;
    }
}
