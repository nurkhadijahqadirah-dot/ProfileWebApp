package com.profileapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewProfileServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/Profile";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get search parameter if exists
        String searchTerm = request.getParameter("search");
        boolean isSearching = searchTerm != null && !searchTerm.trim().isEmpty();
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Start HTML with beautiful design
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>View All Profiles</title>");
        out.println("    <style>");
        
        // CSS with Brown (#8B4513) and Olive (#808000) theme
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            min-height: 100vh;");
        out.println("            display: flex;");
        out.println("            flex-direction: column;");
        out.println("            align-items: center;");
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
        out.println("            max-width: 1300px;");
        out.println("            margin: 20px auto;");
        out.println("            animation: slideIn 0.8s ease-out;");
        out.println("        }");
        out.println("        @keyframes slideIn {");
        out.println("            from { opacity: 0; transform: translateY(30px); }");
        out.println("            to { opacity: 1; transform: translateY(0); }");
        out.println("        }");
        out.println("        .header {");
        out.println("            text-align: center;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        .header h1 {");
        out.println("            font-size: 2.8rem;");
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
        out.println("        .search-container {");
        out.println("            background: rgba(255, 255, 255, 0.95);");
        out.println("            backdrop-filter: blur(15px);");
        out.println("            border-radius: 20px;");
        out.println("            padding: 25px 40px;");
        out.println("            border: 1.5px solid rgba(139, 69, 19, 0.3);");
        out.println("            box-shadow: 0 15px 40px rgba(139, 69, 19, 0.2),");
        out.println("                        0 8px 25px rgba(128, 128, 0, 0.15);");
        out.println("            margin-bottom: 30px;");
        out.println("            text-align: center;");
        out.println("        }");
        out.println("        .search-form {");
        out.println("            display: flex;");
        out.println("            gap: 15px;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            flex-wrap: wrap;");
        out.println("        }");
        out.println("        .search-input {");
        out.println("            flex: 1;");
        out.println("            max-width: 500px;");
        out.println("            padding: 16px 25px;");
        out.println("            border: 2px solid rgba(139, 69, 19, 0.3);");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1.1rem;");
        out.println("            background: rgba(255, 255, 255, 0.9);");
        out.println("            transition: all 0.3s ease;");
        out.println("            color: #333;");
        out.println("        }");
        out.println("        .search-input:focus {");
        out.println("            outline: none;");
        out.println("            border-color: #808000;");
        out.println("            box-shadow: 0 0 0 3px rgba(128, 128, 0, 0.2);");
        out.println("            transform: translateY(-2px);");
        out.println("        }");
        out.println("        .search-btn {");
        out.println("            background: #8B4513;");
        out.println("            color: white;");
        out.println("            border: none;");
        out.println("            padding: 16px 35px;");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1.1rem;");
        out.println("            font-weight: 600;");
        out.println("            cursor: pointer;");
        out.println("            transition: all 0.3s ease;");
        out.println("            box-shadow: 0 8px 25px rgba(139, 69, 19, 0.3);");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 10px;");
        out.println("        }");
        out.println("        .search-btn:hover {");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(139, 69, 19, 0.4);");
        out.println("            background: #A0522D;");
        out.println("        }");
        out.println("        .clear-btn {");
        out.println("            background: #808000;");
        out.println("            color: white;");
        out.println("            border: none;");
        out.println("            padding: 16px 25px;");
        out.println("            border-radius: 15px;");
        out.println("            font-size: 1.1rem;");
        out.println("            font-weight: 600;");
        out.println("            cursor: pointer;");
        out.println("            transition: all 0.3s ease;");
        out.println("            box-shadow: 0 8px 25px rgba(128, 128, 0, 0.3);");
        out.println("            text-decoration: none;");
        out.println("            display: inline-flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 10px;");
        out.println("        }");
        out.println("        .clear-btn:hover {");
        out.println("            transform: translateY(-3px);");
        out.println("            box-shadow: 0 15px 40px rgba(128, 128, 0, 0.4);");
        out.println("            background: #8f8f00;");
        out.println("        }");
        out.println("        .profiles-table-container {");
        out.println("            background: rgba(255, 255, 255, 0.95);");
        out.println("            backdrop-filter: blur(15px);");
        out.println("            border-radius: 25px;");
        out.println("            padding: 40px;");
        out.println("            border: 1.5px solid rgba(139, 69, 19, 0.3);");
        out.println("            box-shadow: 0 20px 50px rgba(139, 69, 19, 0.2),");
        out.println("                        0 10px 30px rgba(128, 128, 0, 0.15);");
        out.println("            overflow-x: auto;");
        out.println("            margin-bottom: 40px;");
        out.println("        }");
        out.println("        table {");
        out.println("            width: 100%;");
        out.println("            border-collapse: separate;");
        out.println("            border-spacing: 0;");
        out.println("        }");
        out.println("        th {");
        out.println("            background: linear-gradient(135deg, rgba(139, 69, 19, 0.1), rgba(128, 128, 0, 0.1));");
        out.println("            color: #8B4513;");
        out.println("            font-weight: 700;");
        out.println("            padding: 18px 15px;");
        out.println("            text-align: left;");
        out.println("            font-size: 1.1rem;");
        out.println("            border-bottom: 3px solid #808000;");
        out.println("        }");
        out.println("        td {");
        out.println("            padding: 16px 15px;");
        out.println("            border-bottom: 1px solid rgba(139, 69, 19, 0.2);");
        out.println("            color: #333;");
        out.println("            font-size: 1rem;");
        out.println("            vertical-align: top;");
        out.println("        }");
        out.println("        tr:hover td {");
        out.println("            background: rgba(245, 245, 220, 0.3);");
        out.println("        }");
        out.println("        .profile-id {");
        out.println("            background: rgba(139, 69, 19, 0.1);");
        out.println("            padding: 6px 12px;");
        out.println("            border-radius: 10px;");
        out.println("            font-size: 0.85rem;");
        out.println("            color: #8B4513;");
        out.println("            font-weight: 600;");
        out.println("        }");
        out.println("        .student-id {");
        out.println("            color: #8B4513;");
        out.println("            font-weight: 600;");
        out.println("        }");
        out.println("        .hobbies-list {");
        out.println("            display: flex;");
        out.println("            flex-wrap: wrap;");
        out.println("            gap: 8px;");
        out.println("            margin-top: 8px;");
        out.println("        }");
        out.println("        .hobby-tag {");
        out.println("            background: linear-gradient(135deg, rgba(139, 69, 19, 0.1), rgba(128, 128, 0, 0.1));");
        out.println("            color: #8B4513;");
        out.println("            padding: 5px 10px;");
        out.println("            border-radius: 12px;");
        out.println("            font-size: 0.8rem;");
        out.println("            font-weight: 600;");
        out.println("            border: 1px solid rgba(139, 69, 19, 0.2);");
        out.println("        }");
        out.println("        .introduction {");
        out.println("            max-height: 100px;");
        out.println("            overflow-y: auto;");
        out.println("            color: #555;");
        out.println("            line-height: 1.5;");
        out.println("            font-size: 0.9rem;");
        out.println("        }");
        out.println("        .email {");
        out.println("            color: #8B4513;");
        out.println("            font-weight: 500;");
        out.println("        }");
        out.println("        .search-highlight {");
        out.println("            background-color: #f5f5dc;");
        out.println("            padding: 2px 4px;");
        out.println("            border-radius: 4px;");
        out.println("            font-weight: bold;");
        out.println("            color: #8B4513;");
        out.println("        }");
        out.println("        .action-buttons {");
        out.println("            display: flex;");
        out.println("            flex-direction: column;");
        out.println("            gap: 10px;");
        out.println("            min-width: 150px;");
        out.println("        }");
        out.println("        .edit-btn {");
        out.println("            background: #808000;");
        out.println("            color: white;");
        out.println("            padding: 10px 20px;");
        out.println("            border-radius: 10px;");
        out.println("            text-decoration: none;");
        out.println("            font-weight: 600;");
        out.println("            font-size: 0.9rem;");
        out.println("            box-shadow: 0 4px 15px rgba(128, 128, 0, 0.3);");
        out.println("            transition: all 0.3s ease;");
        out.println("            border: none;");
        out.println("            cursor: pointer;");
        out.println("            text-align: center;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 8px;");
        out.println("        }");
        out.println("        .edit-btn:hover {");
        out.println("            transform: translateY(-2px);");
        out.println("            box-shadow: 0 6px 20px rgba(128, 128, 0, 0.4);");
        out.println("            background: #8f8f00;");
        out.println("        }");
        out.println("        .delete-btn {");
        out.println("            background: #8B4513;");
        out.println("            color: white;");
        out.println("            padding: 10px 20px;");
        out.println("            border-radius: 10px;");
        out.println("            text-decoration: none;");
        out.println("            font-weight: 600;");
        out.println("            font-size: 0.9rem;");
        out.println("            box-shadow: 0 4px 15px rgba(139, 69, 19, 0.3);");
        out.println("            transition: all 0.3s ease;");
        out.println("            border: none;");
        out.println("            cursor: pointer;");
        out.println("            text-align: center;");
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            gap: 8px;");
        out.println("        }");
        out.println("        .delete-btn:hover {");
        out.println("            transform: translateY(-2px);");
        out.println("            box-shadow: 0 6px 20px rgba(139, 69, 19, 0.4);");
        out.println("            background: #A0522D;");
        out.println("        }");
        out.println("        .btn-container {");
        out.println("            text-align: center;");
        out.println("            margin-top: 30px;");
        out.println("            display: flex;");
        out.println("            gap: 20px;");
        out.println("            justify-content: center;");
        out.println("        }");
        out.println("        .btn {");
        out.println("            display: inline-block;");
        out.println("            padding: 15px 40px;");
        out.println("            border-radius: 15px;");
        out.println("            font-weight: 600;");
        out.println("            text-decoration: none;");
        out.println("            transition: all 0.3s ease;");
        out.println("            font-size: 1.1rem;");
        out.println("        }");
        out.println("        .btn-primary {");
        out.println("            background: #8B4513;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 10px 30px rgba(139, 69, 19, 0.3);");
        out.println("        }");
        out.println("        .btn-primary:hover {");
        out.println("            background: #A0522D;");
        out.println("            transform: translateY(-3px);");
        out.println("        }");
        out.println("        .btn-secondary {");
        out.println("            background: #808000;");
        out.println("            color: white;");
        out.println("            box-shadow: 0 10px 30px rgba(128, 128, 0, 0.3);");
        out.println("        }");
        out.println("        .btn-secondary:hover {");
        out.println("            background: #8f8f00;");
        out.println("            transform: translateY(-3px);");
        out.println("        }");
        out.println("        .btn:hover {");
        out.println("            transform: translateY(-3px);");
        out.println("        }");
        out.println("        .success-message {");
        out.println("            background: rgba(128, 128, 0, 0.15);");
        out.println("            border: 1px solid rgba(128, 128, 0, 0.3);");
        out.println("            color: #556b2f;");
        out.println("            padding: 15px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-bottom: 20px;");
        out.println("            text-align: center;");
        out.println("            font-weight: 600;");
        out.println("        }");
        out.println("        .error-message {");
        out.println("            background: rgba(139, 69, 19, 0.1);");
        out.println("            border: 1px solid rgba(139, 69, 19, 0.3);");
        out.println("            color: #8B4513;");
        out.println("            padding: 15px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-bottom: 20px;");
        out.println("            text-align: center;");
        out.println("            font-weight: 600;");
        out.println("        }");
        out.println("        @media (max-width: 768px) {");
        out.println("            .container { padding: 10px; }");
        out.println("            .profiles-table-container { padding: 20px; }");
        out.println("            .header h1 { font-size: 2rem; }");
        out.println("            .search-container { padding: 20px; }");
        out.println("            .search-form { flex-direction: column; }");
        out.println("            .search-input { max-width: 100%; width: 100%; }");
        out.println("            .btn-container { flex-direction: column; }");
        out.println("            .btn { width: 100%; margin-bottom: 10px; }");
        out.println("            .search-btn, .clear-btn { width: 100%; }");
        out.println("            th, td { padding: 12px 10px; font-size: 0.9rem; }");
        out.println("            .action-buttons { flex-direction: row; }");
        out.println("            .edit-btn, .delete-btn { flex: 1; }");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("    <div class='container'>");
        out.println("        <div class='header'>");
        out.println("            <h1>All Student Profiles</h1>");
        out.println("        </div>");
        
        // Search Form
        out.println("        <div class='search-container'>");
        out.println("            <form method='GET' action='ViewProfileServlet' class='search-form'>");
        out.println("                <input type='text' name='search' class='search-input' ");
        out.println("                       placeholder='Search by name, student ID, program, or email...' ");
        if (isSearching) {
            out.println("                       value='" + escapeHtml(searchTerm) + "' ");
        }
        out.println("                />");
        out.println("                <button type='submit' class='search-btn'>");
        out.println("                    Search");
        out.println("                </button>");
        if (isSearching) {
            out.println("                <a href='ViewProfileServlet' class='clear-btn'>");
            out.println("                   Clear Search");
            out.println("                </a>");
        }
        out.println("            </form>");
        out.println("        </div>");
        
        try {
            // Step 1: Load driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            
            // Step 2: Connect to database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Step 3: Query database with search
            Statement stmt = conn.createStatement();
            String sql;
            
            if (isSearching) {
                // Build search query
                String searchPattern = "%" + searchTerm + "%";
                sql = "SELECT * FROM PROFILE WHERE " +
                      "UPPER(NAME) LIKE UPPER('" + searchPattern.replace("'", "''") + "') OR " +
                      "UPPER(STUDENT_ID) LIKE UPPER('" + searchPattern.replace("'", "''") + "') OR " +
                      "UPPER(PROGRAM) LIKE UPPER('" + searchPattern.replace("'", "''") + "') OR " +
                      "UPPER(EMAIL) LIKE UPPER('" + searchPattern.replace("'", "''") + "') OR " +
                      "UPPER(HOBBIES) LIKE UPPER('" + searchPattern.replace("'", "''") + "') " +
                      "ORDER BY NAME";
            } else {
                sql = "SELECT * FROM PROFILE ORDER BY NAME";
            }
            
            ResultSet rs = stmt.executeQuery(sql);
            
            // Collect profiles
            List<ProfileBean> profiles = new ArrayList<>();
            int count = 0;
            
            while (rs.next()) {
                count++;
                ProfileBean profile = new ProfileBean();
                profile.setId(rs.getString("ID"));
                profile.setName(rs.getString("NAME"));
                profile.setStudentId(rs.getString("STUDENT_ID"));
                profile.setProgram(rs.getString("PROGRAM"));
                profile.setEmail(rs.getString("EMAIL"));
                profile.setHobbies(rs.getString("HOBBIES"));
                profile.setIntroduction(rs.getString("INTRODUCTION"));
                profiles.add(profile);
            }
            
            // Display appropriate messages 
            if (count > 0) {
                if (isSearching) {
                    out.println("        <div class='success-message'>");
                    out.println("           Found " + count + " matching profile(s) for \"" + escapeHtml(searchTerm) + "\"");
                    out.println("        </div>");
                } else {
                    out.println("        <div class='success-message'>");
                    out.println("            Successfully retrieved " + count + " profiles from database");
                    out.println("        </div>");
                }
            } else {
                if (isSearching) {
                    out.println("        <div class='error-message'>");
                    out.println("            No profiles found matching \"" + escapeHtml(searchTerm) + "\"");
                    out.println("            <p style='font-size: 0.9rem; margin-top: 10px; color: #666;'>");
                    out.println("                Try searching by: name, student ID, program, email, or hobby");
                    out.println("            </p>");
                    out.println("        </div>");
                } else {
                    out.println("        <div class='error-message'>");
                    out.println("            No profiles found in database. Please create some profiles first.");
                    out.println("        </div>");
                }
            }
            
            // Display table if there are profiles
            if (count > 0) {
                out.println("        <div class='profiles-table-container'>");
                out.println("            <table>");
                out.println("                <thead>");
                out.println("                    <tr>");
                out.println("                        <th>ID</th>");
                out.println("                        <th>Name</th>");
                out.println("                        <th>Student ID</th>");
                out.println("                        <th>Email</th>");
                out.println("                        <th>Program</th>");
                out.println("                        <th>Hobbies</th>");
                out.println("                        <th>Introduction</th>");
                out.println("                        <th>Actions</th>");
                out.println("                    </tr>");
                out.println("                </thead>");
                out.println("                <tbody>");
                
                for (ProfileBean profile : profiles) {
                    out.println("                <tr>");
                    
                    // ID column
                    out.println("                    <td>");
                    out.println("                        <span class='profile-id'>" + 
                              escapeHtml(profile.getId()) + "</span>");
                    out.println("                    </td>");
                    
                    // Name column
                    String name = profile.getName() != null ? profile.getName() : "N/A";
                    if (isSearching) {
                        name = highlightText(name, searchTerm);
                    } else {
                        name = escapeHtml(name);
                    }
                    out.println("                    <td><strong>" + name + "</strong></td>");
                    
                    // Student ID column
                    String studentId = profile.getStudentId() != null ? profile.getStudentId() : "N/A";
                    if (isSearching) {
                        studentId = highlightText(studentId, searchTerm);
                    } else {
                        studentId = escapeHtml(studentId);
                    }
                    out.println("                    <td>");
                    out.println("                        <span class='student-id'>" + studentId + "</span>");
                    out.println("                    </td>");
                    
                    // Email column
                    String email = profile.getEmail() != null ? profile.getEmail() : "N/A";
                    if (isSearching) {
                        email = highlightText(email, searchTerm);
                    } else {
                        email = escapeHtml(email);
                    }
                    out.println("                    <td>");
                    out.println("                        <span class='email'>" + email + "</span>");
                    out.println("                    </td>");
                    
                    // Program column
                    String program = profile.getProgram() != null ? profile.getProgram() : "N/A";
                    if (isSearching) {
                        program = highlightText(program, searchTerm);
                    } else {
                        program = escapeHtml(program);
                    }
                    out.println("                    <td>" + program + "</td>");
                    
                    // Hobbies column
                    out.println("                    <td>");
                    if (profile.getHobbies() != null && !profile.getHobbies().trim().isEmpty()) {
                        String[] hobbies = profile.getHobbies().split(",");
                        out.println("                        <div class='hobbies-list'>");
                        for (String hobby : hobbies) {
                            String trimmedHobby = hobby.trim();
                            if (!trimmedHobby.isEmpty()) {
                                String displayHobby = trimmedHobby;
                                if (isSearching) {
                                    displayHobby = highlightText(displayHobby, searchTerm);
                                } else {
                                    displayHobby = escapeHtml(displayHobby);
                                }
                                out.println("                            <span class='hobby-tag'>" + displayHobby + "</span>");
                            }
                        }
                        out.println("                        </div>");
                    } else {
                        out.println("                        <em>No hobbies listed</em>");
                    }
                    out.println("                    </td>");
                    
                    // Introduction column
                    out.println("                    <td>");
                    out.println("                        <div class='introduction'>");
                    if (profile.getIntroduction() != null && !profile.getIntroduction().trim().isEmpty()) {
                        String intro = profile.getIntroduction();
                        if (isSearching) {
                            intro = highlightText(intro, searchTerm);
                            intro = intro.replace("\n", "<br>").replace("\r", "");
                        } else {
                            intro = escapeHtml(intro).replace("\n", "<br>").replace("\r", "");
                        }
                        out.println("                            " + intro);
                    } else {
                        out.println("                            <em>No introduction provided</em>");
                    }
                    out.println("                        </div>");
                    out.println("                    </td>");
                    
                    // ACTIONS COLUMN - Edit and Delete buttons
                    out.println("                    <td>");
                    out.println("                        <div class='action-buttons'>");
                    out.println("                            <a href='EditDeleteServlet?action=edit&id=" + escapeHtml(profile.getId()) + "' class='edit-btn'>");
                    out.println("                                Edit");
                    out.println("                            </a>");
                    out.println("                            <a href='EditDeleteServlet?action=delete&id=" + escapeHtml(profile.getId()) + "' class='delete-btn'");
                    out.println("                               onclick=\"return confirm('Are you sure you want to delete this profile? This action cannot be undone.');\">");
                    out.println("                                Delete");
                    out.println("                            </a>");
                    out.println("                        </div>");
                    out.println("                    </td>");
                    
                    out.println("                </tr>");
                }
                
                out.println("                </tbody>");
                out.println("            </table>");
                out.println("        </div>");
                
            }
            
            // Close resources
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (ClassNotFoundException e) {
            out.println("        <div class='error-message'>");
            out.println("            Database Driver Error");
            out.println("            <p>Message: " + e.getMessage() + "</p>");
            out.println("            <p><strong>Fix:</strong> Add derbyclient.jar to your project libraries</p>");
            out.println("        </div>");
            
        } catch (SQLException e) {
            out.println("        <div class='error-message'>");
            out.println("            Database Connection Error");
            out.println("            <p>Message: " + e.getMessage() + "</p>");
            out.println("            <p>URL: " + DB_URL + "</p>");
            out.println("            <p>User: " + DB_USER + "</p>");
            out.println("        </div>");
            
        } catch (Exception e) {
            out.println("        <div class='error-message'>");
            out.println("            Unexpected Error");
            out.println("            <p>Message: " + e.getMessage() + "</p>");
            out.println("        </div>");
        }
        
        // Navigation buttons
        out.println("        <div class='btn-container'>");
        out.println("            <a href='index.html' class='btn btn-primary'>Create New Profile</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    // Helper method to highlight search terms
    private String highlightText(String text, String searchTerm) {
        if (text == null || searchTerm == null || searchTerm.isEmpty()) {
            return escapeHtml(text);
        }
        
        String escapedText = escapeHtml(text);
        String lowerText = escapedText.toLowerCase();
        String lowerSearch = searchTerm.toLowerCase();
        
        if (lowerText.contains(lowerSearch)) {
            int index = lowerText.indexOf(lowerSearch);
            String before = escapedText.substring(0, index);
            String match = escapedText.substring(index, index + searchTerm.length());
            String after = escapedText.substring(index + searchTerm.length());
            return before + "<span class='search-highlight'>" + match + "</span>" + after;
        }
        
        return escapedText;
    }
    
    // Helper method to escape HTML special characters
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}