package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentUpdateWordBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.UpdateWordViewModel;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateWordFragment extends Fragment {

    private FragmentUpdateWordBinding binding;
    private UpdateWordViewModel viewModel;
    private WordSaveEntity wordSaveEntity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateWordBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UpdateWordViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        if (getArguments() != null) {
            UpdateWordFragmentArgs args = UpdateWordFragmentArgs.fromBundle(getArguments());
            wordSaveEntity = args.getUpdateWord();
            populateFields();
        }

        binding.updateWordButton.setOnClickListener(v -> onUpdateWordButtonClicked());
        binding.deleteWordButton.setOnClickListener(v -> onDeleteWordButtonClicked());
    }

    private void populateFields() {
        binding.spanishWordEditText.setText(wordSaveEntity.getSpanishWord());
        binding.turkishWordEditText.setText(wordSaveEntity.getTurkishWord());
        binding.wordTypeEditText.setText(wordSaveEntity.getWordType());
        binding.exampleSentenceEditText.setText(wordSaveEntity.getExampleSentenceTurkish());
        binding.exampleSentenceTranslationEditText.setText(wordSaveEntity.getExampleSentenceTranslation());
    }

    private void onUpdateWordButtonClicked() {
        String spanishWord = binding.spanishWordEditText.getText().toString().trim();
        String turkishWord = binding.turkishWordEditText.getText().toString().trim();
        String wordType = binding.wordTypeEditText.getText().toString().trim();
        String exampleSentence = binding.exampleSentenceEditText.getText().toString().trim();
        String exampleSentenceTranslation = binding.exampleSentenceTranslationEditText.getText().toString().trim();

        if (!spanishWord.isEmpty() && !turkishWord.isEmpty() && !wordType.isEmpty()) {
            WordSaveEntity updatedWord = new WordSaveEntity(wordSaveEntity.getWordId(), spanishWord, turkishWord, wordType, exampleSentence, exampleSentenceTranslation, true, true);
            viewModel.updateWord(updatedWord);
            Toast.makeText(getContext(), "Kelime Güncellendi", Toast.LENGTH_SHORT).show();
            navigateUp();
        } else {
            Toast.makeText(getContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDeleteWordButtonClicked() {
        Snackbar.make(binding.getRoot(), "Kelime Silinsin mi?", Snackbar.LENGTH_SHORT)
                .setAction("Evet", v -> {
                    viewModel.deleteWordById(wordSaveEntity.getWordId());
                    Toast.makeText(getContext(), "Kelime Silindi", Toast.LENGTH_SHORT).show();
                    navigateUp();
                })
                .show();
    }

    private void navigateUp() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        navController.navigateUp();
    }

    @Override
    public void onStop() {
        super.onStop();
        clearFields();
    }

    private void clearFields() {
        binding.spanishWordEditText.setText("");
        binding.turkishWordEditText.setText("");
        binding.wordTypeEditText.setText("");
        binding.exampleSentenceEditText.setText("");
        binding.exampleSentenceTranslationEditText.setText("");
    }
}
