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
    private final Set<Connection> usedConnections = new HashSet<>(MAX_AMOUNT_CONNECTIONS);

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private ConnectionPool() { //private Constructor
        this.freeConnections = new HashSet<>(MAX_AMOUNT_CONNECTIONS);
        for (int i = 0; i < MAX_AMOUNT_CONNECTIONS; i++) {
            freeConnections.add(getConnectionToFillThePool());
        }
    }

    private Connection getConnectionToFillThePool(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error");
        }
        return connection;
    }

    public synchronized Connection getConnection() {
        Connection connection = null;
        if (freeConnections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("DB synchronized connection error");
            }
        } else {
            connection = freeConnections.iterator().next(); /* get() from set by iterator().next() */
            usedConnections.add(connection);
            freeConnections.remove(connection);
        }
        return connection;
    }

    public synchronized void restoreConnection(Connection connection) {
        if (usedConnections.contains(connection)) { /* I check here if the connection that I return is the same connection that I received. */
            if (freeConnections.size() < MAX_AMOUNT_CONNECTIONS) {
                freeConnections.add(connection);
          /*  try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
                usedConnections.remove(connection);
                notify(); // TODO synchronized only a small block?
            }
        }
    }

    public void closeAllConnections() {
        if (usedConnections.isEmpty()) {
            for (Connection freeConnection : freeConnections) {
                try {
                    freeConnection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("DB close connection error");
                }
            }
        }
    }
}
