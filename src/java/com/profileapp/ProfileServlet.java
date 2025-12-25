package com.profileapp;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

public class ProfileServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/Profile";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // For testing - show working message
        response.setContentType("text/html");
        response.getWriter().println("<h1>ProfileServlet is WORKING!</h1>");
        response.getWriter().println("<p>Accessed via GET method</p>");
        response.getWriter().println("<a href='index.html'>Go to Form</a>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            String name = request.getParameter("name");
            String studentId = request.getParameter("studentId");
            String program = request.getParameter("program");
            String email = request.getParameter("email");
            String hobbies = request.getParameter("hobbies");
            String introduction = request.getParameter("introduction");
            
            // Generate unique ID
            String id = "PROF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Create ProfileBean object
            ProfileBean profile = new ProfileBean();
            profile.setId(id);
            profile.setName(name);
            profile.setStudentId(studentId);
            profile.setProgram(program);
            profile.setEmail(email);
            profile.setHobbies(hobbies);
            profile.setIntroduction(introduction);
            
            // Save to database
            boolean success = saveToDatabase(profile);
            
            if (success) {
                request.setAttribute("profile", profile);
                RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("index.html?error=save_failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.html?error=server_error");
        }
    }
    
    private boolean saveToDatabase(ProfileBean profile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String sql = "INSERT INTO PROFILE (ID, NAME, STUDENT_ID, PROGRAM, EMAIL, HOBBIES, INTRODUCTION) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, profile.getId());
            pstmt.setString(2, profile.getName());
            pstmt.setString(3, profile.getStudentId());
            pstmt.setString(4, profile.getProgram());
            pstmt.setString(5, profile.getEmail());
            pstmt.setString(6, profile.getHobbies());
            pstmt.setString(7, profile.getIntroduction());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }
}