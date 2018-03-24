<%@ page language="java" 
import="java.util.*" 
import="mainpack.tomysql"
pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	
    <%
    String result="false";
    try{
    	tomysql.getConnection();
    	result="true";
    }catch(Exception e){
    	
    }
     %>
    <title>My JSP 'index.jsp' starting page</title>
	
  </head>
  
  <body>
   	<h1>the server is success start:<%=result%></h1><br>
  </body>
</html>
