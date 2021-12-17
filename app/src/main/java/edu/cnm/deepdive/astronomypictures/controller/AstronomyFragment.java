package edu.cnm.deepdive.astronomypictures.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.astronomypictures.R;
import edu.cnm.deepdive.astronomypictures.databinding.FragmentAstronomyBinding;
import edu.cnm.deepdive.astronomypictures.model.entity.Image;
import edu.cnm.deepdive.astronomypictures.viewmodel.ImageViewModel;

public class AstronomyFragment extends Fragment {

  private ImageViewModel viewModel;
  private FragmentAstronomyBinding binding;
  private Image image;
  String url;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentAstronomyBinding.inflate(inflater, container, false);
    binding.apodSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Image image = (Image) parent.getItemAtPosition(position);
        binding.title.setText(image.getTitle());
        Picasso.get().load(image.getUrl()).into(binding.image);

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(ImageViewModel.class);
    getLifecycle().addObserver(viewModel);
    viewModel.loadRandomImages();
    viewModel.getRandomImages().observe(getViewLifecycleOwner(), (images) -> {
      ArrayAdapter<Image> adapter = new ArrayAdapter<>(getContext(), R.layout.item_image_spinner,
          images);
      adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
      binding.apodSelector.setAdapter(adapter);
    });
    viewModel.getThrowable()
        .observe(getViewLifecycleOwner(), this::displayError);
  }

  private void displayError(Throwable throwable) {
    if (throwable != null) {
      Snackbar snackbar = Snackbar.make(binding.getRoot(),
          getString(R.string.image_error_message, throwable.getMessage()),
          Snackbar.LENGTH_INDEFINITE);
      snackbar.setAction(R.string.error_dismiss, (v) -> snackbar.dismiss());
      snackbar.show();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}