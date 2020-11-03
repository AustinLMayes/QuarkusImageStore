package me.austinlm.imagestore;

import java.sql.Blob;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representation of an uploaded image.
 *
 * @author Austin Mayes
 */
@Entity(name = "images")
public class Image {

  /**
   * ID - auto generated
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * Image name - user provided
   */
  private String name;
  /**
   * Image data blob
   */
  private Blob data;

  public Image() {
  }

  // ID not provided since it is set automatically
  public Image(String name, Blob data) {
    this.name = name;
    this.data = data;
  }

  String getName() {
    return name;
  }

  Blob getData() {
    return data;
  }
}
