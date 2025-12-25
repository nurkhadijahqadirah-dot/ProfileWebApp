package com.profileapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditDeleteServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/Profile";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        if (action == null || id == null) {
            response.sendRedirect("ViewProfileServlet");
            return;
        }
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            if (action.equals("edit")) {
                // Display edit form
                ProfileBean profile = getProfileById(id, conn);
                if (profile != null) {
                    displayEditForm(out, profile);
                } else {
                    out.println("<h1>Profile not found!</h1>");
                    out.println("<a href='ViewProfileServlet'>Back to Profiles</a>");
                }
                
            } else if (action.equals("delete")) {
                // Confirm delete
                ProfileBean profile = getProfileById(id, conn);
                if (profile != null) {
                    displayDeleteConfirmation(out, profile);
                } else {
                    out.println("<h1>Profile not found!</h1>");
                    out.println("<a href='ViewProfileServlet'>Back to Profiles</a>");
                }
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
            out.println("<a href='ViewProfileServlet'>Back to Profiles</a>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            if (action.equals("update")) {
                // Update profile
                updateProfile(request, conn);
                response.sendRedirect("ViewProfileServlet?message=updated");
                
            } else if (action.equals("confirmDelete")) {
                // Delete profile
                deleteProfile(id, conn);
                response.sendRedirect("ViewProfileServlet?message=deleted");
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProfileServlet?error=operation_failed");
        }
    }
    
    private ProfileBean getProfileById(String id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM PROFILE WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            ProfileBean profile = new ProfileBean();
            profile.setId(rs.getString("ID"));
            profile.setName(rs.getString("NAME"));
            profile.setStudentId(rs.getString("STUDENT_ID"));
            profile.setProgram(rs.getString("PROGRAM"));
            profile.setEmail(rs.getString("EMAIL"));
            profile.setHobbies(rs.getString("HOBBIES"));
            profile.setIntroduction(rs.getString("INTRODUCTION"));
            return profile;
        }
        return null;
    }
    
    private void updateProfile(HttpServletRequest request, Connection conn) throws SQLException {
        String sql = "UPDATE PROFILE SET NAME = ?, STUDENT_ID = ?, PROGRAM = ?, " +
                     "EMAIL = ?, HOBBIES = ?, INTRODUCTION = ? WHERE ID = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, request.getParameter("name"));
        pstmt.setString(2, request.getParameter("studentId"));
        pstmt.setString(3, request.getParameter("program"));
        pstmt.setString(4, request.getParameter("email"));
        pstmt.setString(5, request.getParameter("hobbies"));
        pstmt.setString(6, request.getParameter("introduction"));
        pstmt.setString(7, request.getParameter("id"));
        
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    private void deleteProfile(String id, Connection conn) throws SQLException {
        String sql = "DELETE FROM PROFILE WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    private void displayEditForm(PrintWriter out, ProfileBean profile) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Edit Profile</title>");
        out.println("    <style>");
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            min-height: 100vh;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            background: linear-gradient(135deg, #fff8dc 0%, #f5f5dc 25%, #f5f5dc 50%, #f5f5dc 75%, #fff8dc 100%);");
        out.println("            background-size: 400% 400%;");
        out.println("            animation: gradientBG 15s ease infinite;");
        out.println("            margin: 0;");
        out.println("            padding: 20px;");
        out.println("        }");
        out.println("        @keyframes gradientBG {");
        out.println("            0% { background-position: 0% 50%; }");
        out.println("            50% { background-position: 100% 50%; }");
        out.println("            100% { background-position: 0% 50%; }");
        out.println("        }");
        out.println("        .container {");
        out.println("            width: 100%;");
        out.println("            max-width: 800px;");
        out.println("            animation: slideIn 0.8s ease-out;");
        out.println("        }");
        out.println("        @keyframes slideIn {");
        out.println("            from { opacity: 0; transform: translateY(30px); }");
        out.println("            to { opacity: 1; transform: translateY(0); }");
        out.println("        }");
        out.println("        .form-wrapper {");
        out.println("            background: rgba(255, 255, 255, 0.95);");
        out.println("            backdrop-filter: blur(15px);");
        out.println("            border-radius: 25px;");
        out.println("            padding: 40px;");
        out.println("            border: 1.5px solid rgba(139, 69, 19, 0.3);");
        out.println("            box-shadow: 0 20px 50px rgba(139, 69, 19, 0.2),");
        out.println("                        0 10px 30px rgba(128, 128, 0, 0.15);");
        out.println("        }");
        out.println("        h1 {");
        out.println("            text-align: center;");
        out.println("            font-size: 2.5rem;");
        out.println("            font-weight: 800;");
        out.println("            background: linear-gradient(45deg, #8B4513 0%, #808000 50%, #8B4513 100%);");
        out.println("            background-size: 300% 300%;");
        out.println("            -webkit-background-clip: text;");
        out.println("            -webkit-text-fill-color: transparent;");
        out.println("            background-clip: text;");
        out.println("            animation: gradientText 4s ease infinite;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        @keyframes gradientText {");
        out.println("            0% { background-position: 0% 50%; }");
        out.println("            50% { background-position: 100% 50%; }");
        out.println("            100% { background-position: 0% 50%; }");
        out.println("        }");
        out.println("        .form-group { margin-bottom: 25px; }");
        out.println("        .form-group label {");
        out.println("            display: block;");
        out.println("            margin-bottom: 8px;");
        out.println("            color: #8B4513;");
        out.println("            font-weight: 600;");
        out.println("            font-size: 1.1rem;");
        out.println("        }");
        out.println("        .form-group input, .form-group select, .form-group textarea {");
        out.println("            width: 100%;");
        out.println("            padding: 15px;");
        out.println("            border: 2px solid rgba(139, 69, 19, 0.3);");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1rem;");
        out.println("            background: rgba(255, 255, 255, 0.9);");
        out.println("            transition: all 0.3s ease;");
        out.println("            color: #333;");
        out.println("            box-sizing: border-box;");
        out.println("        }");
        out.println("        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {");
        out.println("            outline: none;");
        out.println("            border-color: #808000;");
        out.println("            box-shadow: 0 0 0 3px rgba(128, 128, 0, 0.2);");
        out.println("            transform: translateY(-2px);");
        out.println("        }");
        out.println("        .btn-container {");
        out.println("            display: flex;");
        out.println("            gap: 15px;");
        out.println("            margin-top: 30px;");
        out.println("        }");
        out.println("        .btn {");
        out.println("            flex: 1;");
        out.println("            padding: 15px;");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1.1rem;");
        out.println("            font-weight: 600;");
        out.println("            cursor: pointer;");
        out.println("            border: none;");
        out.println("            text-decoration: none;");
        out.println("            text-align: center;");
        out.println("            transition: all 0.3s ease;");
        out.println("        }");
        out.println("        .btn-save {");
        out.println("            background: #808000;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 8px 25px rgba(128, 128, 0, 0.3);");
        out.println("        }");
        out.println("        .btn-save:hover {");
        out.println("            background: #8f8f00;");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(128, 128, 0, 0.4);");
        out.println("        }");
        out.println("        .btn-cancel {");
        out.println("            background: #8B4513;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 8px 25px rgba(139, 69, 19, 0.3);");
        out.println("        }");
        out.println("        .btn-cancel:hover {");
        out.println("            background: #A0522D;");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(139, 69, 19, 0.4);");
        out.println("        }");
        out.println("        .btn:hover { transform: translateY(-3px); }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='form-wrapper'>");
        out.println("            <h1>Edit Profile</h1>");
        out.println("            <form action='EditDeleteServlet' method='POST'>");
        out.println("                <input type='hidden' name='action' value='update'>");
        out.println("                <input type='hidden' name='id' value='" + profile.getId() + "'>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='name'>Full Name:</label>");
        out.println("                    <input type='text' id='name' name='name' value='" + 
                  (profile.getName() != null ? profile.getName().replace("'", "&#39;") : "") + "' required>");
        out.println("                </div>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='studentId'>Student ID:</label>");
        out.println("                    <input type='text' id='studentId' name='studentId' value='" + 
                  (profile.getStudentId() != null ? profile.getStudentId().replace("'", "&#39;") : "") + "' required>");
        out.println("                </div>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='program'>Program:</label>");
        out.println("                    <select id='program' name='program' required>");
        String[] programs = {"Computer Science", "Information Technology", "Software Engineering", "Data Science", "Cybersecurity"};
        for (String program : programs) {
            out.println("                        <option value='" + program + "'" + 
                      (program.equals(profile.getProgram()) ? " selected" : "") + ">" + program + "</option>");
        }
        out.println("                    </select>");
        out.println("                </div>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='email'>Email:</label>");
        out.println("                    <input type='email' id='email' name='email' value='" + 
                  (profile.getEmail() != null ? profile.getEmail().replace("'", "&#39;") : "") + "' required>");
        out.println("                </div>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='hobbies'>Hobbies:</label>");
        out.println("                    <textarea id='hobbies' name='hobbies' rows='3'>" + 
                  (profile.getHobbies() != null ? profile.getHobbies().replace("'", "&#39;") : "") + "</textarea>");
        out.println("                </div>");
        
        out.println("                <div class='form-group'>");
        out.println("                    <label for='introduction'>Self Introduction:</label>");
        out.println("                    <textarea id='introduction' name='introduction' rows='5' required>" + 
                  (profile.getIntroduction() != null ? profile.getIntroduction().replace("'", "&#39;") : "") + "</textarea>");
        out.println("                </div>");
        
        out.println("                <div class='btn-container'>");
        out.println("                    <button type='submit' class='btn btn-save'>Save Changes</button>");
        out.println("                    <a href='ViewProfileServlet' class='btn btn-cancel'>Cancel</a>");
        out.println("                </div>");
        out.println("            </form>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void displayDeleteConfirmation(PrintWriter out, ProfileBean profile) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Delete Profile</title>");
        out.println("    <style>");
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            min-height: 100vh;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            background: linear-gradient(135deg, #fff8dc 0%, #f5f5dc 25%, #f5f5dc 50%, #f5f5dc 75%, #fff8dc 100%);");
        out.println("            background-size: 400% 400%;");
        out.println("            animation: gradientBG 15s ease infinite;");
        out.println("            margin: 0;");
        out.println("            padding: 20px;");
        out.println("        }");
        out.println("        @keyframes gradientBG {");
        out.println("            0% { background-position: 0% 50%; }");
        out.println("            50% { background-position: 100% 50%; }");
        out.println("            100% { background-position: 0% 50%; }");
        out.println("        }");
        out.println("        .container {");
        out.println("            width: 100%;");
        out.println("            max-width: 500px;");
        out.println("            animation: slideIn 0.8s ease-out;");
        out.println("        }");
        out.println("        @keyframes slideIn {");
        out.println("            from { opacity: 0; transform: translateY(30px); }");
        out.println("            to { opacity: 1; transform: translateY(0); }");
        out.println("        }");
        out.println("        .warning-container {");
        out.println("            background: rgba(255, 255, 255, 0.95);");
        out.println("            backdrop-filter: blur(15px);");
        out.println("            border-radius: 25px;");
        out.println("            padding: 40px;");
        out.println("            border: 1.5px solid rgba(139, 69, 19, 0.3);");
        out.println("            box-shadow: 0 20px 50px rgba(139, 69, 19, 0.2),");
        out.println("                        0 10px 30px rgba(128, 128, 0, 0.15);");
        out.println("            text-align: center;");
        out.println("        }");
        out.println("        h1 {");
        out.println("            font-size: 2.2rem;");
        out.println("            font-weight: 800;");
        out.println("            background: linear-gradient(45deg, #8B4513 0%, #808000 50%, #8B4513 100%);");
        out.println("            background-size: 300% 300%;");
        out.println("            -webkit-background-clip: text;");
        out.println("            -webkit-text-fill-color: transparent;");
        out.println("            background-clip: text;");
        out.println("            animation: gradientText 4s ease infinite;");
        out.println("            margin-bottom: 20px;");
        out.println("        }");
        out.println("        @keyframes gradientText {");
        out.println("            0% { background-position: 0% 50%; }");
        out.println("            50% { background-position: 100% 50%; }");
        out.println("            100% { background-position: 0% 50%; }");
        out.println("        }");
        out.println("        .warning {");
        out.println("            background: rgba(255, 193, 7, 0.1);");
        out.println("            border: 1px solid rgba(255, 193, 7, 0.3);");
        out.println("            padding: 20px;");
        out.println("            border-radius: 15px;");
        out.println("            margin: 20px 0;");
        out.println("        }");
        out.println("        .warning h3 {");
        out.println("            color: #8B4513;");
        out.println("            margin-top: 0;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 10px;");
        out.println("        }");
        out.println("        .warning p {");
        out.println("            color: #333;");
        out.println("            font-size: 1.1rem;");
        out.println("            line-height: 1.5;");
        out.println("        }");
        out.println("        .btn-container {");
        out.println("            display: flex;");
        out.println("            gap: 15px;");
        out.println("            margin-top: 30px;");
        out.println("        }");
        out.println("        .btn {");
        out.println("            flex: 1;");
        out.println("            padding: 15px;");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1.1rem;");
        out.println("            font-weight: 600;");
        out.println("            cursor: pointer;");
        out.println("            border: none;");
        out.println("            text-decoration: none;");
        out.println("            text-align: center;");
        out.println("            transition: all 0.3s ease;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 10px;");
        out.println("        }");
        out.println("        .btn-delete {");
        out.println("            background: #8B4513;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 8px 25px rgba(139, 69, 19, 0.3);");
        out.println("        }");
        out.println("        .btn-delete:hover {");
        out.println("            background: #A0522D;");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(139, 69, 19, 0.4);");
        out.println("        }");
        out.println("        .btn-cancel {");
        out.println("            background: #808000;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 8px 25px rgba(128, 128, 0, 0.3);");
        out.println("        }");
        out.println("        .btn-cancel:hover {");
        out.println("            background: #8f8f00;");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(128, 128, 0, 0.4);");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='warning-container'>");
        out.println("            <h1>Delete Profile</h1>");
        out.println("            <div class='warning'>");
        out.println("                <h3>Warning: This action cannot be undone!</h3>");
        out.println("                <p>Are you sure you want to delete the profile of <strong>" + 
                      (profile.getName() != null ? profile.getName() : "Unknown") + "</strong>?</p>");
        out.println("                <p style='font-size: 0.9rem; color: #666; margin-top: 10px;'>");
        out.println("                    All data including name, student ID, program, email, hobbies, and introduction will be permanently removed.");
        out.println("                </p>");
        out.println("            </div>");
        out.println("            <div class='btn-container'>");
        out.println("                <form action='EditDeleteServlet' method='POST' style='display: inline; width: 100%;'>");
        out.println("                    <input type='hidden' name='action' value='confirmDelete'>");
        out.println("                    <input type='hidden' name='id' value='" + profile.getId() + "'>");
        out.println("                    <button type='submit' class='btn btn-delete'>");
        out.println("                           Delete Permanently");
        out.println("                    </button>");
        out.println("                </form>");
        out.println("                <a href='ViewProfileServlet' class='btn btn-cancel'>");
        out.println("                     Cancel");
        out.println("                </a>");
        out.println("            </div>");
        out.println("            <p style='font-size: 0.85rem; color: #888; margin-top: 20px;'>");
        out.println("                Note: Deleted profiles cannot be recovered.");
        out.println("            </p>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
}