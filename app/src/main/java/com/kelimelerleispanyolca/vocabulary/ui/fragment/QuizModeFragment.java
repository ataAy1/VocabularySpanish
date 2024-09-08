package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentQuizModeBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.QuizModeViewModel;

import java.util.List;
import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizModeFragment extends Fragment {
    private String answer;
    private int id;
    private FragmentQuizModeBinding binding;
    private QuizModeViewModel viewModel;
    private Drawable customBackground;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int repeatedWordsCount, quizModeProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizModeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(QuizModeViewModel.class);

        initializeSharedPreferences();
        initializeCustomBackground();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        observeViewModelData();
    }

    private void initializeSharedPreferences() {
        sharedPreferences = requireContext().getSharedPreferences("Graph", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        repeatedWordsCount = sharedPreferences.getInt("repeatedWordsCount", 0);
        quizModeProgress = sharedPreferences.getInt("quizModeProgress", 0);
        repeatedWordsCount++;
    }

    private void initializeCustomBackground() {
        customBackground = ContextCompat.getDrawable(requireContext(), R.drawable.button_design_light);
    }

    private void setupToolbar() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigateUp();
        });
    }

    private void observeViewModelData() {
        viewModel.observeSeenWordsCount().observe(getViewLifecycleOwner(), value -> {
            if (value <= 10) {
                viewModel.resetWordSeenStatus();
            }
        });

        viewModel.getRandomKelimeler().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                setupQuizOptions(userList);
                viewModel.markWordAsSeen(id);
                handleAnswerCheck();
            }
        });
    }

    private void setupQuizOptions(List<WordSaveEntity> userList) {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        String question = userList.get(0).getSpanishWord();
        String question1 = userList.get(1).getSpanishWord();
        String question2 = userList.get(2).getSpanishWord();
        String question3 = userList.get(3).getSpanishWord();

        answer = userList.get(randomNumber).getSpanishWord();
        String questionOfAnswer = userList.get(randomNumber).getTurkishWord();
        id = userList.get(randomNumber).getWordId();

        binding.questionOfTextView.setText(questionOfAnswer);
        binding.firstOptionButton.setText(question);
        binding.secondOptionButton.setText(question1);
        binding.thirdOptionButton.setText(question2);
        binding.fourthOptionButton.setText(question3);
    }

    private void handleAnswerCheck() {
        Handler handler = new Handler();

        binding.firstOptionButton.setOnClickListener(v -> handleButtonClick(binding.firstOptionButton, handler));
        binding.secondOptionButton.setOnClickListener(v -> handleButtonClick(binding.secondOptionButton, handler));
        binding.thirdOptionButton.setOnClickListener(v -> handleButtonClick(binding.thirdOptionButton, handler));
        binding.fourthOptionButton.setOnClickListener(v -> handleButtonClick(binding.fourthOptionButton, handler));
    }

    private void handleButtonClick(View button, Handler handler) {
        String buttonText = ((Button) button).getText().toString();

        if (buttonText.equals(answer)) {
            button.setBackgroundColor(Color.GREEN);
            handler.postDelayed(() -> {
                button.setBackground(customBackground);
                viewModel.fetchQuizQuestions();
                viewModel.loadSavedWordsCount();
            }, 3000);
        } else {
            binding.tryAgainTextView.setText("Yeniden Deneyin");
            binding.tryAgainTextView.setTextColor(Color.RED);
            handler.postDelayed(() -> binding.tryAgainTextView.setText(" "), 3000);
        }

        updateSharedPreferences();
    }

    private void updateSharedPreferences() {
        editor.putInt("repeatedWordsCount", repeatedWordsCount++);
        editor.putInt("quizModeProgress", quizModeProgress++);
        editor.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
