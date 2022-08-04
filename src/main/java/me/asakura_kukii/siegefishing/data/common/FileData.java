package me.asakura_kukii.siegefishing.data.common;

import me.asakura_kukii.siegefishing.io.loader.common.FileType;

public abstract class FileData {
    //information
    public String identifier;
    public String fileName;
    public FileType fT;

    public FileData (String identifier, String fileName, FileType fT) {
        this.identifier = identifier;
        this.fileName = fileName;
        this.fT = fT;
    }
}
