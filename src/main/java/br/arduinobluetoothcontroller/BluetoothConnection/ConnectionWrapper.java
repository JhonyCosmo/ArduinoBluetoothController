package br.arduinobluetoothcontroller.BluetoothConnection;

public  class ConnectionWrapper {
    private static  ConnectionThread connect;
    public static ConnectionThread getConnection() {
        return connect;
    }
    public static void setConnection( ConnectionThread mConnect ) {
        connect = mConnect;
    }
}
