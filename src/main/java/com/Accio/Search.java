package com.Accio;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //getting keyword from front end
        String Keyword=request.getParameter("Keyword");
        // setting up connection to data base
        try{
        Connection connection=DatabaseConnection.getConnection();

           PreparedStatement preparedStatement= connection.prepareStatement("insert into sql values(?, ?);");
            preparedStatement.setString(1,Keyword);
            preparedStatement.setString(2,"http://localhost:8080/SearchEngine111/Search?Keyword="+Keyword);
             preparedStatement.executeUpdate();

            //getting results after running ranking query
            ResultSet resultSet = connection.createStatement().executeQuery("select pageTitle, pageLink,(length(lower(pageText))-length(replace(lower(pageText), '" + Keyword.toLowerCase() + "','')) )/length('" + Keyword+ "') as countoccurence from pages order by countoccurence desc limit 30;");
            ArrayList<SearchResult> results = new ArrayList<SearchResult>();
            //transferring values from resultSet to results arrayList
            while (resultSet.next()) {
                SearchResult searchResult = new SearchResult();
                searchResult.setTitle(resultSet.getString("pageTitle"));
                searchResult.setLink(resultSet.getString("pageLink"));
                results.add(searchResult);
            }
            // displaying results arraylist the console
            //for(SearchResult result:results) {
               // System.out.println(result.getTitle() + "\n" + result.getLink() + "\n");
            //}
            request.setAttribute("results", results);
            request.getRequestDispatcher("search.jsp").forward(request,response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            //out.println("<h3>This is the Keyword you have entered "+Keyword+"</h3>");
        }
        catch(SQLException | ServletException e){
            throw new RuntimeException(e);
        }

    }

}
