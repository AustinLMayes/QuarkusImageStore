package me.austinlm.imagestore;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Display application errors to the user
 */
@Provider
public class ErrorHandler implements ExceptionMapper<WebApplicationException> {

  @Override
  public Response toResponse(WebApplicationException ex) {
    return Response.status(ex.getResponse().getStatus())
        .entity(ex.getMessage())
        .build();
  }

}
