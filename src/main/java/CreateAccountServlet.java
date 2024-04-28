import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.sql.*;

public class CreateAccountServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/first";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            String accountName = request.getParameter("account_name");
            String accountType = request.getParameter("account_type");

            try {
            	Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO accounts (account_name, account_type, user_id) VALUES (?, ?, (SELECT id FROM users WHERE username = ?))");
                pstmt.setString(1, accountName);
                pstmt.setString(2, accountType);
                pstmt.setString(3, username);
                pstmt.executeUpdate();

                pstmt.close();
                conn.close();

                response.sendRedirect("functions.html");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("login.html");
        }
    }
}
