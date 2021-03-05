package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConnectionPool { // ConnectionPool is Singleton
    private static final int MAX_AMOUNT_CONNECTIONS = 5;
    private static ConnectionPool instance;
    String host = "localhost:3306";
    String dbName = "coupon_management_system";
    String url = "jdbc:mysql://" + host + "/" + dbName + "?useSSL=false";
    String username = "root";
    String password = "";
    private final Set<Connection> freeConnections;
    private Set<Connection> usedConnections;

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private Connection getConnectionToFillThePool(){ // TODO Method?
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private ConnectionPool() { //Constructor
        this.freeConnections = new HashSet<>(MAX_AMOUNT_CONNECTIONS); // How many Connections do we need?
        for (int i = 0; i < MAX_AMOUNT_CONNECTIONS; i++) {
            freeConnections.add(getConnectionToFillThePool()); // TODO OR to call restoreConnection() method??
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        if (freeConnections.isEmpty()) {
            try {
                wait();  //TODO?
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            connection = freeConnections.iterator().next(); // get from set
            usedConnections.add(connection); //
            freeConnections.remove(connection);
        }
        return connection;
    }

    public void restoreConnection(Connection connection) { // todo I UNDERSTOOD right: don't forget to call this method in the end of query INSTEAD OF TO CLOSE() CONNECTION IN QUERY
        if (usedConnections.contains(connection)) { // I check here if the connection that I return is the same connection that I received.
            if (freeConnections.size() < MAX_AMOUNT_CONNECTIONS) { // todo I don't sure if i need this check
                freeConnections.add(connection);
          /*  try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
                usedConnections.remove(connection); // TODO check after Threads addition
                notify(); // TODO to check after Thread?
            }
        }
    }

    public void closeAllConnections() {
        if (usedConnections.isEmpty()) {
            for (Connection freeConnection : freeConnections) {
                try {
                    freeConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
