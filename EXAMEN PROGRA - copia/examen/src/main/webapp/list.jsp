<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.ejemplo.modelo.Item" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Items (CRUD)</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="items">Items</a>
    <a class="btn btn-outline-light" href="items?action=new">+ Nuevo</a>
  </div>
</nav>

<div class="container my-4">

  <!-- Mensajes sencillos de confirmación -->
  <%
    String ok = request.getParameter("ok");
    String err = request.getParameter("err");
    if (ok != null) {
  %>
    <div class="alert alert-success">Acción realizada correctamente.</div>
  <% } else if (err != null) { %>
    <div class="alert alert-danger">Hubo un error: <%= err %></div>
  <% } %>

  <div class="alert alert-info">Imagen por URL o archivo subido.</div>

  <%
    List<Item> items = (List<Item>) request.getAttribute("items");
    if (items == null || items.isEmpty()) {
  %>
    <div class="p-5 text-center text-muted border rounded">No hay registros aún.</div>
  <%
    } else {
  %>

  <div class="row g-3">
    <%
      for (Item it : items) {
        String img = (it.getImagen()==null || it.getImagen().isBlank())
          ? "https://picsum.photos/400/300" : it.getImagen();
        boolean isUrl = img.startsWith("http");
        String src = isUrl ? img : (request.getContextPath()+"/"+img);
    %>
    <div class="col-12 col-sm-6 col-lg-4">
      <div class="card h-100">
        <img src="<%= src %>" class="card-img-top" alt="imagen">
        <div class="card-body">
          <h5 class="card-title"><%= it.getTitulo() %></h5>
          <p class="card-text"><%= it.getDescripcion()==null? "" : it.getDescripcion() %></p>
          <p class="mb-3"><b>Precio:</b> ₡<%= String.format(java.util.Locale.US,"%,.2f", it.getPrecio()) %></p>
          <div class="d-flex gap-2">
            <a class="btn btn-primary" href="items?action=edit&id=<%= it.getId() %>">Editar</a>
            <form method="post" action="items" class="m-0">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="<%= it.getId() %>">
              <button class="btn btn-outline-danger" type="submit">Eliminar</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    <% } %>
  </div>

  <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
