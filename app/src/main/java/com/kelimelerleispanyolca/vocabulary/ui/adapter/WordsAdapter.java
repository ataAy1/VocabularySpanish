package com.kelimelerleispanyolca.vocabulary.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.databinding.ItemWordBinding;
import com.kelimelerleispanyolca.vocabulary.ui.fragment.WordsFragmentDirections;
import com.kelimelerleispanyolca.vocabulary.ui.viewModel.WordsViewModel;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.KelimelerTasarim> {

    public List<WordSaveEntity> keliemerArray;
    private Context context;
    private WordsViewModel viewModel;

    public WordsAdapter(List<WordSaveEntity> keliemerArray, Context context, WordsViewModel viewModel) {
        this.keliemerArray = keliemerArray;
        this.context = context;
        this.viewModel = viewModel;
    }


    @Override
    public WordsAdapter.KelimelerTasarim onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWordBinding binding = ItemWordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new KelimelerTasarim(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsAdapter.KelimelerTasarim holder, int position) {
        holder.binding.tvWordsNumber.setText(String.valueOf(position + 1));

        WordSaveEntity kelimeGuncelle = keliemerArray.get(position);
        if (keliemerArray.get(position) .getTurkishWord() != null) {
            int id = keliemerArray.get(position).getWordId();
            String dinlenecekKelime = keliemerArray.get(position).getSpanishWord().toString();
            String dinlenecekCumle = keliemerArray.get(position).getExampleSentenceTurkish().toString();

            holder.binding.tvWordSpanish.setText(keliemerArray.get(position).getSpanishWord());
            holder.binding.tvWordTurkish.setText(keliemerArray.get(position).getTurkishWord());
            holder.binding.tvWordType.setText(keliemerArray.get(position).getWordType());
            holder.binding.tvExampleSentenceTurkish.setText(keliemerArray.get(position).getExampleSentenceTurkish());
            holder.binding.tvExampleSentenceTranslation.setText(keliemerArray.get(position).getExampleSentenceTranslation());

            holder.binding.ivListenWordAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.playWordAudio(dinlenecekKelime, v.getContext());
                }
            });

            holder.binding.ivListenWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.playWordAudio(dinlenecekCumle, v.getContext());
                }
            });
        }


        holder.binding.ivDeleteWord.setOnClickListener(v -> {
            WordSaveEntity wordToUpdate = keliemerArray.get(position);
            WordsFragmentDirections.ActionWordsFragmentToUpdateWordFragment action =
                    WordsFragmentDirections.actionWordsFragmentToUpdateWordFragment(wordToUpdate);
            Navigation.findNavController(v).navigate(action);
        });


    }

    @Override
    public int getItemCount() {
        return keliemerArray.size();
    }

    public class KelimelerTasarim extends RecyclerView.ViewHolder {
        private ItemWordBinding binding;

        public KelimelerTasarim(ItemWordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
