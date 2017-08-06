<html>
<head>
<title>Login Page for Examples</title>
<body bgcolor="white">
<form method="POST" action='<%= response.encodeURL("j_security_check") %>' >
  <table border="0" cellspacing="5">
    <tr>
      <th align="right">Nom d'usuari: </th>
      <td align="left"><input type="text" name="j_username"></td>
    </tr>
    <tr>
      <th align="right">Contrassenya: </th>
      <td align="left"><input type="password" name="j_password"></td>
    </tr>
    <tr>
      <td align="right"><input type="submit" value="Log In"></td>
      <td align="left"><input type="reset"></td>
    </tr>
  </table>
</form>
</body>
</html>
