package malachite.api.models;

public class News {
  public static final String DB_ID    = "id";    //$NON-NLS-1$
  public static final String DB_TITLE = "title"; //$NON-NLS-1$
  public static final String DB_BODY  = "body";  //$NON-NLS-1$
  
  public final int    id;
  public final String title;
  public final String body;
  
  public News(String title, String body) {
    this(0, title, body);
  }
  
  public News(int id, String title, String body) {
    this.id    = id;
    this.title = title;
    this.body  = body;
  }
}