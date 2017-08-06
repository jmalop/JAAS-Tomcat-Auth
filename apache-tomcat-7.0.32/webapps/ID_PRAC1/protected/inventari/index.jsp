<%@page language="java" import="java.io.BufferedReader"%>
<%@page language="java" import="java.io.DataInputStream"%>
<%@page language="java" import="java.io.File"%>
<%@page language="java" import="java.io.FileInputStream"%>
<%@page language="java" import="java.io.FileNotFoundException"%>
<%@page language="java" import="java.io.FileReader"%>
<%@page language="java" import="java.io.InputStreamReader"%>
<%@page language="java" import="java.util.Iterator"%>
<%@page language="java" import="java.util.List"%>
<%@page language="java" import="java.util.ArrayList"%>

<%
  if (request.getParameter("logoff") != null) {
    session.invalidate();
    response.sendRedirect("../index.jsp");
    return;
  }
%>

<html>
<head>
<title>Modul d'inventari</title>
</head>
<body bgcolor="white">

<h1>MODUL D'INVENTARI</h1>

<%
	// Separem els rols que tenim assignats a l'usuari:
	String temp = "";
   	temp = (String)session.getAttribute("rolsUsuari");
	String[] rols = temp.split("[.]");

	// Guardarem les accions assignades segons el rol de l'usuari:
	List<String> lsAccions = new ArrayList<String>();

	// Obrim el fitxer que indica les polítiques de la pàgina segons el rol:
	FileInputStream fs = new FileInputStream(System.getProperty("catalina.home") + "/conf/rols_accions.txt");
        DataInputStream in = new DataInputStream(fs);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

	String linia;
	while ((linia = br.readLine()) != null)
	{
		// Separem els camps de la línia:
		String[] temp2 = linia.split(":");

		// Primer comprovem que la línia marqui el mòdul adequat, en cas
		// contrari no cal continuar tractant la línia:
		if (temp2[0].equals("inventari"))
		{
			// Comprovem si al segon camp, els rols, existeix algun
			// dels rols de l'usuari en qüestió:
			String[] temp3 = temp2[1].split("[.]");

			for (int i=0; i<temp3.length; i++)
			{
				for (int j=0; j<rols.length; j++)
				{
					// Si té algun dels rols, mostrem l'operació:
					if (temp3[i].equals(rols[j]))
					{ 
						// Només l'afegirem si no existeix a la llista:
						Iterator it = lsAccions.iterator();
						String sActual = "";
						boolean bTrobat = false;
						
						while (it.hasNext() && (!(bTrobat))) 
						{
							sActual = (String)it.next();
							if (sActual.equals(temp2[2])) 
							{
								bTrobat = true;
							}
						}

						if (!(bTrobat))
						{
							lsAccions.add(temp2[2]);
						}
					}
				}
			}
		}
	}

	// Tanquem el fitxer:
	in.close();
%>
<b><u>Accions autoritzades:</u></b><br>
<%
	// Mostrem les accions disponibles al mòdul segons els rols assignats:
	Iterator it = lsAccions.iterator();

	while(it.hasNext())
	{
%>
		<%=(String)it.next() %><br>
<%
	}
%>

<br><br>
<a href='<%= response.encodeURL("../index.jsp") %>'>Menu principal</a><br>
<a href='<%= response.encodeURL("index.jsp?logoff=true") %>'>Tancar sessio</a>

</body>
</html>
