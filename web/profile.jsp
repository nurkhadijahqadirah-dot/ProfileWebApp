<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%
    // Get attributes from servlet
    String name = (String) request.getAttribute("name");
    String studentId = (String) request.getAttribute("studentId");
    String program = (String) request.getAttribute("program");
    String email = (String) request.getAttribute("email");
    String hobbies = (String) request.getAttribute("hobbies");
    String introduction = (String) request.getAttribute("introduction");
    
    // Split hobbies into array
    String[] hobbiesArray = hobbies != null ? hobbies.split(",") : new String[0];
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - <%= name %></title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="profile-container">
            <div class="profile-header">
    <div class="profile-icon"></div>
    <h1><%= name %></h1>
    <div class="student-id">Student ID: <%= studentId %></div>
</div>
            
            <div class="profile-details">
                <div class="detail-group">
                    <h3>Program</h3>
                    <p><%= program %></p>
                </div>
                
                <div class="detail-group">
                    <h3>Email</h3>
                    <p><%= email %></p>
                </div>
                
                <div class="detail-group">
                    <h3>Hobbies</h3>
                    <div class="hobbies-list">
                        <% for(String hobby : hobbiesArray) { 
                            String trimmedHobby = hobby.trim();
                            if(!trimmedHobby.isEmpty()) { %>
                                <span class="hobby-tag"><%= trimmedHobby %></span>
                        <%   }
                        } %>
                    </div>
                </div>
                
                <div class="detail-group">
                    <h3>Self Introduction</h3>
                    <p><%= introduction != null ? introduction.replace("\n", "<br>") : "" %></p>
                </div>
            </div>
            
            <div style="text-align: center;">
                <a href="index.html" class="back-btn">Create Another Profile</a>
            </div>
        </div>
    </div>
</body>
</html>
