package malachite.api.models;

public class News {
  public static final String ID    = "id";    //$NON-NLS-1$
  public static final String TITLE = "title"; //$NON-NLS-1$
  public static final String BODY  = "body";  //$NON-NLS-1$
  
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