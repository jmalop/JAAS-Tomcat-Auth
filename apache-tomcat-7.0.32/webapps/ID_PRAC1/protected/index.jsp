<%
  if (request.getParameter("logoff") != null) {
    session.invalidate();
    response.sendRedirect("index.jsp");
    return;
  }
%>
<html>
<head>
<title>Menu principal</title>
</head>
<body bgcolor="white">

<h1>MENU PRINCIPAL</h1><br><br>
<%
  	if (request.getUserPrincipal() != null) 
	{
%>
    		Usuari: <br>
    		<b><%= util.HTMLFilter.filter(request.getUserPrincipal().getName()) %></b>
    		<br><br>
<%
  	} else {
%>
    		No s'ha pogut identificar l'usuari.<br><br>
<%
  	}
%>

Sessio:<br> <b><%= session.getId() %></b><br><br>

Llistat de rols: <br>
<%
	// Aquesta variable la passarem entre pàgines a través de la sessió:
	String rols = "";

	if (request.isUserInRole("G")) 
	{
		if (rols.equals(""))
		{
			rols = "G";
		}
		else
		{
			rols += ".G";
		}
%>
      		
      		<b>Gerent</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("DV")) 
	{
		if (rols.equals(""))
		{
			rols = "DV";
		}
		else
		{
			rols += ".DV";
		}
%>
      		
      		<b>Direccio Ventes</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("C")) 
	{
		if (rols.equals(""))
		{
			rols = "C";
		}
		else
		{
			rols += ".C";
		}
%>
      		
      		<b>Comercial</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("A")) 
	{
		if (rols.equals(""))
		{
			rols = "A";
		}
		else
		{
			rols += ".A";
		}
%>
      		
      		<b>Administracio</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("P")) 
	{
		if (rols.equals(""))
		{
			rols = "P";
		}
		else
		{
			rols += ".P";
		}
%>
      		
      		<b>Produccio</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("M")) 
	{
		if (rols.equals(""))
		{
			rols = "M";
		}
		else
		{
			rols += ".M";
		}
%>
      		
      		<b>Magatzem</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("T")) 
	{
		if (rols.equals(""))
		{
			rols = "T";
		}
		else
		{
			rols += ".T";
		}
%>
      		
      		<b>Treballador</b><br>
<%
  	}
%>
<%
	if (request.isUserInRole("CL")) 
	{
		if (rols.equals(""))
		{
			rols = "CL";
		}
		else
		{
			rols += ".CL";
		}
%>
      		
      		<b>Client</b><br>
<%
  	}

	// Afegim el llistat de rols a la sessió de l'usuari, si no existeix ja:
	if ((String)session.getAttribute("varUsuario") == null) 
	{
		session.putValue("rolsUsuari",rols); 
	}
%>

<br>
<a href='<%= response.encodeURL("ventes/index.jsp") %>'>MODUL DE VENTES</a> <br>
<a href='<%= response.encodeURL("compres/index.jsp") %>'>MODUL DE COMPRES</a> <br>
<a href='<%= response.encodeURL("rrhh/index.jsp") %>'>MODUL DE RECURSOS HUMANS</a> <br>
<a href='<%= response.encodeURL("inventari/index.jsp") %>'>MODUL D'INVENTARI</a> <br><br>

<a href='<%= response.encodeURL("index.jsp?logoff=true") %>'>Tancar sessio</a>

</body>
</html>
