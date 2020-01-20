package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import play.mvc.Controller;
import play.db.NamedDatabase;
import play.db.Database;

class JavaJdbcConnection {
    private Database db;
    private DatabaseExecutionContext executionContext;

    @Inject
    public JavaJdbcConnection(Database db, DatabaseExecutionContext executionContext) {
        this.db = db;
        this.executionContext = executionContext;
    }

    public CompletionStage<Void> updateSomething() {
        return CompletableFuture.runAsync(
                () -> {
                    // get jdbc connection
                    Connection connection = db.getConnection();
                    Statement st;
                    // do whatever you need with the db connection
                    try {
                        st = connection.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                    ResultSet rs = null;
                    try {
                        rs = st.executeQuery("SELECT * FROM public.restaurants");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    while (true)
                    {
                        try {
                            if (!rs.next()) break;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.print("Column 1 returned ");
                        try {
                            System.out.println(rs.getString(1));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                },
                executionContext);
    }
}