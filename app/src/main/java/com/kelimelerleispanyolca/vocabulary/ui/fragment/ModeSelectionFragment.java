package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentModeSelectionBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.ModeSelectionViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ModeSelectionFragment extends Fragment {

    private FragmentModeSelectionBinding binding;
    private ModeSelectionViewModel viewModel;

    private Boolean controlValue = false;
    private int totalNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentModeSelectionBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ModeSelectionViewModel.class);

        binding.listeningModeImageView.setOnClickListener(this::onListeningModeClicked);
        binding.learningModeImageView.setOnClickListener(this::onLearningModeClicked);
        binding.quizModeImageView.setOnClickListener(this::onQuizModeClicked);
        binding.writingModeImageView.setOnClickListener(this::onWritingModeClicked);
        binding.wordSavingImageView.setOnClickListener(this::onWordSavingModeClicked);

        viewModel.getSavedWordsCountLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                totalNumber = count != null ? count : 0;
                controlValue = totalNumber >= 10;
            }
        });

        return binding.getRoot();
    }

    private void navigateTo(int actionId) {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(actionId);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void onWritingModeClicked(View view) {
        if (controlValue) {
            navigateTo(R.id.action_modeSelectionFragment_to_writingModeFragment);
        } else {
            showToast(getString(R.string.minimum_word_count_message, totalNumber));
        }
    }

    private void onQuizModeClicked(View view) {
        if (controlValue) {
            navigateTo(R.id.action_modeSelectionFragment_to_quizModeFragment);
        } else {
            showToast(getString(R.string.minimum_word_count_message, totalNumber));
        }
    }

    private void onLearningModeClicked(View view) {
        navigateTo(R.id.action_modeSelectionFragment_to_learningModeFragment);
    }

    private void onListeningModeClicked(View view) {
        if (controlValue) {
            navigateTo(R.id.action_modeSelectionFragment_to_listeningModFragment);
        } else {
            showToast(getString(R.string.minimum_word_count_message, totalNumber));
        }
    }

    private void onWordSavingModeClicked(View view) {
        navigateTo(R.id.action_modeSelectionFragment_to_addWordFragment);
    }
}
