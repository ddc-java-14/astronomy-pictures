package edu.cnm.deepdive.astronomypictures.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.astronomypictures.model.entity.Image;
import edu.cnm.deepdive.astronomypictures.service.AstronomyDatabase;
import edu.cnm.deepdive.astronomypictures.service.ImageRepository;
import edu.cnm.deepdive.astronomypictures.service.WebServiceProxy;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class ImageViewModel extends AndroidViewModel implements LifecycleObserver {

  private final ImageRepository repository;
  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Long> imageId;
  private final LiveData<Image> image;
  private final MutableLiveData<Image> apod;
  private final MutableLiveData<List<Image>> randomImages;
  private final CompositeDisposable pending;
  private final AstronomyDatabase database;

  public ImageViewModel(@NonNull Application application) {
    super(application);
    database = AstronomyDatabase.getInstance();
    repository = new ImageRepository(application);
    throwable = new MutableLiveData<>();
    imageId = new MutableLiveData<>();
    image = Transformations.switchMap(imageId, repository::get);
    randomImages = new MutableLiveData<>();
    pending = new CompositeDisposable();
    apod = new MutableLiveData<>();
  }

  public LiveData<Image> getImage() {
    return image;
  }

  public LiveData<Image> getApod() {
    return apod;
  }

  public LiveData<List<Image>> getRandomImages() {
    return randomImages;
  }

  public void setImageId(long id) {
    imageId.setValue(id);
  }

  public LiveData<List<Image>> getImages() {
    return repository.getAll();
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void save(Image image) {
    pending.add(
        repository
            .save(image)
            .subscribe(
                (savedImage) -> {
                },
                this::postThrowable
            )
    );
  }

  public void loadAPOD() {
    pending.add(
        repository.get()
            .subscribe(
                apod::postValue,
                throwable::postValue
            )
    );
  }

  public void loadRandomImages() {
    pending.add(
        repository.getRandomImages()
            .subscribe(
                randomImages::postValue,
                throwable::postValue
            )
    );
  }

  public void delete(Image image) {
    pending.add(
        repository
            .delete(image)
            .subscribe(
                () -> {
                },
                this::postThrowable
            )
    );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}
