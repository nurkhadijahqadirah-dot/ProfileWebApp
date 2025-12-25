<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.profileapp.ProfileBean" %>
<%
    ProfileBean profile = (ProfileBean) request.getAttribute("profile");
    
    // Get first letter for avatar
    String avatarInitial = "";
    if (profile != null && profile.getName() != null && !profile.getName().isEmpty()) {
        avatarInitial = profile.getName().substring(0, 1).toUpperCase();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - <%= profile != null ? profile.getName() : "User" %></title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #fff8dc 0%, #f5f5dc 25%, #f5f5dc 50%, #f5f5dc 75%, #fff8dc 100%);
            background-size: 400% 400%;
            animation: gradientBG 15s ease infinite;
            margin: 0;
            padding: 20px;
        }

        @keyframes gradientBG {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .container {
            width: 100%;
            max-width: 800px;
            animation: slideIn 0.8s ease-out;
        }

        @keyframes slideIn {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .profile-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(15px);
            border-radius: 25px;
            padding: 40px;
            border: 1.5px solid rgba(139, 69, 19, 0.3);
            box-shadow: 0 20px 50px rgba(139, 69, 19, 0.2),
                        0 10px 30px rgba(128, 128, 0, 0.15);
        }

        .profile-header {
            text-align: center;
            margin-bottom: 40px;
            padding-bottom: 30px;
            border-bottom: 2px solid rgba(139, 69, 19, 0.2);
        }

        .profile-icon {
            width: 120px;
            height: 120px;
            background: linear-gradient(135deg, #8B4513, #A0522D);
            border-radius: 50%;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: white;
            font-weight: bold;
            box-shadow: 0 10px 30px rgba(139, 69, 19, 0.3);
        }

        .profile-header h1 {
            font-size: 2.5rem;
            font-weight: 800;
            background: linear-gradient(45deg, #8B4513 0%, #808000 50%, #8B4513 100%);
            background-size: 300% 300%;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            animation: gradientText 4s ease infinite;
            margin: 10px 0;
        }

        @keyframes gradientText {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .detail-group {
            margin-bottom: 30px;
        }

        .detail-group h3 {
            color: #8B4513;
            margin-bottom: 10px;
            font-size: 1.3rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .detail-group h3::before {
            content: '✦';
            color: #808000;
        }

        .detail-group p {
            background: rgba(245, 245, 220, 0.5);
            padding: 15px;
            border-radius: 10px;
            border-left: 4px solid #808000;
            line-height: 1.6;
            margin: 0;
        }

        .hobbies-list {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .hobby-tag {
            background: linear-gradient(135deg, 
                rgba(139, 69, 19, 0.1), 
                rgba(128, 128, 0, 0.1));
            color: #8B4513;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
            border: 2px solid rgba(139, 69, 19, 0.2);
            transition: all 0.3s ease;
        }

        .hobby-tag:hover {
            transform: translateY(-3px);
            background: linear-gradient(135deg, 
                rgba(139, 69, 19, 0.2), 
                rgba(128, 128, 0, 0.2));
            box-shadow: 0 5px 15px rgba(139, 69, 19, 0.2);
            border-color: #808000;
        }

        .btn-container {
            text-align: center;
            margin-top: 30px;
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            border-radius: 15px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            font-size: 1rem;
        }

        .btn-primary {
            background: #8B4513;
            color: white;
            box-shadow: 0 8px 25px rgba(139, 69, 19, 0.3);
        }

        .btn-primary:hover {
            background: #A0522D;
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(139, 69, 19, 0.4);
        }

        .btn-secondary {
            background: #808000;
            color: white;
            box-shadow: 0 8px 25px rgba(128, 128, 0, 0.3);
        }

        .btn-secondary:hover {
            background: #8f8f00;
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(128, 128, 0, 0.4);
        }

        .profile-id {
            background: rgba(139, 69, 19, 0.1);
            padding: 8px 15px;
            border-radius: 10px;
            font-size: 0.9rem;
            color: #8B4513;
            display: inline-block;
            margin-top: 10px;
        }

        @media (max-width: 768px) {
            .profile-container { padding: 25px; }
            .profile-header h1 { font-size: 2rem; }
            .profile-icon { width: 100px; height: 100px; font-size: 2.5rem; }
            .btn-container { flex-direction: column; }
            .btn { width: 100%; margin-bottom: 10px; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="profile-container">
            <div class="profile-header">
                <div class="profile-icon"><%= avatarInitial %></div>
                <h1><%= profile != null ? profile.getName() : "User" %></h1>
                <div style="color: #666; margin-bottom: 10px;">
                    Student ID: <%= profile != null ? profile.getStudentId() : "" %>
                </div>
                <% if (profile != null && profile.getId() != null) { %>
                    <div class="profile-id">Profile ID: <%= profile.getId() %></div>
                <% } %>
            </div>
            
            <div class="profile-details">
                <div class="detail-group">
                    <h3>Program</h3>
                    <p><%= profile != null ? profile.getProgram() : "" %></p>
                </div>
                
                <div class="detail-group">
                    <h3>Email</h3>
                    <p><%= profile != null ? profile.getEmail() : "" %></p>
                </div>
                
                <div class="detail-group">
                    <h3>Hobbies</h3>
                    <div class="hobbies-list">
                        <% 
                        if (profile != null && profile.getHobbies() != null) {
                            String[] hobbiesArray = profile.getHobbies().split(",");
                            for(String hobby : hobbiesArray) { 
                                String trimmedHobby = hobby.trim();
                                if(!trimmedHobby.isEmpty()) { 
                        %>
                            <span class="hobby-tag"><%= trimmedHobby %></span>
                        <% 
                                }
                            }
                        }
                        %>
                    </div>
                </div>
                
                <div class="detail-group">
                    <h3>Self Introduction</h3>
                    <p><%= profile != null && profile.getIntroduction() != null ? 
                          profile.getIntroduction().replace("\n", "<br>") : "" %></p>
                </div>
            </div>
            
            <div class="btn-container">
                <a href="index.html" class="btn btn-primary">Create Another Profile</a>
                <a href="<%= request.getContextPath() %>/ViewProfileServlet" class="btn btn-secondary">View All Profiles</a>
            </div>
        </div>
    </div>
</body>
</html>