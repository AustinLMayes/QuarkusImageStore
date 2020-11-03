package me.austinlm.imagestore;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Takes an image and renders it in HTML.
 *
 * @author Austin Mayes
 */
@Provider
@Produces("text/html")
public class ImageWriter implements MessageBodyWriter<Image> {

  @Override
  public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations,
      MediaType mediaType) {
    return type == Image.class;
  }

  @Override
  public void writeTo(Image image, Class<?> aClass, Type type, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream)
      throws IOException, WebApplicationException {

    Writer writer = new PrintWriter(outputStream);
    Blob data = image.getData();
    byte[] dataBytes = new byte[0];
    try {
      dataBytes = data.getBytes(1, (int) data.length());
    } catch (SQLException e) {
      throw new WebApplicationException(e);
    }
    String encode = Base64.getEncoder().encodeToString(dataBytes);
    writer.write("<head><title>" + image.getName() + "</title></head>");
    writer.write("<img style=\"width: 50%\" src=\"data:image/jpeg;base64," + encode + "\" />");

    writer.flush();
    writer.close();
  }
}
