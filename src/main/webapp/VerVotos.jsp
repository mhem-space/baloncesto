<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Votos</title>
    <link href="estilos.css" rel="stylesheet" type="text/css" />
</head>
<body class="verVotos">
    <h1>Votos de los jugadores</h1>
    <table>
        <tr>
            <th>Nombre del jugador</th>
            <th>Nº de votos</th>
        </tr>
        <% for (int i = 0; i < ((List<String>) session.getAttribute("nombres")).size(); i++) { %>
        <tr>
            <td><%= ((List<String>) session.getAttribute("nombres")).get(i)%></td>
            <td><%= ((List<Integer>) session.getAttribute("votos")).get(i) %></td>
        </tr>
        <% } %>
    </table>
    <a href="index.html"> Ir a la página principal</a>
</body>
</html>
