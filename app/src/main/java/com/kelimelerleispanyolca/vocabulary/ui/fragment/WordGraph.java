package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelimelerleispanyolca.vocabulary.databinding.FragmentWordGraphBinding;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.WordsGraphViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WordGraph extends Fragment {

    private FragmentWordGraphBinding binding;
    private WordsGraphViewModel viewModel;
    private SharedPreferences sharedPreferences;

    int repeatedWordsCount, learnedWordsCount, listeningModeProgress, quizModeProgress, writingModeProgress;

    int totalNumber  = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWordGraphBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WordsGraphViewModel.class);
        viewModel.loadCurrentDate();
        viewModel.loadLearnedWordsGraph();
        viewModel.loadRepeatedWordsGraph();

        sharedPreferences = getContext().getSharedPreferences("Graph", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        repeatedWordsCount = sharedPreferences.getInt("repeatedWordsCount", 0);
        learnedWordsCount = sharedPreferences.getInt("learnedWordsCount", 0);
        listeningModeProgress = sharedPreferences.getInt("listeningModeProgress", 0);
        quizModeProgress = sharedPreferences.getInt("quizModeProgress", 0);
        writingModeProgress = sharedPreferences.getInt("writingModeProgress", 0);
        totalNumber  += writingModeProgress + listeningModeProgress + quizModeProgress;

        binding.learningModeWordsCountText.setText("Öğrenme Moduna Yollanan Kelime Sayısı:" + learnedWordsCount);
        binding.practiceModeWordsCountText.setText("Pratik Yapılan Kelime Sayısı:" + totalNumber );


        setupPieChart(learnedWordsCount, repeatedWordsCount, listeningModeProgress, quizModeProgress, writingModeProgress);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.learningModeWordsCountText.setText("Öğrenme Moduna Yollanan Kelime Sayısı:" + learnedWordsCount);
        binding.practiceModeWordsCountText.setText("Pratik Yapılan Kelime Sayısı:" + totalNumber );
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.learningModeWordsCountText.setText("Öğrenme Moduna Yollanan Kelime Sayısı:" + learnedWordsCount);
        binding.practiceModeWordsCountText.setText("Pratik Yapılan Kelime Sayısı:" + totalNumber );
    }

    private void setupPieChart(int a, int b, int c, int d, int e) {
        List<PieEntry> pieEntries = new ArrayList<>();
        if (a != 0) {
            pieEntries.add(new PieEntry(a, "Ögrenilen Kelimeler"));
        }
        if (c != 0) {
            pieEntries.add(new PieEntry(c, "Dinleme Modu"));
        }
        if (d != 0) {
            pieEntries.add(new PieEntry(d, "Quiz Modu"));
        }
        if (e != 0) {
            pieEntries.add(new PieEntry(e, "Yazma Modu"));
        }


        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueTextSize(24f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(24f);

        PieChart pieChart = binding.vocabularyPieChart;
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        String EXPIRY_CHECK = "unique_worker_name";


        long currentTimeMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long desiredExecutionTimeMillis = calendar.getTimeInMillis();

        long delayMillis = desiredExecutionTimeMillis - currentTimeMillis;

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(DailyStatsWorker.class)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);

    }
}