package com.kelimelerleispanyolca.vocabulary.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kelimelerleispanyolca.vocabulary.R;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.databinding.FragmentWordsBinding;
import com.kelimelerleispanyolca.vocabulary.ui.adapter.WordsAdapter;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.WordsViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WordsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWordsBinding binding;
    private WordsViewModel viewModel;
    private WordsAdapter kelimelerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWordsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        setupRecyclerView();
        observeViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        setupMenu();
    }

    private void setupRecyclerView() {
        binding.wordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void observeViewModel() {
        viewModel.fetchAllWords().observe(getViewLifecycleOwner(), kelimeKaydetmes -> {
            kelimelerAdapter = new WordsAdapter(kelimeKaydetmes, getContext(), viewModel);
            binding.wordsRecyclerView.setAdapter(kelimelerAdapter);
        });
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);
        }
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
                MenuItem item = menu.findItem(R.id.ara_menu);
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(WordsFragment.this);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        performSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        performSearch(newText);
        return false;
    }

    private void performSearch(String query) {
        viewModel.searchWord(query).observe(getViewLifecycleOwner(), tumKelimeler -> {
            kelimelerAdapter = new WordsAdapter((List<WordSaveEntity>) tumKelimeler, getContext(), viewModel);
            binding.wordsRecyclerView.setAdapter(kelimelerAdapter);
        });
    }
}
