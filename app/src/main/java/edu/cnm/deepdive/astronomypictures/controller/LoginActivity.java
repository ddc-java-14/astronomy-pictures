package edu.cnm.deepdive.astronomypictures.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.astronomypictures.controller.MainActivity;
import edu.cnm.deepdive.astronomypictures.R;
import edu.cnm.deepdive.astronomypictures.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.astronomypictures.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding;
  private LoginViewModel viewModel;
  private ActivityResultLauncher<Intent> launcher;
  private boolean silent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(viewModel);
    launcher = registerForActivityResult(new StartActivityForResult(), viewModel::completeSignIn);
    silent = true;
    viewModel.getAccount().observe(this, this::handleAccount);
    viewModel.getThrowable().observe(this, this::informFailure);
  }

  private void informFailure(Throwable throwable) {
    if (throwable != null) {
      Snackbar
          .make(binding.getRoot(), R.string.login_failure_message, Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      Intent intent = new Intent(this, MainActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    } else if (silent) {
      silent = false;
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      binding.signIn.setOnClickListener((v) -> viewModel.startSignIn(launcher));
      setContentView(binding.getRoot());
    }
  }

}