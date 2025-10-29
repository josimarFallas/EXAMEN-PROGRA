package com.ejemplo.web;

import com.ejemplo.dao.ItemDAO;
import com.ejemplo.modelo.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebServlet(name = "ItemServlet", urlPatterns = {"/items"})
@MultipartConfig
public class ItemServlet extends HttpServlet {

    private final ItemDAO dao = new ItemDAO();

    // GET: lista / formulario nuevo / formulario editar
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = p(req.getParameter("action"));
        if (action.isEmpty()) action = "list";

        switch (action) {
            case "new": {
                req.setAttribute("item", new Item());
                req.getRequestDispatcher("/form.jsp").forward(req, resp);
                break;
            }
            case "edit": {
                try {
                    int id = Integer.parseInt(p(req.getParameter("id")));
                    Item it = dao.findById(id);
                    if (it == null) {
                        resp.sendRedirect("items?err=No%20existe%20el%20registro");
                        return;
                    }
                    req.setAttribute("item", it);
                    req.getRequestDispatcher("/form.jsp").forward(req, resp);
                } catch (Exception e) {
                    resp.sendRedirect("items?err=ID%20inv%C3%A1lido");
                }
                break;
            }
            default: {
                List<Item> items = dao.findAll();
                req.setAttribute("items", items);
                req.getRequestDispatcher("/list.jsp").forward(req, resp);
            }
        }
    }

    // POST: guardar (insert/update) / eliminar
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = p(req.getParameter("action"));

        if ("save".equals(action)) {
            req.setCharacterEncoding("UTF-8");

            String idStr       = p(req.getParameter("id"));
            String titulo      = p(req.getParameter("titulo"));
            String descripcion = p(req.getParameter("descripcion"));
            String precioStr   = p(req.getParameter("precio"));
            String imageUrl    = p(req.getParameter("imageUrl"));

            double precio = 0;
            try { precio = Double.parseDouble(precioStr); } catch (Exception ignored) {}

            // Validación mínima
            if (titulo.isBlank()) {
                req.setAttribute("error", "El título es obligatorio.");
                req.setAttribute("item", new Item(idStr.isBlank()?0:toInt(idStr), titulo, descripcion, precio, imageUrl));
                req.getRequestDispatcher("/form.jsp").forward(req, resp);
                return;
            }
            if (precio <= 0) {
                req.setAttribute("error", "El precio debe ser mayor que 0.");
                req.setAttribute("item", new Item(idStr.isBlank()?0:toInt(idStr), titulo, descripcion, precio, imageUrl));
                req.getRequestDispatcher("/form.jsp").forward(req, resp);
                return;
            }

            // Imagen: si no hay URL, intentamos archivo subido
            String finalImagen = imageUrl;
            Part filePart = null;
            try { filePart = req.getPart("imageFile"); } catch (Exception ignored) {}
            if (finalImagen.isBlank() && filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" +
                        Path.of(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadDir = getServletContext().getRealPath("/uploads");
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, fileName);
                filePart.write(file.getAbsolutePath());
                finalImagen = "uploads/" + fileName; // ruta relativa servida por la app
            }

            Item it = new Item();
            it.setTitulo(titulo);
            it.setDescripcion(descripcion);
            it.setPrecio(precio);
            it.setImagen(finalImagen);

            boolean ok;
            if (!idStr.isBlank()) {
                it.setId(toInt(idStr));
                ok = dao.update(it);
            } else {
                ok = dao.insert(it);
            }

            resp.sendRedirect(ok ? "items?ok=1" : "items?err=No%20se%20pudo%20guardar");
            return;
        }

        if ("delete".equals(action)) {
            boolean ok = false;
            try { ok = dao.delete(toInt(p(req.getParameter("id")))); } catch (Exception ignored) {}
            resp.sendRedirect(ok ? "items?ok=1" : "items?err=No%20se%20pudo%20eliminar");
        }
    }

    // Helpers
    private String p(String s){ return s==null? "" : s.trim(); }
    private int toInt(String s){ try { return Integer.parseInt(s); } catch (Exception e){ return 0; } }
}
