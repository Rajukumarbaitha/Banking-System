import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.sql.*;

public class BalanceServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/first";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            try {
            	Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM accounts WHERE user_id = (SELECT id FROM users WHERE username = ?)");
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    PrintWriter out = response.getWriter();
                    out.println(balance);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
