package com.example.aplikacja3;

class FileData {
    private int size;
    private String type;
    private byte[] bytes;

    public FileData(int size, String type, byte[] bytes) {
        this.size = size;
        this.type = type;
        this.bytes = bytes;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public byte[] getBytes() {
        return bytes;
    }
}