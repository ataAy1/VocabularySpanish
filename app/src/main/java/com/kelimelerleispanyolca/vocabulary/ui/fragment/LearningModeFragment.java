package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentLearningModeBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.LearningModeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LearningModeFragment extends Fragment {

    private FragmentLearningModeBinding binding;
    private LearningModeViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int totalKelimeGostermeSayac;
    private int kelimeId;
    private String kelimeIngilizce, kelimeTurkce, kelimeTuru, ornekCumle, ornekCumleCeviri;
    private int learnedWordsCount;

    private static final int MIN_DISTANCE = 150;
    private static final String TAG = "Swipe posion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearningModeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LearningModeViewModel.class);

        sharedPreferences = getContext().getSharedPreferences("Grafik", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        learnedWordsCount = sharedPreferences.getInt("learnedWordsCount", 0);

        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigateUp();
        });

        binding.learningModeImage.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Swipe/Kaydır - Öğrenme Modunda kelime kalması için sola ", Toast.LENGTH_LONG).show()
        );

        binding.practicalModeImage.setOnClickListener(v ->
                Toast.makeText(requireContext(), " Swipe/Kaydır - Dinleme, Yazma,Quiz için sağa ", Toast.LENGTH_LONG).show()
        );

        setupObservers();
        setupSwipeListener();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.getVocabularyListLiveData().observe(getViewLifecycleOwner(), liste -> {
            if (liste != null && !liste.isEmpty()) {
                kelimeId = liste.get(0).getId();
                kelimeIngilizce = liste.get(0).getSpanishWord();
                kelimeTurkce = liste.get(0).getTurkishTranslation();
                kelimeTuru = liste.get(0).getWordType();
                ornekCumle = liste.get(0).getSpanishWord();
                ornekCumleCeviri = liste.get(0).getTurkishTranslation();

                binding.spanishWordLabel.setText(kelimeIngilizce);
                binding.turkishWordLabel.setText(kelimeTurkce);
                binding.wordTypeLabel.setText(kelimeTuru);
                binding.spanishSentenceEditText.setText(ornekCumle);
                binding.turkishSentenceEditText.setText(ornekCumleCeviri);
            } else {
                binding.turkishWordLabel.setText("Tum Kelimeler Ezberlendi, Yeni Kelime Ekleyiniz");
                binding.spanishWordLabel.setText("");
                binding.wordTypeLabel.setText("");
                Toast.makeText(requireContext(), "Tum kelimeler ezberlendi. Yeni en az 10 kelime ekleyiniz ...", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getShowAgainWordCountLiveData().observe(getViewLifecycleOwner(), ogrenmeModuSayac -> {
            int totalWords = totalKelimeGostermeSayac + ogrenmeModuSayac;
            int progress = (int) ((-15 + ogrenmeModuSayac) / (float) totalWords * 100);

            binding.progressBar.setMax(100);
            binding.progressBar.setProgress(progress);
            String text = "Toplam Kelimelerin <b>" + progress + "%</b> çalışma moduna yollandı";
            binding.progressTextview.setText(Html.fromHtml(text));
        });

        viewModel.getSeenWordsCountLiveData().observe(getViewLifecycleOwner(), totalGorulme -> {
            if (totalGorulme != null && totalGorulme <= 8) {
                viewModel.resetSeenWordsCount();
                viewModel.fetchRandomVocabulary();
            }
        });
    }

    private void setupSwipeListener() {
        binding.getRoot().setOnTouchListener(new OnSwipeListener(requireContext()) {
            @Override
            public void onSwipeRight() {
                performSwipeAnimation(Color.GREEN, binding.getRoot().getWidth());
                editor.putInt("learnedWordsCount", ++learnedWordsCount).commit();
                viewModel.saveWordLearningRecord(new WordSaveEntity(0, kelimeIngilizce, kelimeTurkce, kelimeTuru, ornekCumle, ornekCumleCeviri, true, true));
                viewModel.markWordAsSeen(kelimeId);
                viewModel.updateLearnedWordsData();
                viewModel.deleteVocabularyById(kelimeId);
                viewModel.fetchWordSeenCount();
                viewModel.fetchTotalSeenWordsCount();
            }

            @Override
            public void onSwipeLeft() {
                performSwipeAnimation(Color.BLUE, -binding.getRoot().getWidth());
                viewModel.markWordAsSeen(kelimeId);
                viewModel.updateLearnedWordsData();
                viewModel.fetchRandomVocabulary();
                viewModel.fetchTotalSeenWordsCount();
                viewModel.fetchWordSeenCount();
            }
        });
    }

    private void performSwipeAnimation(int color, float translationX) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#333030"), color);
        colorAnimator.addUpdateListener(animator -> binding.getRoot().setBackgroundColor((int) animator.getAnimatedValue()));

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(binding.getRoot(), "translationX", 0f, translationX);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(binding.getRoot(), "rotation", 0f, 360f);
        rotationAnimator.setDuration(750);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(colorAnimator, translationAnimator, rotationAnimator);
        animatorSet.setDuration(750);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#333030"));
                binding.getRoot().setTranslationX(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();
    }
}
