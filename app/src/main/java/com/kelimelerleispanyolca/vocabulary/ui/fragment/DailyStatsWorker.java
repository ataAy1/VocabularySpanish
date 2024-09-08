package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class DailyStatsWorker extends Worker {
    private static final String TAG = "DailyStatsWorker";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public DailyStatsWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams
    ) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        sharedPreferences = getApplicationContext().getSharedPreferences("StatisticsPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Reset daily statistics
        editor.putInt("learnedWordsCount", 0);
        editor.putInt("repeatedWordsCount", 0);
        editor.putInt("listeningModeProgress", 0);
        editor.putInt("quizModeProgress", 0);
        editor.putInt("writingModeProgress", 0);
        editor.commit();
        return Result.success();
    }
}