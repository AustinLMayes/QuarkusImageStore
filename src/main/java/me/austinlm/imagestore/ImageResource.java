package me.austinlm.imagestore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@Path("/")
public class ImageResource {

  @Inject
  EntityManager entityManager;

  @GET // Index
  public String index() {
    return "<form action=\"/\" method=\"post\" enctype=\"multipart/form-data\">\n"
        + "        \n"
        + "       <p>\n"
        + "        Name: <input type=\"text\" name=\"name\" size=\"50%\" />\n"
        + "        <br>"
        + "        Image: <input type=\"file\" name=\"image\" size=\"100%\" />\n"
        + "       </p>\n"
        + "\n"
        + "       <input type=\"submit\" value=\"Upload It\" />\n"
        + "    </form>";
  }

  @POST
  @Transactional
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response submit(@MultipartForm Upload upload) throws SQLException, IOException {
    InputStream is = new ByteArrayInputStream(upload.data);
    String mimeType = URLConnection.guessContentTypeFromStream(is);
    if (mimeType == null || !mimeType.startsWith("image")) {
      throw new WebApplicationException("Uploaded file must be an image");
    }
    Image image = new Image(upload.name, new SerialBlob(upload.data));
    entityManager.persist(image);
    return Response.ok(image).build();
  }

  @GET
  @Path("{id}")
  public Image image(@PathParam int id) throws IOException, SQLException {
    Image image = entityManager.find(Image.class, id);
    if (image == null) {
      throw new WebApplicationException("Image with id " + id + "not found!", 404);
    }
    return image;
  }

  /**
   * Representation of an upload request, so RE can do all the parsing stuff for us
   */
  public static class Upload {

    @FormParam("name")
    @PartType("text/plain")
    private String name;

    @FormParam("image")
    @PartType("application/octet-stream")
    private byte[] data;
  }
}