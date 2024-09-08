package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentListeningModeBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.ListeningModeViewModel;

import java.text.Normalizer;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListeningModFragment extends Fragment {

    private TextToSpeech textToSpeech;
    private FragmentListeningModeBinding binding;
    private ListeningModeViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String kelimedinleme, kelimeCeviri;
    private int id;
    private int repeatedWordsCount, listeningModeProgress;
    private StringBuilder hintBuilder = new StringBuilder();
    private int hintIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListeningModeBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ListeningModeViewModel.class);

        sharedPreferences = getContext().getSharedPreferences("Graph", Context.MODE_PRIVATE);
        repeatedWordsCount = sharedPreferences.getInt("repeatedWordsCount", 0);
        listeningModeProgress = sharedPreferences.getInt("listeningModeProgress", 0);
        editor = sharedPreferences.edit();

        Drawable customBackground = ContextCompat.getDrawable(requireContext(), R.drawable.button_design_light);

        viewModel.getCounterListening().observe(getViewLifecycleOwner(), counterValue -> {
            if (counterValue != null && counterValue <= 10) {
                viewModel.resetSeenWords();
            }
        });

        viewModel.getRandomKelime().observe(getViewLifecycleOwner(), kelimeKaydetme -> {
            if (kelimeKaydetme != null && !kelimeKaydetme.isEmpty()) {
                id = kelimeKaydetme.get(0).getWordId();
                kelimedinleme = kelimeKaydetme.get(0).getSpanishWord();
                kelimeCeviri = kelimeKaydetme.get(0).getTurkishWord();
                binding.translationButton.setText("Çevirisine Bak");
                binding.translationButton.setTextColor(Color.WHITE);
                setTextToSpeech(kelimedinleme);
            }
        });

        binding.translationButton.setOnClickListener(view -> {
            binding.translationButton.setTextColor(Color.WHITE);
            binding.translationButton.setText(kelimeCeviri.toLowerCase());
        });

        binding.checkAnswerButton.setOnClickListener(v -> checkAnswer(customBackground));

        binding.hintButton.setOnClickListener(v -> provideHint());

        binding.listenButton.setOnClickListener(v -> setTextToSpeech(kelimedinleme));

        return binding.getRoot();
    }

    private void checkAnswer(Drawable customBackground) {
        String normalizedUserInput = normalizeString(binding.userInputEditText.getText().toString().toLowerCase());
        String normalizedCorrectAnswer = normalizeString(kelimedinleme.toLowerCase());

        if (normalizedUserInput.equals(normalizedCorrectAnswer)) {
            binding.checkAnswerButton.setBackgroundColor(Color.GREEN);
            binding.checkAnswerButton.setText("Doğru Cevap");
            updatePreferences();
            new Handler().postDelayed(() -> {
                resetAndFetchNewWord();
                binding.checkAnswerButton.setText("Cevabımı Kontrol Et");
                binding.checkAnswerButton.setAllCaps(false);
                binding.checkAnswerButton.setTextColor(Color.WHITE);
                binding.checkAnswerButton.setBackground(customBackground);
                viewModel.markWordAsSeenInWritingMode(id);
                viewModel.updateRepeatedWordsData();
                viewModel.fetchQuizSeenWordsCount();
                viewModel.fetchRandomWordsForListening();
            }, 3000);
        } else {
            binding.checkAnswerButton.setBackgroundColor(Color.RED);
            binding.checkAnswerButton.setText("Yeniden Deneyin");
            new Handler().postDelayed(() -> {
                binding.checkAnswerButton.setBackground(customBackground);
                binding.checkAnswerButton.setText("Kontrol ET");
                binding.checkAnswerButton.setTextColor(Color.WHITE);
                binding.userInputEditText.setText("");
            }, 2000);
        }
    }

    private void provideHint() {
        if (hintIndex < kelimedinleme.length()) {
            char nextChar = kelimedinleme.charAt(hintIndex);
            hintBuilder.append(nextChar != ' ' ? nextChar : ' ');
            binding.userInputEditText.setText(hintBuilder.toString());
            hintIndex++;
        } else {
            binding.userInputEditText.setText(kelimedinleme);
        }
    }

    private void resetAndFetchNewWord() {
        hintBuilder = new StringBuilder();
        hintIndex = 0;
        binding.hintButton.setTextColor(Color.WHITE);
        binding.hintButton.setAllCaps(false);
        binding.hintButton.setText("İpucu Ver");
    }

    private void updatePreferences() {
        repeatedWordsCount++;
        listeningModeProgress++;
        editor.putInt("repeatedWordsCount", repeatedWordsCount);
        editor.putInt("listeningModeProgress", listeningModeProgress);
        editor.apply();
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigateUp();
        });
    }

    private void setTextToSpeech(String text) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(requireContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(new Locale("es", "ES"));
                    if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            });
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
