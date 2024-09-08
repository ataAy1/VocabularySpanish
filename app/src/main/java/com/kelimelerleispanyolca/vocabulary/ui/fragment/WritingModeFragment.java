package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentWritingModeBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.WritingModeViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.Disposable;

@AndroidEntryPoint
public class WritingModeFragment extends Fragment {

    private FragmentWritingModeBinding binding;
    private WritingModeViewModel viewModel;
    private List<WordSaveEntity> kelimeList;
    private StringBuilder enteredTextBuilder;
    private int hintStep = 0;
    private String turkishWord, spanishWord, wordType;
    private List<String> enteredTextList = new ArrayList<>();
    private int answerCounter = 3;

    private Drawable customBackground;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int repeatedWordsCount, writingModeProgress;
    private Disposable disposable;
    private int id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWritingModeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WritingModeViewModel.class);
        customBackground = ContextCompat.getDrawable(requireContext(), R.drawable.button_design_light);

        sharedPreferences = getContext().getSharedPreferences("Graph", Context.MODE_PRIVATE);
        repeatedWordsCount = sharedPreferences.getInt("repeatedWordsCount", 0);
        writingModeProgress = sharedPreferences.getInt("writingModeProgress", 0);

        repeatedWordsCount++;
        writingModeProgress++;
        editor = sharedPreferences.edit();

        initializeObservers();
        initializeUI();

        return binding.getRoot();
    }

    private void initializeObservers() {
        viewModel.getData.observe(getViewLifecycleOwner(), list -> {
            if (!list.isEmpty()) {
                WordSaveEntity word = list.get(0);
                id = word.getWordId();
                turkishWord = word.getTurkishWord();
                spanishWord = word.getSpanishWord();
                wordType = word.getWordType();
                viewModel.markWordAsShown(id);
                giveHint(spanishWord);
                recreateEditTextsIfNeeded();
                binding.turkishWordTextView.setText(turkishWord);
            }
        });

        viewModel.getCounter.observe(getViewLifecycleOwner(), counter -> {
            if (counter <= 10) {
                viewModel.resetSeenWords();
            }
        });
    }

    private void initializeUI() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigateUp();
        });

        binding.checkAnswerButton.setOnClickListener(v -> checkAnswer());
    }

    private void checkAnswer() {
        if (enteredTextBuilder != null) {
            String normalizedUserInput = normalizeString(enteredTextBuilder.toString());
            String normalizedCorrectAnswer = normalizeString(spanishWord.toLowerCase());
            if (normalizedUserInput.equals(normalizedCorrectAnswer)) {
                handleCorrectAnswer();
            } else {
                handleIncorrectAnswer();
            }
        }
    }

    private void handleCorrectAnswer() {
        editor.putInt("repeatedWordsCount", repeatedWordsCount++);
        editor.putInt("writingModeProgress", writingModeProgress++);
        editor.commit();

        binding.checkAnswerButton.setText("Doğru");
        binding.checkAnswerButton.setBackgroundColor(Color.GREEN);
        binding.checkAnswerButton.setEnabled(false);

        new Handler().postDelayed(() -> {
            resetButtonState();
            viewModel.loadRepeatedWords();
            viewModel.loadWritingModeWords();
        }, 2000);
    }

    private void handleIncorrectAnswer() {
        if (answerCounter > 0) {
            binding.checkAnswerButton.setBackgroundColor(Color.RED);
            binding.checkAnswerButton.setEnabled(false);
            binding.checkAnswerButton.setText(answerCounter + " Yanlış hakkınız kaldı");
            answerCounter--;

            new Handler().postDelayed(() -> resetButtonState(), 3200);
        } else {
            binding.checkAnswerButton.setBackgroundColor(Color.RED);
            binding.checkAnswerButton.setEnabled(false);
            binding.checkAnswerButton.setText("Doğru Cevap:" + spanishWord);

            new Handler().postDelayed(() -> {
                resetButtonState();
                viewModel.loadRepeatedWords();
                viewModel.loadWritingModeWords();
            }, 4500);
        }
    }

    private void resetButtonState() {
        binding.checkAnswerButton.setText("Kontrol Et");
        binding.checkAnswerButton.setBackground(customBackground);
        binding.checkAnswerButton.setTextColor(Color.WHITE);
        binding.checkAnswerButton.setEnabled(true);
        answerCounter = 3;
        enteredTextBuilder.setLength(0);
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", "");
    }

    private void giveHint(String getKelime) {
        String kelime = getKelime;
        binding.hintTextview.setText(" ");
        hintStep = 0;
        binding.showHintButtonFirst.setVisibility(View.VISIBLE);
        binding.showHintButtonSecond.setVisibility(View.VISIBLE);
        binding.showAnswerButton.setVisibility(View.VISIBLE);

        binding.textViewSecondHint.setVisibility(View.VISIBLE);
        binding.textViewFirstHint.setVisibility(View.VISIBLE);
        binding.textViewFinalHint.setVisibility(View.VISIBLE);

        int wordLength = kelime.length();
        int division = wordLength / 3;

        binding.showHintButtonFirst.setOnClickListener(v -> {
            hintStep += division;
            updateHintText(kelime, hintStep);
            binding.showHintButtonFirst.setVisibility(View.GONE);
            binding.textViewSecondHint.setVisibility(View.GONE);
        });

        binding.showHintButtonSecond.setOnClickListener(v -> {
            hintStep += division;
            updateHintText(kelime, hintStep);
            binding.showHintButtonSecond.setVisibility(View.GONE);
            binding.textViewFirstHint.setVisibility(View.GONE);
        });

        binding.showAnswerButton.setOnClickListener(v -> {
            hintStep = wordLength;
            updateHintText(kelime, hintStep);
            binding.showAnswerButton.setVisibility(View.GONE);
            binding.textViewFinalHint.setVisibility(View.GONE);
        });
    }

    private void updateHintText(String kelime, int hintStep) {
        if (kelime == null || kelime.isEmpty()) {
            binding.hintTextview.setText("No word to hint.");
            return;
        }

        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < kelime.length(); i++) {
            char currentChar = kelime.charAt(i);
            hint.append(i < hintStep ? currentChar : "-");
        }

        binding.hintTextview.setText(hint.toString());
    }

    private void recreateEditTextsIfNeeded() {
        if (isVisible() && spanishWord != null && !spanishWord.isEmpty()) {
            String wordWithoutSpaces = spanishWord.replaceAll("\\s+", "");
            createEditTexts(wordWithoutSpaces.length());
        }
    }

    private void createEditTexts(int numEditTexts) {
        binding.editTextContainer.removeAllViews();

        if (numEditTexts > 0) {
            int editTextWidth = 1080 / numEditTexts;
            int editTextHeight = 200;

            for (int i = 0; i < numEditTexts; i++) {
                final int index = i;
                EditText editText = new EditText(requireContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(editTextWidth, editTextHeight);
                params.setMargins(8, 8, 8, 8);
                editText.setLayoutParams(params);
                editText.setTextSize(18);
                editText.setHint(" " + (i + 1));
                editText.setGravity(Gravity.CENTER);
                editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_design_light));
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 1 && binding.editTextContainer.getChildCount() > 0) {
                            View nextView = binding.editTextContainer.getChildAt(Math.min(index + 1, binding.editTextContainer.getChildCount() - 1));
                            if (nextView instanceof EditText) {
                                ((EditText) nextView).requestFocus();
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateEnteredText();
                    }
                });

                binding.editTextContainer.addView(editText);
            }
        }
    }


    private void updateEnteredText() {
        enteredTextBuilder = new StringBuilder();
        for (int i = 0; i < binding.editTextContainer.getChildCount(); i++) {
            View view = binding.editTextContainer.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                enteredTextBuilder.append(editText.getText().toString());
            }
        }
    }
}
