<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ejemplo.modelo.Item" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Formulario Item</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<%
  Item it = (Item)request.getAttribute("item");
  String errorMsg = (String)request.getAttribute("error");
%>

<nav class="navbar navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="items">Items</a>
  </div>
</nav>

<div class="container my-4">
  <h3><%= (it != null && it.getId() > 0) ? "Editar Item" : "Nuevo Item" %></h3>

  <% if (errorMsg != null) { %>
    <div class="alert alert-danger"><%= errorMsg %></div>
  <% } %>

  <form action="items" method="post" enctype="multipart/form-data" class="row g-3">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id" value="<%= (it!=null)? it.getId() : "" %>">

    <div class="col-12">
      <label class="form-label">Título *</label>
      <input class="form-control" type="text" name="titulo" required
             value="<%= (it!=null && it.getTitulo()!=null)? it.getTitulo() : "" %>">
    </div>

    <div class="col-12">
      <label class="form-label">Descripción</label>
      <textarea class="form-control" name="descripcion" rows="3"><%= (it!=null && it.getDescripcion()!=null)? it.getDescripcion() : "" %></textarea>
    </div>

    <div class="col-sm-6">
      <label class="form-label">Precio (₡) *</label>
      <input class="form-control" type="number" name="precio" step="0.01" min="0.01" required
             value="<%= (it!=null)? it.getPrecio() : 0 %>">
    </div>

    <div class="col-sm-6">
      <label class="form-label">Imagen por URL (opcional)</label>
      <input class="form-control" type="url" name="imageUrl" placeholder="https://...">
      <div class="form-text">Si dejas la URL vacía, puedes subir archivo.</div>
    </div>

    <div class="col-12">
      <label class="form-label">Subir archivo (opcional)</label>
      <input class="form-control" type="file" name="imageFile" accept="image/*">
    </div>

    <% if (it!=null && it.getImagen()!=null && !it.getImagen().isBlank()) {
         String img = it.getImagen();
         boolean isUrl = img.startsWith("http");
         String src = isUrl ? img : (request.getContextPath()+"/"+img);
    %>
      <div class="col-12">
        <div class="alert alert-secondary p-2">Vista previa actual:</div>
        <img src="<%= src %>" style="max-width:260px; border-radius:8px; border:1px solid #eee">
      </div>
    <% } %>

    <div class="col-12 d-flex gap-2">
      <button class="btn btn-primary" type="submit">Guardar</button>
      <a class="btn btn-outline-secondary" href="items">Cancelar</a>
    </div>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
