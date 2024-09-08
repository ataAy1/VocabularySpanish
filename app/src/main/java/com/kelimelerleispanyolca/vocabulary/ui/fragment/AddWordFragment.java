package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentAddWordBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.AddWordViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint


public class AddWordFragment extends Fragment {

    private FragmentAddWordBinding binding;
    private AddWordViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddWordBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddWordViewModel.class);


        binding.saveWordButton.setOnClickListener(v -> {

            String kelimeIngilizce = binding.spanishWordEditText.getText().toString().trim();
            String kelimeTurkce = binding.turkishWordEditText.getText().toString().trim();
            String kelimeTuru = binding.wordTypeEditText.getText().toString().trim();
            String exampleSentenceEditText = binding.exampleSentenceEditText.getText().toString().trim();
            String exampleSentenceTranslationEditText = binding.exampleSentenceTranslationEditText.getText().toString().trim();

            if (!kelimeIngilizce.isEmpty() && !kelimeTurkce.isEmpty() && !kelimeTuru.isEmpty() && !exampleSentenceEditText.isEmpty() && !exampleSentenceEditText.isEmpty()) {
                viewModel.addWord(kelimeIngilizce, kelimeTurkce, kelimeTuru, exampleSentenceEditText, exampleSentenceTranslationEditText);
                Toast.makeText(getContext(), "Yeni Kelime Eklendi", Toast.LENGTH_SHORT).show();
                binding.spanishWordEditText.setText("");
                binding.turkishWordEditText.setText("");
                binding.wordTypeEditText.setText("");
                binding.exampleSentenceEditText.setText("");
                binding.exampleSentenceTranslationEditText.setText("");


            } else {
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            }


        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ata", "ataaa");
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}