package edu.cnm.deepdive.astronomypictures.service;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.astronomypictures.BuildConfig;
import edu.cnm.deepdive.astronomypictures.model.dao.ImageDao;
import edu.cnm.deepdive.astronomypictures.model.entity.Image;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

public class ImageRepository {

  private final Application context;
  private final ImageDao imageDao;
  private final WebServiceProxy proxy;

  public ImageRepository(Application context) {
    this.context = context;
    imageDao = AstronomyDatabase
        .getInstance()
        .getImageDao();
    proxy = WebServiceProxy.getInstance();
  }

  public LiveData<Image> get(long imageId) {
    return imageDao.select(imageId);
  }

  public LiveData<List<Image>> getAll() {
    return imageDao.selectAll();
  }

  public Single<Image> save(Image image) {
    Single<Image> task;
    if (image.getId() == 0) {
      image.setCreated(new Date());
      task = imageDao
          .insert(image)
          .map((id) -> {
            image.setId(id);
            return image;
          });
    } else {
      task = imageDao
          .update(image)
          .map((count) -> image);
    }
    return task.subscribeOn(Schedulers.io());
  }

  public Single<Image> get() {
    return proxy.get(BuildConfig.API_KEY)
        .subscribeOn(Schedulers.io());
  }

  public Single<List<Image>> getRandomImages() {
    return proxy.getRandomImages(BuildConfig.API_KEY, "10")
        .subscribeOn(Schedulers.io());
  }

  public Completable delete(Image image) {
    return (image.getId() == 0)
        ? Completable.complete()
        : imageDao
            .delete(image)
            .ignoreElement()
            .subscribeOn(Schedulers.io());
  }
}