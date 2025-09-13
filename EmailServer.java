// Simple Java Server for storing emails
// This is a basic server that students can easily understand

import java.io.*;  // For reading and writing data
import java.net.*; // For network connections

public class EmailServer {
    
    // This is where we store the emails (like a list)
    private static String[] emails = new String[100]; // Array to store up to 100 emails
    private static int emailCount = 0; // Keep track of how many emails we have
    
    // Main method - this is where our program starts
    public static void main(String[] args) {
        System.out.println("=== Simple Email Server Starting ===");
        System.out.println("Server will run on port 8080");
        System.out.println("Open your browser and go to: http://localhost:8080");
        
        try {
            // Create a server that listens on port 8080
            ServerSocket server = new ServerSocket(8080);  
            System.out.println("Server is ready and waiting for connections...");
            
            // Keep the server running forever
            while (true) {
                // Wait for someone to connect to our server
                Socket client = server.accept();
                System.out.println("Someone connected from: " + client.getInetAddress());
                
                // Handle the request from this client
                handleRequest(client);
                
                // Close the connection
                client.close();
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // This method handles what happens when someone connects
    private static void handleRequest(Socket client) {
        try {
            // Create readers and writers to talk to the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            
            // Read the first line of the request (this tells us what they want)
            String requestLine = reader.readLine();
            System.out.println("Request: " + requestLine);
            
            // Check what type of request this is
            if (requestLine != null) {
                if (requestLine.startsWith("GET /")) {
                    // Someone wants to see our webpage
                    sendWebPage(writer);
                } else if (requestLine.startsWith("POST /submit")) {
                    // Someone is submitting an email
                    handleEmailSubmission(reader, writer);
                } else {
                    // We don't know what they want
                    sendNotFound(writer);
                }
            }
            
            writer.close();
            reader.close();
            
        } catch (Exception e) {
            System.out.println("Error handling request: " + e.getMessage());
        }
    }
    
    // Send a simple webpage back to the browser
    private static void sendWebPage(PrintWriter writer) {
        String html = "<!DOCTYPE html>" +
                     "<html><head><title>Email Server</title></head>" +
                     "<body>" +
                     "<h1>Welcome to our Email Server!</h1>" +
                     "<p>Total emails stored: " + emailCount + "</p>" +
                     "<h2>Stored Emails:</h2>" +
                     "<ul>";
        
        // Add all stored emails to the webpage
        for (int i = 0; i < emailCount; i++) {
            html += "<li>" + emails[i] + "</li>";
        }
        
        html += "</ul></body></html>";
        
        // Send the HTTP response
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html");
        writer.println("Content-Length: " + html.length());
        writer.println();
        writer.println(html);
        writer.flush();
    }
    
    // Handle when someone submits an email
    private static void handleEmailSubmission(BufferedReader reader, PrintWriter writer) {
        try {
            // Read all the request data
            String line;
            String email = "";
            String name = "";
            
            // Skip the headers until we find an empty line
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println("Header: " + line);
            }
            
            // Read the email data
            if ((line = reader.readLine()) != null) {
                System.out.println("Email data: " + line);
                
                // Parse email and name from form data
                if (line.contains("email=")) {
                    email = line.substring(line.indexOf("email=") + 6);
                    // Remove any extra data after the email
                    if (email.contains("&")) {
                        email = email.substring(0, email.indexOf("&"));
                    }
                }
                
                if (line.contains("name=")) {
                    name = line.substring(line.indexOf("name=") + 5);
                    // Remove any extra data after the name
                    if (name.contains("&")) {
                        name = name.substring(0, name.indexOf("&"));
                    }
                }
            }
            
            System.out.println("Parsed email: '" + email + "'");
            System.out.println("Parsed name: '" + name + "'");
            
            // Store the email if we found one
            if (!email.isEmpty() && emailCount < 100) {
                emails[emailCount] = email;
                emailCount++;
                System.out.println("Stored email: " + email);
                System.out.println("Total emails: " + emailCount);
                
                // Send success response with CORS headers
                String response = "Email '" + email + "' stored successfully! Total emails: " + emailCount;
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/plain");
                writer.println("Access-Control-Allow-Origin: *");
                writer.println("Access-Control-Allow-Methods: POST, GET, OPTIONS");
                writer.println("Access-Control-Allow-Headers: Content-Type");
                writer.println("Content-Length: " + response.length());
                writer.println();
                writer.println(response);
            } else {
                // Send error response
                String response = "Error: Could not store email or server is full";
                writer.println("HTTP/1.1 400 Bad Request");
                writer.println("Content-Type: text/plain");
                writer.println("Access-Control-Allow-Origin: *");
                writer.println("Content-Length: " + response.length());
                writer.println();
                writer.println(response);
            }
            
        } catch (Exception e) {
            System.out.println("Error processing email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Send a "not found" response
    private static void sendNotFound(PrintWriter writer) {
        String response = "404 - Page not found";
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println("Content-Length: " + response.length());
        writer.println();
        writer.println(response);
        writer.flush();
    }
}
