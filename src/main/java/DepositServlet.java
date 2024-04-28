import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.sql.*;

public class DepositServlet extends HttpServlet {
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
            double amount = Double.parseDouble(request.getParameter("amount"));

            try {
            	Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE user_id = (SELECT id FROM users WHERE username = ?)");
                pstmt.setDouble(1, amount);
                pstmt.setString(2, username);
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
