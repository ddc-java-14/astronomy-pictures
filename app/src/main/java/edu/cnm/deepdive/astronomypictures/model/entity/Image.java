package edu.cnm.deepdive.astronomypictures.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "NotNullFieldNotInitialized"})
@Entity(
    tableName = "image",
    indices = {
        @Index(value = {"date"}, unique = true)
    }
)
public class Image {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "image_id")
  private long id;

  @NonNull
  private Date created = new Date();

  @Expose
  @NonNull
  private String date;

  @Expose
  @NonNull
  private String title;

  @Expose
  @SerializedName("explanation")
  private String description;

  @NonNull
  @ColumnInfo(name = "media_type", index = true)
  private String mediaType;

  @Expose
  @NonNull
  private String url;

  @Expose
  @SerializedName("hdurl")
  private String hdUrl;

  private String path;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public String getDate() {
    return date;
  }

  public void setDate(@NonNull String date) {
    this.date = date;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @NonNull
  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(@NonNull String mediaType) {
    this.mediaType = mediaType;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  public String getHdUrl() {
    return hdUrl;
  }

  public void setHdUrl(String hdUrl) {
    this.hdUrl = hdUrl;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @NonNull
  @Override
  public String toString() {
    return title;
  }
}
